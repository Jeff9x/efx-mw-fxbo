package com.empirefx.fxbo.routes;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GetIBReferralLinkRouteBuilder extends RouteBuilder {

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
                .get("/get-ib-referral-link/{fxboUserId}")
                .description("Get IB Referral Links for a User")
                .produces("application/json")
                .to("direct:getIbReferralLinks");

        from("direct:getIbReferralLinks").routeId("com.empirefx.request.getIbReferralLinks")
                .noStreamCaching().noMessageHistory().noTracing()
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .process("getIbReferralLinkRequestProcessor")
                .process("headersSetterProcessor")
                .log(LoggingLevel.INFO, "Fetching referral links for FXBO User ID: ${header.fxboUserId}")
                .removeHeaders("CamelHttp*")
                .doTry()
                .log(LoggingLevel.INFO, "\n Calling FXBO Endpoint :: Get IB Referral Links :: {{atomic1.uriGetIbReferralLinks}}")
                .enrich().simple("{{atomic1.uriGetIbReferralLinks}}").id("getIbReferralLinks")
                .to("direct:getIbReferralLinkResponse");

        from("direct:getIbReferralLinkResponse")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .log("Incoming response: ${body}")
                .setBody(simple("${body}"))
                .convertBodyTo(String.class)
                .unmarshal().json()
                .end();
    }
}