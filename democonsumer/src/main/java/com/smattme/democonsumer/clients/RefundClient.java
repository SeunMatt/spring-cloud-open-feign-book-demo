package com.smattme.democonsumer.clients;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.smattme.democonsumer.config.TokenFeignClientConfig;
import com.smattme.democonsumer.responses.GenericClientResponse;

@FeignClient(name = "refund-client", url = "${demoserver.base-url}",
			configuration = TokenFeignClientConfig.class)
public interface RefundClient {

	@PostMapping("/core/token/refunds")
	GenericClientResponse<Map<String, Object>> submitRefund(@RequestBody Map<String, Object> request);

}
