package com.smattme.democonsumer.services;

import com.smattme.democonsumer.clients.OrderClient;
import com.smattme.democonsumer.clients.ProductClient;
import com.smattme.democonsumer.exceptions.CustomApplicationException;
import com.smattme.democonsumer.responses.GenericClientResponse;
import com.smattme.democonsumer.responses.ProductResponse;
import com.smattme.democonsumer.responses.ProductsResponse;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InventoryService {

	private OrderClient orderClient;
	private ProductClient productClient;

	@Autowired
	public InventoryService(OrderClient orderClient, ProductClient productClient) {
		this.orderClient = orderClient;
		this.productClient = productClient;
	}

	public Map<String, Object> makePurchase(int quantity) {

		Map<String, Object> finalResponse = new HashMap<>();

		// get available products
		GenericClientResponse<ProductsResponse> productsResponse;
		productsResponse = productClient.getProducts();

		List<ProductResponse> products = productsResponse
			.getData().getProducts();

		// place order for the first product
		Map<String, Object> orderRequest = new HashMap<>();

		if (quantity > 0)
			orderRequest.put("quantity", quantity);

		var productName = products.get(0).getName();
		orderRequest.put("product", productName);

		try {

			GenericClientResponse<Map<String, Object>> orderResponse;
			orderResponse = orderClient.placeOrder(orderRequest);
			finalResponse.put("orderResponseStatus", orderResponse.isStatus());
			finalResponse.put("orderResponseCode", orderResponse.getCode());

		} catch (CustomApplicationException ex) {

			finalResponse.put("orderResponseCode", ex.getHttpStatus().value());
			finalResponse.put("orderResponseMessage", ex.getMessage());
			finalResponse.put("orderResponseErrors", ex.getErrors());

		}

		finalResponse.put("availableProducts", products);
		return finalResponse;
	}

}
