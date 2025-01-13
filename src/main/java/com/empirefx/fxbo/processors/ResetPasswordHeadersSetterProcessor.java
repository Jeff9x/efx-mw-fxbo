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
public class ResetPasswordHeadersSetterProcessor implements Processor {
    @Value("${adaptive.token}")
    private String token;
    @Value("${atomic1.uriResetPassword}")
    private String uriResetPassword;
    @Value("${atomic1.uri-paramsResetPassword}")
    private String uriParamsResetPassword;
    @Value("${adaptive.resetPasswordConfig}")
    private String config;

    private final ObjectMapper objectMapper = new ObjectMapper(); // For JSON serialization

    @Override
    public void process(Exchange exchange) throws Exception {
        // Get Caller IP
        HttpServletRequest request = exchange.getIn().getBody(HttpServletRequest.class);

        String ipRemote = Objects.isNull(request) ? EMPTY : request.getRemoteAddr();

        System.out.println("Incoming Caller IP :"+ipRemote);

        Map<String, Object> requestBody = exchange.getIn().getBody(Map.class);

//        System.out.println("Incoming Request Validate: " + requestBody);

        // Parse the JSON payload
        JSONObject jsonObject = new JSONObject(requestBody);
        System.out.println("Incoming JSON: " + jsonObject);

        String platformId = String.valueOf(jsonObject.get("platformId"));
        System.out.println("Incoming Platform Id: " + platformId);

        String newPassword = String.valueOf(jsonObject.get("newPassword"));
        System.out.println("Incoming Password: " + newPassword);

        // Set the Bearer token in the Authorization header
        exchange.getIn().setHeader(CALLER_IP, ipRemote);
        exchange.getIn().setHeader("Authorization", token);
        System.out.println("Authorization Header Set:");

        String crmAccountId = exchange.getIn().getHeader("crmAccountId", String.class);
        System.out.println("Crm Account ID :"+crmAccountId);

        String finaUrl = uriResetPassword+platformId+"-"+crmAccountId+uriParamsResetPassword;
        System.out.println("Final URL :"+finaUrl);
        exchange.getIn().setHeader("finaUrl", finaUrl);

        // Construct the final payload
        String finalPayload = AppTokenJava.toStringResetPassword(config, newPassword);
        System.out.println("Final Payload: " + finalPayload);
        // Set the JSON string back to the exchange body for further processing
        exchange.getIn().setBody(finalPayload);

    }


}