package com.empirefx.fxbo.routes;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class GetFXBOFxRatesRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

// Handle validation exceptions with a structured error response
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
                .post("/payments/exchange-rate")
                .description("Adapter REST Service")
                .produces("application/json")
                .to("direct:fetchFxRates");

        from("direct:fetchFxRates").routeId("com.empirefx.request.dispatchRequest31")
                .noStreamCaching().noMessageHistory().noTracing()
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("Accept", constant("application/json"))
                .process("headersSetterProcessor")
                .marshal().json()
                .doTry()
                .log(LoggingLevel.INFO, "\n Calling FXBO Fetch Fx Rates Endpoint :: Request :: {{atomic.uriFxRates}}")
                .enrich().simple("{{atomic.uriFxRates}}").id("callServiceBack31")
                .to("direct:fetchFxRatesResponse");

        from("direct:fetchFxRatesResponse")
            .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                        .setHeader("Accept", constant("application/json"))
            .log(LoggingLevel.INFO, "Raw Incoming Body: ${body}")
            .process("rateProcessor")
            .end();
            }
        }