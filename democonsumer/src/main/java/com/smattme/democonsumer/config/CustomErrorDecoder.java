package com.smattme.democonsumer.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.StreamUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smattme.democonsumer.exceptions.CustomApplicationException;
import com.smattme.democonsumer.helpers.GenericResponse;

import static feign.FeignException.errorStatus;
import feign.Response;
import feign.codec.ErrorDecoder;
import io.vavr.control.Try;

public class CustomErrorDecoder implements ErrorDecoder {

	private static final Logger logger = LoggerFactory.getLogger(CustomErrorDecoder.class);
	private ObjectMapper objectMapper;

	public CustomErrorDecoder(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public Exception decode(String methodKey, Response response) {

		try {

			// if the response has no body, use the HTTP status code and
			// its reason phrase
			if (Objects.isNull(response.body())) {
				HttpStatus httpStatus = HttpStatus.valueOf(response.status());
				return new CustomApplicationException(httpStatus, httpStatus.getReasonPhrase());
			}

			// read the error response body as String
			InputStream bodyStream = response.body().asInputStream();
			String errorJson = StreamUtils.copyToString(bodyStream, StandardCharsets.UTF_8);

			// this is a Jackson Object type that aid parsing JSON string to generic Map
			// object
			// i.e. Map<String, Object> instead of just Map
			TypeReference<Map<String, Object>> mapTypeReference = new TypeReference<Map<String, Object>>() {
			};

			// parse the response body to a Map<String, Object> object and if something went
			// wrong return a singleton map with
			// the returned error as responseDescription
			Map<String, Object> errorBody = Try.of(() -> objectMapper.readValue(errorJson, mapTypeReference))
					.getOrElse(GenericResponse.genericErrorResponse(HttpStatus.SERVICE_UNAVAILABLE,
							"External service not available"));

			// return a new CustomApplicationException using the status returned by the
			// response and
			// the message parsed above. Even if the responseDescription is not present one
			// way or another
			// use a fallback message
			String message = errorBody.getOrDefault("message", "Service not available now, please try again")
					.toString();
			List<String> errors = (List<String>) errorBody.getOrDefault("errors", Collections.singleton(message));
			return new CustomApplicationException(HttpStatus.valueOf(response.status()), message, errors, null);

		} catch (IOException e) {
			logger.error("Error reading error input stream of feign: " + e.getMessage(), e);

			// use default mechanism if something bad happens in the process
			return errorStatus(methodKey, response);
		}

	}

}
