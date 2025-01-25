package com.example.real_time_analytics.controller;

import com.example.real_time_analytics.model.Order;
import com.example.real_time_analytics.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for handling Order-related operations.
 * Exposes endpoints for creating, retrieving, and listing orders.
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * Endpoint to create a new order.
     *
     * @param order The order object to be created, passed in the request body.
     * @return The created Order object.
     */
    @PostMapping("/create")
    public Order createOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }

    /**
     * Endpoint to retrieve an order by its ID.
     *
     * @param id The ID of the order to be retrieved, passed as a path variable.
     * @return The Order object corresponding to the given ID.
     * @throws RuntimeException if the order is not found.
     */
    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable String id) {
        return orderService.getOrderById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    /**
     * Endpoint to retrieve all orders.
     *
     * @return A list of all Order objects.
     */
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }
}
