package com.empirefx.fxbo.processors;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.empirefx.fxbo.commonlib.constants.ConstantsCommons.CALLER_IP;
import static com.empirefx.fxbo.commonlib.constants.ConstantsCommons.EMPTY;

@Component
public class CreateUserAccountHeadersSetterProcessor implements Processor {
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
        // Get Caller IP
        HttpServletRequest request = exchange.getIn().getBody(HttpServletRequest.class);

        String ipRemote = Objects.isNull(request) ? EMPTY : request.getRemoteAddr();

        // Set the Bearer token in the Authorization header
        exchange.getIn().setHeader(CALLER_IP, ipRemote);
        exchange.getIn().setHeader("Authorization", token);
        System.out.println("Authorization Header Set:");

        System.out.println("Incoming Caller IP :"+ipRemote);
    }


}