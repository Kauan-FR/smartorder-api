package com.kauanferreira.smartorder.dto.request;

import com.kauanferreira.smartorder.validation.NotBlankField;
import jakarta.validation.constraints.Size;

/**
 * Request payload for customer-facing address creation.
 *
 * <p>Unlike {@link AddressRequest}, this DTO does not accept a userId — the
 * authenticated user is always resolved from the JWT token by the service
 * layer, preventing customers from creating addresses on behalf of other
 * users.</p>
 *
 * @author Kauan Santos Ferreira
 * @since 2026
 */
public record AddressCustomerRequest(

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
        String country
) {
}
