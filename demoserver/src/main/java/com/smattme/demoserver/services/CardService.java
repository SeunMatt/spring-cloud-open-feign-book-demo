package com.smattme.demoserver.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smattme.demoserver.helpers.CryptoHelper;
import com.smattme.demoserver.helpers.GenericResponse;

import io.vavr.control.Try;

@Service
public class CardService {
	
	private ObjectMapper objectMapper;
	
	@Value("${spring.security.static-aes-key}")
	private String aesSecret;
	
	
	public CardService(ObjectMapper objectMapper) {
		super();
		this.objectMapper = objectMapper;
	}



	public String getCustomerCardDetails(String customerId) {

		Map<String, String> cardDetails = new HashMap<>();
		cardDetails.put("pan", "5432 XXXX XXXX 1234");
		cardDetails.put("expiry", "09/21");
		cardDetails.put("cvv", "000");

		String response = Try.of(() -> objectMapper.writeValueAsString(GenericResponse.generic200Response(cardDetails)))
				.getOrElse("");

		// return encrypted data
		return CryptoHelper.encryptDataAES(response, aesSecret.getBytes());

	}

}
