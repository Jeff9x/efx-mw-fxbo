package com.empirefx.fxbo.routes;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.ValidationException;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class GetFXBOTradingAccountByIdRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

// Global exception handler for validation errors
        onException(ValidationException.class)
                .log("Validation failed: ${exception.message}")
                .handled(true)
                .setBody(simple("Validation failed: ${exception.message}"));

        rest()
                .get("/trading-accounts/{accountId}")
                .description("Adapter REST Service")
                .produces("application/json")
                .to("direct:fetchTradingAccount");

        from("direct:fetchTradingAccount").routeId("com.empirefx.request.dispatchRequest300")
                .noStreamCaching().noMessageHistory().noTracing()
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .process("headersSetterProcessor")
                .process("getTradingAccountRequestProcessor")
                .log(LoggingLevel.INFO, "Body Set ${body}")
                .removeHeaders("CamelHttp*")
                .doTry()
                .log(LoggingLevel.INFO, "\n Calling FXBO Fetch Trading Accounts Endpoint :: Request :: {{atomic.uriTradingAccounts}}")
                .enrich().simple("{{atomic.uriTradingAccounts}}").id("callServiceBack300")
                .to("direct:fetchIndividualTradingAccountResponse");

        from("direct:fetchIndividualTradingAccountResponse")
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .log("Incoming response: ${body}")
                .process("emptyResponseProcessor")
                .doTry()
                    .unmarshal().json()
                    .process("emptyResponseProcessor")
                        .choice()
                            .when(simple("${body[]} == null")) // Adjust condition based on actual error field
                                .log("Request failed: ${body[]}")
                            .otherwise()
                                .log("Request was successful.")
                        .endChoice()
                .endDoTry()
                .process("successResponseProcessor")
                    .doCatch(Exception.class)
                        .log("Exception during processing: ${exception.message}")
                .end();
    }
}