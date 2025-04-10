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
public class GetIbApplicationRequestProcessor implements Processor {

    @Value("${adaptive.token}")
    private String token;
    @Value("${atomic.uriIbApplication}")
    private String uriIbApplication;
    @Value("${atomic.uri-params}")
    private String uriParams;


    @Override
    public void process(Exchange exchange) throws Exception {
        // Retrieve the fxbouserid path parameter from the Camel exchange
        HttpServletRequest request = exchange.getIn().getBody(HttpServletRequest.class);

        String ipRemote = Objects.isNull(request) ? EMPTY : request.getRemoteAddr();

        // Set the Bearer token in the Authorization header
        exchange.getIn().setHeader(CALLER_IP, ipRemote);
        exchange.getIn().setHeader("Authorization", token);
        System.out.println("Authorization Header Set:");

        System.out.println("Incoming Caller IP :"+ipRemote);

        String IbId = exchange.getIn().getHeader("configId", String.class);
        System.out.println("configId :"+IbId);


        String finaUrl = uriIbApplication+"/"+IbId+uriParams;
        System.out.println("Fina URL :"+finaUrl);
        exchange.getIn().setHeader("finaUrl", finaUrl);
    }
}
