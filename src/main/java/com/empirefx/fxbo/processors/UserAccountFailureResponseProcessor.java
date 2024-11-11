package com.empirefx.fxbo.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserAccountFailureResponseProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        // Retrieve the full error response as a Map
        Map<String, Object> errorResponse = exchange.getIn().getBody(Map.class);

        // Extract relevant parts from the error response
        Integer code = (Integer) errorResponse.get("code");
        String message = (String) errorResponse.get("message");
        String type = (String) errorResponse.get("type");
        String exception = null;

        // Check if there is a 'debug' field and extract the 'exception' message if available
        if (errorResponse.containsKey("debug")) {
            Map<String, Object> debugInfo = (Map<String, Object>) errorResponse.get("debug");
            exception = (String) debugInfo.get("exception");
        }

        // Construct the simplified failure response
        Map<String, Object> simplifiedFailureResponse = new HashMap<>();
        simplifiedFailureResponse.put("code", code != null ? code : 400); // default to 400 if code is missing
        simplifiedFailureResponse.put("message", message != null ? message : "An error occurred");
//        simplifiedFailureResponse.put("type", type);

        // Include the exception message if available
        if (exception != null) {
            simplifiedFailureResponse.put("errors", exception);
        }

        // Set the response body and headers
        exchange.getIn().setBody(simplifiedFailureResponse);
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, code != null ? code : 400);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");
    }
}