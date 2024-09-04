package com.empirefx.fxbo_countries;


import com.empirefx.fxbo_countries.configurations.CamelRouterTestSupport;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.builder.ExchangeBuilder;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.UseAdviceWith;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static com.empirefx.fxbo_countries.commons.Examples.REQUEST_EXAMPLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static com.empirefx.fxbo_countries.commonlib.constants.ConstantsCommons.*;


@CamelSpringBootTest
@UseAdviceWith
@ActiveProfiles(value = "dev")
@SpringBootTest(classes = EmpireFxGetFXBOCountriesApplication.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class BeginRequestTest extends CamelRouterTestSupport {

	private static final Logger logger = LoggerFactory.getLogger(BeginRequestTest.class);

	@Test
	void transfortRequestTestSuccess() throws Exception {

		logger.info("============= transfortRequestTestSuccess====================");

		Long messageID = System.currentTimeMillis();

		Exchange exchange = ExchangeBuilder.anExchange(this.getContext())
				.withBody(REQUEST_EXAMPLE)
				.withHeader("messageID", messageID.toString())
				.withHeader("serviceCode", "1015")
				.withHeader("serviceName", "SendSMSOTP")
				.withHeader("channelName", "zenpay")
				.withHeader("channelCode", "101")
				.withHeader("routeCode", "103")
				.withHeader("routeName", "zencore")
				.withHeader("timeStamp", "someTimestamp")
				.withHeader("serviceMode", "sync")
				.withHeader("callbackURL", "")
				.withHeader("minorServiceVersion", "1.0")
				.build();

		Message message = exchange.getIn();

		Exchange exchangeFinal = ExchangeBuilder.anExchange(this.getContext())
				.withProperty(REQUEST_EXAMPLE, message)
				.build();

		this.getProducerTemplate().send("direct:callServiceBack", exchangeFinal);

		Map<String, Object> caseCamel = exchange.getIn().getHeaders();
		System.out.println("------------------" +caseCamel);
		// Case 1: no DataValidationException
		assertNotNull(exchange);
		assertNotNull(caseCamel);
		assertEquals("1015", caseCamel.get("serviceCode"));
	}

	@Test
	void bathRequestTestFailure() throws Exception {

		logger.info("============= bathRequestTestFailure====================");

		Long messageID = System.currentTimeMillis();

		Exchange exchange = ExchangeBuilder.anExchange(this.getContext())
				.withBody(REQUEST_EXAMPLE)
				.withHeader("messageID", messageID.toString())
				.withHeader("serviceCode", "1015")
				.withHeader("serviceName", "SendSMSOTP")
				.withHeader("channelName", "zenpay")
				.withHeader("channelCode", "101")
				.withHeader("routeCode", "103")
				.withHeader("routeName", "zencore")
				.withHeader("timeStamp", "someTimestamp")
				.withHeader("serviceMode", "sync")
				.withHeader("callbackURL", "")
				.withHeader("minorServiceVersion", "1.0")
				.build();

		Message message = exchange.getIn();

		Exchange exchangeFinal = ExchangeBuilder.anExchange(this.getContext())
				.withProperty(ORIGINAL_REQUEST, message)
				.build();

		this.getProducerTemplate().send("direct:callServiceBack", exchangeFinal);

		Map<String, Object>  headers = exchangeFinal.getIn().getHeaders();

		// Case 1: DataValidationException
		assertNotNull(headers);
		assertNotNull(exchange);
	}
}