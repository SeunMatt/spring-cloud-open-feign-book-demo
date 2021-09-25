package com.smattme.demoserver.config;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class TokenAuthenticationToken extends AbstractAuthenticationToken {

    
	private static final long serialVersionUID = -4964924983629058375L;
    private String principal;
    private String credentials;


    public TokenAuthenticationToken(String principal, String credentials) {
        super(new ArrayList<>());
        this.principal = principal;
        this.credentials = credentials;
    }


    public TokenAuthenticationToken(String principal, String credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
    }

    
    public TokenAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }


}
