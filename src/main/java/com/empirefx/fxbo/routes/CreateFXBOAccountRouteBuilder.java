package com.empirefx.fxbo.routes;


import com.empirefx.fxbo.commonlib.exceptions.UnsuccessfullException;
import com.empirefx.fxbo.commonlib.models.EhfResponse;
import com.empirefx.fxbo.commonlib.models.RequestWrapper;
import com.empirefx.fxbo.commonlib.models.ResponseWrapper;
import com.empirefx.fxbo.processors.LogAppLoggerProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.stereotype.Component;

import static com.empirefx.fxbo.commonlib.enums.HTTPCommonHeadersEnum.CONTENT_TYPE;
import static com.empirefx.fxbo.commonlib.constants.ConstantsCommons.*;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Component
public class CreateFXBOAccountRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        onException(Exception.class)
                .handled(true)
                .removeHeaders("*")
                .process("errorHandlingProcessor");

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
                .setHeader("Authorization", constant("Bearer 8700c43e5ca0a3f3393b09677ef4e07b2c4e97a82e859b5e278e0a3dbbd86cfbb52f478a6d756079a8476fdb476e1a53cc658b9eb147345b4a39c807"))  // Set Authorization header
                .doTry()
                .log(LoggingLevel.INFO, "\n Calling FXBO Endpoint :: Create Account Request :: {{atomic1.uri}}")
                .enrich().simple("{{atomic1.uri}}").id("callServiceBack1")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .log("Processed response with content type: ${header.Content-Type}")
                .setBody(simple("${body}"))
                .convertBodyTo(String.class)
                .unmarshal().json()
                .removeHeader("Authorization");
    }
}