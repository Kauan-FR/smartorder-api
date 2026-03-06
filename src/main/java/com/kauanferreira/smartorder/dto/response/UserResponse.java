package com.kauanferreira.smartorder.dto.response;

import com.kauanferreira.smartorder.enums.Role;

import java.time.LocalDateTime;

/**
 * DTO for returning {@link com.kauanferreira.smartorder.entity.User} data to the client.
 *
 * <p>The password field is intentionally excluded for security purposes.</p>
 *
 * @param id        the user unique identifier
 * @param name      the user name
 * @param email     the user email
 * @param role      the user role
 * @param phone     the user phone number
 * @param createdAt the account creation timestamp
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
public record UserResponse(

        Long id,
        String name,
        String email,
        Role role,
        String phone,
        LocalDateTime createdAt
) {
}
