package com.example.demo.service;

import com.example.demo.dto.CartDTO;
import com.example.demo.dto.CartItemDTO;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Product;
import com.example.demo.model.userModel;
import com.example.demo.Repo.CartItemRepo;
import com.example.demo.Repo.CartRepo;
import com.example.demo.Repo.ProductRepo;
import com.example.demo.Repo.userRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private CartItemRepo cartItemRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private userRepo userRepo;

    @Transactional
    public CartDTO addToCart(Long userId, Long productId, Integer quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }
        userModel user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found."));
        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock.");
        }

        Cart cart = cartRepo.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart(user, LocalDateTime.now());
                    return cartRepo.save(newCart);
                });

        CartItem cartItem = cartItemRepo.findByCartIdAndProductId(cart.getId(), productId)
                .orElse(new CartItem(cart, product, 0));
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItemRepo.save(cartItem);

        return getCart(userId);
    }

    public CartDTO getCart(Long userId) {
        Cart cart = cartRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found."));
        List<CartItemDTO> items = cart.getItems().stream()
                .map(item -> new CartItemDTO(
                        item.getId(),
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getProduct().getPrice(),
                        item.getQuantity()))
                .collect(Collectors.toList());
        Double totalPrice = items.stream()
                .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                .sum();
        return new CartDTO(cart.getId(), userId, items, totalPrice);
    }

    @Transactional
    public CartDTO updateCartItem(Long userId, Long productId, Integer quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }
        Cart cart = cartRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found."));
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found."));
        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock.");
        }

        CartItem cartItem = cartItemRepo.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new RuntimeException("Cart item not found."));
        cartItem.setQuantity(quantity);
        cartItemRepo.save(cartItem);

        return getCart(userId);
    }

    @Transactional
    public CartDTO removeFromCart(Long userId, Long productId) {
        Cart cart = cartRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found."));
        CartItem cartItem = cartItemRepo.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new RuntimeException("Cart item not found."));
        cartItemRepo.delete(cartItem);
        return getCart(userId);
    }

    @Transactional
    public void clearCart(Long userId) {
        Cart cart = cartRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found."));
        cartItemRepo.deleteAll(cart.getItems());
    }
}
