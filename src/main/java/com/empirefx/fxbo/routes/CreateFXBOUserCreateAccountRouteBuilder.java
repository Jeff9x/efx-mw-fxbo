package com.empirefx.fxbo.routes;

import com.empirefx.fxbo.processors.ValidateCreateUserRequestProcessor;
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
                .doTry()
                    .unmarshal().json()
                        .choice()
                            .when(simple("${body[error]} != null")) // Adjust condition based on actual error field
                                .log("Request failed: ${body[error]}")
                            .otherwise()
                                .log("Request was successful.")
                        .endChoice()
                .endDoTry()
                .doCatch(Exception.class)
                    .log("Exception during processing: ${exception.message}")
                .end();
    }
}