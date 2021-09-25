package com.smattme.democonsumer.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import com.smattme.democonsumer.interceptors.SignatureRequestInterceptor;

import feign.RequestInterceptor;

public class SignatureFeignClientConfig {

	private static final Logger logger = LoggerFactory.getLogger(SignatureFeignClientConfig.class);

	@Value("${demoserver.client-id}")
	private String clientId;

	@Value("${demoserver.signature-secret}")
	private String signatureSecret;

	@Bean
	public RequestInterceptor requestInterceptor() {
		return new SignatureRequestInterceptor(clientId, signatureSecret);
	}

	@Bean
	feign.Logger.Level feignLoggerLevel() {
		if (logger.isDebugEnabled())
			return feign.Logger.Level.FULL;
		return feign.Logger.Level.BASIC;
	}

}
