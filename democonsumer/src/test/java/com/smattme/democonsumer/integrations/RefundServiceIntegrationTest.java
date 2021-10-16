package com.smattme.democonsumer.integrations;

import com.smattme.democonsumer.services.RefundService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class RefundServiceIntegrationTest {

	@Autowired
	private RefundService refundService;

	@Test
	void givenRefundRequest_whenSubmitRefund_thenReturnSuccess() {

		Map<String, Object> response = refundService.submitRefundRequest();

		var statusStr = response.get("status").toString();
		boolean status = Boolean.parseBoolean(statusStr);
		assertTrue(status);

		assertEquals(200, response.get("code"));

		String expectedMessage = "Refund request submitted successfully";
		assertEquals(expectedMessage, response.get("message"));

	}

}
