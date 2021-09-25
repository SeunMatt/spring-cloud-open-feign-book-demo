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
import com.smattme.demoserver.services.WalletService;

@RestController
public class WalletController {
	
	private WalletService walletService;

	public WalletController(WalletService walletService) {
		super();
		this.walletService = walletService;
	}
	
	
	@PostMapping(Routes.Wallet.CREDIT_CUSTOMER)
	public ResponseEntity<Map<String, Object>> creditCustomer(@RequestBody Map<String, Object> request) {

		Map<String, String> rules = new HashMap<>();
		rules.put("accountId", "required");
		rules.put("amountInCents", "required");
		rules.put("currency", "required");

		List<String> errors = MapValidator.validate(request, rules);

		if (!errors.isEmpty())
			return ResponseEntity.ok(GenericResponse.genericValidationErrors(errors));

		Map<String, Object> response = walletService.creditCustomer(request);

		return ResponseEntity.ok(response);
	}

}
