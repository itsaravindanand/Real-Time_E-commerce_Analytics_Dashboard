package com.example.real_time_analytics.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.example.real_time_analytics.model.Product;
import com.example.real_time_analytics.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ElasticSearchService {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    // Index a Product document in Elasticsearch
    public void indexProduct(Product product) {
        Map<String, Object> data = new HashMap<>();
        data.put("name", product.getName());
        data.put("description", product.getDescription());
        data.put("price", product.getPrice());
        data.put("stockQuantity", product.getStockQuantity());
        data.put("category", product.getCategory());
        data.put("tags", product.getTags());
        data.put("imageUrl", product.getImageUrl());
        data.put("timestamp", System.currentTimeMillis());

        indexDocument("products-index", data, product.getId().toString());
    }

    // Delete a Product document in Elasticsearch
    public void deleteProductById(String productId) {
        try {
            elasticsearchClient.delete(d -> d
                    .index("products-index")
                    .id(productId)
            );
            System.out.println("Product document with ID " + productId + " deleted from Elasticsearch.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error deleting product document from Elasticsearch: " + e.getMessage());
        }
    }

    // Index an Order document in Elasticsearch
    public void indexOrder(Order order) {
        Map<String, Object> data = new HashMap<>();
        data.put("items", order.getItems());
        data.put("totalPrice", order.getTotalPrice());
        data.put("timestamp", System.currentTimeMillis());

        indexDocument("orders-index", data, order.getId().toString());
    }

    // Delete an Order document in Elasticsearch
    public void deleteOrderById(String orderId) {
        try {
            elasticsearchClient.delete(d -> d
                    .index("orders-index")
                    .id(orderId)
            );
            System.out.println("Order document with ID " + orderId + " deleted from Elasticsearch.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error deleting order document from Elasticsearch: " + e.getMessage());
        }
    }

    // Helper method to index documents in Elasticsearch
    private void indexDocument(String indexName, Map<String, Object> data, String id) {
        try {
            IndexRequest<Map<String, Object>> request = IndexRequest.of(i -> i
                    .index(indexName)
                    .id(id)
                    .document(data)
            );
            IndexResponse response = elasticsearchClient.index(request);
            System.out.println("Document indexed with ID: " + response.id());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error indexing document: " + e.getMessage());
        }
    }
}
