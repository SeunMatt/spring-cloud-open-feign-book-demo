package com.smattme.demoserver.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.smattme.demoserver.exceptions.CustomApplicationException;

public class TokenAuthenticationProvider implements AuthenticationProvider {

    
    private String staticApiToken;
    private Logger logger = LoggerFactory.getLogger(TokenAuthenticationProvider.class);

    public TokenAuthenticationProvider(String staticApiToken) {
        this.staticApiToken = staticApiToken;
    }

    /**
     * this Authentication provider compares the 
     * user's supplied API token to a static token.
     * A production instance will use full-on oAuth or at least
     * a database-backed token source
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        if(logger.isDebugEnabled())
            logger.debug("authentication using TokenAuthenticationProvider");

        String clientCredentials = (String) authentication.getCredentials();

        if(!StringUtils.equals(clientCredentials, staticApiToken))
            throw new CustomApplicationException(HttpStatus.UNAUTHORIZED, "Invalid authentication token");

        authentication.setAuthenticated(true);
        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return TokenAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
