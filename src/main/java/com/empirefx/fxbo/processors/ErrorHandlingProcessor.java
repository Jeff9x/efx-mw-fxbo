package com.empirefx.fxbo.processors;

//import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import com.empirefx.fxbo.commonlib.exceptions.DataValidationException;
import com.empirefx.fxbo.commonlib.exceptions.SystemUnavailableException;
import com.empirefx.fxbo.commonlib.models.EhfInfo;
import com.empirefx.fxbo.commonlib.models.Item;
import com.empirefx.fxbo.commonlib.models.ResponseHeader;
import com.empirefx.fxbo.commonlib.models.ResponseWrapper;
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


import static com.empirefx.fxbo.commonlib.enums.ErrorCodesEnum.*;

@Component
public class ErrorHandlingProcessor implements Processor {

	private static final Logger logger = LoggerFactory.getLogger(ErrorHandlingProcessor.class);

	@Override
	public void process(Exchange exchange) throws Exception {

		Exception exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);

		String messageID = "Emmanuel";

		ResponseWrapper responseWrapper = new ResponseWrapper();
		exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.INTERNAL_SERVER_ERROR.value());


		Item entry = null;
		List<Item> listofItems = new ArrayList<>();

		EhfInfo ehfInfo = new EhfInfo();

		if (exception instanceof DataValidationException
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
		responseWrapper.setHeader(header);
		exchange.getIn().setBody(responseWrapper, ResponseWrapper.class);
		exchange.getIn().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

		// Logs all required information about the error
		logger.error("Adapter::Error::MessageID [{}]::Exception [{}]", messageID, exception.getMessage());

	}
}
