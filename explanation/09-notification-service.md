# Notification Service

## 📌 Overview
The **Notification Service** acts as the central communication hub for the entire HRMS ecosystem. Whenever another microservice needs to alert a user (e.g., about a new message, an approved leave request, a performance review submission, or an OTP for login), they dispatch a request to this service.

By centralizing the notification logic here, the system avoids duplicating mail server configurations and formatting templates across multiple applications. It also allows notifications to be queued, retried, and monitored independently.

## 🏗️ Architecture & Flow

```mermaid
sequenceDiagram
    participant OS as Other Services (e.g., Leave Service)
    participant NS as Notification Service
    participant SMTP as Google SMTP
    participant U as User Inbox

    OS->>NS: Request to Send Email ("Leave Approved")
    NS->>NS: Formats Message Template
    NS->>SMTP: Connects Securely via TLS (Port 587)
    SMTP-->>U: Delivers Email
    
    rect rgb(200, 255, 200)
    Note over U: User opens "Leave Approved" email
    end
```

### 🔑 Key Responsibilities:
1. **Email Delivery**: Uses standard SMTP protocols to dispatch HTML and text emails.
2. **Template Management**: Stores standard branding elements and dynamic placeholders for varying message types.
3. **Decoupled Alerts**: Listens to requests from any microservice across the platform without needing direct point-to-point hardcoding.

## 💻 Technical Details

### Technologies & Dependencies
- **Spring Boot Mail Starter**: Abstraction over JavaMailSender.
- **JavaMail**: The underlying API for SMTP interactions.
- **MySQL Driver**: Optional use for storing logs of sent/failed messages.

### Configuration Highlights (`application.properties`)
```properties
spring.application.name=notification-service
server.port=8086

# Mail Server Configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=rkonda863@gmail.com
# Use an App Password, not a real password
spring.mail.password=zplsqhgqsbptleua 

# TLS Security Settings
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
```

## 🚀 How to Run
**Using Maven:**
```bash
mvn spring-boot:run
```

**Using Docker:**
```bash
docker run -p 8086:8086 notification-service:latest
```
