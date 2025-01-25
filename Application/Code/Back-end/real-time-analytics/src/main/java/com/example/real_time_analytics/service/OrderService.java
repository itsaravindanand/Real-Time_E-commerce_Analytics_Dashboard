package com.example.real_time_analytics.service;

import com.example.real_time_analytics.model.Order;
import com.example.real_time_analytics.model.OrderItem;
import com.example.real_time_analytics.model.Product;
import com.example.real_time_analytics.repository.OrderRepository;
import com.example.real_time_analytics.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Optional<Order> getOrderById(String id) {
        return orderRepository.findById(id);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order createOrder(Order order) {
        double totalPrice = 0;

        for (OrderItem item : order.getItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductId()));

            if (product.getStockQuantity() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }

            item.setProductName(product.getName());
            item.setPrice(product.getPrice() * item.getQuantity());
            totalPrice += item.getPrice();

            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.save(product);
        }

        order.setTotalPrice(totalPrice);
        Order savedOrder = orderRepository.save(order);

        // Publish the "create" action to Kafka
        publishToKafka(savedOrder, "create");

        return savedOrder;
    }

    public Order updateOrder(Order order) {
        double totalPrice = 0;

        for (OrderItem item : order.getItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductId()));

            item.setProductName(product.getName());
            item.setPrice(product.getPrice() * item.getQuantity());
            totalPrice += item.getPrice();
        }

        order.setTotalPrice(totalPrice);
        Order updatedOrder = orderRepository.save(order);

        // Publish the "update" action to Kafka
        publishToKafka(updatedOrder, "update");

        return updatedOrder;
    }

    public void deleteOrder(String id) {
        Optional<Order> order = orderRepository.findById(id);

        order.ifPresent(value -> {
            orderRepository.deleteById(id);

            // Publish the "delete" action to Kafka
            publishToKafka(value, "delete");
        });
    }

    private void publishToKafka(Order order, String action) {
        try {
            Map<String, Object> orderMessage = new HashMap<>();
            orderMessage.put("action", action);
            orderMessage.put("data", order);
            kafkaProducerService.sendMessage("order-events", objectMapper.writeValueAsString(orderMessage));
        } catch (JsonProcessingException e) {
            System.err.println("Failed to convert order to JSON: " + e.getMessage());
        }
    }
}
