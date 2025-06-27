package com.example.demo.service;

import com.example.demo.dto.UserDTO;
import com.example.demo.model.role;
import com.example.demo.model.userModel;
import com.example.demo.Repo.roleRepo;
import com.example.demo.Repo.userRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private userRepo userRepo;

    @Autowired
    private roleRepo roleRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public List<UserDTO> getAllUsers() {
        return userRepo.findAll().stream()
                .map(user -> new UserDTO(
                        user.getId(),
                        user.getFullName(),
                        user.getEmail(),
                        user.getRoles().stream().map(role::getName).collect(Collectors.toSet())))
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        userModel user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found."));
        return new UserDTO(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRoles().stream().map(role::getName).collect(Collectors.toSet()));
    }

    @Transactional
    public UserDTO updateUser(Long id, UserDTO dto) {
        userModel user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found."));
        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail()) && userRepo.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use.");
        }
        if (dto.getFullName() != null) {
            user.setFullName(dto.getFullName());
        }
        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
            Set<role> roles = dto.getRoles().stream()
                    .map(name -> roleRepo.findByName(name)
                            .orElseThrow(() -> new RuntimeException("Role not found: " + name)))
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }
        userRepo.save(user);
        return new UserDTO(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRoles().stream().map(role::getName).collect(Collectors.toSet()));
    }

    @Transactional
    public void deleteUser(Long id) {
        userModel user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found."));
        userRepo.delete(user);
    }
}
