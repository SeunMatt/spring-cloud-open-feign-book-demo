package com.smattme.democonsumer.exceptions;

import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

public class CustomApplicationException extends RuntimeException {

	private HttpStatus httpStatus;
	private List<String> errors;

	public CustomApplicationException(String message) {
		this(HttpStatus.BAD_REQUEST, message);
	}

	public CustomApplicationException(HttpStatus httpStatus, String message) {
		this(httpStatus, message, Collections.singletonList(message));
	}

	public CustomApplicationException(HttpStatus httpStatus, String message,
		List<String> errors) {

		super(message);
		this.httpStatus = httpStatus;
		this.errors = errors;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public List<String> getErrors() {
		return errors;
	}
}
