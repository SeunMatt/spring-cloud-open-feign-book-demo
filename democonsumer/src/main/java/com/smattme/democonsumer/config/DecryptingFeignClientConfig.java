package com.smattme.democonsumer.config;

import com.smattme.democonsumer.interceptors.TokenRequestInterceptor;
import feign.Client;
import feign.RequestInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class DecryptingFeignClientConfig {

	private static final Logger logger = LoggerFactory.getLogger(DecryptingFeignClientConfig.class);

	@Value("${demoserver.auth-token}")
	private String authToken;

	@Value("${demoserver.aes-secret}")
	private String aesKey;

	@Bean
	public RequestInterceptor requestInterceptor() {
		return new TokenRequestInterceptor(authToken);
	}

	@Bean
	public Client client() {
		// passing null parameters will trigger using platform defaults
		return new DecryptingFeignClient(null, null, aesKey);
	}

	@Bean
	feign.Logger.Level feignLoggerLevel() {
		if (logger.isDebugEnabled())
			return feign.Logger.Level.FULL;
		return feign.Logger.Level.BASIC;
	}

}
