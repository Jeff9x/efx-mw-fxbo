package com.empirefx.fxbo.processors;

import com.empirefx.fxbo.commonlib.configurations.AppConfiguration;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.empirefx.fxbo.commonlib.constants.ConstantsCommons.*;


@Component
public class ValidateCreateUserRequestProcessor implements Processor {


    private final AppConfiguration appConfiguration;

    public ValidateCreateUserRequestProcessor(AppConfiguration appConfiguration) {
        this.appConfiguration = appConfiguration;
    }


    public void process(Exchange exchange) throws Exception {


        exchange.getIn().setHeader(CALLER_IP, exchange.getIn().getHeader(CALLER_IP));

        Map requestBody = exchange.getIn().getBody(Map.class);

        System.out.println("Incoming Request Validate: " + requestBody);
        String firstName = (String) requestBody.get("firstName");
        validateFirstName(firstName);
        String lastName = (String) requestBody.get("lastName");
        validateLastName(lastName);
        String country = (String) requestBody.get("country");
        validateCountry(country);
        String phone = (String) requestBody.get("phone");
        validatePhoneNumber(phone);
        String email = (String) requestBody.get("email");
        validateEmail(email);
        String clientType = (String) requestBody.get("clientType");
        validateClientType(clientType);
        boolean emailVerified = (boolean) requestBody.get("emailVerified");
        validateEmailVerified(emailVerified);

    }



    public static void validateFirstName(String firstName) {
        if (StringUtils.isEmpty(firstName)) {
            throw new IllegalArgumentException("First name must not be empty.");
        }
        if (firstName.length() < 2 || firstName.length() > 50) {
            throw new IllegalArgumentException("First name must be between 2 and 50 characters.");
        }
    }

    public static void validateLastName(String lastName) {
        if (StringUtils.isEmpty(lastName)) {
            throw new IllegalArgumentException("Last name must not be empty.");
        }
        if (lastName.length() < 2 || lastName.length() > 50) {
            throw new IllegalArgumentException("Last name must be between 2 and 50 characters.");
        }
    }

    public static void validatePhoneNumber(String phone) {
        if (StringUtils.isEmpty(phone)) {
            throw new IllegalArgumentException("Phone number must not be empty.");
        }
        // Check if the phone number matches a valid pattern (e.g., international format)
        if (!phone.matches("^\\+?[0-9. ()-]{7,25}$")) {
            throw new IllegalArgumentException("Phone number must be a valid format and between 7 and 25 characters.");
        }
    }

    public static void validateCountry(String country) {
        if (StringUtils.isEmpty(country)) {
            throw new IllegalArgumentException("Country code must not be empty.");
        }
        if (country.length() != 2) {
            throw new IllegalArgumentException("Country code must be exactly 2 characters.");
        }
        // Check if the country code is uppercase (ISO Alpha-2 standard)
        if (!country.matches("^[A-Z]{2}$")) {
            throw new IllegalArgumentException("Country code must be in uppercase format (e.g., US, KE).");
        }
    }

    public static void validateEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            throw new IllegalArgumentException("Email address must not be empty.");
        }
        // Basic email pattern validation
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Email address must be in a valid format.");
        }
    }

    public static void validateClientType(String clientType) {
        if (StringUtils.isEmpty(clientType)) {
            throw new IllegalArgumentException("Client type must not be empty.");
        }
        if (!clientType.equals("Individual") && !clientType.equals("Corporate")) {
            throw new IllegalArgumentException("Client type must be either 'Individual' or 'Corporate'.");
        }
    }

    public static void validateEmailVerified(boolean emailVerified) {
        // Ensure emailVerified is true if the application requires it to be confirmed
        if (!emailVerified) {
            throw new IllegalArgumentException("Email verification status must be true.");
        }
    }

}