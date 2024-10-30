package com.empirefx.fxbo.routes;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.ValidationException;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Component
public class CreateFXBOCreatePOADocumentRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

// Global exception handler for validation errors
        onException(ValidationException.class)
                .log("Validation failed: ${exception.message}")
                .handled(true)
                .setBody(simple("Validation failed: ${exception.message}"));

        rest()
                .post("/upload-poa-document")
                .description("Adapter REST Service")
                .consumes(APPLICATION_JSON_VALUE)
                .produces(APPLICATION_JSON_VALUE)
                .to("direct:createPOADocument");

        from("direct:createPOADocument").routeId("com.empirefx.request.dispatchRequest20")
                .noStreamCaching().noMessageHistory().noTracing()
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("Accept", constant("application/json"))
                .process("headersSetterProcessor")
                .process("imagePoaUploadRequestProcessor")
                .doTry()
//                .log("Processed request To Backend : ${body}")
                .log(LoggingLevel.INFO, "\n Calling FXBO Endpoint :: Create Upload POA Request :: {{atomic1.uriPOI}}")
                .enrich().simple("{{atomic1.uriPOI}}").id("callServiceBack20")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .to("direct:fetchPOAResponse");

        from("direct:fetchPOAResponse")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .log("Incoming response: ${body}")
                .log("Processed response with content type: ${header.Content-Type}")
//                .log("Processed response : ${body}")
                .removeHeaders("*")
                .removeHeader("Authorization")
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