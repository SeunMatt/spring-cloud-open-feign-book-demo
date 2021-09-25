package com.smattme.democonsumer.integrations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.smattme.democonsumer.services.UserRegisterationService;

@SpringBootTest
public class UserRegisterationServiceIntegrationTest {

	@Autowired
	private UserRegisterationService userRegisterationService;

	@Test
	void givenUserEmail_whenRegisterNewUser_thenSendEmailSuccessfully() {

		Map<String, Object> response = userRegisterationService.registerNewUser("hello@example.com");

		boolean status = Boolean.parseBoolean(response.get("status").toString());
		assertTrue(status);

		assertEquals(200, response.get("code"));

		String expectedMessage = "Email sent successfully";
		assertEquals(expectedMessage, response.get("message"));
	}

}
