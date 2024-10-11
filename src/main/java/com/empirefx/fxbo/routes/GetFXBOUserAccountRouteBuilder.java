package com.empirefx.fxbo.routes;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;


@Component
public class GetFXBOUserAccountRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

//        onException(Exception.class)
//                .handled(true)
//                .removeHeaders("*")
//                .process("errorHandlingProcessor");

        rest()
                .get("/customers/{fxboUserId}")
                .description("Adapter REST Service")
                .produces("application/json")
                .to("direct:fetchUserAccount");

        from("direct:fetchUserAccount").routeId("com.empirefx.request.dispatchRequest31")
                .noStreamCaching().noMessageHistory().noTracing()
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .process("headersSetterProcessor")
                .process("getUserAccountRequestProcessor")
                .log(LoggingLevel.INFO, "Body Set ${body}")
                .removeHeaders("CamelHttp*")
                .doTry()
                .log(LoggingLevel.INFO, "\n Calling FXBO Fetch User Account Endpoint :: Request :: ${headers.finalUrl}")
                .enrich().simple("${headers.finalUrl}").id("callServiceBack31")
                .to("direct:fetchUserAccountResponse");

        from("direct:fetchUserAccountResponse")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .log("Processed response with content type: ${header.Content-Type}")
                .setBody(simple("${body}"))
                .convertBodyTo(String.class)
                .unmarshal().json()
                .end();
    }
}