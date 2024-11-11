package com.empirefx.fxbo.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FailureResponseProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        // Retrieve validation error messages from exception or initial failure payload
        Object errorDetail = exchange.getIn().getBody();

        // Retrieve the full error response
        Map<String, Object> errorResponse = exchange.getIn().getBody(Map.class);

        // Extract the 'children' object under 'errors'
        Map<String, Object> children = (Map<String, Object>) ((Map<String, Object>) errorResponse.get("errors")).get("children");

        // Filter the children to only include fields with actual errors
        Map<String, Object> filteredErrors = new HashMap<>();
        for (Map.Entry<String, Object> entry : children.entrySet()) {
            Object fieldValue = entry.getValue();

            // Check if the entry is a Map containing "errors" as a key
            if (fieldValue instanceof Map) {
                Map<String, Object> fieldErrors = (Map<String, Object>) fieldValue;
                List<String> errors = (List<String>) fieldErrors.get("errors");
                if (errors != null && !errors.isEmpty()) {
                    filteredErrors.put(entry.getKey(), Map.of("errors", errors));
                }
            }
        }
        Map<String, Object> failureResponse = Map.of(
                "code", 400,
                "message", "FXBO Response Validation Failed",
                "errors", Map.of("errorDetails", filteredErrors)
        );

        exchange.getIn().setBody(failureResponse);
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 400);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");
    }
}