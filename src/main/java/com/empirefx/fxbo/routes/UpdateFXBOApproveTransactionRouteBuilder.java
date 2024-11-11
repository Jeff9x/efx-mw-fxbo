package com.empirefx.fxbo.routes;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.ValidationException;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

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
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .convertBodyTo(String.class) // Convert InputStream to String
                .process(exchange -> {
                    String body = exchange.getIn().getBody(String.class);
                    Map jsonMap = new ObjectMapper().readValue(body, Map.class); // Parse JSON to Map
                    exchange.getIn().setBody(jsonMap); // Replace body with Map
                })
                .choice()
                    .when(simple("${body[code]} == 404"))
                        .log(LoggingLevel.WARN, "Processing failure response...")
                        .process("approveTransactionFailureResponseProcessor")
                    .otherwise()
                        .log(LoggingLevel.INFO, "Processing success response...")
                        .process("successResponseProcessor")
                .end();
    }
}