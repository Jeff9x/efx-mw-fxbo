package com.empirefx.fxbo.processors;
import com.empirefx.fxbo.commonlib.configurations.AppConfiguration;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ValidateCurrencyConversionRequestProcessor implements Processor {

    private final AppConfiguration appConfiguration;

    public ValidateCurrencyConversionRequestProcessor(AppConfiguration appConfiguration) {
        this.appConfiguration = appConfiguration;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Map<String, Object> requestBody = exchange.getIn().getBody(Map.class);

        System.out.println("Incoming Request Validate: " + requestBody);

        String fromCurrency = (String) requestBody.get("fromCurrency");
        validateFromCurrency(fromCurrency);

        String toCurrency = (String) requestBody.get("toCurrency");
        validateToCurrency(toCurrency);

//        Double amount = (Double) requestBody.get("amount");
//        validateAmount(amount);
    }

    public static void validateFromCurrency(String fromCurrency) {
        if (StringUtils.isEmpty(fromCurrency)) {
            throw new IllegalArgumentException("From Currency must not be empty.");
        }
        if (fromCurrency.length() != 3 || !fromCurrency.matches("^[A-Z]{3}$")) {
            throw new IllegalArgumentException("From Currency must be a 3-letter uppercase ISO code (e.g., USD, KES).");
        }
    }

    public static void validateToCurrency(String toCurrency) {
        if (StringUtils.isEmpty(toCurrency)) {
            throw new IllegalArgumentException("To Currency must not be empty.");
        }
        if (toCurrency.length() != 3 || !toCurrency.matches("^[A-Z]{3}$")) {
            throw new IllegalArgumentException("To Currency must be a 3-letter uppercase ISO code (e.g., USD, KES).");
        }
    }

//    public static void validateAmount(Double amount) {
//        if (amount == null) {
//            throw new IllegalArgumentException("Amount must not be null.");
//        }
//        if (amount <= 0) {
//            throw new IllegalArgumentException("Amount must be a positive number.");
//        }
//    }
}