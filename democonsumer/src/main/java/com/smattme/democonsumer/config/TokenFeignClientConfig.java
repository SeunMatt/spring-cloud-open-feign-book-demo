package com.smattme.democonsumer.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import com.smattme.democonsumer.interceptors.TokenRequestInterceptor;

import feign.RequestInterceptor;

public class TokenFeignClientConfig {

	@Value("${demoserver.auth-token}")
	private String authToken;

	private static final Logger logger = LoggerFactory.getLogger(TokenFeignClientConfig.class);

	public TokenFeignClientConfig() {}

	@Bean
	public RequestInterceptor requestInterceptor() {
		return new TokenRequestInterceptor(authToken);
	}

	@Bean
	feign.Logger.Level feignLoggerLevel() {
		if (logger.isDebugEnabled())
			return feign.Logger.Level.FULL;
		return feign.Logger.Level.BASIC;
	}

}
