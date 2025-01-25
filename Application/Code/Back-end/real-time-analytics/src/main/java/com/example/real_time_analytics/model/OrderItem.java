package com.example.real_time_analytics.model;

/**
 * Represents an individual item in an order.
 * This class contains details about the product, such as its ID, name, quantity, and price.
 */
public class OrderItem {
    private String productId; // Unique identifier for the product
    private String productName; // Name of the product
    private int quantity; // Quantity of the product in the order
    private double price; // Total price for the quantity of this product in the order

    // Getters and Setters

    /**
     * Retrieves the unique identifier of the product.
     *
     * @return The product ID.
     */
    public String getProductId() {
        return productId;
    }

    /**
     * Sets the unique identifier of the product.
     *
     * @param productId The product ID to set.
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * Retrieves the name of the product.
     *
     * @return The product name.
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Sets the name of the product.
     *
     * @param productName The product name to set.
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * Retrieves the quantity of the product in the order.
     *
     * @return The quantity of the product.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the product in the order.
     *
     * @param quantity The quantity to set.
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Retrieves the total price for the quantity of this product in the order.
     *
     * @return The total price for this product.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the total price for the quantity of this product in the order.
     *
     * @param price The total price to set.
     */
    public void setPrice(double price) {
        this.price = price;
    }
}
