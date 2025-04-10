package com.empirefx.fxbo.routes;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Component
public class CreateIBReferralLinkRouteBuilder extends RouteBuilder {
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
                .post("/create-ib-referral-link")
                .description("Create IB Referral Link REST Service")
                .consumes(APPLICATION_JSON_VALUE)
                .produces("application/json")
                .to("direct:createIbReferralLink");

        from("direct:createIbReferralLink")
                .noStreamCaching().noMessageHistory().noTracing()
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("Accept", constant("application/json"))
                .process("headersSetterProcessor")
                .marshal().json()
                .doTry()
                .log(LoggingLevel.INFO, "\n Calling FXBO Endpoint :: Create IB Referral Link Request :: {{atomic1.uriCreateIbReferralLink}}")
                .enrich().simple("{{atomic1.uriCreateIbReferralLink}}").id("createIbReferralLink")
                .to("direct:fetchIBReferralLinkResponse");

        from("direct:fetchIBReferralLinkResponse")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .log("Incoming response: ${body}")
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