package com.empirefx.fxbo.processors;

import com.empirefx.fxbo.commonlib.configurations.AppConfiguration;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ValidatePOIDocumentUploadRequestProcessor implements Processor {

    private final AppConfiguration appConfiguration;

    public ValidatePOIDocumentUploadRequestProcessor(AppConfiguration appConfiguration) {
        this.appConfiguration = appConfiguration;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Map<String, Object> requestBody = exchange.getIn().getBody(Map.class);

        System.out.println("Incoming Request Validate: " + requestBody);

        Integer config = (Integer) requestBody.get("config");
        validateConfig(config);

        Integer user = (Integer) requestBody.get("user");
        validateUser(user);

        String status = (String) requestBody.get("status");
        validateStatus(status);

        Map<String, Object> data = (Map<String, Object>) requestBody.get("data");
        validateData(data);

        Boolean uploadedByClient = (Boolean) requestBody.get("uploadedByClient");
        validateUploadedByClient(uploadedByClient);
    }

    public static void validateConfig(Integer config) {
        if (config == null || config != 6) {
            throw new IllegalArgumentException("Config must be exactly 6.");
        }
    }

    public static void validateUser(Integer user) {
        if (user == null || user <= 0) {
            throw new IllegalArgumentException("User ID must be a positive integer.");
        }
    }

    public static void validateStatus(String status) {
        if (StringUtils.isEmpty(status)) {
            throw new IllegalArgumentException("Status must not be empty.");
        }
        if (!status.equalsIgnoreCase("pending") && !status.equalsIgnoreCase("approved") && !status.equalsIgnoreCase("rejected")) {
            throw new IllegalArgumentException("Status must be 'pending', 'approved', or 'rejected'.");
        }
    }

    public static void validateData(Map<String, Object> data) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("Data must not be null or empty.");
        }

        String expiresAt = (String) data.get("expires_at");
        validateExpiresAt(expiresAt);

        String documentNumber = (String) data.get("document_number");
        validateDocumentNumber(documentNumber);

        String type = (String) data.get("type");
        validateType(type);

        String countryOfIssue = (String) data.get("country_of_issue");
        validateCountryOfIssue(countryOfIssue);

        List<Map<String, Object>> frontSide = (List<Map<String, Object>>) data.get("front_side");
        validateDocumentSide(frontSide, "FRONT_SIDE");

        List<Map<String, Object>> backSide = (List<Map<String, Object>>) data.get("back_side");
        validateDocumentSide(backSide, "BACK_SIDE");
    }

    public static void validateExpiresAt(String expiresAt) {
        if (StringUtils.isEmpty(expiresAt)) {
            throw new IllegalArgumentException("Expires at date must not be empty.");
        }
        if (!expiresAt.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new IllegalArgumentException("Expires at date must be in 'YYYY-MM-DD' format.");
        }
    }

    public static void validateDocumentNumber(String documentNumber) {
        if (StringUtils.isEmpty(documentNumber)) {
            throw new IllegalArgumentException("Document number must not be empty.");
        }
        if (documentNumber.length() > 20) {
            throw new IllegalArgumentException("Document number must be less than or equal to 20 characters.");
        }
    }

    public static void validateType(String type) {
        if (StringUtils.isEmpty(type)) {
            throw new IllegalArgumentException("Document type must not be empty.");
        }
    }

    public static void validateCountryOfIssue(String countryOfIssue) {
        if (StringUtils.isEmpty(countryOfIssue) || countryOfIssue.length() != 2 || !countryOfIssue.matches("^[A-Z]{2}$")) {
            throw new IllegalArgumentException("Country of issue must be a 2-letter ISO code in uppercase (e.g., 'US', 'KE').");
        }
    }

    public static void validateDocumentSide(List<Map<String, Object>> side, String sideName) {
        if (side == null || side.isEmpty()) {
            throw new IllegalArgumentException(sideName + " must not be null or empty.");
        }

        for (Map<String, Object> fileData : side) {
            String file = (String) fileData.get("file");
            if (StringUtils.isEmpty(file) || !file.startsWith("http")) {
                throw new IllegalArgumentException(sideName + " file URL must be a valid HTTP/HTTPS URL.");
            }

            String name = (String) fileData.get("name");
            if (StringUtils.isEmpty(name) || (!name.equals(sideName))) {
                throw new IllegalArgumentException(sideName + " name must be '" + sideName + "'.");
            }
        }
    }

    public static void validateUploadedByClient(Boolean uploadedByClient) {
        if (uploadedByClient == null) {
            throw new IllegalArgumentException("Uploaded by Client status must not be null.");
        }
    }
}