package com.kauanferreira.smartorder.dto.response;

/**
 * Response DTO for successful authentication.
 *
 * <p>Contains the JWT token, user name, email, and role
 * for the client to identify the authenticated user.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
public record AuthResponse(
        String token,
        String name,
        String email,
        String role
) {
}
