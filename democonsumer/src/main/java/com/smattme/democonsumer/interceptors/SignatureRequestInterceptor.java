package com.smattme.democonsumer.interceptors;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class SignatureRequestInterceptor implements RequestInterceptor {

	private String clientId;
	private String signatureSecret;

	public SignatureRequestInterceptor(String clientId, String signatureSecret) {
		this.clientId = clientId;
		this.signatureSecret = signatureSecret;
	}

	@Override
	public void apply(RequestTemplate template) {

		// read the raw request body if it's available
		String requestBody = (template.body() != null && template.body().length > 0)
				? new String(template.body(), StandardCharsets.UTF_8)
				: "";

		// generate a timestamp
		long timestamp = System.currentTimeMillis();

		// build the signature body
		String body = template.path() + requestBody + timestamp;

		try {

			// compute the signature
			String signature = generateHMACSHA256Signature(body, signatureSecret);

			// add the signature and timestamp to the request header
			template.header("timestamp", String.valueOf(timestamp));
			template.header("signature", signature);
			template.header("clientId", clientId);

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	private String generateHMACSHA256Signature(String message, String secret) throws Exception {
		Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
		SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
		hmacSHA256.init(secretKeySpec);
		return Base64.getEncoder().encodeToString(hmacSHA256.doFinal(message.getBytes()));
	}

}
