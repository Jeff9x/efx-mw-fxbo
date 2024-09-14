package com.empirefx.fxbo.processors;


import com.empirefx.fxbo.commonlib.cache.ProviderCredentialsLocalCache;
import com.empirefx.fxbo.commonlib.configurations.AppConfiguration;
import com.empirefx.fxbo.commonlib.models.RequestWrapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.empirefx.fxbo.commonlib.constants.ConstantsCommons.*;

@Component
public class ProviderCredentialsCheckerProcessor implements Processor {

	private static final Logger logger = LoggerFactory.getLogger(ProviderCredentialsCheckerProcessor.class);

    private final ProviderCredentialsLocalCache providerCredentialsLocalCache;

    private final AppConfiguration appConfiguration;
    
    @Value("${vault-connector.uri}")
    private String vaultConnectorUri;
    
    @Value("${vault-connector.uri-params}")
    private String vaultConnectorUriParams;

    public ProviderCredentialsCheckerProcessor(ProviderCredentialsLocalCache providerCredentialsLocalCache, AppConfiguration appConfiguration) {
        this.providerCredentialsLocalCache = providerCredentialsLocalCache;
        this.appConfiguration = appConfiguration;
    }


    @Override
    public void process(Exchange exchange) throws Exception {
        RequestWrapper request = exchange.getProperty(ORIGINAL_REQUEST, RequestWrapper.class);
        Boolean providerCredentialsPresent = providerCredentialsLocalCache.isProviderCredentialsPresent();
        exchange.setProperty(NO_PROVIDER_CREDENTIALS_PRESENT, !providerCredentialsPresent);
        if (Boolean.FALSE.equals(providerCredentialsPresent)) {
        	logger.debug(DEBUG_MESSAGE);
            String dynamicUri = vaultConnectorUri;
            dynamicUri += request.getHeader().getRouteCode();
            dynamicUri += SLASH;
            dynamicUri += appConfiguration.getVasco().getCred();
            dynamicUri += vaultConnectorUriParams;
            logger.info("\n Credentials not found in local cache :: {}", dynamicUri); //Change this log
            exchange.setProperty(VAULT_CONNECTOR_URI, dynamicUri);
            exchange.getIn().removeHeaders(REMOVE_HEADERS_PATTERN,"conversationID","messageID","mockSuccessResponseFlag");
            exchange.getIn().setHeader(SERVICE_CODE, request.getHeader().getServiceCode());
            exchange.getIn().setBody(null);
        } else{
            logger.info("\n Credentials found in local cache "); //Change this log
        }
    }

}
