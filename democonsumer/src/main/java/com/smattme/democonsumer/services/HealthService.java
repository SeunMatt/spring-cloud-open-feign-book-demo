package com.smattme.democonsumer.services;

import com.smattme.democonsumer.clients.HealthClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class HealthService {

	private HealthClient healthClient;

	@Autowired
	public HealthService(HealthClient healthClient) {
		this.healthClient = healthClient;
	}


	public Map<String, Object> systemHealthStatus() {

		//get health status for upstream server
		String upstreamServerStatus = healthClient.health();

		//get number of active threads
		int activeThreads = Thread.activeCount();

		Map<String, Object> response = new HashMap<>();
		response.put("upstreamServerStatus", upstreamServerStatus);
		response.put("activeThreads", activeThreads);
		return response;
	}

}
