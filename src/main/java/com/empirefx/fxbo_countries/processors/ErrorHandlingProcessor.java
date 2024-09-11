package com.empirefx.fxbo_countries.processors;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import com.empirefx.fxbo_countries.commonlib.exceptions.DataValidationException;
import com.empirefx.fxbo_countries.commonlib.exceptions.SystemUnavailableException;
import com.empirefx.fxbo_countries.commonlib.models.EhfInfo;
import com.empirefx.fxbo_countries.commonlib.models.Item;
import com.empirefx.fxbo_countries.commonlib.models.ResponseHeader;
import com.empirefx.fxbo_countries.commonlib.models.RequestWrapper;
import com.empirefx.fxbo_countries.commonlib.models.ResponsePayload;
import com.empirefx.fxbo_countries.commonlib.models.ResponseWrapper;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangeTimedOutException;
import org.apache.camel.Processor;
import org.apache.camel.ValidationException;
import org.apache.camel.component.jsonvalidator.JsonValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;


import static com.empirefx.fxbo_countries.commonlib.enums.ErrorCodesEnum.*;
import static com.empirefx.fxbo_countries.commonlib.constants.ConstantsCommons.*;
@Component
public class ErrorHandlingProcessor implements Processor {

	private static final Logger logger = LoggerFactory.getLogger(ErrorHandlingProcessor.class);

	@Override
	public void process(Exchange exchange) throws Exception {

		Exception exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
		
//		RequestWrapper originalRequest = exchange.getProperty(ORIGINAL_REQUEST, RequestWrapper.class);
//
//		String routeCode = "";
//		String routeName = "";
//		String channelCode = "";
//		String channelName = "";
		String messageID = "Emmanuel";
//		String conversationID = "";
//		String serviceName = "";
//		String serviceCode = "";

		ResponseWrapper responseWrapper = new ResponseWrapper();
		exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.INTERNAL_SERVER_ERROR.value());

//		if (null != originalRequest) {
//			ResponsePayload responsePayload = new ResponsePayload();
//
//			responsePayload.setPrimaryData(originalRequest.getRequestPayload().getPrimaryData());
//			responseWrapper.setResponsePayload(responsePayload);
//
//			routeCode = originalRequest.getHeader().getRouteCode();
//			routeName = originalRequest.getHeader().getRouteName();
//			channelCode = originalRequest.getHeader().getChannelCode();
//			channelName = originalRequest.getHeader().getChannelName();
			messageID =  messageID;
//			serviceName = originalRequest.getHeader().getServiceName();
//			serviceCode = originalRequest.getHeader().getServiceCode();
//		}

		Item entry = null;
		List<Item> listofItems = new ArrayList<>();

		EhfInfo ehfInfo = new EhfInfo();

		if (exception instanceof DataValidationException || exception instanceof UnrecognizedPropertyException
				|| exception instanceof ValidationException || exception instanceof JsonValidationException) {
			entry = new Item();
			entry.setEhfDesc(EHF1014.getMessage());
			entry.setEhfRef(EHF1014.getCode());
			listofItems.add(entry);
			exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.BAD_REQUEST.value());
		} else if (exception instanceof ExchangeTimedOutException || exception instanceof SocketTimeoutException || exception instanceof SystemUnavailableException) {
			entry = new Item();
			entry.setEhfDesc(EHF1004.getMessage());
			entry.setEhfRef(EHF1004.getCode());
			listofItems.add(entry);
			exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.NOT_FOUND.value());
		} else {
			entry = new Item();
			entry.setEhfDesc(EHF1999.getMessage());
			entry.setEhfRef(EHF1999.getCode());
			listofItems.add(entry);
		}

		ehfInfo.setItem(listofItems);
		ResponseHeader header = new ResponseHeader();
		header.setEhfInfo(ehfInfo);

		header.setMessageID(messageID);
//		header.setConversationID(conversationID);
//		header.setTargetSystemID("NotAvailable");
//		header.setChannelCode(channelCode);
//		header.setChannelName(channelName);
//		header.setRouteCode(routeCode);
//		header.setRouteName(routeName);
//		header.setServiceCode(serviceCode);
//		header.setServiceName(serviceName);
		header.setConversationID(exchange.getIn().getHeader("conversationID", String.class));
		responseWrapper.setHeader(header);
		exchange.getIn().setBody(responseWrapper, ResponseWrapper.class);
		exchange.getIn().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

		// Logs all required information about the error
		logger.error("Adapter::Error::MessageID [{}]::Exception [{}]", messageID, exception.getMessage());

	}
}
