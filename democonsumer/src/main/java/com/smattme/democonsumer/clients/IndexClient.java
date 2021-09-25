package com.smattme.democonsumer.clients;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.smattme.democonsumer.responses.GenericClientResponse;

@FeignClient(name = "index-client", url = "http://localhost:9000")
public interface IndexClient {

	@GetMapping("/")
	GenericClientResponse<Map<String, Object>> index(@RequestHeader String apiVersion);

	@GetMapping("/")
	GenericClientResponse<Map<String, Object>> indexSearch(@RequestParam String query);

	@GetMapping("/user/{id}")
	GenericClientResponse<Map<String, Object>> indexUserDetails(@PathVariable String id);

	@PostMapping("/")
	GenericClientResponse<Map<String, Object>> indexPost(@RequestBody Map<String, Object> request);

	@DeleteMapping("/")
	GenericClientResponse<Map<String, Object>> indexDelete(@RequestBody Map<String, Object> request);

	@PutMapping("/")
	GenericClientResponse<Map<String, Object>> indexPut(@RequestBody Map<String, Object> request);
}
