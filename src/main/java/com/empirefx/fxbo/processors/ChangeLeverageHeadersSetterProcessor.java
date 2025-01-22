package com.empirefx.fxbo.processors;

import com.empirefx.fxbo.models.provider.AppTokenJava;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

import static com.empirefx.fxbo.commonlib.constants.ConstantsCommons.CALLER_IP;
import static com.empirefx.fxbo.commonlib.constants.ConstantsCommons.EMPTY;

@Component
public class ChangeLeverageHeadersSetterProcessor implements Processor {
    @Value("${adaptive.token}")
    private String token;
    @Value("${atomic1.uriChangeLeverage}")
    private String uriChangeLeverage;
    @Value("${atomic1.uri-paramsResetPassword}")
    private String uriParamsChangeLeverage;
//    @Value("${adaptive.changeLeverageConfig}")
//    private String config;

    private final ObjectMapper objectMapper = new ObjectMapper(); // For JSON serialization

    @Override
    public void process(Exchange exchange) throws Exception {
        // Get Caller IP
        HttpServletRequest request = exchange.getIn().getBody(HttpServletRequest.class);

        String ipRemote = Objects.isNull(request) ? EMPTY : request.getRemoteAddr();

        System.out.println("Incoming Caller IP :" + ipRemote);

        // Get and validate the request body
        Map<String, Object> requestBody = exchange.getIn().getBody(Map.class);

        if (requestBody == null) {
            throw new IllegalArgumentException("Request body is missing or invalid.");
        }

        // Parse the JSON payload
        JSONObject jsonObject = new JSONObject(requestBody);
        System.out.println("Incoming JSON: " + jsonObject);

        String platformId = String.valueOf(jsonObject.get("platformId"));
        System.out.println("Incoming Platform Id: " + platformId);

        String leverage = String.valueOf(jsonObject.get("leverage"));
        System.out.println("Incoming Leverage: " + leverage);

        // Set the Bearer token in the Authorization header
        exchange.getIn().setHeader(CALLER_IP, ipRemote);
        exchange.getIn().setHeader("Authorization", token);
        System.out.println("Authorization Header Set:");

        // Get CRM account ID from headers
        String crmAccountId = exchange.getIn().getHeader("crmAccountId", String.class);
        if (crmAccountId == null) {
            throw new IllegalArgumentException("CRM account ID is missing in the request headers.");
        }
        System.out.println("CRM Account ID :" + crmAccountId);

        // Construct the final URL
        String finaUrl = uriChangeLeverage + platformId + "-" + crmAccountId + uriParamsChangeLeverage;
        System.out.println("Final URL :" + finaUrl);
        exchange.getIn().setHeader("finaUrl", finaUrl);

        // Construct the final payload
        String finalPayload = AppTokenJava.toStringChangeLeverage(Integer.parseInt(leverage));
        System.out.println("Final Payload: " + finalPayload);

        // Set the JSON string back to the exchange body for further processing
        exchange.getIn().setBody(finalPayload);
    }
}