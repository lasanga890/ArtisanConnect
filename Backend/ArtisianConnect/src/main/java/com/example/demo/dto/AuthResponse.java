package com.example.demo.dto;

public class AuthResponse {
    private String message;

    public AuthResponse(String message) {
        this.message = message;
    }

   
    public String getMessage() {
        return message;
    }
}
