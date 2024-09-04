package com.empirefx.fxbo_countries.processors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.empirefx.fxbo_countries.commonlib.models.Keys;
import com.empirefx.fxbo_countries.commonlib.models.RequestWrapper;
import jakarta.annotation.PostConstruct;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


import static com.empirefx.fxbo_countries.commonlib.constants.ConstantsCommons.*;

@Component
public class EhfRequestCreatorProcessor implements Processor {

	private Gson gson;

	@PostConstruct
	public void createNewDocumentBuilder() {
		gson = new GsonBuilder().setPrettyPrinting().create();
	}

	@Override
	public void process(Exchange exchange) throws Exception {

		RequestWrapper request = exchange.getProperty(ORIGINAL_REQUEST, RequestWrapper.class);

		String body = exchange.getIn().getBody(String.class);

		JsonObject jsonObjectResponse = JsonParser.parseString(body).getAsJsonObject();

		String code = "";
		String msg = "";

		StringBuilder errorKey = new StringBuilder(request.getHeader().getRouteCode()).append(".");

		code = jsonObjectResponse.get("errorCode").getAsString();
		msg = jsonObjectResponse.get("errorMessage").getAsString();

		String encodedString = Base64.getEncoder().encodeToString(msg.getBytes());

		List<StringBuilder> keys = new ArrayList<>();
		errorKey.append(code);
		errorKey.append(".");
		errorKey.append(encodedString);

		keys.add(errorKey);

		Keys ehfKeys = new Keys();
		ehfKeys.setEhfKeys(keys);
		exchange.getIn().setBody(gson.toJson(ehfKeys));
	}
}