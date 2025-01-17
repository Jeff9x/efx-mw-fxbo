package com.empirefx.fxbo.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class InternalFundsTransferProcessor implements Processor {

    private final ObjectMapper objectMapper;

    public InternalFundsTransferProcessor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void process(Exchange exchange) throws Exception {

        // Extract the incoming request body as a Map
        @SuppressWarnings("unchecked")
        Map<String, Object> requestBody = exchange.getIn().getBody(Map.class);

        System.out.println("Incoming Request: " + requestBody);

        // Parse the JSON payload
        JSONObject jsonObject = new JSONObject(requestBody);
        System.out.println("Incoming JSON: " + jsonObject);

        String fromLogin = String.valueOf(jsonObject.get("fromLogin"));
        System.out.println("Incoming fromSid: " + fromLogin);
        String toLogin = String.valueOf(jsonObject.get("toLogin"));
        System.out.println("Incoming toLogin: " + toLogin);

        String comment = "Internal Transfer "+fromLogin+" -> "+toLogin;

        //        // Set default values if not present in the payload
        requestBody.put("status", "approved");   // Default value for 'enabled'
        requestBody.put("comment", comment);       // Default value for 'ib'
        requestBody.put("type", "internal transfer");    // Default value for 'trader'

        // Convert the Map to JSON using ObjectMapper
        String jsonRequestBody = objectMapper.writeValueAsString(requestBody);

        System.out.println("Processed JSON Request: " + jsonRequestBody);
        // Set the JSON string back to the exchange body for further processing
        exchange.getIn().setBody(jsonRequestBody);
    }
}
