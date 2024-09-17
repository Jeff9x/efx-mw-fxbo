package com.empirefx.fxbo.processors;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;

@Component
public class RequestToInputStreamProcessor implements Processor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void process(Exchange exchange) throws Exception {
        // Get the body as LinkedHashMap (assuming it was deserialized)
        LinkedHashMap<String, Object> body = exchange.getIn().getBody(LinkedHashMap.class);

        // Convert the LinkedHashMap back to JSON
        String json = objectMapper.writeValueAsString(body);

        // Convert JSON string to InputStream
        InputStream inputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

        // Set the InputStream back into the exchange body
        exchange.getIn().setBody(inputStream);
    }
}