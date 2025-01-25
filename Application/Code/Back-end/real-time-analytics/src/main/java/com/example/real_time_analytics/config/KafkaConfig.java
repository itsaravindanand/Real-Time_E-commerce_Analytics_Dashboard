package com.example.real_time_analytics.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka configuration class to set up producers, consumers, topics, and listener factories.
 */
@Configuration
public class KafkaConfig {

    /**
     * Defines the Kafka topic for order events.
     *
     * @return A NewTopic object representing the "order-events" topic with 3 partitions and 1 replica.
     */
    @Bean
    public NewTopic orderEventsTopic() {
        return TopicBuilder.name("order-events")
                .partitions(3) // Number of partitions for parallelism
                .replicas(1)   // Number of replicas for fault tolerance
                .build();
    }

    /**
     * Defines the Kafka topic for product events.
     *
     * @return A NewTopic object representing the "product-events" topic with 3 partitions and 1 replica.
     */
    @Bean
    public NewTopic productEventsTopic() {
        return TopicBuilder.name("product-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    /**
     * Configures the producer factory to produce messages to Kafka.
     *
     * @return A ProducerFactory configured with necessary producer properties.
     */
    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // Kafka broker address
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class); // Serialize keys as strings
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class); // Serialize values as strings
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    /**
     * Creates a KafkaTemplate to send messages to Kafka topics.
     *
     * @return A KafkaTemplate configured to use the producer factory.
     */
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    /**
     * Configures the consumer factory to consume messages from Kafka.
     *
     * @return A ConsumerFactory configured with necessary consumer properties.
     */
    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // Kafka broker address
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "real_time_analytics_group"); // Consumer group ID
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class); // Deserialize keys as strings
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class); // Deserialize values as strings
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // Start from the earliest message if no offset is present
        return new DefaultKafkaConsumerFactory<>(props);
    }

    /**
     * Creates a listener container factory for Kafka consumers.
     *
     * @return A ConcurrentKafkaListenerContainerFactory for managing Kafka listeners.
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
