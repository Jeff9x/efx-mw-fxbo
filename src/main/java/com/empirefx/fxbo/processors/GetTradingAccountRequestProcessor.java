package com.empirefx.fxbo.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GetTradingAccountRequestProcessor implements Processor {

    private final ObjectMapper objectMapper;

    public GetTradingAccountRequestProcessor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        // Retrieve the fxbouserid path parameter from the Camel exchange
        String fxboaccountId = exchange.getIn().getHeader("accountId", String.class);

        System.out.println("fxboaccountId: " + fxboaccountId);

        if (fxboaccountId != null) {
            // Parse the userId from the path parameter
            int accountId = Integer.parseInt(fxboaccountId);

            // Construct the new payload
            Map<String, Object> payload = new HashMap<>();
            payload.put("login", accountId);

            // Convert the Map to JSON using ObjectMapper
            String jsonRequestBody = objectMapper.writeValueAsString(payload);

            // Set the payload as the body of the exchange
            exchange.getIn().setBody(jsonRequestBody);
        } else {
            // Handle the case when fxbouserid is missing or invalid
            throw new IllegalArgumentException("fxbouserid path parameter is missing or invalid");
        }
    }
}