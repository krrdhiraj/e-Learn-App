# 📚 E-Learning App with Microservices Architecture

A scalable, distributed e-learning platform developed using **Microservices Architecture** with Java Spring Boot.This application showcases advanced backend engineering patterns like service discovery, centralized config, event-driven communication, secure API gateways, and multi-database integration.

---

## 🚀 Key Features

- 🧑‍💻 **User Management** – Registration, login, and profile management with Keycloak
- 📚 **Course Service** – Create, view, and manage courses by price and content
- 🎥 **Video Service** – Upload, stream, and process course videos
- 🗂️ **Category Service** – Organize courses by domain (e.g., Frontend, Backend, DevOps)
- 💳 **Order & Payment Service** – Buy courses via Razorpay integration
- 🛡️ **Secure Gateway** – API Gateway with JWT authentication
- ⚙️ **Central Config & Discovery** – Spring Cloud Config + Eureka Discovery
- 📢 **Async Messaging** – RabbitMQ and Kafka for inter-service communication

---

## 🛠️ Tech Stack

| Category             | Technology                                 |
|----------------------|---------------------------------------------|
| Language             | Java 21                                     |
| Framework            | Spring Boot, Spring Cloud                   |
| Communication        | REST, Kafka, RabbitMQ                       |
| Databases            | MySQL, PostgreSQL, MongoDB                  |
| Streaming/Upload     | Video streaming, Multipart Upload           |
| Containerization     | Docker, Docker Compose                      |
| API Gateway          | Spring Cloud Gateway                        |
| Service Discovery    | Eureka                                      |
| Configuration Server | Spring Cloud Config                         |
| Security/Auth        | Keycloak (Authorization Server), JWT, Spring Security |
| Payment Gateway      | Razorpay API                                |

---

## 🧱 Microservices Overview

| Service                 | Description                                               | Database       |
|--------------------------|-----------------------------------------------------------|----------------|
| `course-service`         | Handles course data with pricing and content              | MySQL          |
| `video-service`          | Manages video uploads, streaming, and processing          | MongoDB        |
| `category-service`       | Groups courses into categories like Frontend, Backend     | MongoDB        |
| `payment-service`        | Processes payments via Razorpay                           | MySQL          |
| `order-service`          | Manages course purchase and order lifecycle               | MySQL          |
| `notification-service`   | Sends emails/alerts using RabbitMQ                        | -              |       |
| `gateway-service`        | API Gateway with JWT Authentication                       | -              |
| `config-server`          | Centralized config management                             | -              |
| `discovery-server`       | Eureka-based service discovery                            | -              |
| `auth-server`            | Keycloak-based authorization and identity provider        | -              |

---

## 📁 Folder Structure

```
e-learning-app/
│
├── course-service/
├── video-service/
├── category-service/
├── payment-service/
├── order-service/
├── notification-service/
├── gateway-service/
├── config-server/
├── discovery-server/
├── auth-server/ (Keycloak setup)
├── docker-compose.yml
└── README.md
```

---

## 🐳 Run with Docker

### Prerequisites

- Docker + Docker Compose
- Java 21
- Maven

### Steps

```bash
docker-compose up --build
```

> Ensure ports used by RabbitMQ (5672), Kafka (9092), Keycloak (8080), etc., are free.

---

## 🔐 Authentication & Authorization

- 🔑 **Keycloak**: Central authorization server (OpenID Connect/OAuth2)
- 🔐 **JWT Auth**: Issued by Keycloak and validated by API Gateway
- 🔐 **Role-based Access**: Admin, User, Student roles supported

---

## 💳 Payment Integration

- **Razorpay** integrated in the `payment-service` for secure transactions.
- `order-service` tracks course purchase history, status, and delivery.

---

## 📬 Event-Driven Communication

- **Kafka**: Used for user activity streams and analytics
- **RabbitMQ**: Used for system notifications and email messages

---

## ✅ Testing

```bash
./mvnw test
```

Tests included per service using:
- Spring Boot Test
---

## 📧 Contact

Got questions or feedback?

- Email: k4.dhiraj4@gmail.com
- Issues: [Open here](https://github.com/e-Learn-App/issues)
