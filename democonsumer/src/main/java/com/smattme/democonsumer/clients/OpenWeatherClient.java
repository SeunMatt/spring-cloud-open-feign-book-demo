package com.smattme.democonsumer.clients;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "weather-client", url="https://api.openweathermap.org")
public interface OpenWeatherClient {
	
	@GetMapping("data/2.5/weather")
	Map<String, Object> getCurrentWeatherData(@RequestParam("q") String city, @RequestParam("appid") String appId);

}
