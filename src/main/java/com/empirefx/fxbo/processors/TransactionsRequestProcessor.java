package com.empirefx.fxbo.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class TransactionsRequestProcessor implements Processor {

    private final ObjectMapper objectMapper;

    public TransactionsRequestProcessor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        // Extract incoming request body as a Map
        @SuppressWarnings("unchecked")
        Map<String, Object> requestBody = exchange.getIn().getBody(Map.class);

        System.out.println("Incoming Request: " + requestBody);

        Integer fromUserId = (Integer) requestBody.get("fromUserId");
        Integer limit = (Integer) requestBody.get("limit");
        Integer offset = (Integer) requestBody.get("offset");

        // Create new transformed request structure
        Map<String, Object> transformedRequest = new HashMap<>();


        // Add ordering rules
        transformedRequest.put("orders", List.of(
                Map.of("field", "createdAt", "direction", "DESC")
        ));

        // Create segment block with limit and offset
        Map<String, Object> segment = new HashMap<>();
        segment.put("limit", limit);
        segment.put("offset", offset);
        transformedRequest.put("segment", segment);
        transformedRequest.put("fromUserId", fromUserId);

        // Convert Map to JSON string
        String jsonTransformedRequest = objectMapper.writeValueAsString(transformedRequest);

        System.out.println("Processed JSON Request: " + jsonTransformedRequest);

        // Set transformed JSON body back to exchange
        exchange.getIn().setBody(jsonTransformedRequest);
    }
}