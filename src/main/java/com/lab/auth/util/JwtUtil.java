package com.lab.auth.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

/**
 * 🔑 JWT Utility
 *
 * Java 8 Features Demonstrated:
 * - Function<Claims, T>  — Functional Interface for generic claims extraction
 * - Optional             — null-safe roles extraction
 * - Stream + map()       — roles list transformation to uppercase
 * - Method References    — Claims::getSubject, String::toUpperCase
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Generate a JWT token with roles embedded as claims.
     * Java 8: Stream + map() to uppercase all role names.
     */
    public String generateToken(String username, List<String> roles) {
        Map<String, Object> claims = new HashMap<>();

        // Java 8 Stream — transform roles to uppercase
        claims.put("roles", roles.stream()
                .map(String::toUpperCase)
                .toList());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Java 8 Functional Interface — generic claims extractor.
     * Callers pass a Function that determines what to extract.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Method references as Function<Claims, T>
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Java 8 Optional — null-safe roles extraction.
     * Returns empty list if roles claim is missing.
     */
    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        return Optional.ofNullable(extractClaim(token, claims -> claims.get("roles")))
                .map(roles -> (List<String>) roles)
                .orElse(Collections.emptyList());
    }

    public boolean validateToken(String token, String username) {
        return extractUsername(token).equals(username) && !extractExpiration(token).before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public long getExpiration() { return expiration; }
}
