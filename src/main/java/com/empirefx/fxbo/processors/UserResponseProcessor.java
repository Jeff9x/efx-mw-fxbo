package com.empirefx.fxbo.processors;
import com.empirefx.fxbo.models.provider.UserResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserResponseProcessor implements Processor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void process(Exchange exchange) throws Exception {
        // Get the response body
        String body = exchange.getIn().getBody(String.class);

        // Check if the response is a single object or a list
        JsonNode jsonNode = objectMapper.readTree(body);

        if (jsonNode.isArray()) {
            // The response is a list
            List<UserResponse> userResponseList = objectMapper.readValue(body, new TypeReference<List<UserResponse>>() {});
            System.out.println("Received a list of UserResponse: ");
            exchange.getIn().setBody(userResponseList);  // Set the list back in the exchange body
        } else {
            // The response is a single object
            UserResponse userResponse = objectMapper.readValue(body, UserResponse.class);
            System.out.println("Received a single UserResponse: ");
            exchange.getIn().setBody(userResponse);  // Set the single object back in the exchange body
        }
    }
}