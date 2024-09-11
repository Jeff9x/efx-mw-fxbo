//package com.empirefx.fxbo_countries;
//
//import com.empirefx.fxbo_countries.commonlib.models.RequestHeader;
//import com.empirefx.fxbo_countries.configurations.CamelRouterTestSupport;
//import com.empirefx.fxbo_countries.commonlib.models.RequestWrapper;
//import com.empirefx.fxbo_countries.commonlib.models.ResponseWrapper;
//import org.apache.camel.Exchange;
//import org.apache.camel.builder.ExchangeBuilder;
//import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
//import org.apache.camel.test.spring.junit5.UseAdviceWith;
//import org.junit.jupiter.api.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.annotation.DirtiesContext.ClassMode;
//
//import static com.empirefx.fxbo_countries.commonlib.constants.ConstantsCommons.*;
//import static com.empirefx.fxbo_countries.commons.Examples.REQUEST_EXAMPLE;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//@CamelSpringBootTest
//@UseAdviceWith
//@SpringBootTest(classes = EmpireFxGetFXBOCountriesApplication.class)
//@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
//class CallBackEndTest extends CamelRouterTestSupport{
//
//	private static final Logger logger = LoggerFactory.getLogger(CallBackEndTest.class);
//
//	/**
//	 * Test to validate channel authentication against vault credentials - success
//	 *
//	 * @throws Exception
//	 */
//	@Test
//	void callServiceSuccessTest() throws Exception {
//
//		logger.info("callServiceSuccessTest");
//
//		Long messageID = System.currentTimeMillis();
//		RequestWrapper requestWrapper = CallBackEndTest.payloadObjectMapper.readValue(REQUEST_EXAMPLE, RequestWrapper.class);
//
//		requestWrapper.setHeader(RequestHeader.builder()
//				.messageID(messageID.toString())
//				.serviceCode("1015")
//				.serviceName("SendSMSOTP")
//				.channelCode("zenpay")
//				.channelName("101")
//				.minorServiceVersion("1.0")
//				.routeCode("103")
//				.routeName("zencore")
//				.timeStamp("someTimestamp")
//				.serviceMode("sync")
//				.callBackURL("")
//				.build());
//		// Sends the message with valid credentials header
//		Exchange exchange = this.getProducerTemplate().send("direct:callServiceBack",
//				ExchangeBuilder.anExchange(this.getContext())
//						.withBody(REQUEST_EXAMPLE.getBytes())
//						.withHeader("messageID", messageID.toString())
//						.withHeader("serviceCode", "1015")
//						.withHeader("serviceName", "SendSMSOTP")
//						.withHeader("channelName", "zenpay")
//						.withHeader("channelCode", "101")
//						.withHeader("routeCode", "103")
//						.withHeader("routeName", "zencore")
//						.withHeader("timeStamp", "someTimestamp")
//						.withHeader("serviceMode", "sync")
//						.withHeader("callbackURL", "")
//						.withHeader("minorServiceVersion", "1.0")
//						.withProperty(ORIGINAL_REQUEST, requestWrapper).build());
//		ResponseWrapper responseWrapper = exchange.getIn().getBody(ResponseWrapper.class);
//
//		String statusRef = responseWrapper.getHeader().getEhfInfo().getItem().get(0).getEhfRef();
//
//		// Case 1: no DataValidationException
//		assertNotNull(exchange);
//		assertEquals("EHF-1000", statusRef);//statusRef, "EHF-1000"
//	}
//
//}