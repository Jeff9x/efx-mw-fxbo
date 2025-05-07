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
public class SetIbPasswordRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        // Global exception handler for validation errors
        onException(ValidationException.class)
                .log("Validation failed: ${exception.message}")
                .handled(true)
                .setBody(simple("Validation failed: ${exception.message}"));

        // REST Endpoint Configuration
        rest()
                .post("/ib/set-password")
                .description("Adapter REST Service for IB Set Password")
                .consumes(APPLICATION_JSON_VALUE)
                .produces("application/json")
                .to("direct:setIbPassword");

        // Route for setting Password
        from("direct:setIbPassword").routeId("com.empirefx.request.setIbPassword")
                .noStreamCaching().noMessageHistory().noTracing()
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("Accept", constant("application/json"))
                .process("headersSetterProcessor") // Add necessary headers
                .process("setIbPasswordProcessor")
                .log("Incoming Body:: ${body}")
                .removeHeaders("CamelHttp*")
                .doTry()
                .log(LoggingLevel.INFO, "\n Calling FXBO Endpoint :: set Password Request :: {{atomic.uriIbSetPassword}}")
                .enrich().simple("{{atomic.uriIbSetPassword}}").id("setIbPassword")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .to("direct:fetchSetIbPasswordResponse");

        // Route to Process the Response
        from("direct:fetchSetIbPasswordResponse")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .log(LoggingLevel.INFO, "Incoming response: ${body}")
                .unmarshal().json()
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
