package com.smattme.democonsumer.services;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service
public class OpenWeatherService {

	private ObjectMapper objectMapper;

	public OpenWeatherService(ObjectMapper objectMapper) {
		super();
		this.objectMapper = objectMapper;
	}

	public Map<String, Object> getCurrentWeatherData(String city, String apiKey) throws Exception {
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		Request request = new Request.Builder()
				.url("https://api.openweathermap.org/data/2.5/weather?q=Lagos&appid=" + apiKey)
				.get()
				.build();
		Response response = client.newCall(request).execute();

		TypeReference<Map<String, Object>> mapType = new TypeReference<Map<String, Object>>() {};
		return objectMapper.readValue(response.body().bytes(), mapType);
	}

}
