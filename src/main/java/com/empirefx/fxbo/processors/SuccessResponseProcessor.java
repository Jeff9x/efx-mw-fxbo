package com.empirefx.fxbo.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
public class SuccessResponseProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        // Assuming the success response payload is already set in the body
        Object successResponse = exchange.getIn().getBody();

        exchange.getIn().setBody(successResponse);
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");
    }
}