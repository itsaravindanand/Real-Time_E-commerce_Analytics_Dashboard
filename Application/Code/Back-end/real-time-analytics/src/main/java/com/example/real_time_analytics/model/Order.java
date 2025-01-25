package com.example.real_time_analytics.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

/**
 * Represents an Order in the e-commerce system.
 * This class is used as a MongoDB document and contains details about the order, such as items and total price.
 */
@Document(collection = "orders") // Maps this class to the "orders" collection in MongoDB
public class Order {

    @Id
    private String id; // Unique identifier for the order
    private List<OrderItem> items; // List of items in the order
    private double totalPrice; // Total price of all items in the order

    // Getters and Setters

    /**
     * Retrieves the unique ID of the order.
     *
     * @return The ID of the order.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique ID of the order.
     *
     * @param id The ID to set for the order.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retrieves the list of items in the order.
     *
     * @return A list of {@link OrderItem} objects.
     */
    public List<OrderItem> getItems() {
        return items;
    }

    /**
     * Sets the list of items for the order.
     *
     * @param items The list of {@link OrderItem} objects to set.
     */
    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    /**
     * Retrieves the total price of the order.
     *
     * @return The total price of the order.
     */
    public double getTotalPrice() {
        return totalPrice;
    }

    /**
     * Sets the total price of the order.
     *
     * @param totalPrice The total price to set for the order.
     */
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
