package com.smattme.democonsumer.clients;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.smattme.democonsumer.config.SignatureFeignClientConfig;
import com.smattme.democonsumer.responses.GenericClientResponse;

@FeignClient(name = "sms-client", url = "${demoserver.base-url}",
	configuration = SignatureFeignClientConfig.class)
public interface SMSClient {

	@PostMapping("/core/sig/sms/send")
	GenericClientResponse<Map<String, Object>> sendSMS(@RequestBody Map<String, Object> request);

}
