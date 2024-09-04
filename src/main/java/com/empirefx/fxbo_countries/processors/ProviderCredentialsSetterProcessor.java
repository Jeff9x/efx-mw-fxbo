package com.empirefx.fxbo_countries.processors;


import com.empirefx.fxbo_countries.commonlib.cache.ProviderCredentialsLocalCache;
import com.empirefx.fxbo_countries.commonlib.exceptions.NoProviderCredentialsException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.empirefx.fxbo_countries.commonlib.constants.ConstantsCommons.*;


@Component
public class ProviderCredentialsSetterProcessor implements Processor {


    private final ProviderCredentialsLocalCache providerCredentialsLocalCache;

    public ProviderCredentialsSetterProcessor(ProviderCredentialsLocalCache providerCredentialsLocalCache) {
        this.providerCredentialsLocalCache = providerCredentialsLocalCache;
    }


    @Override
    public void process(Exchange exchange) throws Exception {
        String username = Optional.ofNullable(exchange.getIn().getHeader(USERNAME_RESPONSE_HEADER_NAME, String.class)).orElseThrow(NoProviderCredentialsException::new);
        String password = Optional.ofNullable(exchange.getIn().getHeader(PASSWORD_RESPONSE_HEADER_NAME, String.class)).orElseThrow(NoProviderCredentialsException::new);
        providerCredentialsLocalCache.setProviderCredential(username, password);
    }
}
