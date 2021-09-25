package com.smattme.democonsumer.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "health-client", url = "${demoserver.base-url}")
public interface HealthClient {

	@GetMapping("/basic/health")
	String health();

}
