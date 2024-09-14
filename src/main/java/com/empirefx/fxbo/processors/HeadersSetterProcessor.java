package com.empirefx.fxbo.processors;

import com.empirefx.fxbo.commonlib.models.AppLogger;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;


import static com.empirefx.fxbo.commonlib.constants.ConstantsCommons.*;
import static org.apache.camel.builder.Builder.constant;


@Component
public class HeadersSetterProcessor implements Processor {
	@Value("${adaptive.authorization}")
	private String authorization;
	@Value("${adaptive.token}")
	private String token;
	@Value("${adaptive.camelHttpMethod}")
	private String camelHttpMethod;
	@Value("${adaptive.method}")
	private String getCamelHttpMethod;
	@Override
	public void process(Exchange exchange) throws Exception {
		System.out.println(authorization);
		System.out.println(token);
		System.out.println(camelHttpMethod);
		System.out.println(getCamelHttpMethod);
		// Get Caller IP
		HttpServletRequest request = exchange.getIn().getBody(HttpServletRequest.class);

		String ipRemote = Objects.isNull(request) ? EMPTY : request.getRemoteAddr();

		exchange.getIn().setHeader(CALLER_IP, ipRemote);
		exchange.getIn().setHeader(authorization+":", constant(token));
		exchange.getIn().setHeader(getCamelHttpMethod, constant(camelHttpMethod));
		exchange.setProperty(APP_REQUEST_TYPE, "Incoming Request Data ------- (From Caller): {}");
		exchange.setProperty(APP_REQUEST, AppLogger.builder()
				.build());
	}


}