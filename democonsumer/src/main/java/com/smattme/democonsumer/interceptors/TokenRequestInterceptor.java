package com.smattme.democonsumer.interceptors;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class TokenRequestInterceptor implements RequestInterceptor {

	private String authToken;

	public TokenRequestInterceptor(String authToken) {
		this.authToken = authToken;
	}

	@Override
	public void apply(RequestTemplate template) {
		// add the token header to the request
		template.header("Auth-Token", authToken);
	}

}
