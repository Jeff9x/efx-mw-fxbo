package com.empirefx.fxbo.routes;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.ValidationException;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GetFXBOAccountBalanceRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

// Handle validation exceptions with a structured error response
        onException(IllegalArgumentException.class)
                .handled(true)
                .log(LoggingLevel.ERROR, "Validation error: ${exception.message}")
                .process(exchange -> {
                    String errorMessage = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, IllegalArgumentException.class).getMessage();
                    exchange.getIn().setBody(Map.of("code", 400, "message", errorMessage));
                    exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");
                    exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 400);
                });

        rest()
                .post("/trading-accounts/account-balance")
                .description("Adapter REST Service Get Trading Accounts Balace API")
                .produces("application/json")
                .to("direct:fetchAccountBalance");

        from("direct:fetchAccountBalance").routeId("com.empirefx.request.dispatchRequest312")
                .noStreamCaching().noMessageHistory().noTracing()
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("Accept", constant("application/json"))
                .process("validateAccountRequestProcessor")
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