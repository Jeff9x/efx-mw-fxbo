package com.empirefx.fxbo.routes;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.ValidationException;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Component
public class UpdateFXBOUserAccountRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

// Global exception handler for validation errors
        onException(ValidationException.class)
                .log("Validation failed: ${exception.message}")
                .handled(true)
                .setBody(simple("Validation failed: ${exception.message}"));

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
                .process("headersSetterProcessor")
                .marshal().json()
                .doTry()
                .log(LoggingLevel.INFO, "\n Calling FXBO Endpoint :: Update User Account Request :: {{atomic1.uriUpdateUser}}")
                .enrich().simple("{{atomic1.uriUpdateUser}}").id("callServiceBack4")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .to("direct:fetchUpdateUserAccountsResponse");

        from("direct:fetchUpdateUserAccountsResponse")
                .log("Processed response with content type: ${header.Content-Type}")
                .doTry()
                .setBody(simple("${body}"))
                .convertBodyTo(String.class)
                .unmarshal().json()
                .end();
    }
}