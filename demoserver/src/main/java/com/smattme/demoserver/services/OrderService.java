package com.smattme.demoserver.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.smattme.demoserver.helpers.GenericResponse;

import io.vavr.control.Try;

@Service
public class OrderService {
	
	private static final List<Map<String, Object>> orderRepo = new ArrayList<>();
	
	public Map<String, Object> placeOrder(Map<String, Object> request) {
		//simulate request processing time
		Try.of( () -> { Thread.sleep(500); return null; });
		orderRepo.add(request);
		return GenericResponse.generic200Response("Order placed successfully");
	}

}
