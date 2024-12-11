package com.empirefx.fxbo.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.empirefx.fxbo.commonlib.constants.ConstantsCommons.CALLER_IP;
import static com.empirefx.fxbo.commonlib.constants.ConstantsCommons.EMPTY;

@Component
public class GetPlatformServerRequestProcessor implements Processor {
    @Value("${adaptive.token}")
    private String token;
    @Value("${atomic.uriPlatformServer}")
    private String uriPlatformServer;
    @Value("${atomic.uri-params}")
    private String uriParamspathTradingAccounts;
    @Override
    public void process(Exchange exchange) throws Exception {
        // Get Caller IP
        HttpServletRequest request = exchange.getIn().getBody(HttpServletRequest.class);

        String ipRemote = Objects.isNull(request) ? EMPTY : request.getRemoteAddr();

        // Set the Bearer token in the Authorization header
        exchange.getIn().setHeader(CALLER_IP, ipRemote);
        exchange.getIn().setHeader("Authorization", token);
        System.out.println("Authorization Header Set:");

        System.out.println("Incoming Caller IP :"+ipRemote);

        String crmserverId = exchange.getIn().getHeader("serverId", String.class);
        System.out.println("Crm PlatformServer ID :"+crmserverId);


        String finaUrl = uriPlatformServer+"/"+crmserverId+uriParamspathTradingAccounts;
        System.out.println("Fina URL :"+finaUrl);
        exchange.getIn().setHeader("finaUrl", finaUrl);
    }


}