package com.smattme.democonsumer.integrations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.smattme.democonsumer.services.UserRegistrationService;

@SpringBootTest
public class UserRegistrationServiceIntegrationTest {

	@Autowired
	private UserRegistrationService userRegistrationService;

	@Test
	void givenUserEmail_whenRegisterNewUser_thenSendEmailSuccessfully() {

		var email = "hello@example.com";
		Map<String, Object> response = userRegistrationService.registerNewUser(email);

		var statusStr = response.get("status").toString();
		boolean status = Boolean.parseBoolean(statusStr);
		assertTrue(status);

		assertEquals(200, response.get("code"));

		String expectedMessage = "Email sent successfully";
		assertEquals(expectedMessage, response.get("message"));
	}

}
