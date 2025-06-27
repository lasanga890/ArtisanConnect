package com.example.demo.service;

import com.example.demo.dto.ProductDTO;
import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.Repo.CategoryRepo;
import com.example.demo.Repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    public ProductDTO createProduct(ProductDTO dto) {
        if (dto.getName() == null || dto.getName().isEmpty()) {
            throw new IllegalArgumentException("Product name is required.");
        }
        if (dto.getPrice() == null || dto.getPrice() < 0) {
            throw new IllegalArgumentException("Invalid product price.");
        }
        if (dto.getStockQuantity() == null || dto.getStockQuantity() < 0) {
            throw new IllegalArgumentException("Invalid stock quantity.");
        }
        Category category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found."));
        Product product = new Product(dto.getName(), dto.getDescription(), dto.getPrice(),
                dto.getStockQuantity(), dto.getArtisanDetails(), category);
        product = productRepo.save(product);
        return new ProductDTO(product.getId(), product.getName(), product.getDescription(),
                product.getPrice(), product.getStockQuantity(), product.getArtisanDetails(), product.getCategory().getId());
    }

    public List<ProductDTO> getAllProducts() {
        return productRepo.findAll().stream()
                .map(prod -> new ProductDTO(prod.getId(), prod.getName(), prod.getDescription(),
                        prod.getPrice(), prod.getStockQuantity(), prod.getArtisanDetails(), prod.getCategory().getId()))
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        categoryRepo.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found."));
        return productRepo.findByCategoryId(categoryId).stream()
                .map(prod -> new ProductDTO(prod.getId(), prod.getName(), prod.getDescription(),
                        prod.getPrice(), prod.getStockQuantity(), prod.getArtisanDetails(), prod.getCategory().getId()))
                .collect(Collectors.toList());
    }

    public ProductDTO getProductById(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found."));
        return new ProductDTO(product.getId(), product.getName(), product.getDescription(),
                product.getPrice(), product.getStockQuantity(), product.getArtisanDetails(), product.getCategory().getId());
    }

    public ProductDTO updateProduct(Long id, ProductDTO dto) {
        if (dto.getName() == null || dto.getName().isEmpty()) {
            throw new IllegalArgumentException("Product name is required.");
        }
        if (dto.getPrice() == null || dto.getPrice() < 0) {
            throw new IllegalArgumentException("Invalid product price.");
        }
        if (dto.getStockQuantity() == null || dto.getStockQuantity() < 0) {
            throw new IllegalArgumentException("Invalid stock quantity.");
        }
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found."));
        Category category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found."));
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity());
        product.setArtisanDetails(dto.getArtisanDetails());
        product.setCategory(category);
        product = productRepo.save(product);
        return new ProductDTO(product.getId(), product.getName(), product.getDescription(),
                product.getPrice(), product.getStockQuantity(), product.getArtisanDetails(), product.getCategory().getId());
    }

    public void deleteProduct(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found."));
        productRepo.delete(product);
    }
}
