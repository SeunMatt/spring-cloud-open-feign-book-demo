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

	public Map<String, Object> getCurrentWeatherDataForLagos(String apiKey) throws Exception {
		String url = "https://api.openweathermap.org/data/2.5/weather?q=Lagos&appid=";
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		Request request = new Request.Builder()
				.url( url + apiKey)
				.get()
				.build();
		Response response = client.newCall(request).execute();
		TypeReference<Map<String, Object>> mapType = new TypeReference<>() {};
		return objectMapper.readValue(response.body().bytes(), mapType);
	}

}
