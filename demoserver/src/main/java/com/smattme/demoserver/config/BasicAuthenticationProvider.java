package com.smattme.demoserver.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.smattme.demoserver.exceptions.CustomApplicationException;

public class BasicAuthenticationProvider implements AuthenticationProvider {

    
    private PasswordEncoder passwordEncoder;
    private UserDetailsService userDetailsService;
    private Logger logger = LoggerFactory.getLogger(BasicAuthenticationProvider.class);

    public BasicAuthenticationProvider(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        if(logger.isDebugEnabled())
            logger.debug("authentication using BasicAuthenticationProvider: " + authentication);

        String clientId = (String) authentication.getPrincipal();
        String clientCredentials = (String) authentication.getCredentials();
        
        var userDetails = userDetailsService.loadUserByUsername(clientId);

        if(!passwordEncoder.matches(clientCredentials, userDetails.getPassword()))
            throw new CustomApplicationException(HttpStatus.UNAUTHORIZED, "Invalid client credentials");

        //authenticate the authentication token
        authentication.setAuthenticated(true);
        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return BasicAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
