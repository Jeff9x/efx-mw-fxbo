package com.empirefx.fxbo.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EmailRequestProcessor implements Processor {

    @Value("${adaptive.emailConfig}") // Configurable layout with default value as 1
    private Integer layout;

    private final ObjectMapper objectMapper = new ObjectMapper(); // For JSON serialization

    @Override
    public void process(Exchange exchange) throws Exception {
        // Extract the incoming payload as a Map
        @SuppressWarnings("unchecked")
        Map<String, Object> requestBody = exchange.getIn().getBody(Map.class);

        if (requestBody == null) {
            throw new IllegalArgumentException("Incoming payload is null or missing.");
        }

        System.out.println("Incoming Request: " + requestBody);

        // Validate required fields in the incoming payload
        if (!requestBody.containsKey("user") || !requestBody.containsKey("subject") || !requestBody.containsKey("content")) {
            throw new IllegalArgumentException("Missing required fields: 'user', 'subject', or 'content'");
        }

        // Extract values from the incoming payload
        Integer user = (Integer) requestBody.get("user");
        String subject = (String) requestBody.get("subject");
        String content = (String) requestBody.get("content");

        // Construct the outgoing payload
        Map<String, Object> finalPayload = new HashMap<>();
        finalPayload.put("user", user);
        finalPayload.put("subject", subject);
        finalPayload.put("content", content);
        finalPayload.put("layout", layout); // Add layout from configuration

        System.out.println("Processed Payload as Map: " + finalPayload);

        // Serialize the payload to JSON
        String finalJsonPayload = objectMapper.writeValueAsString(finalPayload);
        System.out.println("Processed Payload as JSON: " + finalJsonPayload);

        // Set the serialized JSON back into the exchange body
        exchange.getIn().setBody(finalJsonPayload);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");}
}