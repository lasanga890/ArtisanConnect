package com.example.demo.dto;

import java.util.Set;

public class UserDTO {
    private Long id;
    private String fullName;
    private String email;
    private Set<String> roles;

    // Constructors
    public UserDTO() {}

    public UserDTO(Long id, String fullName, String email, Set<String> roles) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.roles = roles;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
