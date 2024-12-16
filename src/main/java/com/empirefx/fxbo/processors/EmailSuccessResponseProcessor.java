package com.empirefx.fxbo.processors;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EmailSuccessResponseProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        // Assuming the response from the backend is a boolean `true`
        Object backendResponse = exchange.getIn().getBody();

        System.out.println("incoming message: " + backendResponse);

        if (backendResponse.equals("true")) {
            // Construct the success payload
            Map<String, Object> successPayload = new HashMap<>();
            successPayload.put("code", 200);
            successPayload.put("message", "Email sent successfully.");

            // Set the success payload as the response body
            exchange.getIn().setBody(successPayload);
        } else {
            // If the backend response is not `true`, handle it as an unexpected case
            Map<String, Object> failurePayload = new HashMap<>();
            failurePayload.put("code", 500);
            failurePayload.put("message", "Unexpected response from backend.");

            exchange.getIn().setBody(failurePayload);
            exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 500);
        }

        // Set response headers
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");
    }
}