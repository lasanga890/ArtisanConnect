package com.example.demo.Repo;

import com.example.demo.model.role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface roleRepo extends JpaRepository<role, Long> {


    Optional<role> findByName(String name);
}
