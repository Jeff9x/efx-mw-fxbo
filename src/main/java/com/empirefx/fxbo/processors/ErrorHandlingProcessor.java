package com.empirefx.fxbo.processors;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ErrorHandlingProcessor implements Processor {

    // Create ObjectMapper instance to parse JSON
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void process(Exchange exchange) throws Exception {
        // Get the response body as a String (JSON payload)
        String responseBody = exchange.getIn().getBody(String.class);

        // Log the incoming response body
        System.out.println("Incoming Response: " + responseBody);

        // Parse the response body into a Map using Jackson
        Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);

        // Check if the response contains errors
        if (responseMap.containsKey("code") && responseMap.containsKey("errors")) {
            int code = (Integer) responseMap.get("code");
            if (code == 400) {
                // Extract the error message
                String message = (String) responseMap.get("message");
                Map<String, Object> errors = (Map<String, Object>) responseMap.get("errors");

                // Process the validation errors
                StringBuilder errorMessage = new StringBuilder("Validation Failed: ").append(message).append("\n");

                // Log and format specific errors (e.g., phone and email registration issues)
                Map<String, Object> childrenErrors = (Map<String, Object>) errors.get("children");
                if (childrenErrors.containsKey("phone")) {
                    Map<String, Object> phoneErrors = (Map<String, Object>) childrenErrors.get("phone");
                    if (phoneErrors.containsKey("errors")) {
                        errorMessage.append("Phone Error: ")
                                .append(phoneErrors.get("errors"))
                                .append("\n");
                    }
                }

                if (childrenErrors.containsKey("email")) {
                    Map<String, Object> emailErrors = (Map<String, Object>) childrenErrors.get("email");
                    if (emailErrors.containsKey("errors")) {
                        errorMessage.append("Email Error: ")
                                .append(emailErrors.get("errors"))
                                .append("\n");
                    }
                }

                // Log the detailed error message
                System.out.println(errorMessage.toString());

                // Set the structured error response back to the exchange body for further processing
                exchange.getIn().setBody(errorMessage.toString());
            }
        }
    }
}