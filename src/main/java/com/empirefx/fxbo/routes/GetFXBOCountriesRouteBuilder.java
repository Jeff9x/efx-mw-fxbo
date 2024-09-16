package com.empirefx.fxbo.routes;


import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import static com.empirefx.fxbo.commonlib.enums.HTTPCommonHeadersEnum.CONTENT_TYPE;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;


@Component
public class GetFXBOCountriesRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        onException(Exception.class)
                .handled(true)
                .removeHeaders("*")
                .process("errorHandlingProcessor");

        rest()
                .get("/utility/countries")
                .description("Adapter REST Service")
                .produces("application/json")
                .to("direct:fetchCountries");

        from("direct:fetchCountries").routeId("com.empirefx.request.dispatchRequest")
                .noStreamCaching().noMessageHistory().noTracing()
                .process("headersSetterProcessor")
                .removeHeaders("*", "Authorization")// Set Authorization header
                .doTry()
                .log(LoggingLevel.INFO, "\n Calling FXBO Endpoint :: Request :: {{atomic.uri}}")
                .enrich().simple("{{atomic.uri}}").id("callServiceBack")
                .setHeader(CONTENT_TYPE.getName(), constant(APPLICATION_JSON_VALUE))
                .to("direct:fetchCountriesResponse");
        ;
        from("direct:fetchCountriesResponse")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .log("Processed response with content type: ${header.Content-Type}")
                .removeHeaders("*")
                .removeHeader("Authorization")
                .doTry()
                .process("countryResponseProcessor");
    }
}