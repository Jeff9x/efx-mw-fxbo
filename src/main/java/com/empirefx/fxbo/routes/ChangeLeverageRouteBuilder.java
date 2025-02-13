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
public class ChangeLeverageRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // Global exception handler for validation errors
        onException(ValidationException.class)
                .log("Validation failed: ${exception.message}")
                .handled(true)
                .setBody(simple("Validation failed: ${exception.message}"));

        // REST Endpoint Configuration
        rest()
                .put("/trading-account/change-leverage/{crmAccountId}")
                .description("Adapter REST Service for Change Leverage")
                .consumes(APPLICATION_JSON_VALUE)
                .produces("application/json")
                .to("direct:changeLeverage");

        // Route for Change Leverage
        from("direct:changeLeverage").routeId("com.empirefx.request.changeLeverage")
                .noStreamCaching().noMessageHistory().noTracing()
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("Accept", constant("application/json"))
                .process("changeLeverageHeadersSetterProcessor") // Add necessary headers
                .removeHeaders("CamelHttp*")
                .doTry()
                .log(LoggingLevel.INFO, "\n Calling FXBO Endpoint :: Change Leverage Request :: ${header.finaUrl}")
                .enrich().simple("${header.finaUrl}").id("callServiceBackChangeLeverage")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .to("direct:fetchChangeLeverageResponse");

        // Route to Process the Response
        from("direct:fetchChangeLeverageResponse")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .log(LoggingLevel.INFO, "Incoming response: ${body}")
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