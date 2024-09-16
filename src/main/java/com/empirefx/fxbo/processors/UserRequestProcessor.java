package com.empirefx.fxbo.processors;
import com.empirefx.fxbo.models.provider.UserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
public class UserRequestProcessor implements Processor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void process(Exchange exchange) throws Exception {
        // Get the request body as a String
        String body = exchange.getIn().getBody(String.class);

        // Deserialize the request body to a UserRequest object
        UserRequest userRequest = objectMapper.readValue(body, UserRequest.class);

        // Log the incoming request
        System.out.println("Received UserRequest: " + userRequest);

        // You can process the userRequest object, for example:
        if (userRequest.getLeverage() > 100) {
            System.out.println("High leverage detected: " + userRequest.getLeverage());
        }

        // Optionally, you can set the processed request back into the exchange
        exchange.getIn().setBody(userRequest);
    }
}