package com.smattme.democonsumer.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.smattme.democonsumer.clients.EmailClient;
import com.smattme.democonsumer.responses.GenericClientResponse;

@Service
public class UserRegisterationService {
	
	private EmailClient emailClient;

	public UserRegisterationService(EmailClient emailClient) {
		this.emailClient = emailClient;
	}
	
	
	public Map<String, Object> registerNewUser(String userEmail) {
		
		//user account creation processes ...
		
		//send welcome email
		Map<String, Object> emailRequest = new HashMap<>();
		emailRequest.put("recipient", userEmail);
		emailRequest.put("message", "Dear User, Welcome to our world");
		emailRequest.put("sender", "DemoApp");

		GenericClientResponse<Map<String, Object>> bankResponse = emailClient.sendEmail(emailRequest);

		Map<String, Object> finalResponse = new HashMap<>();
		finalResponse.put("code", bankResponse.getCode());
		finalResponse.put("status", bankResponse.isStatus());
		finalResponse.put("message", bankResponse.getMessage());

		return finalResponse;
		
	}
	
	

}
