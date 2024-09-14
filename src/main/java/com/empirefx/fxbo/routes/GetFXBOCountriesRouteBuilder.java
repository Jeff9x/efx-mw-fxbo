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
                .setHeader("CamelHttpMethod", constant("GET"))
                .setHeader("Authorization", constant("Bearer 8700c43e5ca0a3f3393b09677ef4e07b2c4e97a82e859b5e278e0a3dbbd86cfbb52f478a6d756079a8476fdb476e1a53cc658b9eb147345b4a39c807"))  // Set Authorization header
                .doTry()
                .log(LoggingLevel.INFO, "\n Calling FXBO Endpoint :: Request :: {{atomic.uri}}")
                .enrich().simple("{{atomic.uri}}").id("callServiceBack")
//                .log("Received response from external service: ${body}")
                .setHeader(CONTENT_TYPE.getName(), constant(APPLICATION_JSON_VALUE))
//                .to("spring-rabbitmq:empirefx?queues=GetFXBO_Countries&routingKey=GetFXBO_Countries")
                .to("direct:fetchCountriesResponse");
        ;
//        from("spring-rabbitmq:empirefx?queues=GetFXBO_Countries&routingKey=GetFXBO_Countries")\
        from("direct:fetchCountriesResponse")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .log("Processed response with content type: ${header.Content-Type}")
//                .setBody(simple("${body}"))
//                .process("createResponseProcessor")
//                .log("Received message from RabbitMQ: ${body}")
//                .log("Original Headers: ${headers}")
                // Remove all headers
                .removeHeaders("*")
//                .log("Headers after removing all headers: ${headers}")
//                .convertBodyTo(List.class)
//                .unmarshal().json()
//                .log(LoggingLevel.INFO, "\n Empirefx FXBO Countries :: Response EHF :: Payload [${body}]")
                .removeHeader("Authorization")
                .doTry()
//                .process("successResponseGeneratorProcessor")
//                .setProperty(APP_REQUEST,method(LogAppLoggerProcessor.class))
//                .log("${body}")
                .convertBodyTo(String.class)
                .unmarshal().json()
//                .doCatch(Exception.class)
//                .setBody(constant("Error fetching countries"))
//                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500))
//                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
//                .log("Processed response with content type: ${header.Content-Type}")
                .setBody(simple("${body}"))
//                .log("Error: ${exception.message}")

                ;

//        from("direct:begin").routeId("zenzngsendsmsotp.zenith.request.begin")
//                .noStreamCaching().noMessageHistory().noTracing()
//                .process("headersSetterProcessor")
//                .setProperty(APP_REQUEST, method(LogAppLoggerProcessor.class))
//                .doTry()
//                .process("validateRequestProcessor")
//                .doCatch(UnsuccessfullException.class)
//                .process("errorHandlingProcessor")
//                .end()
//                .marshal().json(JsonLibrary.Gson, RequestWrapper.class)
//                .process("providerCredentialsCheckerProcessor")
//                .choice()
//                .when(simple(EXCHANGE_PROPERTY + NO_PROVIDER_CREDENTIALS_PRESENT + "}"))
//                  .enrich().simple("https://demo.fxbackoffice.com/rest/dict/countries?version=1.0.0&_locale=en")
//                .id("callVaultConnector")
//                .process("providerCredentialsSetterProcessor")
//                .endChoice()
//                .end();

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