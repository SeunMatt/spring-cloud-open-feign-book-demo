package com.smattme.demoserver.controllers;

import static com.smattme.demoserver.helpers.GenericResponse.genericValidationErrors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.smattme.demoserver.config.Routes;
import com.smattme.demoserver.helpers.MapValidator;
import com.smattme.demoserver.services.OrderService;

@RestController
public class OrderController {
	
	private OrderService orderService;
	
	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}
	
	
	@PostMapping(Routes.Orders.PLACE_ORDER)
	public ResponseEntity<Map<String, Object>> placeOrder(@RequestBody Map<String, Object> request) {
		
		Map<String, String> rules = new HashMap<>();
		rules.put("product", "required");
		rules.put("quantity", "required");
		
		List<String> errors = MapValidator.validate(request, rules);
		
		if(!errors.isEmpty())
			return ResponseEntity.badRequest().body(genericValidationErrors(errors));
		
		Map<String, Object> response = orderService.placeOrder(request);
		
		int status = Integer.parseInt(response.get("code").toString());
		return ResponseEntity.status(status).body(response);
		
		
	}

}
