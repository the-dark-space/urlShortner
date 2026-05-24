# Distributed URL Shortener

A scalable distributed URL shortener built using Spring Boot, Redis, Kafka, JWT Authentication, Docker, and asynchronous analytics pipelines.

The project focuses on backend scalability, event-driven architecture, caching optimization, and distributed systems concepts.

## Features

- URL shortening with custom aliases
- JWT Authentication & Authorization
- Redis-based redirect caching
- Kafka-based asynchronous analytics
- Batch analytics aggregation
- Daily analytics tracking
- AI-assisted malicious URL detection
- Rate limiting
- Dockerized infrastructure
- Swagger/OpenAPI documentation
- Multi-user URL ownership

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 21 | Backend Language |
| Spring Boot | REST APIs |
| Spring Security | Authentication |
| JWT | Stateless Authorization |
| Redis | Distributed Caching |
| Kafka | Event-Driven Analytics |
| MySQL | Persistent Storage |
| Docker | Containerized Infrastructure |
| Swagger/OpenAPI | API Documentation |

## Architecture

```text
Client
   |
   v
Spring Boot API
   |
   +--> Redis Cache
   |
   +--> Kafka Producer
              |
              v
          Kafka Topic
              |
              v
        Kafka Consumer
              |
              v
            MySQL
```

## Redirect Flow

1. User hits short URL
2. Redirect data fetched from Redis
3. On cache miss, fallback to MySQL
4. Redirect response returned immediately
5. Analytics event published to Kafka
6. Kafka consumer asynchronously updates analytics

## Scalability Concepts

- Redis cache-aside pattern for fast redirects
- Kafka-based asynchronous analytics processing
- Batch aggregation for reduced DB writes
- Event-driven architecture
- Stateless JWT authentication
- Distributed containerized setup using Docker

## Local Setup

### Clone Repository

```bash
git clone <your-repo-url>
```

### Run Using Docker

```bash
docker compose up --build
```

### Swagger UI

```text
http://localhost:8080/swagger-ui/index.html
```

## Future Improvements

- Kubernetes deployment
- Distributed locking
- Dead-letter queue handling
- Real-time analytics dashboard
- CI/CD pipelines
- Multi-region deployment

## Key Backend Engineering Concepts Demonstrated

- Distributed caching
- Event-driven architecture
- Asynchronous processing
- Batch aggregation pipelines
- JWT-based stateless authentication
- Authorization & ownership validation
- Dockerized distributed systems

