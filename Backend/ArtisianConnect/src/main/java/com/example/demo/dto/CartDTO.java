package com.example.demo.dto;

import java.util.List;

public class CartDTO {
    private Long id;
    private Long userId;
    private List<CartItemDTO> items;
    private Double totalPrice;

    // Constructors
    public CartDTO() {}

    public CartDTO(Long id, Long userId, List<CartItemDTO> items, Double totalPrice) {
        this.id = id;
        this.userId = userId;
        this.items = items;
        this.totalPrice = totalPrice;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<CartItemDTO> getItems() {
        return items;
    }

    public void setItems(List<CartItemDTO> items) {
        this.items = items;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
