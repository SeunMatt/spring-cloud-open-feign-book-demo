package com.smattme.democonsumer.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smattme.democonsumer.exceptions.CustomApplicationException;
import feign.Response;
import feign.codec.ErrorDecoder;
import io.vavr.control.Try;
import org.springframework.http.HttpStatus;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.smattme.democonsumer.helpers.GenericResponse.genericErrorResponse;
import static feign.FeignException.errorStatus;
import static java.nio.charset.StandardCharsets.UTF_8;

public class CustomErrorDecoder implements ErrorDecoder {

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
				return new CustomApplicationException(httpStatus,
					httpStatus.getReasonPhrase());
			}

			// read the error response body as String
			InputStream bodyStream = response.body().asInputStream();
			String errorJson = StreamUtils.copyToString(bodyStream, UTF_8);

			// this is a Jackson Object type that aid parsing JSON string
			// to generified Map object i.e. Map<String, Object>

			TypeReference<Map<String, Object>> mapTypeReference = new TypeReference<>(){};

			// parse the response body to a Map<String, Object> object and
			// if something went wrong return a singleton map with
			// the returned error as responseDescription

			Map<String, Object> genericResponse;
			genericResponse = genericErrorResponse(HttpStatus.SERVICE_UNAVAILABLE,
				"External service not available");

			Map<String, Object> errorBody;
			errorBody = Try.of(() -> objectMapper.readValue(errorJson, mapTypeReference))
				.getOrElse(genericResponse);

			// return a new CustomApplicationException using the status returned by the
			// response and the message parsed above.
			// If the responseDescription is not present, use a fallback message

			String defaultMsg = "Service not available now, please try again";
			String message = errorBody.getOrDefault("message", defaultMsg).toString();

			var defaultErrors = Collections.singleton(message);
			var errorsObj = errorBody.getOrDefault("errors", defaultErrors);
			List<String> errors = (List<String>) errorsObj;

			var httpStatus = HttpStatus.valueOf(response.status());
			return new CustomApplicationException(httpStatus, message, errors);

		} catch (IOException e) {

			// use default mechanism if something
			// bad happens in the process

			return errorStatus(methodKey, response);
		}

	}

}
