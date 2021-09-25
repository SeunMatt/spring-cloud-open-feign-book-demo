package com.smattme.democonsumer.clients;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.smattme.democonsumer.config.DecryptingFeignClientConfig;
import com.smattme.democonsumer.responses.GenericClientResponse;

@FeignClient(name = "card-client", url = "${demoserver.base-url}",
		configuration = DecryptingFeignClientConfig.class)
public interface CardClient {
	
	@GetMapping("/core/token/cards/{customerId}")
	GenericClientResponse<Map<String, Object>> getCustomerCardDetails(@PathVariable String customerId);

}
