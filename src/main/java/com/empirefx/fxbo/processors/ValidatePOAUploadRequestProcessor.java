package com.empirefx.fxbo.processors;

import com.empirefx.fxbo.commonlib.configurations.AppConfiguration;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ValidatePOAUploadRequestProcessor implements Processor {

    private final AppConfiguration appConfiguration;

    public ValidatePOAUploadRequestProcessor(AppConfiguration appConfiguration) {
        this.appConfiguration = appConfiguration;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Map<String, Object> requestBody = exchange.getIn().getBody(Map.class);

        System.out.println("Incoming Request Validate: " + requestBody);

//        Integer config = (Integer) requestBody.get("config");
//        validateConfig(config);

        Integer user = (Integer) requestBody.get("user");
        validateUser(user);

        String status = (String) requestBody.get("status");
        validateStatus(status);

        Map<String, Object> data = (Map<String, Object>) requestBody.get("data");
        validateData(data);

        Boolean uploadedByClient = (Boolean) requestBody.get("uploadedByClient");
        validateUploadedByClient(uploadedByClient);
    }

//    public static void validateConfig(Integer config) {
//        if (config == null && config != 7) {
//            throw new IllegalArgumentException("Config must be exactly 7.");
//        }
//    }

    public static void validateUser(Integer user) {
        if (user == null || user <= 0) {
            throw new IllegalArgumentException("User ID must be a positive integer.");
        }
    }

    public static void validateStatus(String status) {
        if (StringUtils.isEmpty(status) || !status.equalsIgnoreCase("pending")) {
            throw new IllegalArgumentException("Status must be 'pending'.");
        }
    }

    public static void validateData(Map<String, Object> data) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("Data must not be null or empty.");
        }

        String type = (String) data.get("type");
        validateType(type);

        String address = (String) data.get("address");
        validateAddress(address);

        String postalCode = (String) data.get("postal_code");
        validatePostalCode(postalCode);

        String country = (String) data.get("country");
        validateCountry(country);

        String city = (String) data.get("city");


        List<Map<String, Object>> fileList = (List<Map<String, Object>>) data.get("file");
        validateFileList(fileList);
    }

    public static void validateType(String type) {
        if (StringUtils.isEmpty(type)) {
            throw new IllegalArgumentException("Document type must not be empty.");
        }
        if (!type.equalsIgnoreCase("Utility Bill")) {
            throw new IllegalArgumentException("Document type must be 'Utility Bill'.");
        }
    }

    public static void validateAddress(String address) {
        if (StringUtils.isEmpty(address)) {
            throw new IllegalArgumentException("Address must not be empty.");
        }
        if (address.length() > 100) {
            throw new IllegalArgumentException("Address must be less than 100 characters.");
        }
    }

    public static void validatePostalCode(String postalCode) {
        if (StringUtils.isEmpty(postalCode)) {
            throw new IllegalArgumentException("Postal code must not be empty.");
        }
        if (!postalCode.matches("\\d{4,10}")) {
            throw new IllegalArgumentException("Postal code must be between 4 and 10 digits.");
        }
    }

    public static void validateCountry(String country) {
        if (StringUtils.isEmpty(country) || country.length() != 2 || !country.matches("^[A-Z]{2}$")) {
            throw new IllegalArgumentException("Country must be a 2-letter ISO code in uppercase (e.g., 'US', 'KE').");
        }
    }

    public static void validateFileList(List<Map<String, Object>> fileList) {
        if (fileList == null || fileList.isEmpty()) {
            throw new IllegalArgumentException("File list must not be null or empty.");
        }

        for (Map<String, Object> fileData : fileList) {
            String file = (String) fileData.get("file");
            if (StringUtils.isEmpty(file) || !file.startsWith("http")) {
                throw new IllegalArgumentException("File URL must be a valid HTTP/HTTPS URL.");
            }

            String name = (String) fileData.get("name");
            if (StringUtils.isEmpty(name) || !name.equalsIgnoreCase("Image")) {
                throw new IllegalArgumentException("File name must be 'Image'.");
            }
        }
    }

    public static void validateUploadedByClient(Boolean uploadedByClient) {
        if (uploadedByClient == null || !uploadedByClient) {
            throw new IllegalArgumentException("Uploaded by Client must be true.");
        }
    }
}