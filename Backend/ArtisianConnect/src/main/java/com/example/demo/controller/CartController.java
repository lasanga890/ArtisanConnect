package com.example.demo.controller;

import com.example.demo.dto.CartDTO;
import com.example.demo.model.userModel;
import com.example.demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<CartDTO> addToCart(@RequestParam Long productId, @RequestParam Integer quantity, Authentication authentication) {
        Long userId = ((userModel) authentication.getPrincipal()).getId();
        try {
            return ResponseEntity.ok(cartService.addToCart(userId, productId, quantity));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new CartDTO(null, null, null, null));
        }
    }

    @GetMapping
    public ResponseEntity<CartDTO> getCart(Authentication authentication) {
        Long userId = ((userModel) authentication.getPrincipal()).getId();
        try {
            return ResponseEntity.ok(cartService.getCart(userId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new CartDTO(null, null, null, null));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<CartDTO> updateCartItem(@RequestParam Long productId, @RequestParam Integer quantity, Authentication authentication) {
        Long userId = ((userModel) authentication.getPrincipal()).getId();
        try {
            return ResponseEntity.ok(cartService.updateCartItem(userId, productId, quantity));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new CartDTO(null, null, null, null));
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<CartDTO> removeFromCart(@RequestParam Long productId, Authentication authentication) {
        Long userId = ((userModel) authentication.getPrincipal()).getId();
        try {
            return ResponseEntity.ok(cartService.removeFromCart(userId, productId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new CartDTO(null, null, null, null));
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(Authentication authentication) {
        Long userId = ((userModel) authentication.getPrincipal()).getId();
        try {
            cartService.clearCart(userId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
