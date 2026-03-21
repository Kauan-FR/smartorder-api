package com.kauanferreira.smartorder.exception;

import com.kauanferreira.smartorder.dto.response.StandardError;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Global exception handler for the SmartOrder API.
 *
 * <p>Intercepts exceptions thrown by controllers and converts them
 * into standardized {@link StandardError} responses with appropriate
 * HTTP status codes.</p>
 *
 * <p>Exception to HTTP status mapping:</p>
 * <ul>
 *     <li>{@link ResourceNotFoundException} → 404 (Not Found)</li>
 *     <li>{@link DuplicateResourceException} → 409 (Conflict)</li>
 *     <li>{@link BusinessRuleException} → 422 (Unprocessable Entity)</li>
 *     <li>{@link DatabaseOperationException} → 500 (Internal Server Error)</li>
 *     <li>{@link MethodArgumentNotValidException} → 400 (Bad Request)</li>
 * </ul>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see StandardError
 * @see ResourceNotFoundException
 * @see DuplicateResourceException
 * @see BusinessRuleException
 * @see DatabaseOperationException
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    public static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles {@link ResourceNotFoundException}.
     *
     * <p>Triggered when a requested resource does not exist in the database.</p>
     *
     * @param ex      the exception thrown
     * @param request the HTTP request that caused the exception
     * @return a {@link ResponseEntity} with status 404 and a {@link StandardError} body
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> handleResourceNotFound(ResourceNotFoundException ex,
                                                                HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError error = new StandardError(
                Instant.now(),
                status.value(),
                "Resource not found",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(error);
    }

    /**
     * Handles {@link DuplicateResourceException}.
     *
     * <p>Triggered when an operation would create a duplicate resource,
     * such as registering a user with an email that already exists.</p>
     *
     * @param ex      the exception thrown
     * @param request the HTTP request that caused the exception
     * @return a {@link ResponseEntity} with status 409 and a {@link StandardError} body
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<StandardError> handleDuplicateResource(DuplicateResourceException ex,
                                                                 HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        StandardError error = new StandardError(
                Instant.now(),
                status.value(),
                "Duplicate resource",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(error);
    }

    /**
     * Handles {@link BusinessRuleException}.
     *
     * <p>Triggered when a business rule is violated, such as attempting
     * to place an order with insufficient stock.</p>
     *
     * @param ex      the exception thrown
     * @param request the HTTP request that caused the exception
     * @return a {@link ResponseEntity} with status 422 and a {@link StandardError} body
     */
    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<StandardError> handleBusinessRule(BusinessRuleException ex,
                                                            HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        StandardError error = new StandardError(
                Instant.now(),
                status.value(),
                "Business rule violation",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(error);
    }

    /**
     * Handles {@link DatabaseOperationException}.
     *
     * <p>Triggered when an unexpected database error occurs during
     * a persistence operation.</p>
     *
     * @param ex      the exception thrown
     * @param request the HTTP request that caused the exception
     * @return a {@link ResponseEntity} with status 500 and a {@link StandardError} body
     */
    @ExceptionHandler(DatabaseOperationException.class)
    public ResponseEntity<StandardError> handleDatabaseOperation(DatabaseOperationException ex,
                                                                 HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        logger.error("Database operation failed at {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        StandardError error = new StandardError(
                Instant.now(),
                status.value(),
                "Database erro",
                "An internal error occurred. Please try again later.",
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(error);
    }

    /**
     * Handles {@link MethodArgumentNotValidException}.
     *
     * <p>Triggered when request body validation fails (Bean Validation annotations).
     * Collects all field-level errors into a map for detailed client feedback.</p>
     *
     * @param ex      the exception thrown
     * @param request the HTTP request that caused the exception
     * @return a {@link ResponseEntity} with status 400 and a {@link StandardError} body
     *         including field-level validation errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> handleValidation(MethodArgumentNotValidException ex,
                                                          HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        Map<String, String> fieldErrors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(fieldError -> fieldErrors.put(
                        fieldError.getField(),
                        fieldError.getDefaultMessage()
                ));

        StandardError error = new StandardError(
                Instant.now(),
                status.value(),
                "Validation failed",
                "One or more fields have validation errors.",
                request.getRequestURI(),
                fieldErrors
        );
        return ResponseEntity.status(status).body(error);
    }

    /**
     * Handles {@link BadCredentialsException}.
     *
     * <p>Triggered when a login attempt fails due to invalid
     * email or password credentials.</p>
     *
     * @param ex      the exception thrown
     * @param request the HTTP request that caused the exception
     * @return a {@link ResponseEntity} with status 401 and a {@link StandardError} body
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<StandardError> handleBadCredentials(BadCredentialsException ex,
                                                              HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        StandardError error = new StandardError(
                Instant.now(),
                status.value(),
                "Authentication failed",
                "Invalid email or password.",
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(error);
    }
}
