package com.empirefx.fxbo.processors;

import com.empirefx.fxbo.models.provider.AccountType;
import com.empirefx.fxbo.models.provider.Country;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountTypesResponseProcessor implements Processor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void process(Exchange exchange) throws Exception {
        System.out.println("Provider Response Data -------(From Backend)");
        String body = exchange.getIn().getBody(String.class);
        // Deserialize JSON to List<AccountType>
        List<AccountType> accountTypes = objectMapper.readValue(body, new TypeReference<>() {
        });
        exchange.getIn().setBody(accountTypes);
    }
}