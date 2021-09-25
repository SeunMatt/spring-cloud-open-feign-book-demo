package com.smattme.democonsumer.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import com.smattme.democonsumer.interceptors.EncryptionRequestInterceptor;

import feign.RequestInterceptor;

public class EncryptionFeignClientConfig {

	@Value("${demoserver.aes-secret}")
	private String aesSecretKey;

	@Value("${demoserver.auth-token}")
	private String authToken;

	private static final Logger logger = LoggerFactory.getLogger(EncryptionFeignClientConfig.class);

	@Bean
	public RequestInterceptor requestInterceptor() {
		return new EncryptionRequestInterceptor(aesSecretKey, authToken);
	}

	@Bean
	feign.Logger.Level feignLoggerLevel() {
		if (logger.isDebugEnabled())
			return feign.Logger.Level.FULL;
		return feign.Logger.Level.BASIC;
	}

}
