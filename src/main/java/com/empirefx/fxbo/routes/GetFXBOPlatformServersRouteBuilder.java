package com.empirefx.fxbo.routes;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.ValidationException;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import static com.empirefx.fxbo.commonlib.enums.HTTPCommonHeadersEnum.CONTENT_TYPE;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Component
public class GetFXBOPlatformServersRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

// Global exception handler for validation errors
        onException(ValidationException.class)
                .log("Validation failed: ${exception.message}")
                .handled(true)
                .setBody(simple("Validation failed: ${exception.message}"));

        rest()
                .get("/platform/servers")
                .description("Adapter REST Service")
                .produces("application/json")
                .to("direct:fetchPlatformServers");

        from("direct:fetchPlatformServers").routeId("com.empirefx.request.getPlatformServers")
                .noStreamCaching().noMessageHistory().noTracing()
                .process("headersSetterProcessor")
                .removeHeaders("*", "Authorization")// Set Authorization header
                .doTry()
                .log(LoggingLevel.INFO, "\n Calling FXBO Endpoint :: Request :: {{atomic.uriPlatformServers}}")
                .enrich().simple("{{atomic.uriPlatformServers}}").id("callServiceBack304")
                .setHeader(CONTENT_TYPE.getName(), constant(APPLICATION_JSON_VALUE))
                .to("direct:fetchPlatformServersResponse");

        from("direct:fetchPlatformServersResponse")
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