package com.empirefx.fxbo.processors;
import com.empirefx.fxbo.models.provider.UserAccountResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
public class UserAccountResponseProcessor implements Processor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void process(Exchange exchange) throws Exception {
        // Get the body as a String (raw JSON)
        String jsonBody = exchange.getIn().getBody(String.class);

        // Convert the JSON to a ClientResponse object
        UserAccountResponse clientResponse = objectMapper.readValue(jsonBody, UserAccountResponse.class);

        // Log the received response
        System.out.println("Received ClientResponse: " + clientResponse);

        // Optionally, set the ClientResponse object in the exchange body for further processing
        exchange.getIn().setBody(clientResponse);
    }
}