package com.smattme.demoserver.services;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.smattme.demoserver.helpers.GenericResponse;

@Service
public class ProductService {
	
	private static Map<String, String> products = new HashMap<>();
	static {
		products.put("shoe", "description: Awesome nice office shoe,color: black,size: 11,price:NGN 1500,quantity: 10");
		products.put("pen", "description: Nice ball point pen for writting smoothly,colour: blue,price: NGN 1000,quantity: 100");
		products.put("SamsungA70", "description: Nice Android phone with the latest Android version,color: grey,price: NGN 5000,quantity: 5");
		products.put("iPhoneX", "description: Latest iPhone in town,color: white,price: NGN 5000,quantity: 5");
	}
	
	public Map<String, Object> allProducts() {
		
		var productNames = products.keySet().parallelStream()
				.map(StringUtils::capitalize)
				.map(productName -> Collections.singletonMap("name", StringUtils.capitalize(productName)))
				.collect(Collectors.toList());
		
		return GenericResponse.generic200Response(Collections.singletonMap("products", productNames));
		
	}
	
	public Map<String, Object> singleProduct(String productName) {
		
		var productDetails = products.get(productName.toLowerCase());
		
		if(StringUtils.isEmpty(productDetails))
			return GenericResponse.generic404NotFoundRequest("Product not found");
		
		Map<String, String> detailsMap = new HashMap<String, String>();
	
		detailsMap.put("name", productName);
		Arrays.stream(productDetails.split(","))
				.map(productD -> productD.split(":"))
				.forEach(productDArray -> detailsMap.put(productDArray[0], productDArray[1].trim()));
		
		return GenericResponse.generic200Response(Collections.singletonMap("product", detailsMap));
	}

}
