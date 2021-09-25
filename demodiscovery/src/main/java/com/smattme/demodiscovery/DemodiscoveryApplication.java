package com.smattme.demodiscovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class DemodiscoveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemodiscoveryApplication.class, args);
	}

}
