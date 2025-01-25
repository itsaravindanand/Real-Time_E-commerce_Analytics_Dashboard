package com.example.real_time_analytics.service;

import com.example.real_time_analytics.model.Product;
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
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product addProduct(Product product) {
        Product savedProduct = productRepository.save(product);
        publishToKafka(savedProduct, "create");
        return savedProduct;
    }

    public Product updateProduct(Product product) {
        Product updatedProduct = productRepository.save(product);
        publishToKafka(updatedProduct, "update");
        return updatedProduct;
    }

    public void deleteProduct(String id) {
        // Find and delete the product in MongoDB
        Optional<Product> product = productRepository.findById(id);
        product.ifPresent(value -> {
            productRepository.deleteById(id);
            // Publish deletion event to Kafka
            publishToKafka(value, "delete");
        });
    }

    private void publishToKafka(Product product, String action) {
        try {
            Map<String, Object> productMessage = new HashMap<>();
            productMessage.put("action", action);
            productMessage.put("data", product);
            kafkaProducerService.sendMessage("product-events", objectMapper.writeValueAsString(productMessage));
        } catch (JsonProcessingException e) {
            System.err.println("Failed to convert product to JSON: " + e.getMessage());
        }
    }
}
