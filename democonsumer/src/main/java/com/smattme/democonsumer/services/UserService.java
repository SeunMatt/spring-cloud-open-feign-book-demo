package com.smattme.democonsumer.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smattme.democonsumer.clients.SMSClient;
import com.smattme.democonsumer.responses.GenericClientResponse;

@Service
public class UserService {

	private SMSClient smsClient;

	@Autowired
	public UserService(SMSClient smsClient) {
		super();
		this.smsClient = smsClient;
	}

	public Map<String, Object> sendOTP(String to) {

		Map<String, Object> smsRequest = new HashMap<>();
		smsRequest.put("recipient", to);
		smsRequest.put("message", "Dear User, your OTP is 1234");
		smsRequest.put("sender", "DemoApp");

		GenericClientResponse<Map<String, Object>> smsResponse;
		smsResponse = smsClient.sendSMS(smsRequest);

		Map<String, Object> finalResponse = new HashMap<>();
		finalResponse.put("code", smsResponse.getCode());
		finalResponse.put("status", smsResponse.isStatus());
		finalResponse.put("message", smsResponse.getMessage());

		return finalResponse;

	}

}
