package com.smattme.democonsumer.integrations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.smattme.democonsumer.services.HealthService;

@SpringBootTest
public class HealthServiceIntegrationTest {

	@Autowired
	private HealthService healthService;

	@Test
	void whenSystemHealthStatus_thenReturnStatus() {
		Map<String, Object> response = healthService.systemHealthStatus();
		assertNotNull(response);
		assertEquals(response.get("upstreamServerStatus"), "UP");
		assertTrue(response.containsKey("activeThreads"));
	}

}
