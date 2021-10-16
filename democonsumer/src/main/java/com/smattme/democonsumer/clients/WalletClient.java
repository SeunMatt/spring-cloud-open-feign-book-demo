package com.smattme.democonsumer.clients;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.smattme.democonsumer.config.HttpStatusFeignClientConfig;
import com.smattme.democonsumer.responses.GenericClientResponse;

@FeignClient(name = "wallet-client", url = "${demoserver.base-url}",
	configuration = HttpStatusFeignClientConfig.class)
public interface WalletClient {
	
	@PostMapping("/core/token/wallet/credit")
	GenericClientResponse<Map<String, Object>> creditCustomer(@RequestBody Map<String, Object> request);

}
