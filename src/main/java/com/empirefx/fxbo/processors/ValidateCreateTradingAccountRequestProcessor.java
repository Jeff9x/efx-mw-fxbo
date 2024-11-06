package com.empirefx.fxbo.processors;

import com.empirefx.fxbo.commonlib.configurations.AppConfiguration;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.empirefx.fxbo.commonlib.constants.ConstantsCommons.CALLER_IP;

@Component
public class ValidateCreateTradingAccountRequestProcessor implements Processor {


    private final AppConfiguration appConfiguration;


    public ValidateCreateTradingAccountRequestProcessor(AppConfiguration appConfiguration) {
        this.appConfiguration = appConfiguration;
    }


    public void process(Exchange exchange) throws Exception {


        exchange.getIn().setHeader(CALLER_IP, exchange.getIn().getHeader(CALLER_IP));

        Map requestBody = exchange.getIn().getBody(Map.class);

        System.out.println("Incoming Request Validate: " + requestBody);
        Integer user = (Integer) requestBody.get("user");
        validateUser(user);

        String password = (String) requestBody.get("password");
        validatePassword(password);

        String sid = (String) requestBody.get("sid");
        validateSid(sid);

        String groupName = (String) requestBody.get("groupName");
        validateGroupName(groupName);

        Integer leverage = (Integer) requestBody.get("leverage");
        validateLeverage(leverage);

        Integer initialBalance = (Integer) requestBody.get("initialBalance");
        validateInitialBalance(initialBalance);

        Boolean notifyDisable = (Boolean) requestBody.get("notifyDisable");
        validateNotifyDisable(notifyDisable);

        Boolean readOnly = (Boolean) requestBody.get("readOnly");
        validateReadOnly(readOnly);

        Map<String, String> customFields = (Map<String, String>) requestBody.get("customFields");
        validateCustomFields(customFields);
    }

    public static void validateUser(Integer user) {
        if (user == null || user <= 0) {
            throw new IllegalArgumentException("User ID must be a positive integer.");
        }
    }

    public static void validatePassword(String password) {
        if (StringUtils.isEmpty(password)) {
            throw new IllegalArgumentException("Password must not be empty.");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long.");
        }
    }

    public static void validateSid(String sid) {
        if (StringUtils.isEmpty(sid)) {
            throw new IllegalArgumentException("SID must not be empty.");
        }
        if (!sid.matches("\\d+")) {
            throw new IllegalArgumentException("SID must be numeric.");
        }
    }

    public static void validateGroupName(String groupName) {
        if (StringUtils.isEmpty(groupName)) {
            throw new IllegalArgumentException("Group name must not be empty.");
        }
        if (groupName.length() > 50) {
            throw new IllegalArgumentException("Group name must be less than 50 characters.");
        }
    }

    public static void validateLeverage(Integer leverage) {
        if (leverage == null || leverage <= 0) {
            throw new IllegalArgumentException("Leverage must be a positive integer.");
        }
        if (leverage > 1000) {
            throw new IllegalArgumentException("Leverage cannot exceed 1000.");
        }
    }

    public static void validateInitialBalance(Integer initialBalance) {
        if (initialBalance == null || initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance must be a non-negative integer.");
        }
    }

    public static void validateNotifyDisable(Boolean notifyDisable) {
        if (notifyDisable == null) {
            throw new IllegalArgumentException("Notify Disable status must not be null.");
        }
    }

    public static void validateReadOnly(Boolean readOnly) {
        if (readOnly == null) {
            throw new IllegalArgumentException("Read Only status must not be null.");
        }
    }

    public static void validateCustomFields(Map<String, String> customFields) {
        if (customFields == null || customFields.isEmpty()) {
            throw new IllegalArgumentException("Custom fields must not be null or empty.");
        }

        for (Map.Entry<String, String> entry : customFields.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (StringUtils.isEmpty(value)) {
                throw new IllegalArgumentException("Custom field " + key + " must not be empty.");
            }
            if (value.length() > 100) {
                throw new IllegalArgumentException("Custom field " + key + " must be less than 100 characters.");
            }
        }
    }

}