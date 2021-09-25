package com.smattme.demoserver.services;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.smattme.demoserver.helpers.GenericResponse;

import io.vavr.control.Try;

@Service
public class SMSService {
	
	public Map<String, Object> sendSMS(Map<String, Object> request) {
		//simulate request processing time
		Try.of( () -> { Thread.sleep(500); return null; });
		return GenericResponse.generic200Response("SMS sent successfully");
	}

}
