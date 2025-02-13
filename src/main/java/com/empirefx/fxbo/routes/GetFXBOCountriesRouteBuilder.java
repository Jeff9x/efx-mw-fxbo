package com.empirefx.fxbo.routes;


import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.ValidationException;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;


import static com.empirefx.fxbo.commonlib.enums.HTTPCommonHeadersEnum.CONTENT_TYPE;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;


@Component
public class GetFXBOCountriesRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

// Global exception handler for validation errors
        onException(ValidationException.class)
                .log("Validation failed: ${exception.message}")
                .handled(true)
                .setBody(simple("Validation failed: ${exception.message}"));

        rest()
                .get("/utility/countries")
                .description("Adapter REST Service")
                .produces(APPLICATION_JSON_VALUE)
                .to("direct:fetchCountries");

        from("direct:fetchCountries").routeId("com.empirefx.request.getcountries")
                .noStreamCaching().noMessageHistory().noTracing()
                .process("headersSetterProcessor")
                .removeHeaders("*", "Authorization")// Set Authorization header
                .doTry()
                .log(LoggingLevel.INFO, "\n Calling FXBO Endpoint :: Request :: {{atomic.uri}}")
                .enrich().simple("{{atomic.uri}}").id("callServiceBack")
                .setHeader(CONTENT_TYPE.getName(), constant(APPLICATION_JSON_VALUE))
                .to("direct:fetchCountriesResponse");

        from("direct:fetchCountriesResponse")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .log("Incoming response: ${body}")
                .log("Received response from RabbitMQ queue: ${body}")
                .process(exchange -> {
                    // Convert response to a UTF-8 encoded JSON string if it's not already
//                    String jsonResponse = new String(exchange.getIn().getBody(byte[].class), StandardCharsets.UTF_8);
                    String jsonResponse = exchange.getIn().getBody(String.class);
                    exchange.getIn().setBody(jsonResponse);
                })
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .log("Returning decoded JSON response to caller: ${body}")
                .log("Processed response with content type: ${header.Content-Type}")
                .removeHeaders("*")
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