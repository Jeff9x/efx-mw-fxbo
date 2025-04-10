package com.empirefx.fxbo.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GetIbReferralLinkRequestProcessor implements Processor {


    private final ObjectMapper objectMapper = new ObjectMapper(); // For JSON serialization

    @Override
    public void process(Exchange exchange) throws Exception {
        // Extract the incoming payload as a Map
        @SuppressWarnings("unchecked")

        // Retrieve the fxbouserid path parameter from the Camel exchange
        String fxboUserId = exchange.getIn().getHeader("fxboUserId", String.class);

        System.out.println("fxboUserId: " + fxboUserId);

        if (fxboUserId == null) {

            throw new IllegalArgumentException("Incoming payload is null or missing.");
        }

        // Parse the userId from the path parameter
        int userId = Integer.parseInt(fxboUserId);

        // Construct the outgoing payload
        Map<String, Object> finalPayload = new HashMap<>();
        finalPayload.put("userId", userId);

        System.out.println("Processed Payload as Map: " + finalPayload);

        // Serialize the payload to JSON
        String finalJsonPayload = objectMapper.writeValueAsString(finalPayload);
        System.out.println("Processed Payload as JSON: " + finalJsonPayload);

        // Set the serialized JSON back into the exchange body
        exchange.getIn().setBody(finalJsonPayload);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");
    }
}
