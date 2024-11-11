package com.empirefx.fxbo.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class TransactionResponseProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        // Get the body as a list of transactions
        List<Map<String, Object>> transactions = exchange.getIn().getBody(List.class);

        // Prepare a new list to hold processed transaction summaries
        List<Map<String, Object>> transactionSummaries = new ArrayList<>();

        // Iterate over each transaction, extract the required information
        for (Map<String, Object> transaction : transactions) {
            // Extract the relevant fields for each transaction
            Map<String, Object> transactionSummary = Map.of(
            );

            // Add this summary to the result list
            transactionSummaries.add(transactionSummary);
        }

        // Set the response with the list of transaction summaries
        exchange.getIn().setBody(Map.of("transactions", transactionSummaries));
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
    }
}