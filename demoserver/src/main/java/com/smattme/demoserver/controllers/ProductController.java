package com.smattme.demoserver.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smattme.demoserver.config.Routes;
import com.smattme.demoserver.services.ProductService;

@RestController
public class ProductController {
	
	private ProductService productService;
	
	public ProductController(ProductService productService) {
		this.productService = productService;
	}
	
	
	@GetMapping(Routes.Product.PRODUCTS)
	public ResponseEntity<Map<String, Object>> allProducts() {
		Map<String, Object> response = productService.allProducts();
		return ResponseEntity.status(Integer.parseInt(response.get("code").toString())).body(response);
	}
	
	@GetMapping(Routes.Product.PRODUCT)
	public ResponseEntity<Map<String, Object>> singleProduct(@PathVariable String productName) {
		Map<String, Object> response = productService.singleProduct(productName);
		return ResponseEntity.status(Integer.parseInt(response.get("code").toString())).body(response);
	}

}
