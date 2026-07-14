# JSE Chat Platform - Java Backend

A Spring Boot 3.5.8 microservice providing real-time WebSocket-based chat functionality with REST API fallback, built with Java 21 and modern Spring technologies.

## Features

- **Real-time WebSocket Chat**: STOMP protocol over SockJS for instant messaging
- **REST API Fallback**: HTTP endpoints for clients without WebSocket support
- **Message Queue**: Producer-consumer pattern for reliable message delivery
- **Virtual Threads**: Java 21 virtual threads for efficient concurrent connections
- **User Management**: Session-based user authentication and tracking
- **OpenAPI/Swagger UI**: Auto-generated API documentation
- **Spring DevTools**: Hot reload support for development
- **Comprehensive Validation**: Message and user validation with custom exceptions

## Prerequisites

- **Java**: JDK 21 or higher
- **Maven**: v3.8.0 or higher
- **Spring Boot**: v3.5.8 (handled via Maven)
- **SockJS**: WebSocket emulation library (included)

## Installation

### 1. Navigate to Backend Directory

```bash
cd jse-chat-platform/back-end
```

### 2. Build the Project

```bash
mvn clean install
```

Or skip tests during build:

```bash
mvn clean install -DskipTests
```

## Running the Application

### Start the Server

```bash
mvn spring-boot:run
```

The server will start on **`http://localhost:8090/chat`**

### Verify Server is Running

Open your browser and navigate to:
- **API Docs**: `http://localhost:8090/chat/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8090/chat/v3/api-docs`

### Environment Configuration

Server listens on port `8090` (configurable in `application.yml`). Ensure this port is available or update the configuration.

## Project Structure

```
back-end/
├── src/
│   ├── main/
│   │   ├── java/za/co/jse/
│   │   │   ├── ChatApp.java                    # Spring Boot entry point
│   │   │   ├── configuration/
│   │   │   │   ├── DefaultChatConfig.java      # Chat room initialization
│   │   │   │   └── WebSocketConfig.java        # WebSocket STOMP configuration
│   │   │   ├── controllers/
│   │   │   │   ├── ChatController.java         # WebSocket @MessageMapping handlers
│   │   │   │   ├── MessageController.java      # REST API endpoints
│   │   │   │   └── BaseController.java         # Base controller with common logic
│   │   │   ├── entities/
│   │   │   │   ├── ChatMessage.java            # Message JPA entity
│   │   │   │   ├── ChatRoom.java               # Chat room container
│   │   │   │   ├── ChatUser.java               # User entity
│   │   │   │   └── dtos/                       # Data transfer objects
│   │   │   │       ├── MessageDto.java         # Message DTO for requests
│   │   │   │       └── MessageRespDto.java     # Message response DTO
│   │   │   ├── exceptions/
│   │   │   │   ├── InvalidMessageException.java
│   │   │   │   ├── UserNotFoundException.java
│   │   │   │   └── GlobalExceptionHandler.java # Centralized exception handling
│   │   │   ├── queue/
│   │   │   │   ├── ChatQueue.java              # Thread-safe message queue
│   │   │   │   ├── MessageProducer.java        # Queue producer
│   │   │   │   └── MessageConsumer.java        # Queue consumer with virtual threads
│   │   │   └── services/
│   │   │       ├── IMessageService.java        # Service interface
│   │   │       ├── MessageServiceImpl.java      # Message service implementation
│   │   │       └── UserService.java            # User management service
│   │   └── resources/
│   │       └── application.yml                 # Application configuration
│   └── test/
│       └── java/za/co/jse/
│           ├── ApplicantApplicationTests.java
│           └── services/
│               └── MessageServiceImplTest.java
├── pom.xml                                     # Maven configuration
└── README.md
```

## 🏗️ Architecture Overview

### WebSocket Flow

```
Client                                    Server
  │                                         │
  ├─ Connect to /ws-chat ─────────────────>│
  │                                         ├─ Create STOMP subscription
  │                                         ├─ Listen on /topic/chat/messages
  │
  ├─ Send message to /app/send ──────────>│
  │                                         ├─ ChatController.handleChatMessage()
  │                                         ├─ Validate message
  │                                         ├─ MessageProducer.send() → Queue
  │                                         │
  │                                         ├─ MessageConsumer (virtual thread)
  │                                         ├─ Dequeue message
  │                                         ├─ Broadcast to /topic/chat/messages
  │                                         │
  │<───────────────────── Message Broadcast ─┤
  │                                         │
```

### Message Queue Pattern

- **Producer** (`MessageProducer`): Receives message from controller, enqueues
- **Consumer** (`MessageConsumer`): Runs on virtual thread, dequeues and broadcasts
- **Queue** (`ChatQueue`): Thread-safe blocking queue for reliable delivery

## API Endpoints

### REST Endpoints

#### GET - Join Chat Room / Get User

```
GET /chat/api/v1?username={username}
```

**Response**:
```json
{
  "username": "john_doe",
  "userId": "123"
}
```

#### POST - Send Message

```
POST /chat/api/v1/send
Content-Type: application/json

{
  "username": "john_doe",
  "text": "Hello everyone!"
}
```

**Response**:
```json
{
  "username": "john_doe",
  "text": "Hello everyone!",
  "timestamp": "2026-07-13T18:35:38.269288"
}
```

### WebSocket Endpoints

#### Subscribe to Messages

```
SUBSCRIBE /topic/chat/messages
```

Messages are broadcast in this format:
```json
{
  "username": "john_doe",
  "text": "Hello everyone!",
  "timestamp": "2026-07-13T18:35:38.269288"
}
```

#### Send Message via WebSocket

```
SEND /app/send
Content-Type: application/json

{
  "username": "john_doe",
  "text": "Hello everyone!"
}
```

#### Subscribe to Notifications

```
SUBSCRIBE /topic/chat/notifications
```

## Testing

### Run All Tests

```bash
mvn test
```

### Run Specific Test Method

```bash
mvn test -Dtest=MessageServiceImplTest#should_send_message_if_user_already_exists
```

### Skip Tests During Build

```bash
mvn clean install -DskipTests
```
## Application.yml Configuration

### (`src/main/resources/application.yml`)

```yaml
server:
  port: 8090
  servlet:
    context-path: /chat

spring:
  application:
    name: back-end
  devtools:
    restart:
      enabled: true

springdoc:
  swagger-ui:
    path: /swagger-ui.html
```
### MessageProducer

Accepts validated messages and places them on the queue:
- Thread-safe blocking queue operations
- Prevents duplicate processing

### MessageConsumer

Runs on dedicated virtual threads:
- Dequeues messages continuously
- Broadcasts to all WebSocket subscribers
- Single point of broadcast (prevents duplicates)

### Message Validation

In `MessageServiceImpl.validateMessage()`:
1. Username cannot be null
2. Message text cannot be null
3. User must exist in ChatRoom
4. Throws `InvalidMessageException` or `UserNotFoundException` on validation failure

## Known Issues & Fixes

### Duplicate Messages
- **Cause**: WebSocket controller was broadcasting messages AND queue consumer was broadcasting
- **Fix**: Removed `@SendTo` annotation from controller; queue consumer is now the sole broadcaster
- **Result**: Each message appears exactly once

### User Validation
- **Status**: Working correctly
- **Validation**: Users must join before sending messages
- **Test**: `should_throw_UserNotFoundException_if_user_does_not_exist` passes



Benefits:
- Lightweight concurrent connections
- No context switching overhead
- Scales to thousands of concurrent connections

### CORS Configuration

Currently allows all origins (`setAllowedOrigins("*")`). For production:

```java
.setAllowedOrigins("https://your-frontend-domain.com")
```
### Enable Debug Logging

Add to `application.yml`:

```yaml
logging:
  level:
    za.co.jse: DEBUG
    org.springframework.messaging: DEBUG
```

