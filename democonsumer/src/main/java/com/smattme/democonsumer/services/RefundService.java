package com.smattme.democonsumer.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smattme.democonsumer.clients.RefundClient;
import com.smattme.democonsumer.responses.GenericClientResponse;

@Service
public class RefundService {

	private RefundClient refundClient;

	@Autowired
	public RefundService(RefundClient refundClient) {
		this.refundClient = refundClient;
	}

	public Map<String, Object> submitRefundRequest() {

		// submit a refund request for a product
		Map<String, Object> refundRequest = new HashMap<>();
		refundRequest.put("customerId", "customer123");
		refundRequest.put("product", "shoe");
		refundRequest.put("reason", "Shoe size does not fit as expected");
		GenericClientResponse<Map<String, Object>> response = refundClient.submitRefund(refundRequest);

		Map<String, Object> finalResponse = new HashMap<>();
		finalResponse.put("code", response.getCode());
		finalResponse.put("status", response.isStatus());
		finalResponse.put("message", response.getMessage());

		return finalResponse;

	}

}
