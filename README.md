# Real-Time E-commerce Analytics Dashboard

A comprehensive real-time analytics platform built for e-commerce data. It uses **Spring Boot** for backend APIs, **MongoDB** for data storage, **Kafka** for event streaming, **Elasticsearch** for indexing, and **Kibana** for visualizing analytics.

## Prerequisites

- **Docker** installed on your system.
- **Java 17** or later installed for running the Spring Boot application.
- An **API testing tool** such as Postman, Insomnia, or cURL.

---

## Installation and Setup

### 1. Clone the Repository

Download the project files from GitHub:

```bash
git clone https://github.com/your-username/Real-Time_E-commerce_Analytics_Dashboard.git
cd Real-Time_E-commerce_Analytics_Dashboard
```

### 2. Run Docker Containers

Run the required services using the following Docker commands:

#### **MongoDB**

```bash
docker run -d --name mongodb -p 27017:27017 mongo
```

#### **Zookeeper**

```bash
docker run -d --name zookeeper -p 2181:2181 zookeeper:3.7
```

#### **Kafka**

```bash
docker run -d --name kafka -p 9092:9092 \
  -e KAFKA_BROKER_ID=1 \
  -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 \
  -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 \
  -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 \
  -e KAFKA_MESSAGE_MAX_BYTES=1500000000 \
  -e KAFKA_REPLICA_FETCH_MAX_BYTES=1500000000 \
  -e KAFKA_SOCKET_REQUEST_MAX_BYTES=1500000000 \
  --network="bridge" \
  --link zookeeper:zookeeper \
  bitnami/kafka:latest
```

#### **Elasticsearch**

```bash
docker run -d --name elasticsearch -p 9200:9200 \
  -e "discovery.type=single-node" \
  -e "xpack.security.enabled=false" \
  -v esdata:/usr/share/elasticsearch/data \
  docker.elastic.co/elasticsearch/elasticsearch:8.9.0
```

#### **Kibana**

```bash
docker run -d --name kibana -p 5601:5601 \
  -e ELASTICSEARCH_HOSTS=http://localhost:9200 \
  docker.elastic.co/kibana/kibana:8.9.0
```

### 3. Update `application.properties` (Optional)

If required, update the `src/main/resources/application.properties` file to point to the correct database or service configurations:

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/real_time_analytics
```

---

## Running the Application

1. Build and run the Spring Boot application:

   ```bash
   ./mvnw spring-boot:run
   ```

2. Use any API testing tool to perform API calls for managing products, orders, and viewing analytics. The base URL is:
   ```
   http://localhost:8080
   ```

---

## Access Kibana for Analytics

After starting Kibana, open the browser and go to:

```
http://localhost:5601
```

Here, you can visualize product trends, order patterns, and inventory levels using the pre-configured dashboards.

Feel free to fork this repository or contribute improvements. Happy coding!
