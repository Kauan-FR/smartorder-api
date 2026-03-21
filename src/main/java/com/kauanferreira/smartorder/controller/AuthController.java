package com.kauanferreira.smartorder.controller;

import com.kauanferreira.smartorder.config.security.JwtService;
import com.kauanferreira.smartorder.dto.mapper.UserMapper;
import com.kauanferreira.smartorder.dto.request.AuthRequent;
import com.kauanferreira.smartorder.dto.request.UserRequest;
import com.kauanferreira.smartorder.dto.response.AuthResponse;
import com.kauanferreira.smartorder.dto.response.UserResponse;
import com.kauanferreira.smartorder.entity.User;
import com.kauanferreira.smartorder.services.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * REST controller for authentication operations.
 *
 * <p>Provides endpoints for user registration and login.
 * Login returns a JWT token for subsequent authenticated requests.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see JwtService
 * @see UserService
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user registration and login")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService  jwtService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registers a new user in the system.
     *
     * <p>The password is encrypted with BCrypt before being stored.
     * Returns the created user without the password field.</p>
     *
     * @param request the user registration data
     * @return HTTP 201 with the created user and location header
     */
    @Operation(summary = "Register a new user", description = "Creates a new user account with encrypted password.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRequest request) {
        User entity = UserMapper.toEntity(request);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        User created = userService.create(entity);
        UserResponse response = UserMapper.toResponse(created);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    /**
     * Authenticates a user and returns a JWT token.
     *
     * <p>Validates the email and password against the database.
     * If valid, generates a JWT token containing the user's email and role.</p>
     *
     * @param request the login credentials (email and password)
     * @return HTTP 200 with the JWT token and user information
     */
    @Operation(summary = "Login", description = "Authenticates a user and returns a JWT token.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Invalid email or password")
    })
    @PostMapping("/login")
public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequent request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        User user = (User) authentication.getPrincipal();
        String token = jwtService.generateToken(user);

        AuthResponse response = new AuthResponse(
                token,
                user.getName(),
                user.getEmail(),
                user.getRole().name()
        );

        return ResponseEntity.ok(response);
    }
}
