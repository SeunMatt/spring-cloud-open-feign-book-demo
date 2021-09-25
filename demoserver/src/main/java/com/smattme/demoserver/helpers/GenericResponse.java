package com.smattme.demoserver.helpers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.util.*;


public class GenericResponse {

    /**
     * @param errors
     * @return map object
     */
    public static Map<String, Object> generic400BadRequest(String errors) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", false);
        response.put("errors", Collections.singletonList(errors));
        response.put("message", errors);
        response.put("code", HttpStatus.BAD_REQUEST.value());
        return response;
    }


    public static Map<String, Object> generic404NotFoundRequest(String errors) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", false);
        response.put("errors", Collections.singletonList(errors));
        response.put("message", errors);
        response.put("code", HttpStatus.NOT_FOUND.value());
        return response;
    }

    public static Map<String, Object> generic401UnauthorizedRequest(String errors) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", false);
        response.put("errors", Collections.singletonList(errors));
        response.put("message", errors);
        response.put("code", HttpStatus.UNAUTHORIZED.value());
        return response;
    }

    public static Map<String, Object> genericUserNotFoundException() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", false);
        response.put("code", HttpStatus.NOT_FOUND.value());
        response.put("message", "User record not found");
        response.put("errors", Collections.singletonList("User record not found"));
        return response;
    }

    public static Map<String, Object> genericValidationErrors(List<String> errors) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", false);
        response.put("code", HttpStatus.BAD_REQUEST.value());
        response.put("message", "Missing required parameter(s)");
        response.put("errors", errors);
        return response;
    }

    public static Map<String, Object> genericInternalServerError(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", false);
        response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("message", ((message != null) ? message : "Oops! We're unable to process this request now."));
        response.put("errors", Collections.singletonList(((message != null) ? message : "Oops! We're unable to process this request now.")));
        return response;
    }

    public static Map<String, Object> generic200Response(String message) {
        return generic200Response(message, null);
    }

    public static Map<String, Object> generic200Response(Object data) {
        return generic200Response(null, data);
    }

    public static Map<String, Object> generic200Response(String message, Map<String, Object> data) {
        return generic200Response(message, (Object) data);
    }

    public static Map<String, Object> generic200Response(String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("code", HttpStatus.OK.value());
        response.put("message", ((message != null && !message.isEmpty()) ? message : "Operation Successful"));
        if(data != null) response.put("data", data);
        return response;
    }

    public static Map<String, Object> genericResponse(HttpStatus status, String message, Map<String, Object> data) {
        return genericResponse(status, message, data);
    }

    public static Map<String, Object> genericResponse(HttpStatus status, String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("code", status.value());
        response.put("message", message);
        if(!Objects.isNull(data))
            response.put("data", data);
        return response;
    }

    public static Map<String, Object> genericErrorResponse(HttpStatus status, String message) {
        return genericErrorResponse(status, message, Collections.singletonList(message));
    }

    public static Map<String, Object> genericErrorResponse(HttpStatus status, String message, List<String> errors) {
        return genericErrorResponse(status, message, errors, null);
    }

    public static Map<String, Object> genericErrorResponse(HttpStatus status, String message, Map<String, Object> data) {
        if(StringUtils.isEmpty(message))
            message = status.getReasonPhrase();
        return genericErrorResponse(status, message, Collections.singletonList(message), data);
    }

    public static Map<String, Object> genericErrorResponse(HttpStatus status, String message, List<String> errors, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", false);
        response.put("code", status.value());
        response.put("message", message);
        response.put("errors", errors);
        if (!Objects.isNull(data)) response.put("data", data);
        return response;
    }

}
