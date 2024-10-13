package com.empirefx.fxbo.routes;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.ValidationException;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;


@Component
public class GetFXBOFxRatesRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

// Global exception handler for validation errors
        onException(ValidationException.class)
                .log("Validation failed: ${exception.message}")
                .handled(true)
                .setBody(simple("Validation failed: ${exception.message}"));

        rest()
                .post("/payments/exchange-rate")
                .description("Adapter REST Service")
                .produces("application/json")
                .to("direct:fetchFxRates");

        from("direct:fetchFxRates").routeId("com.empirefx.request.dispatchRequest31")
                .noStreamCaching().noMessageHistory().noTracing()
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .process("headersSetterProcessor")
//                .process("getTradingAccountsRequestProcessor")
                .log(LoggingLevel.INFO, "Body Set ${body}")
                .removeHeaders("CamelHttp*")
                .doTry()
                .log(LoggingLevel.INFO, "\n Calling FXBO Fetch Fx Rates Endpoint :: Request :: {{atomic.uriFxRates}}")
                .enrich().simple("{{atomic.uriFxRates}}").id("callServiceBack31")
                .to("direct:fetchFxRatesResponse");

        from("direct:fetchFxRatesResponse")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .log("Processed response with content type: ${header.Content-Type}")
                .log(LoggingLevel.INFO, "Body Set ${body}")
                .setBody(simple("${body}"))
                .convertBodyTo(String.class)
                .unmarshal().json()
                .end();
    }
}