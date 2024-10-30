package com.empirefx.fxbo.routes;


import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.ValidationException;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;


@Component
//@EnableSwagger2
public class CreateFXBOCreateTradingAccountRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

// Global exception handler for validation errors
        onException(ValidationException.class)
                .log("Validation failed: ${exception.message}")
                .handled(true)
                .setBody(simple("Validation failed: ${exception.message}"));

        rest()
                .post("/trading-accounts")
                .description("Adapter REST Service")
                .consumes(APPLICATION_JSON_VALUE)
                .produces("application/json")
                .to("direct:createTradingAccount");

        from("direct:createTradingAccount").routeId("com.empirefx.request.dispatchRequest100")
                .noStreamCaching().noMessageHistory().noTracing()
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("Accept", constant("application/json"))
                .process("createTradingAccountHeadersSetterProcessor")
                .marshal().json()
                .doTry()
                .log(LoggingLevel.INFO, "\n Calling FXBO Endpoint :: Create Trading Account Request :: {{atomic.uriAccount}}")
                .enrich().simple("{{atomic.uriAccount}}").id("callServiceBack100")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .to("direct:fetchTradingAccountResponse");

        from("direct:fetchTradingAccountResponse")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .log("Incoming response: ${body}")
                .doTry()
                    .unmarshal().json()
                        .choice()
                            .when(simple("${body[error]} != null")) // Adjust condition based on actual error field
                                .log("Request failed: ${body[error]}")
                            .otherwise()
                                .log("Request was successful.")
                        .endChoice()
                .endDoTry()
                .doCatch(Exception.class)
                    .log("Exception during processing: ${exception.message}")
                .log("Processed response with content type: ${header.Content-Type}")
                .setBody(simple("${body}"))
                .log("Response body: ${body}")
                .convertBodyTo(String.class)
                .unmarshal().json()
                .end();
    }
}