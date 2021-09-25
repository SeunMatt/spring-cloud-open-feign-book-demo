package com.smattme.demoserver.services;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.smattme.demoserver.helpers.GenericResponse;

import io.vavr.control.Try;

@Service
public class WalletService {
	
	
	public Map<String, Object> creditCustomer(Map<String, Object> request) {
		// simulate data processing
		Try.of(() -> {
			Thread.sleep(500);
			return null;
		});
		return GenericResponse.generic200Response("Account " + request.get("accountId") + " credited successfully");
	}
	

}
