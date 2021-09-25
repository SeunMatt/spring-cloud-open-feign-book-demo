package com.smattme.demoserver.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.smattme.demoserver.helpers.GenericResponse;

@Service
public class RefundService {
	
private List<Map<String, Object>> refundRepo = new ArrayList<>();
	
	public Map<String, Object> submitRefund(Map<String, Object> request) {
		refundRepo.add(request);
		return GenericResponse.generic200Response("Refund request submitted successfully");
	}

}
