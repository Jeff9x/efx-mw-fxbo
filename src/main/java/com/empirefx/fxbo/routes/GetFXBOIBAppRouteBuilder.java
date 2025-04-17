package com.empirefx.fxbo.routes;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GetFXBOIBAppRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {

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
                .get("/ib-application/{configId}")
                .description("Adapter REST Service")
                .produces("application/json")
                .to("direct:fetchIbApp");

        from("direct:fetchIbApp").routeId("com.empirefx.request.fetchIbApp")
                .noStreamCaching().noMessageHistory().noTracing()
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .process("getIbApplicationRequestProcessor")
                .log(LoggingLevel.INFO, "Body Set ${body}")
                .removeHeaders("CamelHttp*","finalUrl")
                .doTry()
                .log(LoggingLevel.INFO, "\n Calling FXBO Fetch Trading Platform Servers By ID Endpoint :: Request :: ${header.finaUrl}")
                .enrich().simple("${header.finaUrl}").id("fetchIbApps")
                .to("direct:fetchIbAppResponse");

        from("direct:fetchIbAppResponse")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .log("Incoming  response: ${body}")
                .unmarshal().json()
                .choice()
                .when(simple("${body[code]} == 400"))
                .log(LoggingLevel.WARN, "Processing failure response...")
                .process("failureResponseProcessor")
                .otherwise()
                .log(LoggingLevel.INFO, "Processing success response...")
                .process("successResponseProcessor")
                .end();
    }
}
