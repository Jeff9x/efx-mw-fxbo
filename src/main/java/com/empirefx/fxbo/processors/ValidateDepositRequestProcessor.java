package com.empirefx.fxbo.processors;

import com.empirefx.fxbo.commonlib.configurations.AppConfiguration;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ValidateDepositRequestProcessor implements Processor {

    private final AppConfiguration appConfiguration;

    public ValidateDepositRequestProcessor(AppConfiguration appConfiguration) {
        this.appConfiguration = appConfiguration;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Map<String, Object> requestBody = exchange.getIn().getBody(Map.class);

        System.out.println("Incoming Request Validate: " + requestBody);

        Integer amount = (Integer) requestBody.get("amount");
        validateAmount(amount);

        Integer sid = (Integer) requestBody.get("sid");
        validateSid(sid);

        String manager = (String) requestBody.get("manager");
        validateManager(manager);

        String login = (String) requestBody.get("login");
        validateLogin(login);

        String currency = (String) requestBody.get("currency");
        validateCurrency(currency);

        String comment = (String) requestBody.get("comment");
        validateComment(comment);
    }

    public static void validateAmount(Integer amount) {
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Amount must be a positive integer.");
        }
    }

    public static void validateSid(Integer sid) {
        if (sid == null || sid <= 0) {
            throw new IllegalArgumentException("SID must be a positive integer.");
        }
    }

    public static void validateManager(String manager) {
        if (StringUtils.isEmpty(manager)) {
            throw new IllegalArgumentException("Manager must not be empty.");
        }
        if (!manager.matches("\\d+")) {
            throw new IllegalArgumentException("Manager must be a numeric string.");
        }
    }

    public static void validateLogin(String login) {
        if (StringUtils.isEmpty(login)) {
            throw new IllegalArgumentException("Login must not be empty.");
        }
        if (!login.matches("\\d+")) {
            throw new IllegalArgumentException("Login must be a numeric string.");
        }
    }

    public static void validateCurrency(String currency) {
        if (StringUtils.isEmpty(currency)) {
            throw new IllegalArgumentException("Currency must not be empty.");
        }
        if (!currency.matches("^[A-Z]{3}$")) {
            throw new IllegalArgumentException("Currency must be a valid 3-letter ISO code (e.g., USD).");
        }
    }

    public static void validateComment(String comment) {
        if (StringUtils.isEmpty(comment)) {
            throw new IllegalArgumentException("Comment must not be empty.");
        }
        if (comment.length() > 255) {
            throw new IllegalArgumentException("Comment must be less than or equal to 255 characters.");
        }
    }
}