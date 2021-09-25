package com.smattme.democonsumer.clients;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.smattme.democonsumer.responses.GenericClientResponse;

@FeignClient(name = "demoserver")
public interface EmailClient {

	@PostMapping("/basic/email/send")
	GenericClientResponse<Map<String, Object>> sendEmail(@RequestBody Map<String, Object> request);

}
