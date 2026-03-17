package com.kauanferreira.smartorder.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.Map;

/**
 * Standard error response DTO for the SmartOrder API.
 *
 * <p>Provides a consistent error response structure across all API endpoints,
 * including timestamp, HTTP status, error type, message, request path,
 * and optional field-level validation errors.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record StandardError(
        Instant timestamp,
        Integer status,
        String error,
        String message,
        String path,
        Map<String, String> fieldErrors
) {

    /**
     * Creates a StandardError without field-level validation errors.
     *
     * @param timestamp the moment the error occurred
     * @param status    the HTTP status code
     * @param error     the error type description
     * @param message   the detailed error message
     * @param path      the request URI that caused the error
     */
    public StandardError(Instant timestamp, Integer status, String error, String message, String path) {
        this(timestamp, status, error, message, path, null);
    }
}
