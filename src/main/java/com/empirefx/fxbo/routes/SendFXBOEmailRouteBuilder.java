package com.empirefx.fxbo.routes;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SendFXBOEmailRouteBuilder extends RouteBuilder {

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
                .post("/email-notification")
                .description("Adapter REST Service")
                .produces("application/json")
                .to("direct:sendEmailAPI");

        from("direct:sendEmailAPI").routeId("com.empirefx.request.sendEmailAPI")
                .noStreamCaching().noMessageHistory().noTracing()
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .process("headersSetterProcessor")
                .process("emailRequestProcessor")
                .log(LoggingLevel.INFO, "Body Set ${body}")
                .doTry()
                .log(LoggingLevel.INFO, "\n Calling FXBO Send Email Notification Endpoint :: Request :: {{atomic1.urisendEmail}}")
                .enrich().simple("{{atomic1.urisendEmail}}").id("sendEmailAPIResponse")
                .to("direct:sendEmailAPIResponse");

        from("direct:sendEmailAPIResponse")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .log(LoggingLevel.INFO, "Incoming response ${body}")
                .convertBodyTo(String.class) // Convert InputStream to String
                .log(LoggingLevel.INFO, "Processing success response...")
                .process("emailSuccessResponseProcessor")
                .end();
    }
}