package com.kauanferreira.smartorder.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

/**
 * Service responsible for JWT token generation, validation, and data extraction.
 *
 * <p>Uses HMAC-SHA256 signing algorithm with a configurable secret key
 * and expiration time. Tokens contain the user's email as subject
 * and role as a custom claim.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long expiration;

    /**
     * Constructs the JwtService with secret key and expiration from application properties.
     *
     * @param secret     the Base64-encoded secret key for signing tokens
     * @param expiration the token expiration time in milliseconds
     */
    public JwtService(@Value("${jwt.secret}") String secret,
                      @Value("${jwt.expiration}") long expiration) {
        this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
        this.expiration = expiration;
    }

    /**
     * Generates a JWT token for the given user.
     *
     * <p>The token contains:</p>
     * <ul>
     *     <li>Subject: user's email</li>
     *     <li>Custom claim "role": user's role (ADMIN or CUSTOMER)</li>
     *     <li>Issued at: current timestamp</li>
     *     <li>Expiration: current timestamp + configured expiration</li>
     * </ul>
     *
     * @param userDetails the authenticated user
     * @return the signed JWT token string
     */
    public String generateToken(UserDetails userDetails) {
        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claims(Map.of("role", role))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey)
                .compact();
    }

    /**
     * Extracts the username (email) from a JWT token.
     *
     * @param token the JWT token string
     * @return the email stored in the token's subject claim
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Validates a JWT token against the provided user details.
     *
     * <p>A token is valid when:</p>
     * <ul>
     *     <li>The email in the token matches the user's email</li>
     *     <li>The token has not expired</li>
     * </ul>
     *
     * @param token       the JWT token string
     * @param userDetails the user to validate against
     * @return true if the token is valid, false otherwise
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Checks if a JWT token has expired.
     *
     * @param token the JWT token string
     * @return true if the token's expiration date is before the current date
     */
    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    /**
     * Extracts all claims from a JWT token.
     *
     * @param token the JWT token string
     * @return the claims contained in the token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}