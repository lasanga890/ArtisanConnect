package com.example.demo.dto;

import java.util.Set;

public class AuthResponse {
    private String message;
    private String token;
    private String fullName;
    private String email;
    private Set<String> roles;

    public AuthResponse(String message) {
        this.message = message;
    }

    // New constructor for success with token and details
    public AuthResponse(String message, String token, String fullName, String email, Set<String> roles) {
        this.message = message;
        this.token = token;
        this.fullName = fullName;
        this.email = email;
        this.roles = roles;
    }

    // getters & setters here

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public Set<String> getRoles() {
        return roles;
    }
}
