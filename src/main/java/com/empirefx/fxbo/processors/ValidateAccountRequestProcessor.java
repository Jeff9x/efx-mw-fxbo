package com.empirefx.fxbo.processors;

import com.empirefx.fxbo.commonlib.configurations.AppConfiguration;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ValidateAccountRequestProcessor implements Processor {

    private final AppConfiguration appConfiguration;

    public ValidateAccountRequestProcessor(AppConfiguration appConfiguration) {
        this.appConfiguration = appConfiguration;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Map<String, Object> requestBody = exchange.getIn().getBody(Map.class);

        System.out.println("Incoming Request Validate: " + requestBody);

        String accountId = (String) requestBody.get("login");
        validateAccountId(accountId);
    }

    public static void validateAccountId(String accountId) {
        if (StringUtils.isEmpty(accountId)) {
            throw new IllegalArgumentException("Account ID must not be empty.");
        }
        if (!accountId.matches("\\d+") || Integer.parseInt(accountId) <= 0) {
            throw new IllegalArgumentException("Account ID must be a positive numeric value greater than zero.");
        }
    }
}