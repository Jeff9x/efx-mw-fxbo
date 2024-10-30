package com.empirefx.fxbo.routes;

import com.empirefx.fxbo.commonlib.exceptions.UnsuccessfullException;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.ValidationException;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;


import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
@Component
public class CreateFXBOUserCreateAccountRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

// Global exception handler for validation errors
        onException(ValidationException.class)
                .log("Validation failed: ${exception.message}")
                .handled(true)
                .setBody(simple("Validation failed: ${exception.message}"));

        rest()
                .post("/create-user-account")
                .description("Adapter REST Service")
                .consumes(APPLICATION_JSON_VALUE)
                .produces("application/json")
                .to("direct:createUserAccount");

        from("direct:createUserAccount").routeId("com.empirefx.request.dispatchRequest5")
                .noStreamCaching().noMessageHistory().noTracing()
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("Accept", constant("application/json"))
                .process("createUserAccountHeadersSetterProcessor")
                .process("clientRequestProcessor")
                .doTry()
                .log(LoggingLevel.INFO, "\n Calling FXBO Endpoint :: Create Account Request :: {{atomic1.uriUser}}")
                .enrich().simple("{{atomic1.uriUser}}").id("createUserAccount5")
//                .convertBodyTo(String.class)
                .to("direct:fetchUserAccountsResponse");

        from("direct:fetchUserAccountsResponse")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .log("Incoming response: ${body}")
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