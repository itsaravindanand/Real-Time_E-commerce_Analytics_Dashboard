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

    @Autowired
    private ElasticSearchService elasticSearchService;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public Product addProduct(Product product) {
        Product savedProduct = productRepository.save(product);
        publishToKafka(savedProduct);
        return savedProduct;
    }

    public Product updateProduct(Product product) {
        Product updatedProduct = productRepository.save(product);
        publishToKafka(updatedProduct);
        return updatedProduct;
    }

    private void publishToKafka(Product product) {
        try {
            Map<String, Object> productMessage = new HashMap<>();
            productMessage.put("type", "Product");
            productMessage.put("data", product);
            kafkaProducerService.sendMessage(objectMapper.writeValueAsString(productMessage));
        } catch (JsonProcessingException e) {
            System.err.println("Failed to convert product to JSON: " + e.getMessage());
        }
    }

    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public void deleteProduct(String id) {
        productRepository.deleteById(id);
        elasticSearchService.deleteProductById(id);
    }
}
