package com.empirefx.fxbo.routes;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.ValidationException;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;


@Component
public class GetFXBOTradingAccountsRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

// Global exception handler for validation errors
        onException(ValidationException.class)
                .log("Validation failed: ${exception.message}")
                .handled(true)
                .setBody(simple("Validation failed: ${exception.message}"));

        rest()
                .get("/trading-accounts/user/{fxboUserId}")
                .description("Adapter REST Service")
                .produces("application/json")
                .to("direct:fetchTradingAccounts");

        from("direct:fetchTradingAccounts").routeId("com.empirefx.request.dispatchRequest30")
                .noStreamCaching().noMessageHistory().noTracing()
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .process("headersSetterProcessor")
                .process("getTradingAccountsRequestProcessor")
                .log(LoggingLevel.INFO, "Body Set ${body}")
                .removeHeaders("CamelHttp*")
                .doTry()
                .log(LoggingLevel.INFO, "\n Calling FXBO Fetch Trading Accounts Endpoint :: Request :: {{atomic.uriTradingAccounts}}")
                .enrich().simple("{{atomic.uriTradingAccounts}}").id("callServiceBack30")
                .to("direct:fetchTradingAccountsResponse");

        from("direct:fetchTradingAccountsResponse")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .log("Processed response with content type: ${header.Content-Type}")
                .setBody(simple("${body}"))
                .convertBodyTo(String.class)
                .unmarshal().json()
                .end();
    }
}