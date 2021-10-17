package com.smattme.democonsumer.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;

import com.jayway.jsonpath.JsonPath;

import feign.Client.Default;
import feign.Request;
import feign.Request.Options;
import feign.Response;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpStatusFeignClient extends Default {


	public HttpStatusFeignClient(SSLSocketFactory sslContextFactory,
		HostnameVerifier hostnameVerifier) {

		super(sslContextFactory, hostnameVerifier);

	}

	@Override
	public Response execute(Request request,
		Options options) throws IOException {

		Response response = super.execute(request, options);

		// assert the original HTT status is 200 for test and learning purposes
		var assertMsg = "Original HTTP status is not 200";
		Assert.isTrue(response.status() == 200, assertMsg);

		InputStream bodyStream = response.body()
			.asInputStream();

		// copy the input stream to a String
		String responseBody = StreamUtils.copyToString(bodyStream, UTF_8);

		// expected response format
		// {"code":200,"message":"Refund request submitted successfully","status":true,
		// data:{}}
		// use Jayway's JsonPath to read the value of the actual status code
		int actualStatusCode = JsonPath.read(responseBody, "$.code");

		// reconstruct a new response from the original one
		// changing only the status code
		return response.toBuilder().status(actualStatusCode)
				.headers(response.headers()) // re-use the original response headers
				.body(responseBody, UTF_8) // set the body
				.build();

	}

}
