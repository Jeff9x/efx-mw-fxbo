package com.empirefx.fxbo.processors;

import com.empirefx.fxbo.commonlib.models.AppLogger;
import com.empirefx.fxbo.commonlib.models.RequestWrapper;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;


import static com.empirefx.fxbo.commonlib.constants.ConstantsCommons.*;


@Component
public class HeadersSetterProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {

		// Get Caller IP
		HttpServletRequest request = exchange.getIn().getBody(HttpServletRequest.class);
		RequestWrapper requestWrapper = exchange.getIn().getBody(RequestWrapper.class);

//		requestWrapper.setHeader(createRequestHeader(exchange));

		exchange.setProperty(ORIGINAL_REQUEST, requestWrapper);

		String ipRemote = Objects.isNull(request) ? EMPTY : request.getRemoteAddr();

		String conversationID = UUID.randomUUID().toString();

//		String messageId = requestWrapper.getHeader().getMessageID();

//		exchange.getIn().setHeader(MESSAGE_ID_HEADER, messageId);
//		exchange.getIn().setHeader(CONVERSATION_ID_HEADER, conversationID);
		exchange.getIn().setHeader(CALLER_IP, ipRemote);
		exchange.getIn().setHeader(SERVICE_CODE, requestWrapper.getHeader().getServiceCode());
		exchange.setProperty(APP_REQUEST_TYPE, "Incoming Request Data ------- (From Caller): {}");
		exchange.setProperty(APP_REQUEST, AppLogger.builder()
				.headerData(requestWrapper.getHeader())
//				.payloadData(requestWrapper.getRequestPayload())
				.build());
	}


//	public RequestHeader createRequestHeader(Exchange exchange) {
//		return RequestHeader.builder()
//				.messageID(exchange.getIn().getHeader("messageID").toString())
//				.serviceCode(exchange.getIn().getHeader("serviceCode").toString())
//				.serviceName(exchange.getIn().getHeader("serviceName").toString())
//				.channelCode(exchange.getIn().getHeader("channelCode").toString())
//				.channelName(exchange.getIn().getHeader("channelName").toString())
//				.minorServiceVersion(exchange.getIn().getHeader("minorServiceVersion").toString())
//				.routeCode(exchange.getIn().getHeader("routeCode").toString())
//				.routeName(exchange.getIn().getHeader("routeName").toString())
//				.timeStamp(exchange.getIn().getHeader("timeStamp").toString())
//				.serviceMode(exchange.getIn().getHeader("serviceMode").toString())
//				.callBackURL(exchange.getIn().getHeader("callBackURL").toString())
//				.build();
//	}
}