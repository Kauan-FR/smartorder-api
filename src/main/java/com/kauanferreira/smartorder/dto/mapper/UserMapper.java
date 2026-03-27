package com.kauanferreira.smartorder.dto.mapper;

import com.kauanferreira.smartorder.dto.request.UserRequest;
import com.kauanferreira.smartorder.dto.request.UserUpdateRequest;
import com.kauanferreira.smartorder.dto.response.UserResponse;
import com.kauanferreira.smartorder.entity.User;

/**
 * Mapper class for converting between {@link User} entity
 * and its corresponding DTOs.
 *
 * <p>The password is intentionally excluded from the response
 * for security purposes.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see UserRequest
 * @see UserResponse
 * @see User
 */
public final class UserMapper {

    private UserMapper() {
    }

    /**
     * Converts a {@link UserRequest} to a {@link User} entity.
     *
     * @param request the request DTO containing user data
     * @return a new User entity with fields populated from the request
     */
    public static User toEntity(UserRequest request) {
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(request.password());
        user.setRole(request.role());
        user.setPhone(request.phone());
        return user;
    }

    /**
     * Converts a {@link User} entity to a {@link UserResponse} DTO.
     *
     * <p>Password is excluded from the response.</p>
     *
     * @param user the user entity
     * @return a new UserResponse with fields populated from the entity
     */
    public static UserResponse toResponse(User user) {
        return new  UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getPhone(),
                user.getCreatedAt()
        );
    }

    /**
     * Updates an existing {@link User} entity with data from a {@link UserRequest}.
     *
     * <p>Password is not updated here — use the dedicated
     * {@code updatePassword} method in UserService instead.</p>
     *
     * @param request the request DTO containing updated data
     * @param user    the existing entity to update
     */
    public static void updateEntity(UserRequest request, User user) {
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPhone(request.phone());
        user.setRole(request.role());
    }

    /**
     * Converts a {@link UserUpdateRequest} to a {@link User} entity.
     * <p>
     * Password is not set since update operations do not modify passwords.
     * </p>
     *
     * @param request the update request DTO
     * @return a User entity without password
     */
    public static User toEntity(UserUpdateRequest request) {
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setRole(request.role());
        user.setPhone(request.phone());
        return user;
    }
}
