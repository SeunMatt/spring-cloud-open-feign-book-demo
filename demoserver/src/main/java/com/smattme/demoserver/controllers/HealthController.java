package com.smattme.demoserver.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smattme.demoserver.config.Routes;

@RestController
public class HealthController {
	
	@GetMapping(Routes.Health.HEALTH_STATUS)
	public ResponseEntity<String> healthStatus() {
		return ResponseEntity.ok("UP");
	}

}
