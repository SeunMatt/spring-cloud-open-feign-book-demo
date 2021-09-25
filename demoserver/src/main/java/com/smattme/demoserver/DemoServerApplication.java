package com.smattme.demoserver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.smattme.demoserver.filters.AesEncryptionFilter;

@SpringBootApplication
@EnableEurekaClient
public class DemoServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoServerApplication.class, args);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean
	public FilterRegistrationBean<AesEncryptionFilter> filterRegistrationBean(@Value("${spring.security.static-aes-key}") String aesSecretKey) {
		var filterBean = new FilterRegistrationBean<AesEncryptionFilter>();
		filterBean.setFilter(new AesEncryptionFilter(aesSecretKey));
		filterBean.setOrder(FilterRegistrationBean.HIGHEST_PRECEDENCE);
		filterBean.addUrlPatterns("/core/token/bank/debit");
		return filterBean;
	}

}
