package com.empirefx.fxbo_countries.processors;

import com.empirefx.fxbo_countries.commonlib.models.*;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


import static com.empirefx.fxbo_countries.commonlib.enums.ErrorCodesEnum.EHF1999;
import static com.empirefx.fxbo_countries.commonlib.constants.ConstantsCommons.*;


@Component
public class EhfResponseHandlerProcessor implements Processor {
    
	@Override
	public void process(Exchange exchange) throws Exception {
		RequestWrapper requestWrapper = exchange.getProperty(ORIGINAL_REQUEST, RequestWrapper.class);
		EhfResponse ehFResponse = exchange.getIn().getBody(EhfResponse.class);

		ResponsePayload responsePayload = new ResponsePayload();
//		responsePayload.setPrimaryData(requestWrapper.getRequestPayload().getPrimaryData());


		Item ehfItem = null;
		List<Item> listOfEhfItems = new ArrayList<>();

		if (ehFResponse.getRecords().isEmpty()) {
			ehfItem = new Item(EHF1999.getCode(), EHF1999.getMessage());
			listOfEhfItems.add(ehfItem);
		}

		for (EhfObject result : ehFResponse.getRecords()) {
			ehfItem = new Item();
			ehfItem.setEhfRef(result.getEhfRecord().getEhfRef());
			ehfItem.setEhfDesc(result.getEhfRecord().getEhfDesc());
			listOfEhfItems.add(ehfItem);
		}
		EhfInfo ehfInfo = new EhfInfo();
		ehfInfo.setItem(listOfEhfItems);

		ResponseHeader responseHeader = new ResponseHeader();
		responseHeader.setEhfInfo(ehfInfo);
		responseHeader.setConversationID(exchange.getIn().getHeader(CONVERSATION_ID_HEADER, String.class));
		responseHeader.setMessageID(exchange.getIn().getHeader(MESSAGE_ID_HEADER, String.class));
		responseHeader.setTargetSystemID("NotAvailable");
		responseHeader.setChannelCode(requestWrapper.getHeader().getChannelCode());
		responseHeader.setChannelName(requestWrapper.getHeader().getChannelName());
		responseHeader.setRouteCode(requestWrapper.getHeader().getRouteCode());
		responseHeader.setRouteName(requestWrapper.getHeader().getRouteName());
		responseHeader.setServiceName(requestWrapper.getHeader().getServiceName());
		responseHeader.setServiceCode(requestWrapper.getHeader().getServiceCode());
		ResponseWrapper responseWrapper = new ResponseWrapper();
		responseWrapper.setResponsePayload(responsePayload);
		responseWrapper.setHeader(responseHeader);
		exchange.getIn().setBody(responseWrapper, ResponseWrapper.class);
		exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.NOT_FOUND.value());
	}
}
