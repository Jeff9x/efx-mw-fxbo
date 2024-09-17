package com.empirefx.fxbo.processors;
import com.empirefx.fxbo.models.provider.UserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class UserRequestProcessor implements Processor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void process(Exchange exchange) throws Exception {
        // Get the JSON body as a String
        String jsonBody = exchange.getIn().getBody(String.class);

        System.out.println("incoming payload"+jsonBody);

        // Convert the JSON to a UserRequest object
        UserRequest userRequest = objectMapper.readValue(jsonBody, UserRequest.class);

        // Log the received request for debugging
        System.out.println("Request received: " + userRequest);

        // Set the UserRequest object in the exchange body for further processing
        exchange.getIn().setBody(userRequest);
    }
}