package com.smattme.democonsumer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class BasicFeignClientConfig {

	private static final Logger logger = LoggerFactory.getLogger(BasicFeignClientConfig.class);

	private final ObjectMapper objectMapper;

	@Value("${demoserver.username}")
	private String username;

	@Value("${demoserver.password}")
	private String password;

	public BasicFeignClientConfig(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Bean
	public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
		return new BasicAuthRequestInterceptor(username, password);
	}

	@Bean
	public ErrorDecoder errorDecoder() {
		return new CustomErrorDecoder(objectMapper);
	}

	@Bean
	feign.Logger.Level feignLoggerLevel() {
		if (logger.isDebugEnabled())
			return feign.Logger.Level.FULL;
		return feign.Logger.Level.BASIC;
	}

}
