package com.smattme.demoserver.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smattme.demoserver.config.Routes;
import com.smattme.demoserver.services.CardService;

@RestController
public class CardController {
	
	private CardService cardService;

	public CardController(CardService cardService) {
		super();
		this.cardService = cardService;
	}
	
	
	@GetMapping(value = Routes.Card.GET_CARD_DETAILS, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getCustomerCardDetails(@PathVariable String customerId) {
		String response = cardService.getCustomerCardDetails(customerId);
		return ResponseEntity.ok(response);
	}
	
	

}
