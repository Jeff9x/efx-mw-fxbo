package com.empirefx.fxbo.routes;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.ValidationException;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Component
public class CreateFXBOWithdrawalRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

// Global exception handler for validation errors
        onException(ValidationException.class)
                .log("Validation failed: ${exception.message}")
                .handled(true)
                .setBody(simple("Validation failed: ${exception.message}"));

        rest()
                .post("/transactions/withdrawals")
                .description("Adapter REST Service FXBO Deposits")
                .consumes(APPLICATION_JSON_VALUE)
                .produces("application/json")
                .to("direct:makeWithdrawals");

        from("direct:makeWithdrawals").routeId("com.empirefx.request.dispatchRequest102")
                .noStreamCaching().noMessageHistory().noTracing()
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("Accept", constant("application/json"))
                .process("headersSetterProcessor")
                .process("clientRequestWithdrawalProcessor")
                .doTry()
                .log(LoggingLevel.INFO, "\n Calling FXBO Endpoint :: Create Withdrawal Request :: {{atomic1.uriWithdrawal}}")
                .enrich().simple("{{atomic1.uriWithdrawal}}").id("callServiceBack102")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .to("direct:fetchWithdrawalResponse");

        from("direct:fetchWithdrawalResponse")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                .log("Processed response with content type: ${header.Content-Type}")
                .setBody(simple("${body}"))
                .log("Response body: ${body}")
                .convertBodyTo(String.class)
                .unmarshal().json()
                .end();
    }
}