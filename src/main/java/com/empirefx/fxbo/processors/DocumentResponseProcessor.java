package com.empirefx.fxbo.processors;
import com.empirefx.fxbo.models.provider.DocumentResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DocumentResponseProcessor implements Processor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void process(Exchange exchange) throws Exception {
        // Get the body as a String (raw JSON)
        String jsonBody = exchange.getIn().getBody(String.class);

        // Convert the JSON to a List of DocumentResponse objects
        List<DocumentResponse> documentResponses = objectMapper.readValue(jsonBody, new TypeReference<List<DocumentResponse>>() {});

        // Log the received response
        System.out.println("Received DocumentResponses: " + documentResponses);

        // Optionally, set the list of DocumentResponse objects in the exchange body for further processing
        exchange.getIn().setBody(documentResponses);
    }
}