package com.smattme.democonsumer.integrations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.smattme.democonsumer.services.UserAccountService;

@SpringBootTest
public class UserAccountServiceIntegrationTest {

	@Autowired
	private UserAccountService userAccountService;

	@Test
	void givenAccountIdAndAmount_whenDDebitCustomer_thenReturnSuccess() {

		String accountId = "0123456789";
		int amount = 1000;

		Map<String, Object> response;
		response = userAccountService.debitCustomer(accountId, amount);

		var statusStr = response.get("status").toString();
		boolean status = Boolean.parseBoolean(statusStr);
		assertTrue(status);

		assertEquals(200, response.get("code"));

		String expectedMessage = "Account " + accountId + " debited successfully";
		assertEquals(expectedMessage, response.get("message"));
	}

}
