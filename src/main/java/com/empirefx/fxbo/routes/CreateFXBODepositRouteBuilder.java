package com.empirefx.fxbo.routes;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.ValidationException;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Component
public class CreateFXBODepositRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

// Global exception handler for validation errors
        onException(ValidationException.class)
                .log("Validation failed: ${exception.message}")
                .handled(true)
                .setBody(simple("Validation failed: ${exception.message}"));

        rest()
                .post("/transactions/deposits")
                .description("Adapter REST Service FXBO Deposits")
                .consumes(APPLICATION_JSON_VALUE)
                .produces("application/json")
                .to("direct:makeDeposits");

        from("direct:makeDeposits").routeId("com.empirefx.request.dispatchRequest101")
                .noStreamCaching().noMessageHistory().noTracing()
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("Accept", constant("application/json"))
                .process("headersSetterProcessor")
                .process("clientDepositRequestProcessor")
                .log(LoggingLevel.INFO, "\n Calling FXBO Endpoint :: Create Deposit Request :: {{atomic1.uriDeposit}}")
                .enrich().simple("{{atomic1.uriDeposit}}").id("callServiceBack101")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .to("direct:fetchDepositResponse");

        from("direct:fetchDepositResponse")
                .log("Processed response with content type: ${header.Content-Type}")
                .setBody(simple("${body}"))
                .log("Response body: ${body}")
                .convertBodyTo(String.class)
                .unmarshal().json()
                .end();
    }
}
