package com.empirefx.fxbo.routes;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.ValidationException;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class GetFXBOAccountBalanceRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

// Global exception handler for validation errors
        onException(ValidationException.class)
                .log("Validation failed: ${exception.message}")
                .handled(true)
                .setBody(simple("Validation failed: ${exception.message}"));

        rest()
                .post("/trading-accounts/account-balance")
                .description("Adapter REST Service Get Trading Accounts Balace API")
                .produces("application/json")
                .to("direct:fetchAccountBalance");

        from("direct:fetchAccountBalance").routeId("com.empirefx.request.dispatchRequest312")
                .noStreamCaching().noMessageHistory().noTracing()
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("Accept", constant("application/json"))
                .process("headersSetterProcessor")
                .marshal().json()
                .doTry()
                .log(LoggingLevel.INFO, "\n Calling FXBO Fetch Transactions Endpoint :: Request :: {{atomic1.uriAccountBalance}}")
                .enrich().simple("{{atomic1.uriAccountBalance}}").id("callServiceBack312")
                .to("direct:fetchAccountBalanceResponse");

        from("direct:fetchAccountBalanceResponse")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .log("Processed response with content type: ${header.Content-Type}")
                .log("Incoming response: ${body}")
                .removeHeaders("*")
                .removeHeader("Authorization")
                .doTry()
                    .process("accountBalanceResponseProcessor");
    }
}