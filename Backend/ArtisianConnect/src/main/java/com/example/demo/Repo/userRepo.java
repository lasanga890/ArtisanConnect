package com.example.demo.Repo;

import com.example.demo.model.userModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface userRepo extends JpaRepository<userModel, Long> {

    Optional<userModel> findByEmail(String email);
    
    boolean existsByEmail(String email);
}
