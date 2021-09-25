package com.smattme.demoserver.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.smattme.demoserver.helpers.GenericResponse;

@Service
public class CartService {
	
	private List<Map<String, Object>> shoppingCartRepo = new ArrayList<>();
	
	public Map<String, Object> addItemToCart(Map<String, Object> request) {
		shoppingCartRepo.add(request);
		return GenericResponse.generic200Response("Item added to cart successfully");
	}

}
