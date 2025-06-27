package com.example.demo.service;

import com.example.demo.dto.CategoryDTO;
import com.example.demo.model.Category;
import com.example.demo.Repo.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    public CategoryDTO createCategory(CategoryDTO dto) {
        if (dto.getName() == null || dto.getName().isEmpty()) {
            throw new IllegalArgumentException("Category name is required.");
        }
        if (categoryRepo.findByName(dto.getName()).isPresent()) {
            throw new RuntimeException("Category name already exists.");
        }
        Category category = new Category(dto.getName(), dto.getDescription());
        category = categoryRepo.save(category);
        return new CategoryDTO(category.getId(), category.getName(), category.getDescription());
    }

    public List<CategoryDTO> getAllCategories() {
        return categoryRepo.findAll().stream()
                .map(cat -> new CategoryDTO(cat.getId(), cat.getName(), cat.getDescription()))
                .collect(Collectors.toList());
    }

    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found."));
        return new CategoryDTO(category.getId(), category.getName(), category.getDescription());
    }

    public CategoryDTO updateCategory(Long id, CategoryDTO dto) {
        if (dto.getName() == null || dto.getName().isEmpty()) {
            throw new IllegalArgumentException("Category name is required.");
        }
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found."));
        if (!category.getName().equals(dto.getName()) && categoryRepo.findByName(dto.getName()).isPresent()) {
            throw new RuntimeException("Category name already exists.");
        }
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category = categoryRepo.save(category);
        return new CategoryDTO(category.getId(), category.getName(), category.getDescription());
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found."));
        if (!category.getProducts().isEmpty()) {
            throw new RuntimeException("Cannot delete category with associated products.");
        }
        categoryRepo.delete(category);
    }
}
