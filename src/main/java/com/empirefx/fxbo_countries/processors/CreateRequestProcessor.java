package com.empirefx.fxbo_countries.processors;


import com.empirefx.fxbo_countries.commonlib.cache.ProviderCredentialsLocalCache;
import com.empirefx.fxbo_countries.commonlib.constants.ConstantsCommons;
import com.empirefx.fxbo_countries.commonlib.models.AppLogger;
import com.empirefx.fxbo_countries.commonlib.models.RequestWrapper;
import com.empirefx.fxbo_countries.models.provider.ProviderAccountDetails;
import com.empirefx.fxbo_countries.models.provider.ProviderApiRequest;
import com.empirefx.fxbo_countries.models.provider.ProviderSmsDetails;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Base64;

import static com.empirefx.fxbo_countries.commonlib.constants.ConstantsCommons.*;


@Component
public class CreateRequestProcessor implements Processor {

    private final ProviderCredentialsLocalCache providerCredentialsLocalCache;

    public CreateRequestProcessor(ProviderCredentialsLocalCache providerCredentialsLocalCache) {
        this.providerCredentialsLocalCache = providerCredentialsLocalCache;
    }


    @Override
    public void process(Exchange exchange) throws Exception {

        // Set body and headers

        RequestWrapper requestWrapper = exchange.getProperty(ORIGINAL_REQUEST, RequestWrapper.class);

        ProviderSmsDetails providerSmsDetails = ProviderSmsDetails.builder()
                .dest(requestWrapper.getRequestPayload().getPrimaryData().getBusinessKey())
                .src(requestWrapper.getRequestPayload().getSms().getSender())
                .text(requestWrapper.getRequestPayload().getSms().getSmsText())
                .unicode(requestWrapper.getRequestPayload().getSms().getUnicode()).build();

        String encryptedCred = providerCredentialsLocalCache.getPassword();
        String systemId  = "";
        String password  = "";
        if(StringUtils.isNotEmpty(encryptedCred)){
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedCred);
            String decodedCred = new String(decodedBytes);
            systemId = decodedCred.split(":")[0];
            password = decodedCred.split(":")[1];
        }else{
            systemId = "fakeUsername";
            password = "fakePassword";
        }

        ProviderAccountDetails providerAccountDetails = ProviderAccountDetails.builder()
                .password(password)
                .systemId(systemId).build();

        ProviderApiRequest providerApiRequest = ProviderApiRequest.builder()
                .sms(providerSmsDetails)
                .account(providerAccountDetails).build();

        exchange.getIn().setBody(providerApiRequest.toString());


        exchange.getIn().removeHeaders("Camel*");

        exchange.setProperty(ConstantsCommons.APP_REQUEST_TYPE, "Provider Request Data ------- (To Backend): {}");
        exchange.setProperty(ConstantsCommons.APP_REQUEST, AppLogger.builder()
                .headerData(exchange.getIn().getHeaders())
                .payloadData(exchange.getIn().getBody())
                .build());


    }
}