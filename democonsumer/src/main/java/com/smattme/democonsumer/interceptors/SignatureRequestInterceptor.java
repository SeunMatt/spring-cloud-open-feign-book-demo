package com.smattme.democonsumer.interceptors;

import feign.RequestInterceptor;
import feign.RequestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

public class SignatureRequestInterceptor implements RequestInterceptor {

	private String clientId;
	private String secret;

	public SignatureRequestInterceptor(String clientId, String secret) {
		this.clientId = clientId;
		this.secret = secret;
	}

	@Override
	public void apply(RequestTemplate template) {

		// read the raw request body if it's available
		String requestBody = (template.body() != null && template.body().length > 0)
				? new String(template.body(), UTF_8)
				: "";

		// generate a timestamp
		long timestamp = System.currentTimeMillis();

		// build the signature body
		String signatureBody = template.path() + requestBody + timestamp;


		// compute the signature
		String signature = generateHMACSHA256Signature(signatureBody, secret);

		// add the clientId, signature and timestamp to the request header
		template.header("timestamp", String.valueOf(timestamp));
		template.header("signature", signature);
		template.header("clientId", clientId);


	}

	private String generateHMACSHA256Signature(String message, String secret) {

		try {
			var algo = "HmacSHA256";
			Mac hmacSHA256;
			hmacSHA256 = Mac.getInstance(algo);

			byte[] secretBytes = secret.getBytes();
			SecretKeySpec secretKeySpec;
			secretKeySpec = new SecretKeySpec(secretBytes, algo);

			hmacSHA256.init(secretKeySpec);

			byte[] signatureBytes = hmacSHA256.doFinal(message.getBytes());

			return Base64.getEncoder().encodeToString(signatureBytes);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
