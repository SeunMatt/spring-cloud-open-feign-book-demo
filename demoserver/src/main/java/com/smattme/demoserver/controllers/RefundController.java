package com.smattme.demoserver.controllers;

import java.util.Map;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.smattme.demoserver.config.Routes;
import com.smattme.demoserver.helpers.GenericResponse;
import com.smattme.demoserver.services.RefundService;

@RestController
public class RefundController {
	
	private RefundService refundService;
	
	public RefundController(RefundService refundService) {
		this.refundService = refundService;
	}
	
	
	@PostMapping(Routes.Refund.REFUND)
	public ResponseEntity<Map<String, Object>> submitRefund(@RequestBody Map<String, Object> request) {
		if(Objects.isNull(request.get("customerId")) || Objects.isNull(request.get("product")) || Objects.isNull(request.get("reason"))) {
			return ResponseEntity.badRequest()
					.body(GenericResponse.genericErrorResponse(HttpStatus.BAD_REQUEST, "CustomerId, product and reason is required"));
		}
		Map<String, Object> response = refundService.submitRefund(request);
		return ResponseEntity.status(Integer.parseInt(response.get("code").toString())).body(response);
	}
	
	

}
