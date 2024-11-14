package com.example.real_time_analytics.repository;

import com.example.real_time_analytics.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {}