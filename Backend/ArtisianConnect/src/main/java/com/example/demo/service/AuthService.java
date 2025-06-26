package com.example.demo.service;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.model.role;
import com.example.demo.model.userModel;
import com.example.demo.Repo.roleRepo;
import com.example.demo.Repo.userRepo;
import com.example.demo.config.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private userRepo userRepository;

    @Autowired
    private roleRepo roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return new AuthResponse("Email already in use.");
        }

        userModel user = new userModel();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());

        role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));

        user.setRoles(Collections.singleton(userRole));
        userRepository.save(user);

        return new AuthResponse("User registered successfully.");
    }

    public AuthResponse login(LoginRequest request) {
        userModel user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new AuthResponse("Invalid credentials.");
        }

        Set<String> roles = user.getRoles().stream()
                .map(role::getName)
                .collect(Collectors.toSet());

        String token = jwtUtil.generateToken(user.getEmail(), roles);


        return new AuthResponse(
            "Login successful.",
            token,
            user.getFullName(),
            user.getEmail(),
            roles
        );
    }
}
