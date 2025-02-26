package com.empirefx.fxbo.routes;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Component
public class VerifyPhoneNumberRouteBuilder extends RouteBuilder {

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
                .post("/verification/phone")
                .description("Phone Number Verification Service")
                .consumes(APPLICATION_JSON_VALUE)
                .produces("application/json")
                .to("direct:verifyPhoneNumber");

        from("direct:verifyPhoneNumber").routeId("com.empirefx.request.verifyPhoneNumber")
                .noStreamCaching().noMessageHistory().noTracing()
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("Accept", constant("application/json"))
//                .process("validatePhoneNumberProcessor")
                .process("headersSetterProcessor")
                .process("clientPhoneNumberVerificationProcessor")
                .doTry()
                .log(LoggingLevel.INFO, "\n Calling FXBO Endpoint :: Verify Phone Number :: {{atomic.uriVerifyPhone}}")
                .enrich().simple("{{atomic.uriVerifyPhone}}").id("verifyPhoneNumber")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .to("direct:fetchVerificationResponse");

        from("direct:fetchVerificationResponse")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .log("Incoming response: ${body}")
                .setBody(simple("${body}"))
                .convertBodyTo(String.class)
                .unmarshal().json()
                .end();
    }
}
