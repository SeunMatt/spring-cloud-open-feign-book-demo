package com.smattme.democonsumer.integrations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.smattme.democonsumer.services.RefundService;

@SpringBootTest
public class RefundServiceIntegrationTest {

	@Autowired
	private RefundService refundService;

	@Test
	void givenRefundRequest_whenSubmitRefund_thenReturnSuccessResponse() {

		Map<String, Object> response = refundService.submitRefundRequest();

		boolean status = Boolean.parseBoolean(response.get("status").toString());
		assertTrue(status);

		assertEquals(200, response.get("code"));

		String expectedMessage = "Refund request submitted successfully";
		assertEquals(expectedMessage, response.get("message"));

	}

}
