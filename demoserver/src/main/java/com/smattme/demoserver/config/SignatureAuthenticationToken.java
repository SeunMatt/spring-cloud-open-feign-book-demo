package com.smattme.demoserver.config;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class SignatureAuthenticationToken extends AbstractAuthenticationToken {

    
	private static final long serialVersionUID = -4964924983629058375L;
    private String principal;
    private String credentials;
    private String signatureBody;


    public SignatureAuthenticationToken(String principal, String credentials, String signatureBody) {
        super(new ArrayList<>());
        this.principal = principal;
        this.credentials = credentials;
        this.signatureBody = signatureBody;
    }


    public SignatureAuthenticationToken(String principal, String credentials, String signatureBody, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        this.signatureBody = signatureBody;
    }

    


	public String getPrincipal() {
		return principal;
	}


	public void setPrincipal(String principal) {
		this.principal = principal;
	}


	public String getCredentials() {
		return credentials;
	}


	public void setCredentials(String credentials) {
		this.credentials = credentials;
	}


	public String getSignatureBody() {
		return signatureBody;
	}


	public void setSignatureBody(String signatureBody) {
		this.signatureBody = signatureBody;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
    
    


}
