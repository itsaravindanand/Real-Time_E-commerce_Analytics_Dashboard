package com.example.real_time_analytics.service;

import com.example.real_time_analytics.model.Order;
import com.example.real_time_analytics.model.Product;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @Autowired
    private ElasticSearchService elasticSearchService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Listener for product-events
    @KafkaListener(topics = "product-events", groupId = "real_time_analytics_group", containerFactory = "kafkaListenerContainerFactory")
    public void consumeProductEvents(String message) {
        try {
            JsonNode rootNode = objectMapper.readTree(message);
            String action = rootNode.get("action").asText();
            Product product = objectMapper.treeToValue(rootNode.get("data"), Product.class);

            switch (action) {
                case "create":
                case "update":
                    elasticSearchService.indexProduct(product);
                    break;
                case "delete":
                    elasticSearchService.deleteProductById(product.getId().toString());
                    break;
                default:
                    System.err.println("Unknown action for product: " + action);
            }
        } catch (Exception e) {
            System.err.println("Failed to process product message: " + e.getMessage());
        }
    }

    // Listener for order-events
    @KafkaListener(topics = "order-events", groupId = "real_time_analytics_group", containerFactory = "kafkaListenerContainerFactory")
    public void consumeOrderEvents(String message) {
        try {
            JsonNode rootNode = objectMapper.readTree(message);
            String action = rootNode.get("action").asText();
            Order order = objectMapper.treeToValue(rootNode.get("data"), Order.class);

            switch (action) {
                case "create":
                case "update":
                    elasticSearchService.indexOrder(order);
                    break;
                case "delete":
                    elasticSearchService.deleteOrderById(order.getId().toString());
                    break;
                default:
                    System.err.println("Unknown action for order: " + action);
            }
        } catch (Exception e) {
            System.err.println("Failed to process order message: " + e.getMessage());
        }
    }
}
