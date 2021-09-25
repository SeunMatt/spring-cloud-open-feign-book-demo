package com.smattme.democonsumer.integrations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.smattme.democonsumer.services.WalletService;

@SpringBootTest
public class WalletServiceIntegrationTest {

	@Autowired
	private WalletService walletService;

	@Test
	void givenNullAccountId_whenCreditCustomer_thenReturnFailureResponse() {

		//we're passing null accountId which is a required parameter
		//this will cause the request to fail
		Map<String, Object> response = walletService.creditCustomer(null, 1000);

		assertEquals(400, response.get("code"));

		String expectedMessage = "Missing required parameter(s)";
		assertEquals(expectedMessage, response.get("message"));

		List<String> errors = (List<String>) response.get("errors");
		assertEquals(1, errors.size());
		assertEquals("accountId is required", errors.get(0));

	}

}
