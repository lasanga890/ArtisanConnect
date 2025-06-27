package com.example.demo.service;

import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderItemDTO;
import com.example.demo.model.Cart;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.model.Product;
import com.example.demo.model.userModel;
import com.example.demo.Repo.CartRepo;
import com.example.demo.Repo.OrderItemRepo;
import com.example.demo.Repo.OrderRepo;
import com.example.demo.Repo.ProductRepo;
import com.example.demo.Repo.userRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private OrderItemRepo orderItemRepo;

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private userRepo userRepo;

    @Autowired
    private CartService cartService;

    @Transactional
    public OrderDTO placeOrder(Long userId) {
        Cart cart = cartRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found."));
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty.");
        }

        userModel user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));

        double totalAmount = cart.getItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        Order order = new Order(user, totalAmount, "PENDING", LocalDateTime.now());
        order = orderRepo.save(order);

        for (var item : cart.getItems()) {
            Product product = item.getProduct();
            if (product.getStockQuantity() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }
            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepo.save(product);
            OrderItem orderItem = new OrderItem(order, product, item.getQuantity(), product.getPrice());
            orderItemRepo.save(orderItem);
            order.getItems().add(orderItem);
        }

        cartService.clearCart(userId);
        return getOrder(order.getId());
    }

    public OrderDTO getOrder(Long orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found."));
        List<OrderItemDTO> items = order.getItems().stream()
                .map(item -> new OrderItemDTO(
                        item.getId(),
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getUnitPrice()))
                .collect(Collectors.toList());
        return new OrderDTO(
                order.getId(),
                order.getUser().getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getCreatedAt(),
                items);
    }

    public List<OrderDTO> getUserOrders(Long userId) {
        return orderRepo.findByUserId(userId).stream()
                .map(order -> new OrderDTO(
                        order.getId(),
                        order.getUser().getId(),
                        order.getTotalAmount(),
                        order.getStatus(),
                        order.getCreatedAt(),
                        order.getItems().stream()
                                .map(item -> new OrderItemDTO(
                                        item.getId(),
                                        item.getProduct().getId(),
                                        item.getProduct().getName(),
                                        item.getQuantity(),
                                        item.getUnitPrice()))
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepo.findAll().stream()
                .map(order -> new OrderDTO(
                        order.getId(),
                        order.getUser().getId(),
                        order.getTotalAmount(),
                        order.getStatus(),
                        order.getCreatedAt(),
                        order.getItems().stream()
                                .map(item -> new OrderItemDTO(
                                        item.getId(),
                                        item.getProduct().getId(),
                                        item.getProduct().getName(),
                                        item.getQuantity(),
                                        item.getUnitPrice()))
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderDTO updateOrderStatus(Long orderId, String status) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found."));
        if (!List.of("PENDING", "COMPLETED", "CANCELLED").contains(status)) {
            throw new IllegalArgumentException("Invalid status.");
        }
        order.setStatus(status);
        orderRepo.save(order);
        return getOrder(orderId);
    }
}
