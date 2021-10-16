package com.smattme.democonsumer.integrations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.smattme.democonsumer.services.CardService;

@SpringBootTest
public class CardServiceIntegrationTest {

	@Autowired
	private CardService cardService;

	@Test
	void givenCustomerId_whenGetCustomerCardDetails_thenReturnSuccess() {

		String customerId = "user";
		Map<String, Object> response;
		response = cardService.getCustomerCardDetails(customerId);

		var statusStr = response.get("status").toString();
		boolean status = Boolean.parseBoolean(statusStr);
		assertTrue(status);

		assertEquals(200, response.get("code"));

		Map<String, Object> data = (Map<String, Object>) response.get("data");
		assertEquals("000", data.get("cvv"));
		assertEquals("09/21", data.get("expiry"));
		assertEquals("5432 XXXX XXXX 1234", data.get("pan"));

	}

}
