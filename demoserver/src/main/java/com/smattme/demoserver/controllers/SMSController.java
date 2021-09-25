package com.smattme.demoserver.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.smattme.demoserver.config.Routes;
import com.smattme.demoserver.helpers.GenericResponse;
import com.smattme.demoserver.helpers.MapValidator;
import com.smattme.demoserver.services.SMSService;

@RestController
public class SMSController {
	
	
	private SMSService smsService;
	
	public SMSController(SMSService smsService) {
		this.smsService = smsService;
	}
	
	
	@PostMapping(Routes.SMS.SEND_SMS)
	public ResponseEntity<Map<String, Object>> sendSMS(@RequestBody Map<String, Object> request) {
	
		Map<String, String> rules = new HashMap<>();
		rules.put("recipient", "required");
		rules.put("message", "required");
		rules.put("sender", "required");
		
		List<String> errors = MapValidator.validate(request, rules);
		
		if(!errors.isEmpty())
			return ResponseEntity.badRequest().body(GenericResponse.genericValidationErrors(errors));
		
		Map<String, Object> response = smsService.sendSMS(request);
		
		int status = Integer.parseInt(response.get("code").toString());
		return ResponseEntity.status(status).body(response);
		
	}

}
