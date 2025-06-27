package com.example.demo.controller;

import com.example.demo.dto.OrderDTO;
import com.example.demo.model.userModel;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/user/order")
    public ResponseEntity<OrderDTO> placeOrder(Authentication authentication) {
        Long userId = ((userModel) authentication.getPrincipal()).getId();
        try {
            return ResponseEntity.ok(orderService.placeOrder(userId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new OrderDTO(null, null, null, null, null, null));
        }
    }

    @GetMapping("/user/orders")
    public ResponseEntity<List<OrderDTO>> getUserOrders(Authentication authentication) {
        Long userId = ((userModel) authentication.getPrincipal()).getId();
        return ResponseEntity.ok(orderService.getUserOrders(userId));
    }

    @GetMapping("/user/order/{id}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long id, Authentication authentication) {
        Long userId = ((userModel) authentication.getPrincipal()).getId();
        try {
            OrderDTO order = orderService.getOrder(id);
            if (!order.getUserId().equals(userId)) {
                return ResponseEntity.status(403).body(new OrderDTO(null, null, null, null, null, null));
            }
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new OrderDTO(null, null, null, null, null, null));
        }
    }

    @GetMapping("/admin/orders")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PutMapping("/admin/order/{id}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new OrderDTO(null, null, null, null, null, null));
        }
    }
}
