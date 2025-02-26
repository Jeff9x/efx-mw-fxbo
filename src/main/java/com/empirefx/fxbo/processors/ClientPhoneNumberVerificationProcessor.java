package com.empirefx.fxbo.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class ClientPhoneNumberVerificationProcessor implements Processor {

    @Value("${adaptive.phoneVerificationConfig}") // Configurable layout with default value
    private Integer configIds;

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

        // Extract values from the incoming payload
            Integer userId = (Integer) requestBody.get("userId");

        // Construct the outgoing payload
        Map<String, Object> finalPayload = new HashMap<>();
        finalPayload.put("userIds", Collections.singletonList(userId));
        finalPayload.put("configIds", Collections.singletonList(configIds));

        System.out.println("Processed Payload as Map: " + finalPayload);

        // Serialize the payload to JSON
        String finalJsonPayload = objectMapper.writeValueAsString(finalPayload);
        System.out.println("Processed Payload as JSON: " + finalJsonPayload);

        // Set the serialized JSON back into the exchange body
        exchange.getIn().setBody(finalJsonPayload);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");
    }
}
