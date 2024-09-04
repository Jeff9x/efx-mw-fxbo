package com.empirefx.fxbo_countries.processors;

import com.empirefx.fxbo_countries.commonlib.configurations.AppConfiguration;
import com.empirefx.fxbo_countries.commonlib.exceptions.DataValidationException;
import com.empirefx.fxbo_countries.commonlib.models.RequestHeader;
import com.empirefx.fxbo_countries.commonlib.models.RequestPayload;
import com.empirefx.fxbo_countries.commonlib.models.RequestWrapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static com.empirefx.fxbo_countries.commonlib.constants.ConstantsCommons.*;


@Component
public class ValidateRequestProcessor implements Processor {


    private final AppConfiguration appConfiguration;

    public ValidateRequestProcessor(AppConfiguration appConfiguration) {
        this.appConfiguration = appConfiguration;
    }


    public void process(Exchange exchange) throws Exception {

        RequestWrapper requestWrapper = exchange.getProperty(ORIGINAL_REQUEST, RequestWrapper.class);

        boolean valid = validate(requestWrapper);
        if (!valid) throw new DataValidationException();
        exchange.setProperty("valid", valid);

        exchange.getIn().setHeader(MESSAGE_ID_HEADER, exchange.getIn().getHeader(MESSAGE_ID_HEADER));
        exchange.getIn().setHeader(CONVERSATION_ID_HEADER, exchange.getIn().getHeader(CONVERSATION_ID_HEADER));
        exchange.getIn().setHeader(CALLER_IP, exchange.getIn().getHeader(CALLER_IP));
    }

    private boolean validate(RequestWrapper requestWrapper) {
        if (requestWrapper.getRequestPayload() == null || requestWrapper.getHeader() == null) {
            return false;
        }
//        if (!validateHeader(requestWrapper.getHeader())) {
//            return false;
//        }
        return validateBody(requestWrapper.getRequestPayload());
    }


//    private boolean validateHeader(RequestHeader header) {
//        return validateMessageId(header.getMessageID())
//                && isMatching(header.getServiceCode(), appConfiguration.getHeaderValues().getServiceCode())
//                && isMatching(header.getServiceName(), appConfiguration.getHeaderValues().getServiceName())
//                && validateChannelCode(header.getChannelCode())
//                && isMatching(header.getChannelName(), appConfiguration.getHeaderValues().getChannelName())
//                && isMatching(header.getRouteCode(), appConfiguration.getHeaderValues().getRouteCode())
//                && isMatching(header.getRouteName(), appConfiguration.getHeaderValues().getRouteName())
//                && isMatching(header.getServiceMode(), appConfiguration.getHeaderValues().getServiceMode())
//                && isMatching(header.getMinorServiceVersion(), appConfiguration.getHeaderValues().getMinorServiceVersion())
//                && isValidTimestamp(header.getTimeStamp());
//    }

//    private boolean validateChannelCode(String channelCode) {
//        return appConfiguration.getHeaderValues().getChannelCode().contains(channelCode);
//    }

    private boolean validateMessageId(String messageId) {
        return StringUtils.isNotEmpty(messageId) && messageId.length() > 6 && messageId.length() < 32;
    }

    private boolean isMatching(String actual, String expected) {
        return !StringUtils.isEmpty(actual) && actual.equals(expected);
    }

    private boolean isValidTimestamp(String timestamp) {
        if (StringUtils.isEmpty(timestamp)) {
            throw new IllegalArgumentException("Timestamp is required");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSZ");
        try {
            ZonedDateTime.parse(timestamp, formatter.withZone(ZoneOffset.UTC));
            return true;
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid timestamp format: " + timestamp);
        }
    }


    private boolean validateBody(RequestPayload payload) {
        return !(payload.getSms() == null ||
                payload.getPrimaryData() == null ||
                StringUtils.isEmpty(payload.getSms().getSender()) ||
                StringUtils.isEmpty(payload.getSms().getSmsText()) ||
                StringUtils.isEmpty(payload.getSms().getUnicode().toString()) ||
                StringUtils.isEmpty(payload.getPrimaryData().getBusinessKey()) ||
                StringUtils.isEmpty(payload.getPrimaryData().getBusinessKeyType())
        );
    }


}