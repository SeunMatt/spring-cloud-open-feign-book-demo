package com.smattme.demoserver.controllers;

import static com.smattme.demoserver.helpers.GenericResponse.generic200Response;

import java.util.Map;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smattme.demoserver.config.Routes;

@RestController
public class IndexController {
	
	
	@GetMapping(Routes.Index.INDEX)
	public ResponseEntity<Map<String, Object>> index(@RequestHeader String apiVersion) {
		return ResponseEntity.ok(generic200Response("Welcome to Demo Commerce API producer"));
	}
	
	@GetMapping(value = Routes.Index.INDEX, params = "query")
	public ResponseEntity<Map<String, Object>> indexSearch(@RequestParam String query) {
		return ResponseEntity.ok(generic200Response("Welcome to Demo Commerce API producer " + query));
	}

	@GetMapping(value = Routes.Index.INDEX, params = {"size", "page", "search"})
	public ResponseEntity<Map<String, Object>> indexPaginateSearch(@RequestParam Map<String, String> query) {
		return ResponseEntity.ok(generic200Response("Welcome to Demo Commerce API producer " + query));
	}
	
	@GetMapping(Routes.Index.INDEX_ECHO)
	public ResponseEntity<Map<String, Object>> indexEcho(@PathVariable String id) {
		return ResponseEntity.ok(generic200Response("Welcome to Demo Commerce API producer"));
	}
	
	@PostMapping(Routes.Index.INDEX)
	public ResponseEntity<Map<String, Object>> indexPost(@RequestBody Map<String, Object> request) {
		return ResponseEntity.ok(generic200Response("Welcome to Demo Commerce API producer", request));
	}
	
	@DeleteMapping(Routes.Index.INDEX)
	public ResponseEntity<Map<String, Object>> indexDelete(@RequestBody Map<String, Object> request) {
		return ResponseEntity.ok(generic200Response("Welcome to Demo Commerce API producer", request));
	}
	
	@PutMapping(Routes.Index.INDEX)
	public ResponseEntity<Map<String, Object>> indexPut(@RequestBody Map<String, Object> request) {
		return ResponseEntity.ok(generic200Response("Welcome to Demo Commerce API producer", request));
	}
	
	@PatchMapping(Routes.Index.INDEX)
	public ResponseEntity<Map<String, Object>> indexPatch(@RequestBody Map<String, Object> request) {
		return ResponseEntity.ok(generic200Response("Welcome to Demo Commerce API producer", request));
	}

}
