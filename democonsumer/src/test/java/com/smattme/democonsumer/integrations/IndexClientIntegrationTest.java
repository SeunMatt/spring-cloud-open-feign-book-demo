package com.smattme.democonsumer.integrations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.smattme.democonsumer.clients.IndexClient;

@SpringBootTest
public class IndexClientIntegrationTest {
	
	@Autowired
	private IndexClient indexClient;
	
	
	@Test
	void whenIndex_thenReturnSuccessResponse() {
		
		String apiVersion = "1.0.0";
		var response = indexClient.index(apiVersion);
		
		assertTrue(response.isStatus());
		assertEquals(200, response.getCode());
	}
	
	@Test
	void whenIndexUserDetails_thenReturnSuccessResponse() {

		String userId = "AESRTXK12";
		var response = indexClient.indexUserDetails(userId);

		assertTrue(response.isStatus());
		assertEquals(200, response.getCode());
	}
	
	@Test
	void whenIndexPost_thenReturnSuccessResponse() {
		
		Map<String, Object> request = new HashMap<>();
		request.put("id", UUID.randomUUID().toString());
		
		var response = indexClient.indexPost(request);
		assertTrue(response.isStatus());
		assertEquals(200, response.getCode());
	}
	
	@Test
	void whenIndexDelete_thenReturnSuccessResponse() {
		
		Map<String, Object> request = new HashMap<>();
		request.put("id", UUID.randomUUID().toString());
		
		var response = indexClient.indexDelete(request);
		assertTrue(response.isStatus());
		assertEquals(200, response.getCode());
	}
	
	
	@Test
	void whenIndexPut_thenReturnSuccessResponse() {
		
		Map<String, Object> request = new HashMap<>();
		request.put("id", UUID.randomUUID().toString());
		
		var response = indexClient.indexPut(request);
		assertTrue(response.isStatus());
		assertEquals(200, response.getCode());
	}
	
	@Test
	void whenIndexSearch_thenReturnSuccessResponse() {
		String searchQuery = "John";
		var response = indexClient.indexSearch(searchQuery);
		assertTrue(response.isStatus());
		assertEquals(200, response.getCode());
	}

}
