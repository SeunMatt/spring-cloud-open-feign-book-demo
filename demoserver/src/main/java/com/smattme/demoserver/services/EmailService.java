package com.smattme.demoserver.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.smattme.demoserver.helpers.GenericResponse;

@Service
public class EmailService {
	
	private static final List<Map<String, Object>> emailQueue = new ArrayList<>();
	
	
	public Map<String, Object> sendEmail(Map<String, Object> request) {
		emailQueue.add(request);
		return GenericResponse.generic200Response("Email sent successfully");
	}

}
