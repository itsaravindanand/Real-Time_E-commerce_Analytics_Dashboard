package com.example.real_time_analytics.repository;

import com.example.real_time_analytics.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {}
