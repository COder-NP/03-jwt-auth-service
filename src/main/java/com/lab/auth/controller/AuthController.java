package com.lab.auth.controller;

import com.lab.auth.model.AuthRequest;
import com.lab.auth.model.AuthResponse;
import com.lab.auth.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authManager, JwtUtil jwtUtil) {
        this.authenticationManager = authManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        String token = jwtUtil.generateToken(
            request.getUsername(),
            Arrays.asList("ROLE_USER", "ROLE_ADMIN")
        );
        return ResponseEntity.ok(new AuthResponse(token, request.getUsername(), jwtUtil.getExpiration()));
    }

    @GetMapping("/validate")
    public ResponseEntity<Map<String, Object>> validate(@RequestHeader("Authorization") String authHeader) {
        // Java 8 Optional chain — extract Bearer token safely
        return Optional.ofNullable(authHeader)
            .filter(h -> h.startsWith("Bearer "))
            .map(h -> h.substring(7))
            .map(token -> {
                String username = jwtUtil.extractUsername(token);
                return ResponseEntity.ok(Map.<String, Object>of(
                    "valid", jwtUtil.validateToken(token, username),
                    "username", username,
                    "roles", jwtUtil.extractRoles(token)
                ));
            })
            .orElse(ResponseEntity.badRequest().body(Map.of("error", "Invalid Authorization header")));
    }
}
