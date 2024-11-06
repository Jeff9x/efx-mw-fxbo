package com.empirefx.fxbo.routes;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.ValidationException;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Component
public class CreateFXBOCreatePOIDocumentRouteBuilder extends RouteBuilder {

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
                .post("/upload-poi-document")
                .description("Adapter REST Service")
                .consumes(APPLICATION_JSON_VALUE)
                .produces("application/json")
                .to("direct:createPOIDocument");

        from("direct:createPOIDocument").routeId("com.empirefx.request.dispatchRequest2")
                .noStreamCaching().noMessageHistory().noTracing()
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("Accept", constant("application/json"))
                .process("validatePOIDocumentUploadRequestProcessor")
                .process("headersSetterProcessor")
                .process("imageUploadRequestProcessor")
                .doTry()
//                .log("Processed request To Backend : ${body}")
                .log(LoggingLevel.INFO, "\n Calling FXBO Endpoint :: Create Upload POI Request :: {{atomic1.uriPOI}}")
                .enrich().simple("{{atomic1.uriPOI}}").id("callServiceBack2")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .to("direct:fetchPOIResponse");

        from("direct:fetchPOIResponse")
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