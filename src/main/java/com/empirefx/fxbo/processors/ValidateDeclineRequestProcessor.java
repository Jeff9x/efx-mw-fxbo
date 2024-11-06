package com.empirefx.fxbo.processors;

import com.empirefx.fxbo.commonlib.configurations.AppConfiguration;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ValidateDeclineRequestProcessor implements Processor {

    private final AppConfiguration appConfiguration;

    public ValidateDeclineRequestProcessor(AppConfiguration appConfiguration) {
        this.appConfiguration = appConfiguration;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Map<String, Object> requestBody = exchange.getIn().getBody(Map.class);

        System.out.println("Incoming Request Validate: " + requestBody);

        Integer id = (Integer) requestBody.get("id");
        validateId(id);

        String declineReason = (String) requestBody.get("declineReason");
        validateDeclineReason(declineReason);

        String comment = (String) requestBody.get("comment");
        validateComment(comment);
    }

    public static void validateId(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID must be a positive integer.");
        }
    }

    public static void validateDeclineReason(String declineReason) {
        if (StringUtils.isEmpty(declineReason)) {
            throw new IllegalArgumentException("Decline reason must not be empty.");
        }
        if (declineReason.length() > 255) {
            throw new IllegalArgumentException("Decline reason must be less than or equal to 255 characters.");
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