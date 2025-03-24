package com.empirefx.fxbo.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ClientDepositRequestProcessor implements Processor {

    private final ObjectMapper objectMapper;

    public ClientDepositRequestProcessor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void process(Exchange exchange) throws Exception {

        // Extract the incoming request body as a Map
        @SuppressWarnings("unchecked")
        Map<String, Object> requestBody = exchange.getIn().getBody(Map.class);

        System.out.println("Incoming Request: " + requestBody);

        String mt4Comment = (String) requestBody.get("narration");

        System.out.printf("Incoming Narration: %s\n", mt4Comment);

        requestBody.put("status", "approved");
        requestBody.put("mt4Comment", mt4Comment);
        requestBody.remove("narration");

        // Optionally: log the updated request
        System.out.println("Processed Request: " + requestBody);

        // Convert the Map to JSON using ObjectMapper
        String jsonRequestBody = objectMapper.writeValueAsString(requestBody);

        System.out.println("Processed JSON Request: " + jsonRequestBody);
        // Set the JSON string back to the exchange body for further processing
        exchange.getIn().setBody(jsonRequestBody);
    }
}
