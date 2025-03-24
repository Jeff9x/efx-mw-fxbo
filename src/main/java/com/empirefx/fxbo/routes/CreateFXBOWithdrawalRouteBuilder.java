package com.empirefx.fxbo.routes;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Component
public class CreateFXBOWithdrawalRouteBuilder extends RouteBuilder {

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
                .post("/transactions/withdrawals")
                .description("Adapter REST Service FXBO Deposits")
                .consumes(APPLICATION_JSON_VALUE)
                .produces("application/json")
                .to("direct:makeWithdrawals");

        from("direct:makeWithdrawals").routeId("com.empirefx.request.dispatchRequest102")
                .noStreamCaching().noMessageHistory().noTracing()
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("Accept", constant("application/json"))
//                .process("validateDepositRequestProcessor")
                .process("headersSetterProcessor")
//                .process("clientRequestWithdrawalProcessor")
                .process(exchange -> {
                    String jsonBody = new ObjectMapper().writeValueAsString(exchange.getIn().getBody(Map.class));
                    exchange.getIn().setBody(jsonBody);
                })
                .doTry()
                .log(LoggingLevel.INFO, "\n Calling FXBO Endpoint :: Create Withdrawal Request :: {{atomic1.uriWithdrawal}}")
                .enrich().simple("{{atomic1.uriWithdrawal}}").id("callServiceBack102")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .to("direct:fetchWithdrawalResponse");

        from("direct:fetchWithdrawalResponse")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .log("Incoming response: ${body}")
                .convertBodyTo(String.class) // Convert InputStream to String
                .process(exchange -> {
                    String body = exchange.getIn().getBody(String.class);
                    Map jsonMap = new ObjectMapper().readValue(body, Map.class); // Parse JSON to Map
                    exchange.getIn().setBody(jsonMap); // Replace body with Map
                })
                .choice()
                    .when(simple("${body[code]} == 400"))
                        .log(LoggingLevel.WARN, "Processing failure response...")
                        .process("failureResponseProcessor")
                    .otherwise()
                        .log(LoggingLevel.INFO, "Processing success response...")
                        .process("successResponseProcessor")
                .end();
    }
}