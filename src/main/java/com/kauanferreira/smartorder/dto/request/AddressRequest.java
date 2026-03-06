package com.kauanferreira.smartorder.dto.request;

import com.kauanferreira.smartorder.validation.NotBlankField;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO for creating and updating an {@link com.kauanferreira.smartorder.entity.Address}.
 *
 * <p>The user is referenced by its id rather than the full entity.</p>
 *
 * @param street     the street name (required, max 200 characters)
 * @param number     the street number (required, max 20 characters)
 * @param complement the address complement (optional, max 100 characters)
 * @param city       the city name (required, max 100 characters)
 * @param state      the state name (required, max 50 characters)
 * @param zipCode    the zip code (required, max 20 characters)
 * @param country    the country name (required, max 50 characters)
 * @param userId     the id of the associated user (required)
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
public record AddressRequest(

        @NotBlankField(fieldName = "Address street", maxLength = 200)
        String street,

        @NotBlankField(fieldName = "Address number", maxLength = 20)
        String number,

        @Size(max = 100, message = "Complement must not exceed 100 characters")
        String complement,

        @NotBlankField(fieldName = "Address city", maxLength = 100)
        String city,

        @NotBlankField(fieldName = "Address state", maxLength = 50)
        String state,

        @NotBlankField(fieldName = "Address Zip code", maxLength = 20)
        String zipCode,

        @NotBlankField(fieldName = "Address country", maxLength = 50)
        String country,

        @NotNull(message = "User id is required")
        Long userId
) {
}
