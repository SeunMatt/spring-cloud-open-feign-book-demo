package com.smattme.democonsumer.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.smattme.democonsumer.clients.CardClient;
import com.smattme.democonsumer.responses.GenericClientResponse;

@Service
public class CardService {

	private CardClient cardClient;

	public CardService(CardClient cardClient) {
		this.cardClient = cardClient;
	}

	public Map<String, Object> getCustomerCardDetails(String customerId) {

		GenericClientResponse<Map<String, Object>> cardResponse = cardClient.getCustomerCardDetails(customerId);

		Map<String, Object> finalResponse = new HashMap<>();
		finalResponse.put("code", cardResponse.getCode());
		finalResponse.put("status", cardResponse.isStatus());
		finalResponse.put("data", cardResponse.getData());

		return finalResponse;

	}

}
