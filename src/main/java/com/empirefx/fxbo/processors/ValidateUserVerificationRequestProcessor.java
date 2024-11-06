package com.empirefx.fxbo.processors;

import com.empirefx.fxbo.commonlib.configurations.AppConfiguration;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ValidateUserVerificationRequestProcessor implements Processor {

    private final AppConfiguration appConfiguration;

    public ValidateUserVerificationRequestProcessor(AppConfiguration appConfiguration) {
        this.appConfiguration = appConfiguration;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Map<String, Object> requestBody = exchange.getIn().getBody(Map.class);

        System.out.println("Incoming Request Validate: " + requestBody);

        Integer user = (Integer) requestBody.get("user");
        validateUser(user);

        Boolean isVerified = (Boolean) requestBody.get("isVerified");
        validateIsVerified(isVerified);

        Boolean verified = (Boolean) requestBody.get("verified");
        validateVerified(verified);
    }

    public static void validateUser(Integer user) {
        if (user == null || user <= 0) {
            throw new IllegalArgumentException("User ID must be a positive integer.");
        }
    }

    public static void validateIsVerified(Boolean isVerified) {
        if (isVerified == null || !isVerified) {
            throw new IllegalArgumentException("isVerified must be true.");
        }
    }

    public static void validateVerified(Boolean verified) {
        if (verified == null || !verified) {
            throw new IllegalArgumentException("Verified must be true.");
        }
    }
}