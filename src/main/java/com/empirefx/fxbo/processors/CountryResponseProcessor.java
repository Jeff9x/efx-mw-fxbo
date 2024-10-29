package com.empirefx.fxbo.processors;

import com.empirefx.fxbo.models.provider.Country;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;


@Component
public class CountryResponseProcessor implements Processor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void process(Exchange exchange) throws Exception {
        System.out.println("Provider Response Data -------(From Backend)");


        String jsonResponse = exchange.getIn().getBody(String.class);
        String body = new String(exchange.getIn().getBody(byte[].class), StandardCharsets.UTF_8);


        // Deserialize JSON to List<Country>
        List<Country> countries = objectMapper.readValue(body, new TypeReference<List<Country>>() {});
        // Optionally set the processed list back in the exchange (if needed downstream)
        exchange.getIn().setBody(countries);
    }
}