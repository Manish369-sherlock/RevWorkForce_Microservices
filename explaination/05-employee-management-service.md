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


## 🛑 Deep Dive Component Codes & Project Structure
This section contains the full, exhaustive breakdown of the microservice's source code, project structure, and dependencies. It operates as the fundamental source of truth replacing isolated snippets with the actual working code.

### 🌳 Complete Project Tree
```text
employee-management-service/
├── .dockerignore
├── .gitattributes
├── .gitignore
├── Dockerfile
├── hs_err_pid1196.log
├── hs_err_pid21248.log
├── mvnw
├── mvnw.cmd
├── pom.xml
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── revworkforce
│   │   │           └── employeemanagementservice
│   │   │               ├── EmployeeManagementServiceApplication.java
│   │   │               ├── config
│   │   │               │   ├── DataSeeder.java
│   │   │               │   ├── GatewayHeaderAuthenticationFilter.java
│   │   │               │   ├── IpAccessControlFilter.java
│   │   │               │   ├── JwtUtil.java
│   │   │               │   ├── OllamaConfig.java
│   │   │               │   ├── SecurityBeansConfig.java
│   │   │               │   ├── SecurityConfig.java
│   │   │               │   ├── SwaggerConfig.java
│   │   │               │   ├── WebSocketAuthInterceptor.java
│   │   │               │   ├── WebSocketConfig.java
│   │   │               │   └── WebSocketEventListener.java
│   │   │               ├── controller
│   │   │               │   ├── AIController.java
│   │   │               │   ├── AdminAnnouncementController.java
│   │   │               │   ├── AdminDashboardController.java
│   │   │               │   ├── AdminExpenseController.java
│   │   │               │   ├── ChatController.java
│   │   │               │   ├── ChatWebSocketController.java
│   │   │               │   ├── EmployeeAnnouncementController.java
│   │   │               │   ├── ExpenseController.java
│   │   │               │   ├── ManagerExpenseController.java
│   │   │               │   └── ManagerTeamController.java
│   │   │               ├── dto
│   │   │               │   ├── AIChatRequest.java
│   │   │               │   ├── AIChatResponse.java
│   │   │               │   ├── AdjustLeaveBalanceRequest.java
│   │   │               │   ├── AnnouncementRequest.java
│   │   │               │   ├── ApiResponse.java
│   │   │               │   ├── AssignManagerRequest.java
│   │   │               │   ├── AttendanceResponse.java
│   │   │               │   ├── AttendanceSummaryResponse.java
│   │   │               │   ├── ChangePasswordRequest.java
│   │   │               │   ├── ChatMessageRequest.java
│   │   │               │   ├── ChatMessageResponse.java
│   │   │               │   ├── CheckInRequest.java
│   │   │               │   ├── CheckOutRequest.java
│   │   │               │   ├── ConversationResponse.java
│   │   │               │   ├── DashboardResponse.java
│   │   │               │   ├── DepartmentRequest.java
│   │   │               │   ├── DesignationRequest.java
│   │   │               │   ├── EmployeeDashboardResponse.java
│   │   │               │   ├── EmployeeDirectoryResponse.java
│   │   │               │   ├── EmployeeProfileResponse.java
│   │   │               │   ├── EmployeeReportResponse.java
│   │   │               │   ├── ExpenseActionRequest.java
│   │   │               │   ├── ExpenseRequest.java
│   │   │               │   ├── ForceResetPasswordRequest.java
│   │   │               │   ├── GoalProgressRequest.java
│   │   │               │   ├── GoalRequest.java
│   │   │               │   ├── HolidayRequest.java
│   │   │               │   ├── InvoiceParseResponse.java
│   │   │               │   ├── IpRangeRequest.java
│   │   │               │   ├── IpRangeResponse.java
│   │   │               │   ├── LeaveActionRequest.java
│   │   │               │   ├── LeaveAnalysisResponse.java
│   │   │               │   ├── LeaveApplyRequest.java
│   │   │               │   ├── LeaveReportResponse.java
│   │   │               │   ├── LeaveTypeRequest.java
│   │   │               │   ├── LoginRequest.java
│   │   │               │   ├── ManagerFeedbackRequest.java
│   │   │               │   ├── ManagerGoalCommentRequest.java
│   │   │               │   ├── OfficeLocationRequest.java
│   │   │               │   ├── OfficeLocationResponse.java
│   │   │               │   ├── PerformanceReportResponse.java
│   │   │               │   ├── PerformanceReviewRequest.java
│   │   │               │   ├── RefreshTokenRequest.java
│   │   │               │   ├── RegisterEmployeeRequest.java
│   │   │               │   ├── ResendOtpRequest.java
│   │   │               │   ├── TeamLeaveCalendarEntry.java
│   │   │               │   ├── TypingIndicator.java
│   │   │               │   ├── UpdateEmployeeRequest.java
│   │   │               │   ├── UpdateProfileRequest.java
│   │   │               │   └── VerifyOtpRequest.java
│   │   │               ├── exception
│   │   │               │   ├── AccessDeniedException.java
│   │   │               │   ├── AccountDeactivatedException.java
│   │   │               │   ├── BadRequestException.java
│   │   │               │   ├── DuplicateResourceException.java
│   │   │               │   ├── GlobalExceptionHandler.java
│   │   │               │   ├── InsufficientBalanceException.java
│   │   │               │   ├── InvalidActionException.java
│   │   │               │   ├── IpBlockedException.java
│   │   │               │   ├── ResourceNotFoundException.java
│   │   │               │   └── UnauthorizedException.java
│   │   │               ├── integration
│   │   │               │   └── OllamaClient.java
│   │   │               ├── model
│   │   │               │   ├── ActivityLog.java
│   │   │               │   ├── AllowedIpRange.java
│   │   │               │   ├── Announcement.java
│   │   │               │   ├── Attendance.java
│   │   │               │   ├── ChatConversation.java
│   │   │               │   ├── ChatMessage.java
│   │   │               │   ├── Department.java
│   │   │               │   ├── Designation.java
│   │   │               │   ├── Employee.java
│   │   │               │   ├── Expense.java
│   │   │               │   ├── ExpenseItem.java
│   │   │               │   ├── Goal.java
│   │   │               │   ├── Holiday.java
│   │   │               │   ├── LeaveApplication.java
│   │   │               │   ├── LeaveBalance.java
│   │   │               │   ├── LeaveType.java
│   │   │               │   ├── Notification.java
│   │   │               │   ├── OfficeLocation.java
│   │   │               │   ├── PerformanceReview.java
│   │   │               │   └── enums
│   │   │               │       ├── AttendanceStatus.java
│   │   │               │       ├── ExpenseCategory.java
│   │   │               │       ├── ExpenseStatus.java
│   │   │               │       ├── Gender.java
│   │   │               │       ├── GoalPriority.java
│   │   │               │       ├── GoalStatus.java
│   │   │               │       ├── LeaveStatus.java
│   │   │               │       ├── MessageType.java
│   │   │               │       ├── NotificationType.java
│   │   │               │       ├── ReviewStatus.java
│   │   │               │       └── Role.java
│   │   │               ├── repository
│   │   │               │   ├── ActivityLogRepository.java
│   │   │               │   ├── AllowedIpRangeRepository.java
│   │   │               │   ├── AnnouncementRepository.java
│   │   │               │   ├── AttendanceRepository.java
│   │   │               │   ├── ChatConversationRepository.java
│   │   │               │   ├── ChatMessageRepository.java
│   │   │               │   ├── DepartmentRepository.java
│   │   │               │   ├── DesignationRepository.java
│   │   │               │   ├── EmployeeRepository.java
│   │   │               │   ├── ExpenseRepository.java
│   │   │               │   ├── GoalRepository.java
│   │   │               │   ├── HolidayRepository.java
│   │   │               │   ├── LeaveApplicationRepository.java
│   │   │               │   ├── LeaveBalanceRepository.java
│   │   │               │   ├── LeaveTypeRepository.java
│   │   │               │   ├── NotificationRepository.java
│   │   │               │   ├── OfficeLocationRepository.java
│   │   │               │   └── PerformanceReviewRepository.java
│   │   │               ├── service
│   │   │               │   ├── AIService.java
│   │   │               │   ├── AnnouncementService.java
│   │   │               │   ├── AttendanceService.java
│   │   │               │   ├── ChatService.java
│   │   │               │   ├── DashboardService.java
│   │   │               │   ├── DepartmentService.java
│   │   │               │   ├── DesignationService.java
│   │   │               │   ├── EmailService.java
│   │   │               │   ├── EmployeeService.java
│   │   │               │   ├── ExpenseService.java
│   │   │               │   ├── GeoAttendanceService.java
│   │   │               │   ├── InvoiceParserService.java
│   │   │               │   ├── IpAccessControlService.java
│   │   │               │   ├── LeaveAnalysisService.java
│   │   │               │   ├── LeaveService.java
│   │   │               │   ├── NotificationService.java
│   │   │               │   ├── OfficeLocationService.java
│   │   │               │   ├── PerformanceService.java
│   │   │               │   ├── PresenceService.java
│   │   │               │   └── WebSocketNotificationService.java
│   │   │               └── util
│   │   │                   └── NetworkIpUtil.java
│   │   └── resources
│   │       └── application.properties
│   └── test
│       └── java
│           └── com
│               └── revworkforce
│                   └── employeemanagementservice
│                       └── EmployeeManagementServiceApplicationTests.java
└── uploads
    └── expense-receipts
        └── expense-9-1773772477820.pdf
```

### 📦 Dependencies (`pom.xml`)
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>4.0.3</version>
        <relativePath/>
    </parent>
    <groupId>com.revworkforce</groupId>
    <artifactId>employee-management-service</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>employee-management-service</name>
    <description>Onboarding/offboarding, announcements, expenses, chat, AI</description>
    <properties>
        <java.version>17</java.version>
        <spring-cloud.version>2025.1.0</spring-cloud.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webmvc</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-websocket</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-mail</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-config</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>

        <dependency>
            <groupId>org.apache.pdfbox</groupId>
            <artifactId>pdfbox</artifactId>
            <version>3.0.4</version>
        </dependency>

        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>2.8.4</version>
        </dependency>

        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
            <version>0.12.6</version>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
            <version>0.12.6</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
            <version>0.12.6</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <annotationProcessorPaths>
                        <path>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </path>
                    </annotationProcessorPaths>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>

```

### ⚙️ Configurations (`src/main/resources`)
**`application.properties`**
```properties
spring.application.name=employee-management-service
spring.config.import=optional:configserver:http://localhost:8888
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
eureka.instance.hostname=localhost
eureka.instance.prefer-ip-address=false
eureka.instance.instance-id=${spring.application.name}:${server.port}
server.port=8084

spring.datasource.url=jdbc:mysql://localhost:3306/workforce?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=1234
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

ollama.base-url=http://localhost:11434
ollama.model=phi3
ollama.vision-model=llava
ollama.timeout=120000

server.tomcat.max-http-form-content-size=10MB
server.tomcat.max-swallow-size=10MB
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html

jwt.secret=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
jwt.expiration=86400000
jwt.refresh-expiration=604800000

```
