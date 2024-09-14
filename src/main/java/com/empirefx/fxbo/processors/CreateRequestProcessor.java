package com.empirefx.fxbo.processors;


import com.empirefx.fxbo.commonlib.cache.ProviderCredentialsLocalCache;
import com.empirefx.fxbo.commonlib.constants.ConstantsCommons;
import com.empirefx.fxbo.commonlib.models.AppLogger;
import com.empirefx.fxbo.commonlib.models.RequestWrapper;
import com.empirefx.fxbo.models.provider.ProviderAccountDetails;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Base64;

import static com.empirefx.fxbo.commonlib.constants.ConstantsCommons.*;


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



        exchange.getIn().removeHeaders("Camel*");

        exchange.setProperty(ConstantsCommons.APP_REQUEST_TYPE, "Provider Request Data ------- (To Backend): {}");
        exchange.setProperty(ConstantsCommons.APP_REQUEST, AppLogger.builder()
                .headerData(exchange.getIn().getHeaders())
                .payloadData(exchange.getIn().getBody())
                .build());


    }
}