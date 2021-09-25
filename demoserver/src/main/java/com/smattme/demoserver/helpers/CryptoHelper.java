package com.smattme.demoserver.helpers;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.smattme.demoserver.exceptions.CustomApplicationException;

public class CryptoHelper {
	
	private static final int CRYPTO_AUTH_TAG_LENGTH = 128;
	private static final int CRYPTO_IV_LENGTH = 12;
	private static final Logger logger = LoggerFactory.getLogger(CryptoHelper.class);
	
	public static String generateHMACSHA256Signature(String message, String secret) throws Exception {
		Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
		SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
		hmacSHA256.init(secretKeySpec);
		return Base64.getEncoder().encodeToString(hmacSHA256.doFinal(message.getBytes()));
	}
	
	/**
     * this will encrypt data using the AES algorithm
     * in GCM mode with no padding.
     *
     * @param plainData data to be encrypted
     * @param key       a 128-bit secret key for encrypting
     * @return base64 encoded string of encrypted data
     * @throws CustomApplicationException which will be caught by the centralised handler
     */
    public static String encryptDataAES(String plainData, byte[] key) {

        try {

            SecretKey secretKey = new SecretKeySpec(key, "AES");

            SecureRandom secureRandom = new SecureRandom();

            //build the initialising factor
            byte[] iv = new byte[CRYPTO_IV_LENGTH];
            secureRandom.nextBytes(iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec parameterSpec = new GCMParameterSpec(CRYPTO_AUTH_TAG_LENGTH, iv); //128 bit authentication tag length
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

            byte[] cipherText = cipher.doFinal(plainData.getBytes());

            ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + cipherText.length);
            byteBuffer.put(iv);
            byteBuffer.put(cipherText);
            //the first 12 bytes are the IV where others are the cipher message + authentication tag
            byte[] cipherMessage = byteBuffer.array();
            return Base64.getEncoder().encodeToString(cipherMessage);

        } catch (Exception e) {
        	logger.error("Exception while encrypting AES: " + e.getMessage(), e);
            throw new CustomApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong internally");
        }
    }

    public static String encryptDataAES(String plainData, SecretKey secretKey) {
        return encryptDataAES(plainData, secretKey.getEncoded());
    }

    /**
     * this will decrypt the supplied data and return it in
     * plain text using the AES algorithm in CGM mode and No padding
     *
     * @param encryptedDataInBase64 data to be decrypted
     * @param secretKey             the secret key spec i.e. 128-bit length key
     * @return plain data as a String
     * @throws CustomApplicationException which will be caught by the centralised handler
     */
    public static String decryptDataAES(String encryptedDataInBase64, SecretKey secretKey) {

        try {

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

            byte[] encryptedDataBytes = Base64.getDecoder().decode(encryptedDataInBase64.getBytes());

            //remember we stored the iv as the first 12 bytes while encrypting
            byte[] iv = Arrays.copyOfRange(encryptedDataBytes, 0, CRYPTO_IV_LENGTH);

            GCMParameterSpec parameterSpec = new GCMParameterSpec(CRYPTO_AUTH_TAG_LENGTH, iv); //128 bit authentication tag length
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

            //use everything from 12 bytes on as ciphertext
            byte[] plainText = cipher.doFinal(Arrays.copyOfRange(encryptedDataBytes, CRYPTO_IV_LENGTH, encryptedDataBytes.length));

            return new String(plainText);

        } catch (Exception e) {
            logger.error("Exception while decrypting AES: " + e.getMessage(), e);
            throw new CustomApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong internally. Please try again");
        }
    }

    public static String decryptDataAES(String encryptedDataInBase64, byte[] key) {
        SecretKey secretKey = new SecretKeySpec(key, "AES");
        return decryptDataAES(encryptedDataInBase64, secretKey);
    }
	
	
	

}
