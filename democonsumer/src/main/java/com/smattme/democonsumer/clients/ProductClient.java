package com.smattme.democonsumer.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.smattme.democonsumer.config.BasicFeignClientConfig;
import com.smattme.democonsumer.responses.GenericClientResponse;
import com.smattme.democonsumer.responses.ProductsResponse;

@FeignClient(name = "product-client", url = "${demoserver.base-url}",
	configuration = BasicFeignClientConfig.class)
public interface ProductClient {
	
	@GetMapping("/core/basic/products")
	GenericClientResponse<ProductsResponse> getProducts();

}
