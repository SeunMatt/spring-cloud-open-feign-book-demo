package com.smattme.democonsumer.integrations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.smattme.democonsumer.services.UserService;

@SpringBootTest
public class UserServiceIntegrationTest {

	@Autowired
	private UserService userService;

	@Test
	void givenRecipient_whenSendOTP_thenReturnSuccess() {

		var to = "2347011111111";
		Map<String, Object> response = userService.sendOTP(to);

		var statusStr = response.get("status").toString();
		boolean status = Boolean.parseBoolean(statusStr);
		assertTrue(status);

		assertEquals(200, response.get("code"));

		String expectedMessage = "SMS sent successfully";
		assertEquals(expectedMessage, response.get("message"));
	}

}
