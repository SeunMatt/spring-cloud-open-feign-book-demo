package com.smattme.demoserver.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.smattme.demoserver.config.Routes;
import com.smattme.demoserver.helpers.GenericResponse;
import com.smattme.demoserver.helpers.MapValidator;
import com.smattme.demoserver.services.BankService;

@RestController
public class BankController {
	
	
	private BankService bankService;

	public BankController(BankService bankService) {
		this.bankService = bankService;
	}
	
	
	@PostMapping(Routes.Bank.DEBIT_CUSTOMER)
	public ResponseEntity<Map<String, Object>> debitCustomer(@RequestBody Map<String, Object> request) {
		
		Map<String, String> rules = new HashMap<>();
		rules.put("accountId", "required");
		rules.put("amountInCents", "required");
		rules.put("currency", "required");
		
		List<String> errors = MapValidator.validate(request, rules);
		
		if(!errors.isEmpty())
			return ResponseEntity.badRequest().body(GenericResponse.genericValidationErrors(errors));
		
		Map<String, Object> response = bankService.debitCustomer(request);
		
		int status = Integer.parseInt(response.get("code").toString());
		return ResponseEntity.status(status).body(response);
	}
	
	
	
	
	
	
	
	

}
