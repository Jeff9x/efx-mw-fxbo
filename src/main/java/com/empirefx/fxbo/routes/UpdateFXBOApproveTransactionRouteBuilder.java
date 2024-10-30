package com.empirefx.fxbo.routes;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.ValidationException;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Component
public class UpdateFXBOApproveTransactionRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

// Global exception handler for validation errors
        onException(ValidationException.class)
                .log("Validation failed: ${exception.message}")
                .handled(true)
                .setBody(simple("Validation failed: ${exception.message}"));

        rest()
                .put("/transactions/{crmTransactionId}/approve")
                .description("Adapter REST Service For Approve Transaction")
                .consumes(APPLICATION_JSON_VALUE)
                .produces("application/json")
                .to("direct:approveTransaction");

        from("direct:approveTransaction").routeId("com.empirefx.request.dispatchRequest41")
                .noStreamCaching().noMessageHistory().noTracing()
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("Accept", constant("application/json"))
                .process("approveHeadersSetterProcessor")
                .removeHeaders("CamelHttp*")
                .doTry()
                .log(LoggingLevel.INFO, "\n Calling FXBO Endpoint :: Approve Transaction Request :: ${header.finaUrl}")
                .enrich().simple("${header.finaUrl}").id("callServiceBack41")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .to("direct:fetchApproveTransactionResponse");

        from("direct:fetchApproveTransactionResponse")
                .log("Processed response with content type: ${header.Content-Type}")
                .doTry()
                .setBody(simple("${body}"))
                .convertBodyTo(String.class)
                .unmarshal().json()
                .end();
    }
}