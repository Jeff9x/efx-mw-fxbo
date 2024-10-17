package com.empirefx.fxbo.processors;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GetUserAccountRequestProcessor implements Processor {
    @Value("${atomic.uriUserAccount}/")
    private String pathUserAccount;
    @Value("${atomic.uri-params}")
    private String paramsUri;

    private final ObjectMapper objectMapper;

    public GetUserAccountRequestProcessor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        // Retrieve the fxbouserid path parameter from the Camel exchange
        String fxbouserid = exchange.getIn().getHeader("fxboUserId", String.class);

        System.out.println("fxbouserid: " + fxbouserid);


        if (fxbouserid != null) {
            // Parse the userId from the path parameter
            int userId = Integer.parseInt(fxbouserid);

            String finalUrl = pathUserAccount + userId + paramsUri;

            System.out.println("finalUrl: " + finalUrl);



//            // Construct the new payload
//            Map<String, Object> payload = new HashMap<>();
//            payload.put("userId", userId);

//            // Convert the Map to JSON using ObjectMapper
//            String jsonRequestBody = objectMapper.writeValueAsString(payload);
//
//            // Set the payload as the body of the exchange
            exchange.getIn().setHeader("finalUrl", finalUrl);
        } else {
            // Handle the case when fxbouserid is missing or invalid
            throw new IllegalArgumentException("fxbouserid path parameter is missing or invalid");
        }
    }
}