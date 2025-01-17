package com.empirefx.fxbo.routes;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Component
public class InternalFundsTransferRouteBuilder extends RouteBuilder {

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

        // Define REST endpoint for internal funds transfer
        rest()
                .post("/internal-funds-transfer")
                .description("Adapter REST Service for Internal Funds Transfer")
                .consumes(APPLICATION_JSON_VALUE)
                .produces("application/json")
                .to("direct:processFundsTransfer");

        // Route for processing internal funds transfer
        from("direct:processFundsTransfer").routeId("com.empirefx.request.fundsTransfer")
                .noStreamCaching().noMessageHistory().noTracing()
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("Accept", constant("application/json"))
//                .process("validateFundsTransferRequestProcessor") // Validate the request payload
                .process("headersSetterProcessor") // Add necessary headers
                .process("internalFundsTransferProcessor")
                .marshal().json()
                .doTry()
                .log(LoggingLevel.INFO, "\n Calling FXBO Endpoint :: Internal Funds Transfer Request :: {{atomic1.uriInternalFundsTransfer}}")
                .enrich().simple("{{atomic1.uriInternalFundsTransfer}}").id("callFundsTransferService")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .to("direct:fetchFundsTransferResponse");

        // Route to handle the response from the FXBO endpoint
        from("direct:fetchFundsTransferResponse")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .log("Incoming response: ${body}")
                .convertBodyTo(String.class) // Convert InputStream to String
                .process(exchange -> {
                    String body = exchange.getIn().getBody(String.class);
                    Map<String, Object> jsonMap = new ObjectMapper().readValue(body, Map.class); // Parse JSON to Map
                    exchange.getIn().setBody(jsonMap); // Replace body with Map
                })
                .choice()
                    .when(simple("${body[code]} == 400"))
                        .log(LoggingLevel.WARN, "Processing failure response...")
                        .process("failureResponseProcessor") // Handle failure
                    .otherwise()
                        .log(LoggingLevel.INFO, "Processing success response...")
                        .process("successResponseProcessor") // Handle success
                .end();
    }
}