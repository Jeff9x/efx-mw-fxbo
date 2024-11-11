package com.empirefx.fxbo.routes;

import com.empirefx.fxbo.processors.ValidateCreateUserRequestProcessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;


import java.util.Map;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
@Component
public class CreateFXBOUserCreateAccountRouteBuilder extends RouteBuilder {
    private final ValidateCreateUserRequestProcessor validationProcessor;

    public CreateFXBOUserCreateAccountRouteBuilder(ValidateCreateUserRequestProcessor validationProcessor) {
        this.validationProcessor = validationProcessor;
    }


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
                .post("/create-user-account")
                .description("Adapter REST Service")
                .consumes(APPLICATION_JSON_VALUE)
                .produces("application/json")
                .to("direct:createUserAccount");

        from("direct:createUserAccount").routeId("com.empirefx.request.dispatchRequest5")
                .noStreamCaching().noMessageHistory().noTracing()
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("Accept", constant("application/json"))
                .doTry()
                .process("validateCreateUserRequestProcessor")
                .end()
                .process("createUserAccountHeadersSetterProcessor")
                .process("clientRequestProcessor")
                .doTry()
                .log(LoggingLevel.INFO, "\n Calling FXBO Endpoint :: Create Account Request :: {{atomic1.uriUser}}")
                .enrich().simple("{{atomic1.uriUser}}").id("createUserAccount5")
                .to("direct:fetchUserAccountsResponse");

        from("direct:fetchUserAccountsResponse")
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