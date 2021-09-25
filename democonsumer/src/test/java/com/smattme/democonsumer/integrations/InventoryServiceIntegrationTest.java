package com.smattme.democonsumer.integrations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.smattme.democonsumer.responses.ProductResponse;
import com.smattme.democonsumer.services.InventoryService;

@SpringBootTest
public class InventoryServiceIntegrationTest {

	@Autowired
	private InventoryService inventoryService;

	@Test
	void whenMakePurchase_thenListProductsAndOrderOne() {

		Map<String, Object> response = inventoryService.makePurchase(4);
		assertNotNull(response);

		// assert the product size is 4, it should be 4 because we hard-coded the
		// products
		// on
		// the demo server
		List<ProductResponse> products = (List<ProductResponse>) response.get("availableProducts");
		assertEquals(4, products.size());

		// assert that we get a successful response status and code from the Order API
		boolean orderResponseStatus = Boolean.parseBoolean(response.get("orderResponseStatus").toString());
		assertTrue(orderResponseStatus);

		int orderResponseCode = Integer.parseInt(response.get("orderResponseCode").toString());
		assertEquals(200, orderResponseCode);
	}

	@Test
	void whenMakePurchaseWithZeroQuantityAndCustomExHandling_thenListProductsAndFailedOrder() {

		Map<String, Object> response = inventoryService.makePurchase(0);
		assertNotNull(response);

		// assert the product size is 4, it should be 4 because we hardcode the products
		// on
		// the demo server
		List<ProductResponse> products = (List<ProductResponse>) response.get("availableProducts");
		assertEquals(4, products.size());

		// assert that we get a successful response status and code from the Order API
		String orderResponseMessage = response.get("orderResponseMessage").toString();
		assertEquals("Missing required parameter(s)", orderResponseMessage);

		int orderResponseCode = Integer.parseInt(response.get("orderResponseCode").toString());
		assertEquals(400, orderResponseCode);

		List<String> errors = (List<String>) response.get("orderResponseErrors");
		assertEquals(1, errors.size());
		assertEquals("quantity is required", errors.get(0));
	}

}
