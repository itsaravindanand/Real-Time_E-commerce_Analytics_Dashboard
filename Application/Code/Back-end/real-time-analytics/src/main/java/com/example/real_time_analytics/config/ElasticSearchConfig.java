package com.example.real_time_analytics.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up Elasticsearch client beans.
 */
@Configuration
public class ElasticSearchConfig {

    /**
     * Creates a RestHighLevelClient bean for backward compatibility with older Elasticsearch Java APIs.
     *
     * @return RestHighLevelClient instance configured to connect to localhost on port 9200.
     */
    @Bean
    public RestHighLevelClient restHighLevelClient() {
        // Build the REST client with the specified host and port
        RestClientBuilder builder = RestClient.builder(new HttpHost("localhost", 9200, "http"));
        return new RestHighLevelClient(builder);
    }

    /**
     * Creates a low-level ElasticsearchClient bean using the modern Elasticsearch Java API.
     *
     * @param restClient The low-level REST client used for communication with Elasticsearch.
     * @return ElasticsearchClient instance configured with JSON-P mapper for request/response handling.
     */
    @Bean
    public ElasticsearchClient elasticsearchClient(RestClient restClient) {
        // Configure the transport layer with the RestClient and Jackson JSONP mapper
        RestClientTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }

    /**
     * Creates a low-level RestClient bean for raw communication with Elasticsearch.
     *
     * @return RestClient instance configured to connect to localhost on port 9200.
     */
    @Bean
    public RestClient restClient() {
        // Build the low-level REST client
        return RestClient.builder(new HttpHost("localhost", 9200)).build();
    }
}
