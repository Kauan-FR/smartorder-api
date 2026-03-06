package com.kauanferreira.smartorder.dto.mapper;

import com.kauanferreira.smartorder.dto.request.AddressRequest;
import com.kauanferreira.smartorder.dto.response.AddressResponse;
import com.kauanferreira.smartorder.entity.Address;
import com.kauanferreira.smartorder.entity.User;

/**
 * Mapper class for converting between {@link Address} entity
 * and its corresponding DTOs.
 *
 * <p>Handles the nested {@link User} relationship.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see AddressRequest
 * @see AddressResponse
 * @see Address
 */
public final class AddressMapper {

    private AddressMapper() {
    }

    /**
     * Converts an {@link AddressRequest} to an {@link Address} entity.
     *
     * <p>Creates a User reference with only the id set. The service layer
     * validates that the user exists.</p>
     *
     * @param request the request DTO containing address data
     * @return a new Address entity with fields populated from the request
     */
    public static Address toEntity(AddressRequest request){
        Address address = new Address();
        address.setStreet(request.street());
        address.setNumber(request.number());
        address.setComplement(request.complement());
        address.setCity(request.city());
        address.setState(request.state());
        address.setZipCode(request.zipCode());
        address.setCountry(request.country());

        User user = new User();
        user.setId(request.userId());
        address.setUser(user);

        return  address;
    }

    /**
     * Converts an {@link Address} entity to an {@link AddressResponse} DTO.
     *
     * <p>Uses {@link UserMapper#toResponse} for the nested user.</p>
     *
     * @param address the address entity
     * @return a new AddressResponse with fields populated from the entity
     */
    public static AddressResponse toResponse(Address address){
        return new AddressResponse(
                address.getId(),
                address.getStreet(),
                address.getNumber(),
                address.getComplement(),
                address.getCity(),
                address.getState(),
                address.getZipCode(),
                address.getCountry(),
                UserMapper.toResponse(address.getUser())
        );
    }

    /**
     * Updates an existing {@link Address} entity with data from an {@link AddressRequest}.
     *
     * <p>The user association is not updated — an address belongs to the same user.</p>
     *
     * @param request the request DTO containing updated data
     * @param address the existing entity to update
     */
    public static void updateEntity(AddressRequest request, Address address){
        address.setStreet(request.street());
        address.setNumber(request.number());
        address.setComplement(request.complement());
        address.setCity(request.city());
        address.setState(request.state());
        address.setZipCode(request.zipCode());
        address.setCountry(request.country());
    }
}
