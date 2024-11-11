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
public class UpdateFXBOUserAccountRouteBuilder extends RouteBuilder {

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
                .post("/update-user")
                .description("Adapter REST Service")
                .consumes(APPLICATION_JSON_VALUE)
                .produces("application/json")
                .to("direct:updateUserAccount");

        from("direct:updateUserAccount").routeId("com.empirefx.request.dispatchRequest4")
                .noStreamCaching().noMessageHistory().noTracing()
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("Accept", constant("application/json"))
                .process("validateUserVerificationRequestProcessor")
                .process("headersSetterProcessor")
                .marshal().json()
                .doTry()
                .log(LoggingLevel.INFO, "\n Calling FXBO Endpoint :: Update User Account Request :: {{atomic1.uriUpdateUser}}")
                .enrich().simple("{{atomic1.uriUpdateUser}}").id("callServiceBack4")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .to("direct:fetchUpdateUserAccountsResponse");

        from("direct:fetchUpdateUserAccountsResponse")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
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