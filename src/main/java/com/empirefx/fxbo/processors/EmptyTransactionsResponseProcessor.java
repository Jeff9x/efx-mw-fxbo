package com.empirefx.fxbo.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class EmptyTransactionsResponseProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        Object body = exchange.getIn().getBody();

        // Check if the body is empty (null, empty string, or empty map/list)
        boolean isBodyEmpty = body == null
                || (body instanceof String && ((String) body).trim().isEmpty())
                || (body instanceof Map && ((Map<?, ?>) body).isEmpty())
                || (body instanceof Iterable && !((Iterable<?>) body).iterator().hasNext());

        if (isBodyEmpty) {
            // Construct an error response
            Map<String, Object> errorResponse = Map.of(
                    "code", 404,
                    "message", "FXBO Transaction(s) not found. Kindly confirm the fromUserId"
            );

            // Set the error response with a 204 status code
            exchange.getIn().setBody(errorResponse);
            exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 204); // No Content
            exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");
        } else {
            // Proceed with the original 200 response if body is not empty
            exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
        }
    }
}
