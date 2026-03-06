package com.kauanferreira.smartorder.dto.response;

/**
 * DTO for returning {@link com.kauanferreira.smartorder.entity.Address} data to the client.
 *
 * <p>Contains the address details along with its associated user
 * represented as a nested {@link UserResponse}.</p>
 *
 * @param id         the address unique identifier
 * @param street     the street name
 * @param number     the street number
 * @param complement the address complement
 * @param city       the city name
 * @param state      the state name
 * @param zipCode    the zip code
 * @param country    the country name
 * @param user       the associated user data
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
public record AddressResponse(

        Long id,
        String street,
        String number,
        String complement,
        String city,
        String state,
        String zipCode,
        String country,
        UserResponse user
) {
}
