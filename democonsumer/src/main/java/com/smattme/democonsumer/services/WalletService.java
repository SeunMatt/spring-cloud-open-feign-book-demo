package com.smattme.democonsumer.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.smattme.democonsumer.clients.WalletClient;
import com.smattme.democonsumer.exceptions.CustomApplicationException;
import com.smattme.democonsumer.responses.GenericClientResponse;

@Service
public class WalletService {

	private WalletClient walletClient;

	public WalletService(WalletClient walletClient) {
		this.walletClient = walletClient;
	}

	public Map<String, Object> creditCustomer(String accountId, int amount) {

		Map<String, Object> creditRequest = new HashMap<>();
		creditRequest.put("accountId", accountId);
		creditRequest.put("amountInCents", amount);
		creditRequest.put("currency", "NGN");

		Map<String, Object> finalResponse = new HashMap<>();

		try {

			GenericClientResponse<Map<String, Object>> walletResponse = walletClient.creditCustomer(creditRequest);
			finalResponse.put("code", walletResponse.getCode());
			finalResponse.put("status", walletResponse.isStatus());
			finalResponse.put("message", walletResponse.getMessage());

		} catch (CustomApplicationException e) {
			finalResponse.put("code", e.getHttpStatus().value());
			finalResponse.put("message", e.getMessage());
			finalResponse.put("errors", e.getErrors());
		}

		return finalResponse;

	}

}
