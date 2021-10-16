package com.smattme.democonsumer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smattme.democonsumer.interceptors.TokenRequestInterceptor;
import feign.Client;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class HttpStatusFeignClientConfig {

	private static final Logger logger = LoggerFactory.getLogger(HttpStatusFeignClientConfig.class);
	private final ObjectMapper objectMapper;

	@Value("${demoserver.auth-token}")
	private String authToken;

	public HttpStatusFeignClientConfig(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Bean
	public RequestInterceptor requestInterceptor() {
		return new TokenRequestInterceptor(authToken);
	}

	@Bean
	public ErrorDecoder errorDecoder() {
		return new CustomErrorDecoder(objectMapper);
	}

	@Bean
	public Client client() {
		// passing null parameters will trigger using platform defaults
		return new HttpStatusFeignClient(null, null);
	}

	@Bean
	feign.Logger.Level feignLoggerLevel() {
		if (logger.isDebugEnabled())
			return feign.Logger.Level.FULL;
		return feign.Logger.Level.BASIC;
	}

}
