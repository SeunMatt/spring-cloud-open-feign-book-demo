package com.smattme.democonsumer.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smattme.democonsumer.exceptions.CustomApplicationException;

@RestController
public class IndexController {

	@GetMapping("/demo/errorpoint")
	public ResponseEntity<Map<String, Object>> errorPoint(@RequestParam(required = false) String errorKind) {

		if ("custom".equals(errorKind))
			throw new CustomApplicationException("CustomApplicationException raised");

		throw new RuntimeException();
	}

}
