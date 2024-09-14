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
//                .type(RequestWrapper.class)
//                .description("POST Endpoint 2 Send FXBO Create Account")
//                .param().name("body").required(true).type(RestParamType.body).endParam()
//                .param().name("messageID").required(true).description("The messageID ").type(RestParamType.header).endParam()
//                .param().name("serviceCode").required(true).description("The serviceCode ").type(RestParamType.header).endParam()
//                .param().name("serviceName").required(true).description("The serviceName ").type(RestParamType.header).endParam()
//                .param().name("channelCode").required(true).description("The channelCode ").type(RestParamType.header).endParam()
//                .param().name("channelName").required(true).description("The channelName ").type(RestParamType.header).endParam()
//                .param().name("routeCode").required(true).description("The routeCode ").type(RestParamType.header).endParam()
//                .param().name("routeName").required(true).description("The routeName ").type(RestParamType.header).endParam()
//                .param().name("timeStamp").required(true).description("The timeStamp ").type(RestParamType.header).endParam()
//                .param().name("serviceMode").required(true).description("The serviceMode ").type(RestParamType.header).endParam()
//                .param().name("callBackURL").description("The callBackURL ").type(RestParamType.header).endParam()
//                .produces(APPLICATION_JSON_VALUE)
//                .produces(APPLICATION_JSON_VALUE)
//                .outType(ResponseWrapper.class)
//                .responseMessage()
//                .code(OK.value())
//                .endResponseMessage()
                .produces("application/json")
                .to("direct:createAccount");

//        from("seda:dispatchRequest").routeId("com.zenith.request.dispatchRequest")
//                .noStreamCaching().noMessageHistory().noTracing()
//                .to("direct:begin");
//                .to("direct:callServiceBack");

        from("direct:createAccount").routeId("com.empirefx.request.dispatchRequest1")
//                .log("Request payload from external service: ${body}")
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("Accept", constant("application/json"))
                .convertBodyTo(String.class)
                .marshal().json()
//                .log("Request payload Unmarshalled from external service: ${body}")
//                .setHeader("CamelHttpMethod", constant("POST"))
                .setHeader("Authorization", constant("Bearer 8700c43e5ca0a3f3393b09677ef4e07b2c4e97a82e859b5e278e0a3dbbd86cfbb52f478a6d756079a8476fdb476e1a53cc658b9eb147345b4a39c807"))  // Set Authorization header
                .doTry()
                .log(LoggingLevel.INFO, "\n Calling FXBO Endpoint :: Create Account Request :: {{atomic1.uri}}")
                .enrich().simple("{{atomic1.uri}}").id("callServiceBack1")
//                .log("Received response from external service: ${body}")
//                .to("spring-rabbitmq:empirefx?queues=Create_Account&routingKey=Post_FXBO_Account")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .log("Processed response with content type: ${header.Content-Type}")
                .setBody(simple("${body}"))
//                .process("createResponseProcessor")
//                .log("${body}")
                .convertBodyTo(String.class)
                .unmarshal().json()
//                .log(LoggingLevel.INFO, "\n Empirefx FXBO Create Account Response :: Payload [${body}]")
                .removeHeader("Authorization")
//                .doCatch(Exception.class)
//                .setBody(constant("Error fetching countries"))
//                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500))
//                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
//                .log("Processed response with content type: ${header.Content-Type}")
//                .setBody(simple("${body}"))
//                .log("Error: ${exception.message}")
        ;

//        from("direct:callServiceBack").routeId("zenzngsendsmsotp.zenith.request.CustAcctInquiry")
//                .noStreamCaching().noMessageHistory().noTracing()
//                .removeHeaders("*", CONVERSATION_ID_HEADER, MESSAGE_ID_HEADER)
//                .setBody(simple("${exchangeProperty.originalMessage}"))
//                .log(LoggingLevel.INFO, "\n NG Send SMS OTP :: Request :: {{atomic.uri}}") //Change this log
//                .process("createRequestProcessor")
//                .setHeader(ACCEPT, constant(ALL_VALUE))
//                .setHeader(CONTENT_TYPE.getName(), constant(APPLICATION_JSON_VALUE))
//                .convertBodyTo(String.class)
//                .setProperty(APP_REQUEST, method(LogAppLoggerProcessor.class))
//                .enrich().simple("{{atomic.uri}}").id("callServiceBack")
//                .convertBodyTo(String.class)
//                .removeHeader("Authorization")
//                .process("createResponseProcessor")
//                .setProperty(APP_REQUEST,method(LogAppLoggerProcessor.class))
//                .doTry()
//                    .process("successResponseGeneratorProcessor")
//                    .setProperty(APP_REQUEST,method(LogAppLoggerProcessor.class))
//                .doCatch(UnsuccessfullException.class)
//                .log(LoggingLevel.INFO, "\n NG Send SMS OTP :: Request EHF :: {{ehf.uri}}") //Change this log
//                    .process("ehfRequestCreatorProcessor")
//                .log(LoggingLevel.INFO, "\n NG Send SMS OTP :: Request EHF :: ${body}") //Change this log
//                    .removeHeaders("*", CONVERSATION_ID_HEADER, MESSAGE_ID_HEADER)
//                    .enrich("{{ehf.uri}}").id("callEhf")
//                    .removeHeaders("*", CONVERSATION_ID_HEADER, MESSAGE_ID_HEADER)
//                    .unmarshal().json(JsonLibrary.Gson, EhfResponse.class)
//                .log(LoggingLevel.INFO, "\n NG Send SMS OTP :: Response EHF :: Payload [${body}]") //Change this log
//                    .process("ehfResponseHandlerProcessor")
//                .end();
    }
}