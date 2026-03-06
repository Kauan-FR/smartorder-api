package com.kauanferreira.smartorder.dto.request;

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

        @NotBlank(message = "Street is required")
        @Size(max = 200, message = "Street must not exceed 200 characters")
        String street,

        @NotBlank(message = "Number is required")
        @Size(max = 20, message = "Number must not exceed 20 characters")
        String number,

        @Size(max = 100, message = "Complement must not exceed 100 characters")
        String complement,

        @NotBlank(message = "City is required")
        @Size(max = 100, message = "City must not exceed 100 characters")
        String city,

        @NotBlank(message = "State is required")
        @Size(max = 50, message = "State must not exceed 50 characters")
        String state,

        @NotBlank(message = "Zip code is required")
        @Size(max = 20, message = "Zip code must not exceed 20 characters")
        String zipCode,

        @NotBlank(message = "Country is required")
        @Size(max = 50, message = "Country must not exceed 50 characters")
        String country,

        @NotNull(message = "User id is required")
        Long userId
) {
}
