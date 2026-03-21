package com.lab.auth.model;

public class AuthResponse {
    private String token;
    private String username;
    private long expiresIn;

    public AuthResponse(String token, String username, long expiresIn) {
        this.token = token;
        this.username = username;
        this.expiresIn = expiresIn;
    }

    public String getToken() { return token; }
    public String getUsername() { return username; }
    public long getExpiresIn() { return expiresIn; }
}
