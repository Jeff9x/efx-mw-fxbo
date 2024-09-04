package com.empirefx.fxbo_countries.commonlib.cache;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
@Scope("singleton")
@Getter
public class ProviderCredentialsLocalCache {
	
    private static final Logger logger = LoggerFactory.getLogger(ProviderCredentialsLocalCache.class);

    private String username;
    private String password;

    public void setProviderCredential(String username, String password) {
    	logger.debug("Setting provider credentials.");
        this.username = username;
        this.password = password;
    }

    public Boolean isProviderCredentialsPresent() {
    	logger.debug("isProviderCredentialsPresent: {}", (!Objects.isNull(username) && !Objects.isNull(password)));
        return !Objects.isNull(username) && !Objects.isNull(password);
    }

}
