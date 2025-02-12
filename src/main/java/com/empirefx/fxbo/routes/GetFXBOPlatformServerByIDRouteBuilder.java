package com.empirefx.fxbo.routes;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.empirefx.fxbo.commonlib.enums.HTTPCommonHeadersEnum.CONTENT_TYPE;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Component
public class GetFXBOPlatformServerByIDRouteBuilder extends RouteBuilder {

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
                .get("/trading-platform-server/{serverId}")
                .description("Adapter REST Service")
                .produces("application/json")
                .to("direct:fetchPlatformServer");

        from("direct:fetchPlatformServer").routeId("com.empirefx.request.fetchPlatformServer")
                .noStreamCaching().noMessageHistory().noTracing()
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .process("getPlatformServerRequestProcessor")
                .log(LoggingLevel.INFO, "Body Set ${body}")
                .removeHeaders("CamelHttp*","finalUrl")
                .doTry()
                .log(LoggingLevel.INFO, "\n Calling FXBO Fetch Trading Platform Servers By ID Endpoint :: Request :: ${header.finaUrl}")
                .enrich().simple("${header.finaUrl}").id("callServiceBack309")
                .to("direct:fetchPlatformServerResponse");

        from("direct:fetchPlatformServerResponse")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .log("Incoming response: ${body}")
                .doTry()
                .unmarshal().json()
                .process("emptyAccountBalanceResponseProcessor")
                .choice()
                .when(simple("${body[]} == null")) // Adjust condition based on actual error field
                .log("Request failed: ${body[]}")
                .otherwise()
                .log("Request was successful.")
                .endChoice()
                .endDoTry()
                .process("successResponseProcessor")
                .doCatch(Exception.class)
                .log("Exception during processing: ${exception.message}")
                .end();
    }
}