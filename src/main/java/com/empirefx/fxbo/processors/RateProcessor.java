package com.empirefx.fxbo.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

@Component
public class RateProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        // Get the incoming payload as a JSON string
        String incomingPayload = exchange.getIn().getBody(String.class);

        // Parse the incoming JSON payload
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode incomingJson = objectMapper.readTree(incomingPayload);

        // Extract relevant fields from the "rate" object
        JsonNode rateNode = incomingJson.path("rate");
        String fromCurrency = rateNode.path("toCurrency").asText();
        String toCurrency = rateNode.path("fromCurrency").asText();
        String source = rateNode.path("source").asText();
        double rate = rateNode.path("rate").asDouble();

        // Create the outgoing JSON payload as an ObjectNode
        ObjectNode responseJson = objectMapper.createObjectNode();
        responseJson.put("fromCurrency", fromCurrency);
        responseJson.put("toCurrency", toCurrency);
        responseJson.put("source", capitalizeFirstLetter(source));
        responseJson.put("rate", rate);

        // Set the outgoing payload as a JSON object (not a string)
        exchange.getIn().setBody(responseJson);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");
    }

    /**
     * Capitalizes the first letter of the given string.
     *
     * @param input The string to capitalize.
     * @return The capitalized string.
     */
    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
}