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

    @KafkaListener(topics = "order-events", groupId = "real_time_analytics_group", containerFactory = "kafkaListenerContainerFactory")
    public void consume(String message) {
        try {
            JsonNode rootNode = objectMapper.readTree(message);
            String type = rootNode.get("type").asText();

            if ("Order".equals(type)) {
                Order order = objectMapper.treeToValue(rootNode.get("data"), Order.class);
                elasticSearchService.indexOrder(order);
            } else if ("Product".equals(type)) {
                Product product = objectMapper.treeToValue(rootNode.get("data"), Product.class);
                elasticSearchService.indexProduct(product);
            } else {
                elasticSearchService.indexMessage(message); // Handle unknown message types as generic
            }
        } catch (Exception e) {
            System.err.println("Failed to process message: " + e.getMessage());
        }
    }
}
