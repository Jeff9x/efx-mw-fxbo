package com.empirefx.fxbo.routes;


import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import static com.empirefx.fxbo.commonlib.enums.HTTPCommonHeadersEnum.CONTENT_TYPE;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;


@Component
public class CreateFXBOCreateAccountRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

//        onException(Exception.class)
//                .handled(true)
//                .removeHeaders("*")
//                .process("errorHandlingProcessor");

        rest()
                .post("/trading-accounts")
                .description("Adapter REST Service")
                .consumes(APPLICATION_JSON_VALUE)
                .produces("application/json")
                .to("direct:createAccount");

        from("direct:createAccount").routeId("com.empirefx.request.dispatchRequest1")
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("Accept", constant("application/json"))
                .convertBodyTo(String.class)
                .marshal().json()
                .doTry()
                .log(LoggingLevel.INFO, "\n Calling FXBO Endpoint :: Create Account Request :: {{atomic1.uri}}")
                .enrich().simple("{{atomic1.uri}}").id("callServiceBack1")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .to("direct:fetchAccountsResponse");

        from("direct:fetchAccountsResponse")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                .log("Processed response with content type: ${header.Content-Type}")
                .setBody(simple("${body}"))
                .removeHeaders("*")
                .removeHeader("Authorization")
                .doTry()
                .process("userResponseProcessor");
    }
}