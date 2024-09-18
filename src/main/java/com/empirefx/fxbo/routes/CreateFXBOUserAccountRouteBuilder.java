package com.empirefx.fxbo.routes;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.ValidationException;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Component
public class CreateFXBOUserAccountRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

// Global exception handler for validation errors
        onException(ValidationException.class)
                .log("Validation failed: ${exception.message}")
                .handled(true)
                .setBody(simple("Validation failed: ${exception.message}"));

        rest()
                .post("/create-user")
                .description("Adapter REST Service")
                .consumes(APPLICATION_JSON_VALUE)
                .produces("application/json")
                .to("direct:createUserAccount");

        from("direct:createUserAccount").routeId("com.empirefx.request.dispatchRequest2")
                .noStreamCaching().noMessageHistory().noTracing()
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("Accept", constant("application/json"))
                .process("headersSetterProcessor")
                .convertBodyTo(String.class)
                .marshal().json()
//                .process("userRequestValidationProcessor")
                .doTry()
                .log(LoggingLevel.INFO, "\n Calling FXBO Endpoint :: Create User Account Request :: {{atomic1.uriUser}}")
                .enrich().simple("{{atomic1.uriUser}}").id("callServiceBack2")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .to("direct:fetchUserAccountsResponse");

        from("direct:fetchUserAccountsResponse")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                .log("Processed response with content type: ${header.Content-Type}")
                .removeHeaders("*")
                .removeHeader("Authorization")
                .doTry()
                .setBody(simple("${body}"))
                .convertBodyTo(String.class)
                .unmarshal().json()
                .end();
    }
}