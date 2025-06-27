package com.example.demo.Repo;

import com.example.demo.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CategoryRepo extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
}
