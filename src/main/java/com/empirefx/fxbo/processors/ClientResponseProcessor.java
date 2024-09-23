package com.empirefx.fxbo.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ClientResponseProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        // Get the response body as a Map
        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = exchange.getIn().getBody(Map.class);
        System.out.println("Incoming payload"+responseBody);

        // Example: Extract specific fields from the response
        String firstName = (String) responseBody.get("firstName");
        String lastName = (String) responseBody.get("lastName");
        String email = (String) responseBody.get("email");
        String phone = (String) responseBody.get("phone");
        boolean emailVerified = (Boolean) responseBody.get("emailVerified");
        boolean isTrader = (Boolean) responseBody.get("isTrader");
        String clientIp = (String) responseBody.get("clientIp");

        // Optionally log the extracted fields for debugging purposes
        System.out.println("Client Response:");
        System.out.println("Name: " + firstName + " " + lastName);
        System.out.println("Email: " + email);
        System.out.println("Phone: " + phone);
        System.out.println("Email Verified: " + emailVerified);
        System.out.println("Is Trader: " + isTrader);
        System.out.println("Client IP: " + clientIp);

        // You can also modify the response if needed before sending it to the next endpoint
        // For example, you can add additional information to the response
        responseBody.put("processedAt", System.currentTimeMillis());

        // Set the modified response back to the exchange body
        exchange.getIn().setBody(responseBody);
    }
}