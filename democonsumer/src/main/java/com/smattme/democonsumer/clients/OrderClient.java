package com.smattme.democonsumer.clients;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.smattme.democonsumer.config.BasicFeignClientConfig;
import com.smattme.democonsumer.responses.GenericClientResponse;

@FeignClient(name = "order-client", url = "${demoserver.base-url}", 
			 configuration = BasicFeignClientConfig.class)
public interface OrderClient {

	@PostMapping("/core/basic/orders")
	GenericClientResponse<Map<String, Object>> placeOrder(@RequestBody Map<String, Object> request);

}
