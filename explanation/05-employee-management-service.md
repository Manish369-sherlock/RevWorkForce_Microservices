# Employee Management Service

## 📌 Overview
The **Employee Management Service** is a core operational backend service for handling the lifecycle and details of employees within the HRMS. It is responsible for employee onboarding, maintaining records, handling expense management via AI, and keeping track of general HR datasets.

A standout feature is its integration with an external local AI server (Ollama) which runs language (phi3) and vision (llava) models to process document/expense submissions automatically.

## 🏗️ Architecture & Flow

```mermaid
graph TD
    AG[API Gateway] -->|Routes Request| EMS[Employee Management Service]
    EMS -->|Stores Employee Data| DB[(MySQL - workforce)]
    
    subgraph AI Processing
        EMS -->|Uploads Image/Doc| Ollama[Ollama Server :11434]
    end
    
    Ollama -->|Vision Model: llava| EMS: Extracts Text/Receipt details
    Ollama -->|NLP Model: phi3| EMS: Generates summary/analysis
    
    EMS -->|Updates| DB
```

### 🔑 Key Responsibilities:
1. **Employee Operations**: Handles Create, Read, Update, Delete (CRUD) operations for employee profiles, demographics, and statuses.
2. **AI Integration**: Communicates with `Ollama` for running local large language models and vision models to process HR receipts and documents automatically.
3. **Expense Tracking**: Works with the internal database to track and manage submitted expense reports.
4. **File Handling**: Allows large multipart uploads (up to 10MB) for documents, resumes, and receipts.

## 💻 Technical Details

### Technologies & Dependencies
- **Spring Data JPA & Hibernate**: For ORM mapping to the `workforce` database.
- **MySQL Driver**: Stores employee records, expenses, and AI-processed records.
- **Ollama AI Integration**: Directly communicates via REST to an endpoint running an LLM.
- **Spring Boot Web (Multipart)**: Optimized for handling file payloads up to 10MB.

### Configuration Highlights (`application.properties`)
```properties
spring.application.name=employee-management-service
server.port=8084

# DB Properties
spring.datasource.url=jdbc:mysql://localhost:3306/workforce?createDatabaseIfNotExist=true
spring.jpa.hibernate.ddl-auto=update

# Ollama AI Properties
ollama.base-url=http://localhost:11434
ollama.model=phi3
ollama.vision-model=llava
ollama.timeout=120000

# File Upload Sizes
server.tomcat.max-http-form-content-size=10MB
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# API Documentation
springdoc.api-docs.path=/v3/api-docs
```

## 🚀 How to Run
**Prerequisite:** Ensure Ollama is running locally with the `phi3` and `llava` models pulled.
```bash
ollama run phi3
ollama run llava
```

**Using Maven:**
```bash
mvn spring-boot:run
```

**Using Docker:**
```bash
docker run -p 8084:8084 employee-management-service:latest
```
