package com.smattme.democonsumer.responses;

import java.util.List;

public class ProductsResponse {
	
	List<ProductResponse> products;
	
	public ProductsResponse() { }

	public List<ProductResponse> getProducts() {
		return products;
	}

	public void setProducts(List<ProductResponse> products) {
		this.products = products;
	}
	
	
	
}
