package com.empirefx.fxbo.routes;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.ValidationException;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class GetFXBOTransactionsRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

// Global exception handler for validation errors
        onException(ValidationException.class)
                .log("Validation failed: ${exception.message}")
                .handled(true)
                .setBody(simple("Validation failed: ${exception.message}"));

        rest()
                .post("/payments/transactions")
                .description("Adapter REST Service Get Transactions API")
                .produces("application/json")
                .to("direct:fetchTransactions");

        from("direct:fetchTransactions").routeId("com.empirefx.request.dispatchRequest311")
                .noStreamCaching().noMessageHistory().noTracing()
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("Accept", constant("application/json"))
                .process("headersSetterProcessor")
                .marshal().json()
                .doTry()
                .log(LoggingLevel.INFO, "\n Calling FXBO Fetch Transactions Endpoint :: Request :: {{atomic1.uriFetchTransactions}}")
                .enrich().simple("{{atomic1.uriFetchTransactions}}").id("callServiceBack311")
                .to("direct:fetchTransactionsResponse");

        from("direct:fetchTransactionsResponse")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .log("Processed response with content type: ${header.Content-Type}")
//                .log(LoggingLevel.INFO, "Body Set ${body}")
                .setBody(simple("${body}"))
                .convertBodyTo(String.class)
                .unmarshal().json()
                .end();
    }
}