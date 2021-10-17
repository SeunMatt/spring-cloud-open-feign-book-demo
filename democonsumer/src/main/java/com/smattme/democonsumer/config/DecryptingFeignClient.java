package com.smattme.democonsumer.config;

import com.smattme.democonsumer.exceptions.CustomApplicationException;
import feign.Client.Default;
import feign.Request;
import feign.Request.Options;
import feign.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

public class DecryptingFeignClient extends Default {

	private String aesSecret;

    public DecryptingFeignClient(SSLSocketFactory sslContextFactory,
		HostnameVerifier hostnameVerifier, String aesSecret) {

        super(sslContextFactory, hostnameVerifier);
        this.aesSecret = aesSecret;
		
    }

    @Override
    public Response execute(Request request, Options options) throws IOException {

        Response response = super.execute(request, options);

        InputStream bodyStream = response.body().asInputStream();

        // copy the input stream to a String
        String responseBody = StreamUtils.copyToString(bodyStream, UTF_8);

        // decrypt the responseBody using AES algorithm and shared secret
        String decryptedBody = decryptDataAES(responseBody, aesSecret.getBytes());

        // reconstruct a new response from the original one
        // changing only the status code
        return response.toBuilder()
                .status(response.status())
                .headers(response.headers())
                .body(decryptedBody, UTF_8) // set the new body
                .build();

    }

    private String decryptDataAES(String encryptedDataInBase64, byte[] key) {

        try {

            SecretKey secretKey = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

            var encryptedBase64Bytes = encryptedDataInBase64.getBytes();
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] encryptedDataBytes = decoder.decode(encryptedBase64Bytes);

            // remember we stored the iv as the first 12 bytes while encrypting
            byte[] iv = Arrays.copyOfRange(encryptedDataBytes, 0, 12);

            GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

            // use everything from 12 bytes on as cipher text
            int length = encryptedDataBytes.length;
            byte[] bytes = Arrays.copyOfRange(encryptedDataBytes, 12, length);
            byte[] plainText = cipher.doFinal(bytes);

            return new String(plainText);

        } catch (Exception e) {
            throw new CustomApplicationException(e.getMessage());
        }
    }

}
