package com.smattme.demoserver.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.smattme.demoserver.exceptions.CustomApplicationException;
import com.smattme.demoserver.helpers.CryptoHelper;

public class SignatureAuthenticationProvider implements AuthenticationProvider {

    
    private String staticSignatureSecret;
    private Logger logger = LoggerFactory.getLogger(SignatureAuthenticationProvider.class);

    public SignatureAuthenticationProvider(String staticSignatureSecret) {
        this.staticSignatureSecret = staticSignatureSecret;
    }

   
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        if(logger.isDebugEnabled())
            logger.debug("authentication using TokenAuthenticationProvider");
        
        SignatureAuthenticationToken signatureAuthenticationToken = (SignatureAuthenticationToken) authentication;

        String clientSignature = signatureAuthenticationToken.getCredentials();
        String signatureBody = signatureAuthenticationToken.getSignatureBody();
        
        String serverComputedSignature;
		try {
			serverComputedSignature = CryptoHelper.generateHMACSHA256Signature(signatureBody, staticSignatureSecret);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
        

        if(!StringUtils.equals(serverComputedSignature, clientSignature))
            throw new CustomApplicationException(HttpStatus.UNAUTHORIZED, "Invalid request signature");

        authentication.setAuthenticated(true);
        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SignatureAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
