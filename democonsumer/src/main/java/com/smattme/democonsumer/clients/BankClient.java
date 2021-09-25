package com.smattme.democonsumer.clients;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.smattme.democonsumer.config.EncryptionFeignClientConfig;
import com.smattme.democonsumer.responses.GenericClientResponse;

@FeignClient(name = "bank-client", url = "${demoserver.base-url}",
configuration = EncryptionFeignClientConfig.class)
public interface BankClient {
	
	
	@PostMapping("/core/token/bank/debit")
	GenericClientResponse<Map<String, Object>> debitCustomer(@RequestBody Map<String, Object> request);

}
