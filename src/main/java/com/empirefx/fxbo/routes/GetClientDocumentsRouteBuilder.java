package com.empirefx.fxbo.routes;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.empirefx.fxbo.commonlib.enums.HTTPCommonHeadersEnum.CONTENT_TYPE;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Component
public class GetClientDocumentsRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // Global exception handler for validation errors
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
                .get("/client-documents/{clientId}")
                .description("Fetch Client Documents Service")
                .produces("application/json")
                .to("direct:fetchClientDocuments");

        from("direct:fetchClientDocuments").routeId("com.empirefx.request.fetchClientDocuments")
                .noStreamCaching().noMessageHistory().noTracing()
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .process("getClientDocumentsRequestProcessor")
                .process("headersSetterProcessor")
                .log(LoggingLevel.INFO, "Fetching documents for Client ID: ${header.clientId}")
                .removeHeaders("CamelHttp*")
                .doTry()
                .log(LoggingLevel.INFO, "\n Calling FXBO Endpoint :: Verify Client Documents  :: {{atomic.uriVerifyPhone}}")
                .enrich().simple("{{atomic.uriVerifyPhone}}").id("verifyClientDocuments")
                .to("direct:fetchClientDocumentsResponse");

        from("direct:fetchClientDocumentsResponse")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .log("Incoming response: ${body}")
                .setBody(simple("${body}"))
                .convertBodyTo(String.class)
                .unmarshal().json()
                .end();
    }
}
