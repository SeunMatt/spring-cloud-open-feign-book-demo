package com.smattme.demoserver.controllers;

import static com.smattme.demoserver.helpers.GenericResponse.*;

import java.util.Map;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.smattme.demoserver.config.Routes;
import com.smattme.demoserver.services.CartService;

@RestController
public class CartController {
	
	private CartService cartService;
	
	public CartController(CartService cartService) {
		this.cartService = cartService;
	}
	
	@PostMapping(Routes.Cart.ADD_ITEMS)
	public ResponseEntity<Map<String, Object>> addItemToCart(@RequestBody Map<String, Object> request) {
		
		if(Objects.isNull(request.get("customerId")) || Objects.isNull(request.get("product"))) {
			return ResponseEntity.ok(genericErrorResponse(HttpStatus.BAD_REQUEST, "CustomerId and product is required"));
		}
		
		Map<String, Object> response = cartService.addItemToCart(request);
		return ResponseEntity.status(Integer.parseInt(response.get("code").toString())).body(response);
	}

}
