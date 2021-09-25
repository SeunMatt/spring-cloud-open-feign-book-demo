package com.smattme.democonsumer.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smattme.democonsumer.clients.BankClient;
import com.smattme.democonsumer.responses.GenericClientResponse;

@Service
public class UserAccountService {

	private BankClient bankClient;

	@Autowired
	public UserAccountService(BankClient bankClient) {
		this.bankClient = bankClient;
	}

	public Map<String, Object> debitCustomer(String accountId, int amount) {

		Map<String, Object> bankDebitRequest = new HashMap<>();
		bankDebitRequest.put("accountId", accountId);
		bankDebitRequest.put("amountInCents", amount);
		bankDebitRequest.put("currency", "NGN");

		GenericClientResponse<Map<String, Object>> bankResponse = bankClient.debitCustomer(bankDebitRequest);

		Map<String, Object> finalResponse = new HashMap<>();
		finalResponse.put("code", bankResponse.getCode());
		finalResponse.put("status", bankResponse.isStatus());
		finalResponse.put("message", bankResponse.getMessage());

		return finalResponse;

	}

}
