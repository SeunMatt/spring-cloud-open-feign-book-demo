package com.smattme.democonsumer.interceptors;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class EncryptionRequestInterceptor implements RequestInterceptor {

	private String aesSecret;
	private String authToken;

	public EncryptionRequestInterceptor(String aesSecret, String authToken) {
		this.aesSecret = aesSecret;
		this.authToken = authToken;
	}

	@Override
	public void apply(RequestTemplate template) {

		// read the raw request body if it's available
		String requestBody = (template.body() != null && template.body().length > 0)
				? new String(template.body(), StandardCharsets.UTF_8)
				: "";

		// encrypt the request body
		String encryptedBody = encryptDataAES(requestBody, aesSecret.getBytes());

		// set the encrypted body as the new request body
		template.body(encryptedBody);

		// add the token header to the request
		template.header("Auth-Token", authToken);
	}

	private String encryptDataAES(String plainData, byte[] key) {

		try {

			SecretKey secretKey = new SecretKeySpec(key, "AES");

			SecureRandom secureRandom = new SecureRandom();

			// build the initialisation factor
			byte[] iv = new byte[12];
			secureRandom.nextBytes(iv);

			Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
			GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

			byte[] cipherText = cipher.doFinal(plainData.getBytes());

			ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + cipherText.length);
			byteBuffer.put(iv);
			byteBuffer.put(cipherText);
			// the first 12 bytes are the IV where others are the cipher message +
			// authentication tag
			byte[] cipherMessage = byteBuffer.array();
			return Base64.getEncoder().encodeToString(cipherMessage);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
