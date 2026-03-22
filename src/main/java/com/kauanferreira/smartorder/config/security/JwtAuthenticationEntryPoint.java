package com.kauanferreira.smartorder.config.security;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.json.JsonMapper;
import com.kauanferreira.smartorder.dto.response.StandardError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.Instant;

/**
 * Custom authentication entry point that returns a 401 Unauthorized
 * response with a standardized error body when an unauthenticated
 * request tries to access a protected endpoint.
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see StandardError
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final JsonMapper jsonMapper;
    /**
     * Handles unauthorized access attempts by returning a 401 response
     * with a {@link StandardError} JSON body.
     *
     * @param request       the HTTP request
     * @param response      the HTTP response
     * @param authException the authentication exception
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        StandardError error = new StandardError(
                Instant.now(),
                HttpServletResponse.SC_UNAUTHORIZED,
                "Unauthorized",
                "Authentication is required to this resource.",
                request.getRequestURI()
        );

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        jsonMapper.writeValue(response.getOutputStream(), error);
    }
}
