package com.empirefx.fxbo.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DeclineTransactionFailureResponseProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        // Retrieve validation error messages from exception or initial failure payload
        Object errorDetail = exchange.getIn().getBody();

        // Retrieve the full error response
        Map<String, Object> errorResponse = exchange.getIn().getBody(Map.class);

        // Extract the main error from 'errors' -> 'errors'
        String transactionError = null;
        if (errorResponse.containsKey("errors")) {
            Map<String, Object> errors = (Map<String, Object>) errorResponse.get("errors");
            if (errors.containsKey("errors")) {
                transactionError = ((Iterable<?>) errors.get("errors")).iterator().next().toString();
            }
        }

        Map<String, Object> failureResponse = Map.of(
                "code", 400,
                "message", "FXBO Response Validation Failed",
                "errors", Map.of("errorDetails", transactionError)
        );

        exchange.getIn().setBody(failureResponse);
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 400);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");
    }
}