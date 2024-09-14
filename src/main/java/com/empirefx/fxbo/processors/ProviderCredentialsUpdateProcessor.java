package com.empirefx.fxbo.processors;


import com.empirefx.fxbo.commonlib.cache.ProviderCredentialsLocalCache;
import com.empirefx.fxbo.commonlib.models.Credentials;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import static com.empirefx.fxbo.commonlib.constants.ConstantsCommons.*;


@Component
public class ProviderCredentialsUpdateProcessor implements Processor {


    private final ProviderCredentialsLocalCache providerCredentialsLocalCache;

    public ProviderCredentialsUpdateProcessor(ProviderCredentialsLocalCache providerCredentialsLocalCache) {
        this.providerCredentialsLocalCache = providerCredentialsLocalCache;
    }


    @Override
    public void process(Exchange exchange) throws Exception {
    	
    	Credentials credentials = exchange.getIn().getBody(Credentials.class);
        providerCredentialsLocalCache.setProviderCredential(credentials.getUserName(), credentials.getPassword());
        exchange.getIn().removeHeaders(REMOVE_HEADERS_PATTERN);
    }
}
