package com.empirefx.fxbo.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ClientRequestWithdrawalProcessor implements Processor {

    private final ObjectMapper objectMapper;

    public ClientRequestWithdrawalProcessor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void process(Exchange exchange) throws Exception {

        // Extract the incoming request body as a Map
        @SuppressWarnings("unchecked")
        Map<String, Object> requestBody = exchange.getIn().getBody(Map.class);

        System.out.println("Incoming Request: " + requestBody);

        // Set default values if not present in the payload
        requestBody.put("status", "pending");   // Default value for 'enabled'

        // Optionally: log the updated request
        System.out.println("Processed Request: " + requestBody);

        // Convert the Map to JSON using ObjectMapper
        String jsonRequestBody = objectMapper.writeValueAsString(requestBody);

        System.out.println("Processed JSON Request: " + jsonRequestBody);
        // Set the JSON string back to the exchange body for further processing
        exchange.getIn().setBody(jsonRequestBody);
    }
}