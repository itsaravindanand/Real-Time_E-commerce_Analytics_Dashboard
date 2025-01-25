package com.example.real_time_analytics.controller;

import com.example.real_time_analytics.model.Product;
import com.example.real_time_analytics.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for handling Product-related operations.
 * Exposes endpoints for creating, retrieving, updating, and deleting products.
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * Endpoint to create a new product.
     *
     * @param product The product object to be created, passed in the request body.
     * @return The created Product object.
     */
    @PostMapping("/create")
    public Product createProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    /**
     * Endpoint to retrieve a product by its ID.
     *
     * @param id The ID of the product to be retrieved, passed as a path variable.
     * @return The Product object corresponding to the given ID.
     * @throws RuntimeException if the product is not found.
     */
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable String id) {
        return productService.getProductById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    /**
     * Endpoint to retrieve all products.
     *
     * @return A list of all Product objects in the database.
     */
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    /**
     * Endpoint to update an existing product.
     *
     * @param product The updated Product object, passed in the request body.
     * @return The updated Product object.
     */
    @PutMapping("/update")
    public Product updateProduct(@RequestBody Product product) {
        return productService.updateProduct(product);
    }

    /**
     * Endpoint to delete a product by its ID.
     *
     * @param id The ID of the product to be deleted, passed as a path variable.
     */
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
    }
}
