package com.empirefx.fxbo_countries;

import com.empirefx.fxbo_countries.commonlib.models.RequestHeader;
import com.empirefx.fxbo_countries.configurations.CamelRouterTestSupport;
import com.empirefx.fxbo_countries.commonlib.models.RequestWrapper;
import org.apache.camel.EndpointInject;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.UseAdviceWith;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import java.util.HashMap;
import java.util.Map;

import static com.empirefx.fxbo_countries.commons.Examples.REQUEST_EXAMPLE;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@CamelSpringBootTest
@UseAdviceWith
@SpringBootTest(classes = EmpireFxGetFXBOCountriesApplication.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class RouteDispatchRequestTest extends CamelRouterTestSupport {

	@EndpointInject("mock:callServiceBack")
	MockEndpoint mockCallServiceBack;

	private static final Logger logger = LoggerFactory.getLogger(RouteDispatchRequestTest.class);

	/**
	 * Test to validate channel authentication against vault credentials - success
	 * 
	 * @throws Exception
	 */
	@Test
	void requestSuccessTest() throws Exception {

		logger.info("-------------------requestSuccessTest--------------");

		Long messageID = System.currentTimeMillis();


		Map<String, Object> headerCamel = new HashMap<>();

		RequestWrapper request = CamelRouterTestSupport.payloadObjectMapper
				.readValue(REQUEST_EXAMPLE, RequestWrapper.class);

		request.setHeader(RequestHeader.builder()
				.messageID(messageID.toString())
				.serviceCode("1015")
				.serviceName("SendSMSOTP")
				.channelCode("zenpay")
				.channelName("101")
				.minorServiceVersion("1.0")
				.routeCode("103")
				.routeName("zencore")
				.timeStamp("someTimestamp")
				.serviceMode("sync")
				.callBackURL("")
				.build());

		headerCamel.put("messageID", messageID.toString());
		headerCamel.put("Content-Type", "application/json");
		headerCamel.put("Accept", "application/json");
		headerCamel.put("serviceCode", "1015");
		headerCamel.put("serviceName", "SendSMSOTP");
		headerCamel.put("channelName", "zenpay");
		headerCamel.put("channelCode", "101");
		headerCamel.put("routeCode", "103");
		headerCamel.put("routeName", "zencore");
		headerCamel.put("timeStamp", "someTimestamp");
		headerCamel.put("serviceMode", "sync");
		headerCamel.put("callbackURL", "");
		headerCamel.put("minorServiceVersion", "1.0");

		// sending data to route consumer
		String case1 = this.sendMessage("seda:dispatchRequest", request, headerCamel);

		mockCallServiceBack.assertIsSatisfied();
		assertNotNull(case1);
		assertTrue(case1.contains(messageID.toString()));
	}

	@Test
	void requestNotHeaderTest() throws Exception {

		logger.info("-------------------requestNotHeaderTest--------------");

		Long messageID = System.currentTimeMillis();


		Map<String, Object> headerCamelH = new HashMap<String, Object>();

		RequestWrapper request = CamelRouterTestSupport.payloadObjectMapper
				.readValue(REQUEST_EXAMPLE, RequestWrapper.class);

		request.setHeader(RequestHeader.builder()
				.messageID(messageID.toString())
				.serviceCode("1031")
				.serviceName("CreateWallet")
				.channelCode("zenpay")
				.channelName("101")
				.minorServiceVersion("1.0")
				.routeCode("103")
				.routeName("zencore")
				.timeStamp("someTimestamp")
				.serviceMode("sync")
				.callBackURL("")
				.build());

		headerCamelH.put("messageID", messageID.toString());
		headerCamelH.put("Content-Type", "application/json");
		headerCamelH.put("Accept", "application/json");
		headerCamelH.put("serviceCode", "1015");
		headerCamelH.put("serviceName", "SendSMSOTP");
		headerCamelH.put("channelName", "zenpay");
		headerCamelH.put("channelCode", "101");
		headerCamelH.put("routeCode", "103");
		headerCamelH.put("routeName", "zencore");
		headerCamelH.put("timeStamp", "someTimestamp");
		headerCamelH.put("serviceMode", "sync");
		headerCamelH.put("callbackURL", "");
		headerCamelH.put("minorServiceVersion", "1.0");

		mockCallServiceBack.expectedMessageCount(1);

		String case1 = this.sendMessage("seda:dispatchRequest", request, headerCamelH);

		mockCallServiceBack.expectedMessageCount(1);

		assertNotNull(case1);
		assertTrue(case1.contains(messageID.toString()));

	}

}