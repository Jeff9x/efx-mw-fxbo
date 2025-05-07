package com.empirefx.fxbo.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.util.Map;

@Component
public class SetIbPasswordProcessor implements Processor {
    private final ObjectMapper objectMapper;

    public SetIbPasswordProcessor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Map<String, Object> requestBody = exchange.getIn().getBody(Map.class);

        System.out.println("Incoming Request: " + requestBody);


        // Convert the Map to JSON using ObjectMapper
        String jsonRequestBody = objectMapper.writeValueAsString(requestBody);

        System.out.println("Processed JSON Request: " + jsonRequestBody);
        // Set the JSON string back to the exchange body for further processing
        exchange.getIn().setBody(jsonRequestBody);
    }
}
