package com.smattme.demoserver.exceptions.handler;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.smattme.demoserver.config.Routes;
import com.smattme.demoserver.exceptions.CustomApplicationException;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.smattme.demoserver.helpers.GenericResponse.*;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;


/**
 * this is a controller for
 * handling errors on the
 * server
 */

@RestControllerAdvice
@RestController
public class GlobalExceptionHandler implements ErrorController {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public GlobalExceptionHandler() {
        logger.debug("global exception handler initialised");
    }

    @ExceptionHandler(CustomApplicationException.class)
    public ResponseEntity<Map<String, Object>> handleThrowable(CustomApplicationException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        return ResponseEntity.status(e.getHttpStatus()).body(genericErrorResponse(e.getHttpStatus(), e.getMessage(), e.getErrors(), e.getData())) ;
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Map<String, Object>> handleInternalServerError(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.error("global exception handler: " + e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(genericInternalServerError("Something went wrong internally"));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<Map<String, Object>> handleError(MissingRequestHeaderException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(generic400BadRequest(e.getMessage()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleError(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(genericErrorResponse(HttpStatus.METHOD_NOT_ALLOWED, e.getMessage(), Collections.singletonList(e.getMessage())));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleError(MissingServletRequestParameterException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(generic400BadRequest(e.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleError(HttpMessageNotReadableException e) {
        String errorMessage = e.getMessage();
        logger.error(errorMessage, e);

        String[] split = errorMessage != null ? errorMessage.split(":") : null;
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(generic400BadRequest((split == null || split.length == 0) ? errorMessage : split[0]));
    }

    @RequestMapping(Routes.Exception.DEFAULT_ERROR)
    public ResponseEntity<Map<String, Object>> handleError(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpStatus httpStatus = getHttpStatus(request);
        logger.debug("status code in handleError: " + httpStatus);
        String message = getErrorMessage(request, httpStatus);
        logger.debug("error message in handleError: " + message);

        return ResponseEntity.status(httpStatus).body(genericErrorResponse(httpStatus, message));
    }

    private HttpStatus getHttpStatus(HttpServletRequest request) {

        String code = request.getParameter("code");
        Integer status = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        HttpStatus httpStatus;

        if (status != null) httpStatus = HttpStatus.valueOf(status);
        else if (!StringUtils.isEmpty(code)) httpStatus = HttpStatus.valueOf(code);
        else httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        return httpStatus;
    }

    private String getErrorMessage(HttpServletRequest request, HttpStatus httpStatus) {
        String message = (String) request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        if(message != null && !message.isEmpty()) return message;

        switch (httpStatus) {
            case NOT_FOUND:
                message = "The resource does not exist";
                break;
            case INTERNAL_SERVER_ERROR:
                message = "Something went wrong internally";
                break;
            case FORBIDDEN:
                message = "Permission denied";
                break;
            case TOO_MANY_REQUESTS:
                message = "Too many requests";
                break;
            default:
                message = httpStatus.getReasonPhrase();
        }

        return message;
    }


}
