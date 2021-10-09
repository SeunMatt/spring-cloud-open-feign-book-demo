package com.smattme.democonsumer.integrations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.smattme.democonsumer.clients.OpenWeatherClient;
import com.smattme.democonsumer.services.OpenWeatherService;

@SpringBootTest
@Tag("OpenWeatherIntegrationTest")
public class OpenWeatherIntegrationTest {
	
	@Value("${open-weather.api-key}")
	private String apiKey;
	
	@Autowired
	private OpenWeatherClient openWeatherClient;
	
	@Autowired
	private OpenWeatherService openWeatherService;
	
	@Test
	void givenCityName_whenGetWeatherDataUsingOkhttp_thenReturnCurrentWeatherData() throws Exception {
		Map<String, Object> responseMap = openWeatherService.getCurrentWeatherDataForLagos(apiKey);
		assertEquals("Lagos", responseMap.get("name"));
	}
	
	
	@Test
	void givenCityName_whenGetWeatherDataUsingFeignClient_thenReturnCurrentWeatherData() throws Exception {
		String city = "Lagos";
		Map<String, Object> responseMap = openWeatherClient.getCurrentWeatherData(city, apiKey);
		assertEquals("Lagos", responseMap.get("name"));
	}

}
