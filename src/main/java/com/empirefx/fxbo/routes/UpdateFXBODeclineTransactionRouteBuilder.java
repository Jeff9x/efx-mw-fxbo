package com.empirefx.fxbo.routes;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.ValidationException;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Component
public class UpdateFXBODeclineTransactionRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

// Global exception handler for validation errors
        onException(ValidationException.class)
                .log("Validation failed: ${exception.message}")
                .handled(true)
                .setBody(simple("Validation failed: ${exception.message}"));

        rest()
                .put("/transactions/decline")
                .description("Adapter REST Service For Approve Transaction")
                .consumes(APPLICATION_JSON_VALUE)
                .produces("application/json")
                .to("direct:declineTransaction");

        from("direct:declineTransaction").routeId("com.empirefx.request.dispatchRequest411")
                .noStreamCaching().noMessageHistory().noTracing()
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("Accept", constant("application/json"))
                .process("headersSetterProcessor")
                .marshal().json()
                .doTry()
                .log(LoggingLevel.INFO, "\n Calling FXBO Endpoint :: Decline Transaction Request :: {{atomic1.uriDeclineTransaction}}")
                .enrich().simple("{{atomic1.uriDeclineTransaction}}").id("callServiceBack411")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .to("direct:fetchDeclineTransactionResponse");

        from("direct:fetchDeclineTransactionResponse")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                .log("Processed response with content type: ${header.Content-Type}")
                .log("body Response ${body}")
                .doTry()
                .setBody(simple("${body}"))
                .convertBodyTo(String.class)
                .unmarshal().json()
                .end();
    }
}