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
📦 employee-management-service
    📜 .dockerignore
    📜 .gitattributes
    📜 .gitignore
    📜 Dockerfile
    📜 hs_err_pid1196.log
    📜 hs_err_pid21248.log
    📜 mvnw
    📜 mvnw.cmd
    📜 pom.xml
    📂 src
        📂 main
            📂 java
                📂 com
                    📂 revworkforce
                        📂 employeemanagementservice
                            📜 EmployeeManagementServiceApplication.java
                            📂 config
                                📜 DataSeeder.java
                                📜 GatewayHeaderAuthenticationFilter.java
                                📜 IpAccessControlFilter.java
                                📜 JwtUtil.java
                                📜 OllamaConfig.java
                                📜 SecurityBeansConfig.java
                                📜 SecurityConfig.java
                                📜 SwaggerConfig.java
                                📜 WebSocketAuthInterceptor.java
                                📜 WebSocketConfig.java
                                📜 WebSocketEventListener.java
                            📂 controller
                                📜 AdminAnnouncementController.java
                                📜 AdminDashboardController.java
                                📜 AdminExpenseController.java
                                📜 AIController.java
                                📜 ChatController.java
                                📜 ChatWebSocketController.java
                                📜 EmployeeAnnouncementController.java
                                📜 ExpenseController.java
                                📜 ManagerExpenseController.java
                                📜 ManagerTeamController.java
                            📂 dto
                                📜 AdjustLeaveBalanceRequest.java
                                📜 AIChatRequest.java
                                📜 AIChatResponse.java
                                📜 AnnouncementRequest.java
                                📜 ApiResponse.java
                                📜 AssignManagerRequest.java
                                📜 AttendanceResponse.java
                                📜 AttendanceSummaryResponse.java
                                📜 ChangePasswordRequest.java
                                📜 ChatMessageRequest.java
                                📜 ChatMessageResponse.java
                                📜 CheckInRequest.java
                                📜 CheckOutRequest.java
                                📜 ConversationResponse.java
                                📜 DashboardResponse.java
                                📜 DepartmentRequest.java
                                📜 DesignationRequest.java
                                📜 EmployeeDashboardResponse.java
                                📜 EmployeeDirectoryResponse.java
                                📜 EmployeeProfileResponse.java
                                📜 EmployeeReportResponse.java
                                📜 ExpenseActionRequest.java
                                📜 ExpenseRequest.java
                                📜 ForceResetPasswordRequest.java
                                📜 GoalProgressRequest.java
                                📜 GoalRequest.java
                                📜 HolidayRequest.java
                                📜 InvoiceParseResponse.java
                                📜 IpRangeRequest.java
                                📜 IpRangeResponse.java
                                📜 LeaveActionRequest.java
                                📜 LeaveAnalysisResponse.java
                                📜 LeaveApplyRequest.java
                                📜 LeaveReportResponse.java
                                📜 LeaveTypeRequest.java
                                📜 LoginRequest.java
                                📜 ManagerFeedbackRequest.java
                                📜 ManagerGoalCommentRequest.java
                                📜 OfficeLocationRequest.java
                                📜 OfficeLocationResponse.java
                                📜 PerformanceReportResponse.java
                                📜 PerformanceReviewRequest.java
                                📜 RefreshTokenRequest.java
                                📜 RegisterEmployeeRequest.java
                                📜 ResendOtpRequest.java
                                📜 TeamLeaveCalendarEntry.java
                                📜 TypingIndicator.java
                                📜 UpdateEmployeeRequest.java
                                📜 UpdateProfileRequest.java
                                📜 VerifyOtpRequest.java
                            📂 exception
                                📜 AccessDeniedException.java
                                📜 AccountDeactivatedException.java
                                📜 BadRequestException.java
                                📜 DuplicateResourceException.java
                                📜 GlobalExceptionHandler.java
                                📜 InsufficientBalanceException.java
                                📜 InvalidActionException.java
                                📜 IpBlockedException.java
                                📜 ResourceNotFoundException.java
                                📜 UnauthorizedException.java
                            📂 integration
                                📜 OllamaClient.java
                            📂 model
                                📜 ActivityLog.java
                                📜 AllowedIpRange.java
                                📜 Announcement.java
                                📜 Attendance.java
                                📜 ChatConversation.java
                                📜 ChatMessage.java
                                📜 Department.java
                                📜 Designation.java
                                📜 Employee.java
                                📜 Expense.java
                                📜 ExpenseItem.java
                                📜 Goal.java
                                📜 Holiday.java
                                📜 LeaveApplication.java
                                📜 LeaveBalance.java
                                📜 LeaveType.java
                                📜 Notification.java
                                📜 OfficeLocation.java
                                📜 PerformanceReview.java
                                📂 enums
                                    📜 AttendanceStatus.java
                                    📜 ExpenseCategory.java
                                    📜 ExpenseStatus.java
                                    📜 Gender.java
                                    📜 GoalPriority.java
                                    📜 GoalStatus.java
                                    📜 LeaveStatus.java
                                    📜 MessageType.java
                                    📜 NotificationType.java
                                    📜 ReviewStatus.java
                                    📜 Role.java
                            📂 repository
                                📜 ActivityLogRepository.java
                                📜 AllowedIpRangeRepository.java
                                📜 AnnouncementRepository.java
                                📜 AttendanceRepository.java
                                📜 ChatConversationRepository.java
                                📜 ChatMessageRepository.java
                                📜 DepartmentRepository.java
                                📜 DesignationRepository.java
                                📜 EmployeeRepository.java
                                📜 ExpenseRepository.java
                                📜 GoalRepository.java
                                📜 HolidayRepository.java
                                📜 LeaveApplicationRepository.java
                                📜 LeaveBalanceRepository.java
                                📜 LeaveTypeRepository.java
                                📜 NotificationRepository.java
                                📜 OfficeLocationRepository.java
                                📜 PerformanceReviewRepository.java
                            📂 service
                                📜 AIService.java
                                📜 AnnouncementService.java
                                📜 AttendanceService.java
                                📜 ChatService.java
                                📜 DashboardService.java
                                📜 DepartmentService.java
                                📜 DesignationService.java
                                📜 EmailService.java
                                📜 EmployeeService.java
                                📜 ExpenseService.java
                                📜 GeoAttendanceService.java
                                📜 InvoiceParserService.java
                                📜 IpAccessControlService.java
                                📜 LeaveAnalysisService.java
                                📜 LeaveService.java
                                📜 NotificationService.java
                                📜 OfficeLocationService.java
                                📜 PerformanceService.java
                                📜 PresenceService.java
                                📜 WebSocketNotificationService.java
                            📂 util
                                📜 NetworkIpUtil.java
            📂 resources
                📜 application.properties
        📂 test
            📂 java
                📂 com
                    📂 revworkforce
                        📂 employeemanagementservice
                            📜 EmployeeManagementServiceApplicationTests.java
    📂 uploads
        📂 expense-receipts
            📜 expense-9-1773772477820.pdf
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

### ☕ Source Code Files
#### **`src/main/java/com/revworkforce/employeemanagementservice/EmployeeManagementServiceApplication.java`**
```java
package com.revworkforce.employeemanagementservice;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication @EnableDiscoveryClient @EnableFeignClients
public class EmployeeManagementServiceApplication {
    public static void main(String[] args) { SpringApplication.run(EmployeeManagementServiceApplication.class, args); }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/config/DataSeeder.java`**
```java
package com.revworkforce.employeemanagementservice.config;
import com.revworkforce.employeemanagementservice.model.Department;
import com.revworkforce.employeemanagementservice.model.Designation;
import com.revworkforce.employeemanagementservice.model.Employee;
import com.revworkforce.employeemanagementservice.model.LeaveType;
import com.revworkforce.employeemanagementservice.model.enums.Gender;
import com.revworkforce.employeemanagementservice.model.enums.Role;
import com.revworkforce.employeemanagementservice.repository.DepartmentRepository;
import com.revworkforce.employeemanagementservice.repository.DesignationRepository;
import com.revworkforce.employeemanagementservice.repository.EmployeeRepository;
import com.revworkforce.employeemanagementservice.repository.LeaveTypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Configuration
public class DataSeeder {
    @Bean
    CommandLineRunner initDatabase(DepartmentRepository departmentRepository,
            DesignationRepository designationRepository, LeaveTypeRepository leaveTypeRepository) {
        return args -> {
            if (departmentRepository.count() == 0) {
                List<Department> departments = Arrays.asList(
                        Department.builder().departmentName("IT").description("Information Technology").build(),
                        Department.builder().departmentName("HR").description("Human Resources").build(),
                        Department.builder().departmentName("Finance").description("Financial Department").build(),
                        Department.builder().departmentName("Marketing").description("Marketing Department").build());
                departmentRepository.saveAll(departments);
                System.out.println("Seeded Departments");
            }
            if (designationRepository.count() == 0) {
                List<Designation> designations = Arrays.asList(
                        Designation.builder().designationName("Software Engineer").description("Develops software")
                                .build(),
                        Designation.builder().designationName("HR Manager").description("Manages HR").build(),
                        Designation.builder().designationName("Accountant").description("Manages Accounts").build(),
                        Designation.builder().designationName("Marketing Executive").description("Marketing strategies")
                                .build());
                designationRepository.saveAll(designations);
                System.out.println("Seeded Designations");
            }

            if (leaveTypeRepository.count() == 0) {
                List<LeaveType> leaveTypes = Arrays.asList(
                        LeaveType.builder()
                                .leaveTypeName("Earned Leave")
                                .description("Earned/Privilege leave accrued over time. Can be carried forward.")
                                .defaultDays(15)
                                .isPaidLeave(true)
                                .isCarryForwardEnabled(true)
                                .maxCarryForwardDays(5)
                                .isLossOfPay(false)
                                .build(),
                        LeaveType.builder()
                                .leaveTypeName("Sick Leave")
                                .description("Leave for medical/health reasons. May require medical certificate for extended period.")
                                .defaultDays(12)
                                .isPaidLeave(true)
                                .isCarryForwardEnabled(false)
                                .maxCarryForwardDays(0)
                                .isLossOfPay(false)
                                .build(),
                        LeaveType.builder()
                                .leaveTypeName("Casual Leave")
                                .description("Short-duration leave for personal/urgent matters.")
                                .defaultDays(10)
                                .isPaidLeave(true)
                                .isCarryForwardEnabled(false)
                                .maxCarryForwardDays(0)
                                .isLossOfPay(false)
                                .build(),
                        LeaveType.builder()
                                .leaveTypeName("Loss of Pay")
                                .description("Unpaid leave when all other leave balances are exhausted.")
                                .defaultDays(0)
                                .isPaidLeave(false)
                                .isCarryForwardEnabled(false)
                                .maxCarryForwardDays(0)
                                .isLossOfPay(true)
                                .build()
                );
                leaveTypeRepository.saveAll(leaveTypes);
                System.out.println("Seeded Default Leave Types: Earned Leave, Sick Leave, Casual Leave, Loss of Pay");
            }
        };
    }

    @Bean
    CommandLineRunner initAdmin(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!employeeRepository.existsByRole(Role.ADMIN)) {
                Employee admin = Employee.builder()
                        .employeeCode("ADM001")
                        .firstName("System")
                        .lastName("Admin")
                        .email("admin@workforce.com")
                        .passwordHash(passwordEncoder.encode("Admin@123"))
                        .phone("0000000000")
                        .dateOfBirth(LocalDate.of(1990, 1, 1))
                        .gender(Gender.MALE)
                        .address("WorkForce HQ")
                        .joiningDate(LocalDate.now())
                        .salary(BigDecimal.ZERO)
                        .role(Role.ADMIN)
                        .isActive(true)
                        .build();
                employeeRepository.save(admin);
                System.out.println("=== Default Admin Seeded ===");
                System.out.println("Email   : admin@workforce.com");
                System.out.println("Password: Admin@123");
                System.out.println("Code    : ADM001");
                System.out.println("============================");
            }
        };
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/config/GatewayHeaderAuthenticationFilter.java`**
```java
package com.revworkforce.employeemanagementservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class GatewayHeaderAuthenticationFilter extends OncePerRequestFilter {
    private static final String USER_EMAIL_HEADER = "X-User-Email";
    private static final String USER_ROLE_HEADER = "X-User-Role";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String email = request.getHeader(USER_EMAIL_HEADER);
            String role = request.getHeader(USER_ROLE_HEADER);

            if (email != null && !email.isBlank()) {
                String normalizedRole = (role == null || role.isBlank()) ? "USER" : role.trim();
                if (normalizedRole.startsWith("ROLE_")) {
                    normalizedRole = normalizedRole.substring(5);
                }

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_" + normalizedRole))
                        );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/config/IpAccessControlFilter.java`**
```java
package com.revworkforce.employeemanagementservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.revworkforce.employeemanagementservice.service.IpAccessControlService;
import com.revworkforce.employeemanagementservice.util.NetworkIpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class IpAccessControlFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(IpAccessControlFilter.class);

    @Autowired
    private IpAccessControlService ipAccessControlService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String requestUri = request.getRequestURI();
        String clientIp = NetworkIpUtil.resolveClientIp(request);

        boolean isEmployeeEndpoint = requestUri.startsWith("/api/employees");
        boolean isManagerEndpoint = requestUri.startsWith("/api/manager");

        if (isEmployeeEndpoint || isManagerEndpoint) {
            log.info("[IP-FILTER] Checking IP for URI: {} | Client IP: {}", requestUri, clientIp);

            boolean allowed = ipAccessControlService.isIpAllowed(clientIp);

            if (!allowed) {
                log.warn("[IP-FILTER] BLOCKED — IP: {} | URI: {}", clientIp, requestUri);
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write(
                        "{\"success\":false,\"message\":\"Access denied. Your IP address ("
                                + clientIp + ") is not whitelisted. Please contact your administrator.\"}"
                );
                return;
            }

            log.info("[IP-FILTER] ALLOWED — IP: {} | URI: {}", clientIp, requestUri);
        }

        filterChain.doFilter(request, response);
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/config/JwtUtil.java`**
```java
package com.revworkforce.employeemanagementservice.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secretKey;

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token) {
        String email = extractEmail(token);
        return email != null && !email.isBlank() && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/config/OllamaConfig.java`**
```java
package com.revworkforce.employeemanagementservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OllamaConfig {
    @Value("${ollama.timeout:60000}")
    private int timeout;
    @Bean(name = "ollamaRestTemplate")
    public RestTemplate ollamaRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(timeout);
        factory.setReadTimeout(timeout);
        return new RestTemplate(factory);
    }
}
```

#### **`src/main/java/com/revworkforce/employeemanagementservice/config/SecurityBeansConfig.java`**
```java
package com.revworkforce.employeemanagementservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityBeansConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/config/SecurityConfig.java`**
```java
package com.revworkforce.employeemanagementservice.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Value("${app.cors.allowed-origins:http://localhost:4200}")
    private String allowedOrigins;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           GatewayHeaderAuthenticationFilter gatewayHeaderAuthenticationFilter) throws Exception {
        http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(List.of("*"));
                    config.setAllowCredentials(true);
                    return config;
                }))
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/ws/**", "/app/**", "/topic/**", "/user/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType("application/json");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write("{\"success\":false,\"message\":\"Unauthorized\"}");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setContentType("application/json");
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.getWriter().write("{\"success\":false,\"message\":\"Access denied\"}");
                        })
                )
                .addFilterBefore(gatewayHeaderAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/config/SwaggerConfig.java`**
```java
package com.revworkforce.employeemanagementservice.config;
import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI workforceOpenAPI(){
        return new OpenAPI().info(new Info().title("WorkForce HRMS API's").description("API documentation").version("1.0.0").contact(new Contact().name("Rakesh").email("reddykr21@gmail.com"))).addSecurityItem(new SecurityRequirement().addList("Bearer Authentication")).components(new Components().addSecuritySchemes("Bearer Authentication", new SecurityScheme().type(SecurityScheme.Type.HTTP).bearerFormat("JWT").scheme("bearer")));
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/config/WebSocketAuthInterceptor.java`**
```java
package com.revworkforce.employeemanagementservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String jwt = authHeader.substring(7);
                try {
                    String email = jwtUtil.extractEmail(jwt);
                    if (email != null && jwtUtil.isTokenValid(jwt)) {
                            UsernamePasswordAuthenticationToken authToken =
                                    new UsernamePasswordAuthenticationToken(
                                            email, null, AuthorityUtils.NO_AUTHORITIES);
                            SecurityContextHolder.getContext().setAuthentication(authToken);
                            accessor.setUser(authToken);
                    }
                } catch (Exception e) {
                    throw new IllegalArgumentException("Invalid JWT token for WebSocket connection");
                }
            }
        }
        return message;
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/config/WebSocketConfig.java`**
```java
package com.revworkforce.employeemanagementservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Autowired
    private WebSocketAuthInterceptor webSocketAuthInterceptor;

    @Value("${app.cors.allowed-origins:http://localhost:4200}")
    private String allowedOrigins;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");

        registry.setApplicationDestinationPrefixes("/app");

        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins(allowedOrigins.split(","));
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(webSocketAuthInterceptor);
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/config/WebSocketEventListener.java`**
```java
package com.revworkforce.employeemanagementservice.config;

import com.revworkforce.employeemanagementservice.service.PresenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.Map;

@Component
public class WebSocketEventListener {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private PresenceService presenceService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handleWebSocketConnect(SessionConnectedEvent event) {
        Principal principal = event.getUser();
        if (principal != null) {
            String email = principal.getName();
            presenceService.userConnected(email);
            presenceService.touchUser(email);
            logger.info("User connected via WebSocket: {}", email);
            broadcastPresence(email, true);
        }
    }

    @EventListener
    public void handleWebSocketDisconnect(SessionDisconnectEvent event) {
        Principal principal = event.getUser();
        if (principal != null) {
            String email = principal.getName();
            presenceService.userDisconnected(email);
            presenceService.touchUser(email);
            logger.info("User disconnected from WebSocket: {}", email);
            broadcastPresence(email, false);
        }
    }

    private void broadcastPresence(String email, boolean online) {
        Object payload = Map.of("email", email, "online", online);
        messagingTemplate.convertAndSend("/topic/presence", payload);
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/controller/AdminAnnouncementController.java`**
```java
package com.revworkforce.employeemanagementservice.controller;
import jakarta.validation.Valid;
import com.revworkforce.employeemanagementservice.dto.AnnouncementRequest;
import com.revworkforce.employeemanagementservice.dto.ApiResponse;
import com.revworkforce.employeemanagementservice.model.Announcement;
import com.revworkforce.employeemanagementservice.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/announcements")
public class AdminAnnouncementController {
    @Autowired
    private AnnouncementService announcementService;

    @PostMapping
    public ResponseEntity<ApiResponse> createAnnouncement(@RequestHeader("X-User-Email") String adminEmail, @Valid @RequestBody AnnouncementRequest request) {
        Announcement announcement = announcementService.createAnnouncement(adminEmail, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, "Announcement created successfully", announcement));
    }

    @PutMapping("/{announcementId}")
    public ResponseEntity<ApiResponse> updateAnnouncement(@PathVariable Integer announcementId, @Valid @RequestBody AnnouncementRequest request) {
        Announcement announcement = announcementService.updateAnnouncement(announcementId, request);
        return ResponseEntity.ok(new ApiResponse(true, "Announcement updated successfully", announcement));
    }

    @PatchMapping("/{announcementId}/deactivate")
    public ResponseEntity<ApiResponse> deactivateAnnouncement(@PathVariable Integer announcementId) {
        Announcement announcement = announcementService.deactivateAnnouncement(announcementId);
        return ResponseEntity.ok(new ApiResponse(true, "Announcement deactivated successfully", announcement));
    }

    @PatchMapping("/{announcementId}/activate")
    public ResponseEntity<ApiResponse> activateAnnouncement(@PathVariable Integer announcementId) {
        Announcement announcement = announcementService.activateAnnouncement(announcementId);
        return ResponseEntity.ok(new ApiResponse(true, "Announcement activated successfully", announcement));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllAnnouncements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Announcement> announcements = announcementService.getAllAnnouncements(pageable);
        return ResponseEntity.ok(new ApiResponse(true, "Announcements fetched successfully", announcements));
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/controller/AdminDashboardController.java`**
```java
package com.revworkforce.employeemanagementservice.controller;
import com.revworkforce.employeemanagementservice.dto.ApiResponse;
import com.revworkforce.employeemanagementservice.dto.DashboardResponse;
import com.revworkforce.employeemanagementservice.dto.EmployeeReportResponse;
import com.revworkforce.employeemanagementservice.dto.LeaveReportResponse;
import com.revworkforce.employeemanagementservice.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/dashboard")
public class AdminDashboardController {
    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<ApiResponse> getDashboard() {
        DashboardResponse dashboard = dashboardService.getDashboard();
        return ResponseEntity.ok(new ApiResponse(true, "Dashboard data fetched successfully", dashboard));
    }

    @GetMapping("/leave-report")
    public ResponseEntity<ApiResponse> getLeaveReport(@RequestParam(required = false) Integer year) {
        List<LeaveReportResponse> report = dashboardService.getLeaveReport(year);
        return ResponseEntity.ok(new ApiResponse(true, "Leave report fetched successfully", report));
    }

    @GetMapping("/leave-report/department/{departmentId}")
    public ResponseEntity<ApiResponse> getLeaveReportByDepartment(
            @PathVariable Integer departmentId,
            @RequestParam(required = false) Integer year) {
        List<LeaveReportResponse> report = dashboardService.getLeaveReportByDepartment(departmentId, year);
        return ResponseEntity.ok(new ApiResponse(true, "Department leave report fetched successfully", report));
    }

    @GetMapping("/leave-report/employee/{employeeCode}")
    public ResponseEntity<ApiResponse> getLeaveReportByEmployee(
            @PathVariable String employeeCode,
            @RequestParam(required = false) Integer year) {
        List<LeaveReportResponse> report = dashboardService.getLeaveReportByEmployee(employeeCode, year);
        return ResponseEntity.ok(new ApiResponse(true, "Employee leave report fetched successfully", report));
    }

    @GetMapping("/employee-report")
    public ResponseEntity<ApiResponse> getEmployeeReport() {
        EmployeeReportResponse report = dashboardService.getEmployeeReport();
        return ResponseEntity.ok(new ApiResponse(true, "Employee report generated successfully", report));
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/controller/AdminExpenseController.java`**
```java
package com.revworkforce.employeemanagementservice.controller;

import com.revworkforce.employeemanagementservice.dto.ExpenseActionRequest;
import com.revworkforce.employeemanagementservice.model.Expense;
import com.revworkforce.employeemanagementservice.model.enums.ExpenseStatus;
import com.revworkforce.employeemanagementservice.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/expenses")
public class AdminExpenseController {
    @Autowired private ExpenseService expenseService;

    @GetMapping
    public ResponseEntity<Page<Expense>> getAllExpenses(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        ExpenseStatus expenseStatus = null;
        if (status != null) {
            try { expenseStatus = ExpenseStatus.valueOf(status.toUpperCase()); } catch (Exception ignored) {}
        }
        return ResponseEntity.ok(expenseService.getAllExpenses(expenseStatus,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))));
    }

    @GetMapping("/finance-pending")
    public ResponseEntity<Page<Expense>> getFinancePending(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(expenseService.getFinancePendingExpenses(
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "managerActionDate"))));
    }

    @PatchMapping("/{id}/finance-action")
    public ResponseEntity<Expense> financeAction(
            Authentication auth,
            @PathVariable Integer id,
            @RequestBody ExpenseActionRequest request) {
        return ResponseEntity.ok(expenseService.financeAction(auth.getName(), id, request));
    }

    @GetMapping("/{id}/receipt")
    public ResponseEntity<Resource> getExpenseReceipt(Authentication auth, @PathVariable Integer id) {
        ExpenseService.ReceiptFileData receipt = expenseService.getExpenseReceipt(auth.getName(), id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + receipt.fileName() + "\"")
                .contentType(MediaType.parseMediaType(receipt.contentType()))
                .body(receipt.resource());
    }
}


```

#### **`src/main/java/com/revworkforce/employeemanagementservice/controller/AIController.java`**
```java
package com.revworkforce.employeemanagementservice.controller;

import jakarta.validation.Valid;
import com.revworkforce.employeemanagementservice.dto.AIChatRequest;
import com.revworkforce.employeemanagementservice.dto.AIChatResponse;
import com.revworkforce.employeemanagementservice.dto.ApiResponse;
import com.revworkforce.employeemanagementservice.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AIController {
    @Autowired
    private AIService aiService;
    @PostMapping("/chat")
    public ResponseEntity<ApiResponse> chat(@RequestHeader("X-User-Email") String email, @Valid @RequestBody AIChatRequest request) {
        AIChatResponse response = aiService.processMessage(email, request);
        return ResponseEntity.ok(new ApiResponse(true, "AI response generated", response));
    }
}
```

#### **`src/main/java/com/revworkforce/employeemanagementservice/controller/ChatController.java`**
```java
package com.revworkforce.employeemanagementservice.controller;

import com.revworkforce.employeemanagementservice.dto.ApiResponse;
import com.revworkforce.employeemanagementservice.dto.ChatMessageRequest;
import com.revworkforce.employeemanagementservice.dto.ChatMessageResponse;
import com.revworkforce.employeemanagementservice.dto.ConversationResponse;
import com.revworkforce.employeemanagementservice.service.ChatService;
import com.revworkforce.employeemanagementservice.service.PresenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    @Autowired
    private ChatService chatService;

    @Autowired
    private PresenceService presenceService;

    @GetMapping("/conversations")
    public ResponseEntity<ApiResponse> getMyConversations(@RequestHeader("X-User-Email") String email) {
        presenceService.touchUser(email);
        List<ConversationResponse> conversations = chatService.getMyConversations(email);
        return ResponseEntity.ok(new ApiResponse(true, "Conversations fetched successfully", conversations));
    }

    @PostMapping("/conversations/{otherEmployeeId}")
    public ResponseEntity<ApiResponse> getOrCreateConversation(@RequestHeader("X-User-Email") String email, @PathVariable Integer otherEmployeeId) {
        presenceService.touchUser(email);
        ConversationResponse conversation = chatService.getOrCreateConversation(email, otherEmployeeId);
        return ResponseEntity.ok(new ApiResponse(true, "Conversation ready", conversation));
    }

    @GetMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<ApiResponse> getMessages(
            @PathVariable Long conversationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestHeader("X-User-Email") String email) {
        presenceService.touchUser(email);
        Page<ChatMessageResponse> messages = chatService.getMessages(email, conversationId, page, size);
        return ResponseEntity.ok(new ApiResponse(true, "Messages fetched successfully", messages));
    }

    @PostMapping("/messages")
    public ResponseEntity<ApiResponse> sendMessage(@RequestHeader("X-User-Email") String email,
                                                   @RequestBody ChatMessageRequest request) {
        presenceService.touchUser(email);
        ChatMessageResponse sent = chatService.sendMessage(email, request);
        return ResponseEntity.ok(new ApiResponse(true, "Message sent successfully", sent));
    }

    @PatchMapping("/conversations/{conversationId}/read")
    public ResponseEntity<ApiResponse> markConversationAsRead(@RequestHeader("X-User-Email") String email, @PathVariable Long conversationId) {
        presenceService.touchUser(email);
        int count = chatService.markConversationAsRead(email, conversationId);
        return ResponseEntity.ok(new ApiResponse(true, count + " message(s) marked as read"));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<ApiResponse> getUnreadCount(@RequestHeader("X-User-Email") String email) {
        presenceService.touchUser(email);
        long count = chatService.getTotalUnreadCount(email);
        return ResponseEntity.ok(new ApiResponse(true, "Unread count fetched", count));
    }

    @GetMapping("/online-users")
    public ResponseEntity<ApiResponse> getOnlineUsers(@RequestHeader(value = "X-User-Email", required = false) String email) {
        presenceService.touchUser(email);
        Set<String> onlineEmails = presenceService.getOnlineUsers();
        return ResponseEntity.ok(new ApiResponse(true, "Online users fetched", onlineEmails));
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/controller/ChatWebSocketController.java`**
```java
package com.revworkforce.employeemanagementservice.controller;

import com.revworkforce.employeemanagementservice.dto.ChatMessageRequest;
import com.revworkforce.employeemanagementservice.dto.TypingIndicator;
import com.revworkforce.employeemanagementservice.model.Employee;
import com.revworkforce.employeemanagementservice.repository.EmployeeRepository;
import com.revworkforce.employeemanagementservice.service.ChatService;
import com.revworkforce.employeemanagementservice.service.PresenceService;
import com.revworkforce.employeemanagementservice.service.WebSocketNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class ChatWebSocketController {
    @Autowired
    private ChatService chatService;
    @Autowired
    private WebSocketNotificationService wsNotificationService;
    @Autowired
    private PresenceService presenceService;
    @Autowired
    private EmployeeRepository employeeRepository;

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload ChatMessageRequest request, Principal principal) {
        String senderEmail = principal.getName();
        presenceService.touchUser(senderEmail);
        chatService.sendMessage(senderEmail, request);
    }

    @MessageMapping("/chat.typing")
    public void typingIndicator(@Payload TypingIndicator indicator, Principal principal) {
        String senderEmail = principal.getName();
        presenceService.touchUser(senderEmail);
        Employee sender = employeeRepository.findByEmail(senderEmail).orElse(null);
        if (sender == null) return;

        indicator.setSenderId(sender.getEmployeeId());
        indicator.setSenderName(sender.getFirstName() + " " + sender.getLastName());

        Employee recipient = getRecipientFromConversation(indicator.getConversationId(), sender.getEmployeeId());
        if (recipient != null) {
            wsNotificationService.sendTypingIndicator(recipient.getEmail(), indicator);
        }
    }

    private Employee getRecipientFromConversation(Long conversationId, Integer senderId) {
        if (conversationId == null) return null;
        return chatService.getOtherParticipantByConversation(conversationId, senderId);
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/controller/EmployeeAnnouncementController.java`**
```java
package com.revworkforce.employeemanagementservice.controller;
import com.revworkforce.employeemanagementservice.dto.ApiResponse;
import com.revworkforce.employeemanagementservice.model.Announcement;
import com.revworkforce.employeemanagementservice.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees/announcements")
public class EmployeeAnnouncementController {
    @Autowired
    private AnnouncementService announcementService;

    @GetMapping
    public ResponseEntity<ApiResponse> getActiveAnnouncements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Announcement> announcements = announcementService.getActiveAnnouncements(pageable);
        return ResponseEntity.ok(new ApiResponse(true, "Announcements fetched successfully", announcements));
    }

    @GetMapping("/{announcementId}")
    public ResponseEntity<ApiResponse> getAnnouncement(@PathVariable Integer announcementId) {
        Announcement announcement = announcementService.getAnnouncementById(announcementId);
        return ResponseEntity.ok(new ApiResponse(true, "Announcement fetched successfully", announcement));
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/controller/ExpenseController.java`**
```java
package com.revworkforce.employeemanagementservice.controller;

import com.revworkforce.employeemanagementservice.dto.*;
import com.revworkforce.employeemanagementservice.model.Expense;
import com.revworkforce.employeemanagementservice.service.ExpenseService;
import com.revworkforce.employeemanagementservice.service.InvoiceParserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employee/expenses")
public class ExpenseController {
    private static final Logger log = LoggerFactory.getLogger(ExpenseController.class);

    @Autowired private ExpenseService expenseService;
    @Autowired private InvoiceParserService invoiceParserService;

    @PostMapping
    public ResponseEntity<Expense> createExpense(Authentication auth, @RequestBody ExpenseRequest request) {
        return ResponseEntity.ok(expenseService.createExpense(auth.getName(), request));
    }

    @PatchMapping("/{id}/submit")
    public ResponseEntity<Expense> submitExpense(Authentication auth, @PathVariable Integer id) {
        return ResponseEntity.ok(expenseService.submitExpense(auth.getName(), id));
    }

    @GetMapping
    public ResponseEntity<Page<Expense>> getMyExpenses(
            Authentication auth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(expenseService.getMyExpenses(auth.getName(),
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpense(@PathVariable Integer id) {
        return ResponseEntity.ok(expenseService.getExpenseById(id));
    }

    @PostMapping("/parse-invoice")
    public ResponseEntity<InvoiceParseResponse> parseInvoice(@RequestBody java.util.Map<String, String> body) {
        log.info("=== PARSE-INVOICE endpoint called ===");
        String invoiceText = body.get("invoiceText");
        return ResponseEntity.ok(invoiceParserService.parseInvoice(invoiceText));
    }

    @PostMapping("/parse-file")
    public ResponseEntity<InvoiceParseResponse> parseFile(@RequestBody java.util.Map<String, String> body) {
        log.info("=== PARSE-FILE endpoint called === fileType={}, dataLength={}",
                body.get("fileType"),
                body.get("fileData") != null ? body.get("fileData").length() : "null");

        try {
            String base64Data = body.get("fileData");
            String fileType = body.get("fileType");
            InvoiceParseResponse result = invoiceParserService.parseUploadedFile(base64Data, fileType);
            log.info("=== PARSE-FILE result: success={}, vendor={}, amount={} ===",
                    result.isSuccess(), result.getVendorName(), result.getTotalAmount());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("=== PARSE-FILE EXCEPTION: {} ===", e.getMessage(), e);
            return ResponseEntity.ok(InvoiceParseResponse.builder()
                    .success(false)
                    .errorMessage("Server error: " + e.getMessage())
                    .build());
        }
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/controller/ManagerExpenseController.java`**
```java
package com.revworkforce.employeemanagementservice.controller;

import com.revworkforce.employeemanagementservice.dto.ExpenseActionRequest;
import com.revworkforce.employeemanagementservice.model.Expense;
import com.revworkforce.employeemanagementservice.model.enums.ExpenseStatus;
import com.revworkforce.employeemanagementservice.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager/expenses")
public class ManagerExpenseController {
    @Autowired private ExpenseService expenseService;

    @GetMapping
    public ResponseEntity<Page<Expense>> getTeamExpenses(
            Authentication auth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(expenseService.getTeamExpenses(auth.getName(),
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "submittedDate"))));
    }

    @PatchMapping("/{id}/action")
    public ResponseEntity<Expense> actionExpense(
            Authentication auth,
            @PathVariable Integer id,
            @RequestBody ExpenseActionRequest request) {
        return ResponseEntity.ok(expenseService.managerAction(auth.getName(), id, request));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<Expense>> getAllExpenses(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        ExpenseStatus expenseStatus = null;
        if (status != null) {
            try { expenseStatus = ExpenseStatus.valueOf(status.toUpperCase()); } catch (Exception ignored) {}
        }
        return ResponseEntity.ok(expenseService.getAllExpenses(expenseStatus,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))));
    }

    @GetMapping("/finance-pending")
    public ResponseEntity<Page<Expense>> getFinancePending(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(expenseService.getFinancePendingExpenses(
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "managerActionDate"))));
    }

    @PatchMapping("/{id}/finance-action")
    public ResponseEntity<Expense> financeAction(
            Authentication auth,
            @PathVariable Integer id,
            @RequestBody ExpenseActionRequest request) {
        return ResponseEntity.ok(expenseService.financeAction(auth.getName(), id, request));
    }

    @GetMapping("/{id}/receipt")
    public ResponseEntity<Resource> getExpenseReceipt(Authentication auth, @PathVariable Integer id) {
        ExpenseService.ReceiptFileData receipt = expenseService.getExpenseReceipt(auth.getName(), id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + receipt.fileName() + "\"")
                .contentType(MediaType.parseMediaType(receipt.contentType()))
                .body(receipt.resource());
    }
}


```

#### **`src/main/java/com/revworkforce/employeemanagementservice/controller/ManagerTeamController.java`**
```java
package com.revworkforce.employeemanagementservice.controller;

import com.revworkforce.employeemanagementservice.dto.ApiResponse;
import com.revworkforce.employeemanagementservice.dto.EmployeeDirectoryResponse;
import com.revworkforce.employeemanagementservice.dto.EmployeeProfileResponse;
import com.revworkforce.employeemanagementservice.exception.InvalidActionException;
import com.revworkforce.employeemanagementservice.exception.ResourceNotFoundException;
import com.revworkforce.employeemanagementservice.model.Employee;
import com.revworkforce.employeemanagementservice.repository.EmployeeRepository;
import com.revworkforce.employeemanagementservice.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager/team")
public class ManagerTeamController {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<ApiResponse> getTeamMembers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "firstName") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestHeader("X-User-Email") String email) {
        Employee manager = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found"));
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Employee> team = employeeRepository.findByManager_EmployeeCode(manager.getEmployeeCode(), pageable);
        Page<EmployeeDirectoryResponse> response = team.map(this::mapToDirectoryResponse);
        return ResponseEntity.ok(new ApiResponse(true, "Team members fetched successfully", response));
    }

    @GetMapping("/{employeeCode}")
    public ResponseEntity<ApiResponse> getTeamMemberProfile(@RequestHeader("X-User-Email") String email, @PathVariable String employeeCode) {
        Employee manager = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found"));
        Employee member = employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        if (member.getManager() == null || !member.getManager().getEmployeeCode().equals(manager.getEmployeeCode())) {
            throw new InvalidActionException("This employee is not in your team");
        }
        EmployeeProfileResponse profile = employeeService.getEmployeeByCode(employeeCode);
        return ResponseEntity.ok(new ApiResponse(true, "Team member profile fetched successfully", profile));
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse> getTeamCount(@RequestHeader("X-User-Email") String email) {
        Employee manager = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found"));
        List<Employee> teamMembers = employeeRepository.findByManager_EmployeeCode(manager.getEmployeeCode());
        long activeCount = teamMembers.stream().filter(Employee::getIsActive).count();
        return ResponseEntity.ok(new ApiResponse(true, "Team count fetched successfully",
                java.util.Map.of("total", teamMembers.size(), "active", activeCount)));
    }

    private EmployeeDirectoryResponse mapToDirectoryResponse(Employee employee) {
        return EmployeeDirectoryResponse.builder().employeeCode(employee.getEmployeeCode()).firstName(employee.getFirstName()).lastName(employee.getLastName()).email(employee.getEmail()).phone(employee.getPhone()).departmentName(employee.getDepartment() != null ? employee.getDepartment().getDepartmentName() : null).designationTitle(employee.getDesignation() != null ? employee.getDesignation().getDesignationName() : null).role(employee.getRole().name()).isActive(employee.getIsActive()).build();
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/AdjustLeaveBalanceRequest.java`**
```java
package com.revworkforce.employeemanagementservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdjustLeaveBalanceRequest {
    @NotNull(message = "Leave type ID is required")
    private Integer leaveTypeId;
    @NotNull(message = "Total leave is required")
    private Integer totalLeaves;
    @NotBlank(message = "Reason for adjustment is required")
    private String reason;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/AIChatRequest.java`**
```java
package com.revworkforce.employeemanagementservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIChatRequest {
    @NotBlank(message = "Message is required")
    private String message;
    private List<ChatHistoryEntry> history;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatHistoryEntry{
        private String role;
        private String content;
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/AIChatResponse.java`**
```java
package com.revworkforce.employeemanagementservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AIChatResponse {
    private String reply;
    private String action;
    private Object actionData;
    private boolean actionPerformed;
    private List<String> quickReplies;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/AnnouncementRequest.java`**
```java
package com.revworkforce.employeemanagementservice.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementRequest {
    @NotBlank(message = "Title is required")
    private String title;
    @NotBlank(message = "Content is required")
    private String content;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/ApiResponse.java`**
```java
package com.revworkforce.employeemanagementservice.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    private boolean success;
    private String message;
    private Object data;

    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.data = null;
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/AssignManagerRequest.java`**
```java
package com.revworkforce.employeemanagementservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignManagerRequest {
    @NotBlank(message = "Manager code is required")
    private String managerCode;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/AttendanceResponse.java`**
```java
package com.revworkforce.employeemanagementservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceResponse {
    private Integer attendanceId;
    private Integer employeeId;
    private String employeeCode;
    private String employeeName;
    private LocalDate attendanceDate;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private Double totalHours;
    private String status;
    private String checkInIp;
    private String checkOutIp;

    private Double checkInLatitude;
    private Double checkInLongitude;
    private Double checkOutLatitude;
    private Double checkOutLongitude;
    private Boolean locationVerified;
    private Double checkInDistanceMeters;
    private Double checkOutDistanceMeters;
    private String officeLocationName;

    private String notes;
    private Boolean isLate;
    private Boolean isEarlyDeparture;
    private LocalDateTime createdAt;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/AttendanceSummaryResponse.java`**
```java
package com.revworkforce.employeemanagementservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceSummaryResponse {
    private String employeeCode;
    private String employeeName;
    private long totalPresent;
    private long totalAbsent;
    private long totalHalfDay;
    private long totalOnLeave;
    private long totalLateArrivals;
    private long totalEarlyDepartures;
    private Double totalHoursWorked;
    private String month;
    private Integer year;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/ChangePasswordRequest.java`**
```java
package com.revworkforce.employeemanagementservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {
    @NotBlank(message = "Current password is required")
    private String currentPassword;

    @NotBlank(message = "New password is required")
    @Size(min = 8, message = "New password must be at least 8 characters long")
    private String newPassword;

    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/ChatMessageRequest.java`**
```java
package com.revworkforce.employeemanagementservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRequest {
    private Long conversationId;
    private Integer recipientId;
    private String content;
    private String messageType;
    private String fileUrl;
    private String fileName;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/ChatMessageResponse.java`**
```java
package com.revworkforce.employeemanagementservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageResponse {
    private Long messageId;
    private Long conversationId;
    private Integer senderId;
    private String senderName;
    private String senderCode;
    private Integer recipientId;
    private String content;
    private String messageType;
    private String fileUrl;
    private String fileName;
    private Boolean isRead;
    private LocalDateTime createdAt;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/CheckInRequest.java`**
```java
package com.revworkforce.employeemanagementservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckInRequest {
    private String notes;

    private Double latitude;

    private Double longitude;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/CheckOutRequest.java`**
```java
package com.revworkforce.employeemanagementservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckOutRequest {
    private String notes;

    private Double latitude;

    private Double longitude;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/ConversationResponse.java`**
```java
package com.revworkforce.employeemanagementservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationResponse {
    private Long conversationId;
    private Integer otherParticipantId;
    private String otherParticipantEmail;
    private String otherParticipantName;
    private String otherParticipantDesignation;
    private String otherParticipantCode;
    private String otherParticipantRole;
    private String otherParticipantDepartment;
    private String lastMessageText;
    private Integer lastSenderId;
    private LocalDateTime lastMessageAt;
    private long unreadCount;
    private boolean online;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/DashboardResponse.java`**
```java
package com.revworkforce.employeemanagementservice.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardResponse {
    private long totalEmployees;
    private long activeEmployees;
    private long inactiveEmployees;
    private long totalManagers;
    private long totalAdmins;
    private long totalRegularEmployees;
    private long pendingLeaves;
    private long approvedLeavesToday;
    private long totalDepartments;
    private long totalDesignations;
    private Map<String, Long> employeesByDepartment;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/DepartmentRequest.java`**
```java
package com.revworkforce.employeemanagementservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentRequest {
    @NotBlank(message = "Department name is required")
    private String departmentName;
    private String description;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/DesignationRequest.java`**
```java
package com.revworkforce.employeemanagementservice.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DesignationRequest {
    @NotBlank(message = "Designation name is required")
    private String designationName;
    private String description;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/EmployeeDashboardResponse.java`**
```java
package com.revworkforce.employeemanagementservice.dto;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDashboardResponse {
    private String employeeName;
    private String employeeCode;
    private String departmentName;
    private String designationTitle;
    private long pendingLeaveRequests;
    private long approvedLeaves;
    private long unreadNotifications;
    private List<LeaveBalanceSummary> leaveBalances;
    private List<UpcomingHolidaySummary> upcomingHolidays;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LeaveBalanceSummary {
        private String leaveTypeName;
        private int totalLeaves;
        private int usedLeaves;
        private int availableBalance;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpcomingHolidaySummary {
        private String holidayName;
        private LocalDate holidayDate;
        private String description;
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/EmployeeDirectoryResponse.java`**
```java
package com.revworkforce.employeemanagementservice.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDirectoryResponse {
    private Integer employeeId;
    private String employeeCode;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String departmentName;
    private String designationTitle;
    private String role;
    private Boolean isActive;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/EmployeeProfileResponse.java`**
```java
package com.revworkforce.employeemanagementservice.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeProfileResponse {
    private Integer employeeId;
    private String employeeCode;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private Integer departmentId;
    private String departmentName;
    private Integer designationId;
    private String designationTitle;
    private LocalDate joiningDate;
    private BigDecimal salary;
    private String role;
    private Boolean isActive;
    private Boolean twoFactorEnabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ManagerInfo manager;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ManagerInfo {
        private Integer managerId;
        private String managerCode;
        private String managerName;
        private String managerEmail;
        private String managerPhone;
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/EmployeeReportResponse.java`**
```java
package com.revworkforce.employeemanagementservice.dto;

import lombok.*;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeReportResponse {
    private long totalEmployees;
    private long activeEmployees;
    private long inactiveEmployees;
    private Map<String, Long> headcountByDepartment;
    private Map<String, Long> headcountByRole;
    private List<JoiningTrend> joiningTrends;
    private double averageTenureMonths;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class JoiningTrend {
        private String period;
        private long count;
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/ExpenseActionRequest.java`**
```java
package com.revworkforce.employeemanagementservice.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ExpenseActionRequest {
    private String action;
    private String comments;
}


```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/ExpenseRequest.java`**
```java
package com.revworkforce.employeemanagementservice.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ExpenseRequest {
    private String title;
    private String description;
    private String category;
    private BigDecimal totalAmount;
    private String currency;
    private LocalDate expenseDate;
    private String vendorName;
    private String invoiceNumber;
    private String receiptBase64;
    private String receiptFileName;
    private List<ExpenseItemRequest> items;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ExpenseItemRequest {
        private String description;
        private BigDecimal amount;
        private Integer quantity;
    }
}


```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/ForceResetPasswordRequest.java`**
```java
package com.revworkforce.employeemanagementservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForceResetPasswordRequest {
    @NotBlank(message = "New password is required")
    @Size(min = 8, message = "New password must be at least 8 characters long")
    private String newPassword;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/GoalProgressRequest.java`**
```java
package com.revworkforce.employeemanagementservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalProgressRequest {
    @NotNull(message = "Progress percentage is required")
    @Min(value = 0, message = "Progress must be between 0 and 100")
    @Max(value = 100, message = "Progress must be between 0 and 100")
    private Integer progress;
    private String status;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/GoalRequest.java`**
```java
package com.revworkforce.employeemanagementservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalRequest {
    @NotBlank(message = "Goal title is required")
    private String title;
    private String description;
    @NotNull(message = "Deadline is required")
    private LocalDate deadline;
    @NotNull(message = "Priority is required (HIGH, MEDIUM, LOW)")
    private String priority;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/HolidayRequest.java`**
```java
package com.revworkforce.employeemanagementservice.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HolidayRequest {
    @NotBlank(message = "Holiday name is required")
    private String holidayName;
    @NotNull(message = "Holiday date is required")
    private LocalDate holidayDate;
    private String description;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/InvoiceParseResponse.java`**
```java
package com.revworkforce.employeemanagementservice.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InvoiceParseResponse {
    private boolean success;
    private String title;
    private String vendorName;
    private String invoiceNumber;
    private String invoiceDate;
    private BigDecimal totalAmount;
    private String currency;
    private String category;
    private String description;
    private List<ParsedItem> items;
    private String rawText;
    private String errorMessage;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ParsedItem {
        private String description;
        private BigDecimal amount;
        private Integer quantity;
    }
}


```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/IpRangeRequest.java`**
```java
package com.revworkforce.employeemanagementservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IpRangeRequest {
    @NotBlank(message = "IP range is required")
    @Size(max = 50, message = "IP range must not exceed 50 characters")
    private String ipRange;

    @NotBlank(message = "Description is required")
    @Size(max = 200, message = "Description must not exceed 200 characters")
    private String description;

    private Boolean isActive;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/IpRangeResponse.java`**
```java
package com.revworkforce.employeemanagementservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IpRangeResponse {
    private Integer ipRangeId;
    private String ipRange;
    private String description;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/LeaveActionRequest.java`**
```java
package com.revworkforce.employeemanagementservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveActionRequest {
    @NotBlank(message = "Action is required (APPROVED or REJECTED)")
    private String action;
    private String comments;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/LeaveAnalysisResponse.java`**
```java
package com.revworkforce.employeemanagementservice.dto;

import lombok.*;
import java.util.List;
import java.util.Map;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LeaveAnalysisResponse {
    private String employeeName;
    private String department;
    private String designation;

    private int totalLeavesTakenThisYear;
    private int totalLeavesTakenLastYear;
    private Map<String, Integer> leavesByType;
    private Map<String, Integer> leavesByMonth;
    private int pendingLeaveRequests;
    private double averageLeaveDuration;

    private Map<String, Integer> currentBalances;

    private List<String> patterns;
    private String frequencyTrend;
    private int teamMembersOnLeaveToday;

    private int requestedDays;
    private String requestedType;
    private int balanceAfterApproval;

    private String aiSummary;
    private String aiRecommendation;
    private List<String> aiReasons;
}


```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/LeaveApplyRequest.java`**
```java
package com.revworkforce.employeemanagementservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveApplyRequest {
    @NotNull(message = "Leave type ID is required")
    private Integer leaveTypeId;
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    @NotNull(message = "End date is required")
    private LocalDate endDate;
    @NotBlank(message = "Reason is required")
    private String reason;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/LeaveReportResponse.java`**
```java
package com.revworkforce.employeemanagementservice.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveReportResponse {
    private String employeeCode;
    private String employeeName;
    private String departmentName;
    private String leaveTypeName;
    private Integer totalLeaves;
    private Integer usedLeaves;
    private Integer availableBalance;
    private Integer year;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/LeaveTypeRequest.java`**
```java
package com.revworkforce.employeemanagementservice.dto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveTypeRequest {
    @NotBlank(message = "Leave type name is required")
    private String leaveTypeName;

    private String description;

    @NotNull(message = "Default days is required")
    @Min(value = 0, message = "Default days must be 0 or more")
    private Integer defaultDays;

    private Boolean isPaidLeave;

    private Boolean isCarryForwardEnabled;

    @Min(value = 0, message = "Max carry forward days must be 0 or more")
    private Integer maxCarryForwardDays;

    private Boolean isLossOfPay;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/LoginRequest.java`**
```java
package com.revworkforce.employeemanagementservice.dto;
import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
public class LoginRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Password is required")
    private String password;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/ManagerFeedbackRequest.java`**
```java
package com.revworkforce.employeemanagementservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerFeedbackRequest {
    @NotNull(message = "Manager rating is required")
    @Min(value = 1, message = "Rating must be between 1 and 5")
    @Max(value = 5, message = "Rating must be between 1 and 5")
    private Integer managerRating;
    @NotBlank(message = "Feedback is required")
    private String managerFeedback;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/ManagerGoalCommentRequest.java`**
```java
package com.revworkforce.employeemanagementservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagerGoalCommentRequest {
    @NotBlank(message = "Comment is required")
    private String managerComments;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/OfficeLocationRequest.java`**
```java
package com.revworkforce.employeemanagementservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfficeLocationRequest {
    @NotBlank(message = "Location name is required")
    @Size(max = 100, message = "Location name must not exceed 100 characters")
    private String locationName;

    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String address;

    @NotNull(message = "Latitude is required")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    private Double longitude;

    private Integer radiusMeters;

    private Boolean isActive;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/OfficeLocationResponse.java`**
```java
package com.revworkforce.employeemanagementservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfficeLocationResponse {
    private Integer locationId;
    private String locationName;
    private String address;
    private Double latitude;
    private Double longitude;
    private Integer radiusMeters;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/PerformanceReportResponse.java`**
```java
package com.revworkforce.employeemanagementservice.dto;

import lombok.*;
import java.util.List;
import java.util.Map;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PerformanceReportResponse {
    private String employeeName;
    private String employeeCode;
    private String department;
    private String designation;
    private String reportPeriod;

    private int totalPresentDays;
    private int totalAbsentDays;
    private int lateArrivals;
    private double averageHoursPerDay;

    private int totalLeavesTaken;
    private Map<String, Integer> leaveBreakdown;

    private int totalGoals;
    private int completedGoals;
    private int inProgressGoals;
    private double averageGoalProgress;
    private List<GoalSummary> goals;

    private List<ReviewSummary> reviews;
    private Double averageSelfRating;
    private Double averageManagerRating;

    private String aiOverallAssessment;
    private String aiStrengths;
    private String aiAreasForImprovement;
    private String aiRecommendations;
    private String aiRating;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class GoalSummary {
        private String title;
        private String status;
        private int progress;
        private String priority;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ReviewSummary {
        private String period;
        private Integer selfRating;
        private Integer managerRating;
        private String status;
    }
}


```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/PerformanceReviewRequest.java`**
```java
package com.revworkforce.employeemanagementservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceReviewRequest {
    @NotBlank(message = "Review period is required (e.g., 2026-H1, 2026-Q1)")
    private String reviewPeriod;
    private String keyDeliverables;
    private String accomplishments;
    private String areasOfImprovement;
    @Min(value = 1, message = "Self assessment rating must be between 1 and 5")
    @Max(value = 5, message = "Self assessment rating must be between 1 and 5")
    private Integer selfAssessmentRating;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/RefreshTokenRequest.java`**
```java
package com.revworkforce.employeemanagementservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenRequest {
    @NotBlank(message = "Refresh token is required")
    private String refreshToken;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/RegisterEmployeeRequest.java`**
```java
package com.revworkforce.employeemanagementservice.dto;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class RegisterEmployeeRequest {
    @NotBlank(message = "First name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    private String employeeCode;
    private String phone;
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private Integer departmentId;
    private Integer designationId;
    @NotNull(message = "Joining date is required")
    private LocalDate joiningDate;
    private BigDecimal salary;
    private String managerCode;
    private String role;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/ResendOtpRequest.java`**
```java
package com.revworkforce.employeemanagementservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResendOtpRequest {
    @NotBlank(message = "Pre-auth token is required")
    private String preAuthToken;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/TeamLeaveCalendarEntry.java`**
```java
package com.revworkforce.employeemanagementservice.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamLeaveCalendarEntry {
    private String employeeCode;
    private String employeeName;
    private String leaveTypeName;
    private LocalDate startDate;
    private LocalDate endDate;
    private int totalDays;
    private String status;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/TypingIndicator.java`**
```java
package com.revworkforce.employeemanagementservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypingIndicator {
    private Long conversationId;
    private Integer senderId;
    private String senderName;
    private boolean typing;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/UpdateEmployeeRequest.java`**
```java
package com.revworkforce.employeemanagementservice.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmployeeRequest {
    @Size(max = 100)
    private String firstName;
    @Size(max = 100)
    private String lastName;
    @Email(message = "Invalid email format")
    private String email;
    @Size(max = 20)
    private String phone;
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
    @Size(max = 100)
    private String emergencyContactName;
    @Size(max = 20)
    private String emergencyContactPhone;
    private Integer departmentId;
    private Integer designationId;
    private LocalDate joiningDate;
    private BigDecimal salary;
    private String role;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/UpdateProfileRequest.java`**
```java
package com.revworkforce.employeemanagementservice.dto;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Phone must be 10-15 digits, optionally starting with +")
    private String phone;
    private String address;
    @Size(max = 100, message = "Emergency contact name must not exceed 100 characters")
    private String emergencyContactName;
    @Size(max = 20, message = "Emergency contact phone must not exceed 20 characters")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Emergency phone must be 10-15 digits, optionally starting with +")
    private String emergencyContactPhone;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/dto/VerifyOtpRequest.java`**
```java
package com.revworkforce.employeemanagementservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyOtpRequest {
    @NotBlank(message = "Pre-auth token is required")
    private String preAuthToken;

    @NotBlank(message = "OTP is required")
    @Size(min = 6, max = 6, message = "OTP must be exactly 6 digits")
    private String otp;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/exception/AccessDeniedException.java`**
```java
package com.revworkforce.employeemanagementservice.exception;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message) {
        super(message);
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/exception/AccountDeactivatedException.java`**
```java
package com.revworkforce.employeemanagementservice.exception;

public class AccountDeactivatedException extends RuntimeException {
    public AccountDeactivatedException(String message) {
        super(message);
    }

    public AccountDeactivatedException(String employeeCode, String reason) {
        super(String.format("Account '%s' is deactivated. %s", employeeCode, reason));
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/exception/BadRequestException.java`**
```java
package com.revworkforce.employeemanagementservice.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/exception/DuplicateResourceException.java`**
```java
package com.revworkforce.employeemanagementservice.exception;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s already exists with %s: '%s'", resourceName, fieldName, fieldValue));
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/exception/GlobalExceptionHandler.java`**
```java
package com.revworkforce.employeemanagementservice.exception;

import com.revworkforce.employeemanagementservice.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse> handleBadRequest(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse> handleDuplicateResource(DuplicateResourceException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ApiResponse> handleInsufficientBalance(InsufficientBalanceException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse> handleUnauthorized(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(InvalidActionException.class)
    public ResponseEntity<ApiResponse> handleInvalidAction(InvalidActionException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(AccountDeactivatedException.class)
    public ResponseEntity<ApiResponse> handleAccountDeactivated(AccountDeactivatedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(IpBlockedException.class)
    public ResponseEntity<ApiResponse> handleIpBlocked(IpBlockedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(false, "Validation failed", errors));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = String.format("Invalid value '%s' for parameter '%s'. Expected type: %s",
                ex.getValue(), ex.getName(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(false, message));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse> handleNoResourceFound(NoResourceFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(false, "The requested resource was not found"));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "An unexpected error occurred: " + ex.getMessage()));
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/exception/InsufficientBalanceException.java`**
```java
package com.revworkforce.employeemanagementservice.exception;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String message) {
        super(message);
    }

    public InsufficientBalanceException(int available, int requested) {
        super(String.format("Insufficient leave balance. Available: %d, Requested: %d", available, requested));
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/exception/InvalidActionException.java`**
```java
package com.revworkforce.employeemanagementservice.exception;

public class InvalidActionException extends RuntimeException {
    public InvalidActionException(String message) {
        super(message);
    }

    public InvalidActionException(String action, String allowedActions) {
        super(String.format("Invalid action '%s'. Allowed actions: %s", action, allowedActions));
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/exception/IpBlockedException.java`**
```java
package com.revworkforce.employeemanagementservice.exception;

public class IpBlockedException extends RuntimeException {
    public IpBlockedException(String message) {
        super(message);
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/exception/ResourceNotFoundException.java`**
```java
package com.revworkforce.employeemanagementservice.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue));
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/exception/UnauthorizedException.java`**
```java
package com.revworkforce.employeemanagementservice.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/integration/OllamaClient.java`**
```java
package com.revworkforce.employeemanagementservice.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OllamaClient {
    private static final Logger log = LoggerFactory.getLogger(OllamaClient.class);

    @Value("${ollama.base-url:http://localhost:11434}")
    private String baseUrl;
    @Value("${ollama.model:phi3}")
    private String model;
    @Value("${ollama.vision-model:llava}")
    private String visionModel;
    @Value("${ollama.timeout:60000}")
    private int timeout;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private volatile Boolean cachedAvailable = null;
    private volatile long cachedAt = 0;
    private static final long CACHE_TTL_MS = 60_000;

    private volatile RestTemplate cachedRestTemplate;

    private RestTemplate getRestTemplate() {
        if (cachedRestTemplate == null) {
            synchronized (this) {
                if (cachedRestTemplate == null) {
                    SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
                    factory.setConnectTimeout(java.time.Duration.ofMillis(5000));
                    factory.setReadTimeout(java.time.Duration.ofMillis(timeout));
                    cachedRestTemplate = new RestTemplate(factory);
                }
            }
        }
        return cachedRestTemplate;
    }

    public boolean isAvailable() {
        long now = System.currentTimeMillis();
        if (cachedAvailable != null && (now - cachedAt) < CACHE_TTL_MS) {
            return cachedAvailable;
        }
        boolean result = checkAvailability();
        cachedAvailable = result;
        cachedAt = now;
        return result;
    }

    public void refreshAvailability() {
        cachedAvailable = null;
        cachedAt = 0;
    }

    private boolean checkAvailability() {
        try {
            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            factory.setConnectTimeout(java.time.Duration.ofMillis(2000));
            factory.setReadTimeout(java.time.Duration.ofMillis(2000));
            RestTemplate quickTemplate = new RestTemplate(factory);
            ResponseEntity<String> response = quickTemplate.getForEntity(baseUrl + "/api/tags", String.class);
            boolean ok = response.getStatusCode().is2xxSuccessful();
            if (ok) log.info("Ollama is available at {}", baseUrl);
            return ok;
        } catch (Exception e) {
            log.debug("Ollama not reachable at {}: {}", baseUrl, e.getMessage());
            return false;
        }
    }

    public String generate(String prompt) {
        return generate(prompt, 150);
    }

    public String generate(String prompt, int maxTokens) {
        String url = baseUrl + "/api/generate";
        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("prompt", prompt);
        body.put("stream", false);

        Map<String, Object> options = new HashMap<>();
        options.put("num_predict", maxTokens);
        options.put("temperature", 0);
        options.put("top_k", 1);
        options.put("num_ctx", 1024);
        options.put("repeat_penalty", 1.0);
        options.put("num_thread", 4);
        body.put("options", options);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        try {
            log.info("Sending text prompt to Ollama model '{}' (length: {} chars, maxTokens: {})", model, prompt.length(), maxTokens);
            String jsonBody = objectMapper.writeValueAsString(body);
            HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);
            ResponseEntity<String> response = getRestTemplate().postForEntity(url, request, String.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                JsonNode root = objectMapper.readTree(response.getBody());
                String result = root.has("response") ? root.get("response").asText() : "No response from AI model.";
                log.info("Ollama responded successfully ({} chars)", result.length());
                return result;
            }
            log.error("Ollama returned status: {}", response.getStatusCode());
            return "Error: Received status " + response.getStatusCode();
        } catch (Exception e) {
            log.error("Error communicating with Ollama text model: {}", e.getMessage());
            return "Error communicating with AI model: " + e.getMessage();
        }
    }

    public String generateWithImage(String prompt, String base64Image) {
        String url = baseUrl + "/api/generate";
        Map<String, Object> body = new HashMap<>();
        body.put("model", visionModel);
        body.put("prompt", prompt);
        body.put("images", List.of(base64Image));
        body.put("stream", false);

        Map<String, Object> options = new HashMap<>();
        options.put("num_predict", 300);
        options.put("temperature", 0);
        options.put("top_k", 1);
        options.put("num_ctx", 1024);
        options.put("num_thread", 4);
        body.put("options", options);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        try {
            log.info("Sending image prompt to Ollama vision model '{}'", visionModel);
            String jsonBody = objectMapper.writeValueAsString(body);
            HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);
            ResponseEntity<String> response = getRestTemplate().postForEntity(url, request, String.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                JsonNode root = objectMapper.readTree(response.getBody());
                String result = root.has("response") ? root.get("response").asText() : "No response from AI model.";
                log.info("Ollama vision responded successfully ({} chars)", result.length());
                return result;
            }
            log.error("Ollama vision returned status: {}", response.getStatusCode());
            return "Error: Received status " + response.getStatusCode();
        } catch (Exception e) {
            log.error("Error communicating with Ollama vision model: {}", e.getMessage());
            return "Error: Vision model '" + visionModel + "' not available. " + e.getMessage();
        }
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/model/ActivityLog.java`**
```java
package com.revworkforce.employeemanagementservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "activity_log", indexes = {
        @Index(name = "idx_log_action", columnList = "action"),
        @Index(name = "idx_log_performed_by", columnList = "performed_by")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ActivityLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    @EqualsAndHashCode.Include
    private Integer logId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performed_by", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Employee performedBy;

    @Column(nullable = false, length = 100)
    private String action;

    @Column(name = "entity_type", length = 50)
    private String entityType;

    @Column(name = "entity_id")
    private Integer entityId;

    @Column(columnDefinition = "TEXT")
    private String details;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(length = 20)
    @Builder.Default
    private String status = "SUCCESS";

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/model/AllowedIpRange.java`**
```java
package com.revworkforce.employeemanagementservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "allowed_ip_range")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllowedIpRange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ip_range_id")
    private Integer ipRangeId;

    @Column(name = "ip_range", nullable = false, length = 50)
    private String ipRange;

    @Column(length = 200)
    private String description;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/model/Announcement.java`**
```java
package com.revworkforce.employeemanagementservice.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "announcement", indexes = {@Index(name = "idx_announce_active", columnList = "is_active")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"createdBy"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "announcement_id")
    @EqualsAndHashCode.Include
    private Integer announcementId;
    @Column(nullable = false, length = 200)
    private String title;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Employee createdBy;
    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/model/Attendance.java`**
```java
package com.revworkforce.employeemanagementservice.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import com.revworkforce.employeemanagementservice.model.enums.AttendanceStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance", uniqueConstraints = {
        @UniqueConstraint(name = "uk_emp_attendance_date", columnNames = {"employee_id", "attendance_date"})
}, indexes = {
        @Index(name = "idx_attendance_emp", columnList = "employee_id"),
        @Index(name = "idx_attendance_date", columnList = "attendance_date"),
        @Index(name = "idx_attendance_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"employee"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    @EqualsAndHashCode.Include
    private Integer attendanceId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Employee employee;

    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;

    @Column(name = "check_in_time")
    private LocalDateTime checkInTime;

    @Column(name = "check_out_time")
    private LocalDateTime checkOutTime;

    @Column(name = "total_hours")
    private Double totalHours;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    @Builder.Default
    private AttendanceStatus status = AttendanceStatus.PRESENT;

    @Column(name = "check_in_ip", length = 45)
    private String checkInIp;

    @Column(name = "check_out_ip", length = 45)
    private String checkOutIp;

    @Column(name = "check_in_latitude")
    private Double checkInLatitude;

    @Column(name = "check_in_longitude")
    private Double checkInLongitude;

    @Column(name = "check_out_latitude")
    private Double checkOutLatitude;

    @Column(name = "check_out_longitude")
    private Double checkOutLongitude;

    @Column(name = "location_verified")
    @Builder.Default
    private Boolean locationVerified = false;

    @Column(name = "check_in_distance_meters")
    private Double checkInDistanceMeters;

    @Column(name = "check_out_distance_meters")
    private Double checkOutDistanceMeters;

    @Column(name = "office_location_name", length = 100)
    private String officeLocationName;

    @Column(name = "notes", length = 500)
    private String notes;

    @Column(name = "is_late")
    @Builder.Default
    private Boolean isLate = false;

    @Column(name = "is_early_departure")
    @Builder.Default
    private Boolean isEarlyDeparture = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Transient
    public Double getCalculatedHours() {
        if (checkInTime != null && checkOutTime != null) {
            Duration duration = Duration.between(checkInTime, checkOutTime);
            return Math.round(duration.toMinutes() / 60.0 * 100.0) / 100.0;
        }
        return 0.0;
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/model/ChatConversation.java`**
```java
package com.revworkforce.employeemanagementservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_conversation", uniqueConstraints = {
        @UniqueConstraint(name = "uk_conversation_participants", columnNames = {"participant1_id", "participant2_id"})
}, indexes = {
        @Index(name = "idx_conv_p1", columnList = "participant1_id"),
        @Index(name = "idx_conv_p2", columnList = "participant2_id"),
        @Index(name = "idx_conv_last_msg", columnList = "last_message_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"participant1", "participant2"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ChatConversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conversation_id")
    @EqualsAndHashCode.Include
    private Long conversationId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "participant1_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Employee participant1;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "participant2_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Employee participant2;

    @Column(name = "last_message_text", length = 500)
    private String lastMessageText;

    @Column(name = "last_message_at")
    private LocalDateTime lastMessageAt;

    @Column(name = "last_sender_id")
    private Integer lastSenderId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/model/ChatMessage.java`**
```java
package com.revworkforce.employeemanagementservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import com.revworkforce.employeemanagementservice.model.enums.MessageType;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_message", indexes = {
        @Index(name = "idx_msg_conversation", columnList = "conversation_id"),
        @Index(name = "idx_msg_sender", columnList = "sender_id"),
        @Index(name = "idx_msg_created", columnList = "created_at"),
        @Index(name = "idx_msg_read", columnList = "is_read")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"conversation", "sender"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    @EqualsAndHashCode.Include
    private Long messageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ChatConversation conversation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Employee sender;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", nullable = false, length = 10)
    @Builder.Default
    private MessageType messageType = MessageType.TEXT;

    @Column(name = "file_url", length = 500)
    private String fileUrl;

    @Column(name = "file_name", length = 255)
    private String fileName;

    @Column(name = "is_read")
    @Builder.Default
    private Boolean isRead = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/model/Department.java`**
```java
package com.revworkforce.employeemanagementservice.model;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "department")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Integer departmentId;
    @Column(name = "department_name", nullable = false, unique = true, length = 100)
    private String departmentName;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;
    @CreationTimestamp
    @Column(name="created_at", updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/model/Designation.java`**
```java
package com.revworkforce.employeemanagementservice.model;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name="designation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Designation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "designation_id")
    private Integer designationId;
    @Column(name = "designation_name", nullable = false, unique = true, length = 100)
    private String designationName;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/model/Employee.java`**
```java
package com.revworkforce.employeemanagementservice.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import com.revworkforce.employeemanagementservice.model.enums.Gender;
import com.revworkforce.employeemanagementservice.model.enums.Role;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "employee", indexes = {
        @Index(name ="idx_emp_email", columnList = "email"),
        @Index(name = "idx_emp_name", columnList = "first_name, last_name"),
        @Index(name = "idx_emp_dept", columnList = "department_id"),
        @Index(name = "idx_emp_manager", columnList = "manager_code"),
        @Index(name = "idx_emp_role", columnList = "role")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"manager", "department", "designation"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    @EqualsAndHashCode.Include
    private Integer employeeId;
    @Column(name = "employee_code", nullable = false, unique = true, length = 20)
    private String employeeCode;
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;
    @Column(nullable = false, unique = true, length = 255)
    private String email;
    @JsonIgnore
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;
    @Column(length = 20)
    private String phone;
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Gender gender;
    @Column(columnDefinition = "TEXT")
    private String address;
    @Column(name = "emergency_contact_name", length = 100)
    private String emergencyContactName;
    @Column(name = "emergency_contact_phone", length = 20)
    private String emergencyContactPhone;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Department department;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "designation_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Designation designation;
    @Column(name = "joining_date", nullable = false)
    private LocalDate joiningDate;
    @Column(precision = 12, scale = 2)
    private BigDecimal salary;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_code", referencedColumnName = "employee_code")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "manager"})
    private Employee manager;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    @Builder.Default
    private Role role = Role.EMPLOYEE;
    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;
    @Column(name = "two_factor_enabled")
    @Builder.Default
    private Boolean twoFactorEnabled = false;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/model/Expense.java`**
```java
package com.revworkforce.employeemanagementservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import com.revworkforce.employeemanagementservice.model.enums.ExpenseCategory;
import com.revworkforce.employeemanagementservice.model.enums.ExpenseStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "expense", indexes = {
        @Index(name = "idx_expense_emp", columnList = "employee_id"),
        @Index(name = "idx_expense_status", columnList = "status"),
        @Index(name = "idx_expense_date", columnList = "expense_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"employee", "actionedBy", "financeActionedBy", "items"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expense_id")
    @EqualsAndHashCode.Include
    private Integer expenseId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Employee employee;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private ExpenseCategory category = ExpenseCategory.OTHER;

    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Column(length = 10)
    @Builder.Default
    private String currency = "INR";

    @Column(name = "expense_date", nullable = false)
    private LocalDate expenseDate;

    @Column(name = "vendor_name", length = 200)
    private String vendorName;

    @Column(name = "invoice_number", length = 100)
    private String invoiceNumber;

    @Column(name = "receipt_url", length = 500)
    private String receiptUrl;

    @Column(name = "receipt_file_name", length = 255)
    private String receiptFileName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    @Builder.Default
    private ExpenseStatus status = ExpenseStatus.DRAFT;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "actioned_by")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Employee actionedBy;

    @Column(name = "manager_comments", columnDefinition = "TEXT")
    private String managerComments;

    @Column(name = "manager_action_date")
    private LocalDateTime managerActionDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "finance_actioned_by")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Employee financeActionedBy;

    @Column(name = "finance_comments", columnDefinition = "TEXT")
    private String financeComments;

    @Column(name = "finance_action_date")
    private LocalDateTime financeActionDate;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @Column(name = "submitted_date")
    private LocalDateTime submittedDate;

    @Column(name = "reimbursed_date")
    private LocalDateTime reimbursedDate;

    @Column(name = "ai_parsed", columnDefinition = "TEXT")
    private String aiParsedData;

    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ExpenseItem> items = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void addItem(ExpenseItem item) {
        items.add(item);
        item.setExpense(this);
    }
}


```

#### **`src/main/java/com/revworkforce/employeemanagementservice/model/ExpenseItem.java`**
```java
package com.revworkforce.employeemanagementservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "expense_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"expense"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ExpenseItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    @EqualsAndHashCode.Include
    private Integer itemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_id", nullable = false)
    @JsonIgnore
    private Expense expense;

    @Column(nullable = false, length = 200)
    private String description;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "quantity")
    @Builder.Default
    private Integer quantity = 1;
}


```

#### **`src/main/java/com/revworkforce/employeemanagementservice/model/Goal.java`**
```java
package com.revworkforce.employeemanagementservice.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import com.revworkforce.employeemanagementservice.model.enums.GoalPriority;
import com.revworkforce.employeemanagementservice.model.enums.GoalStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "goal", indexes = {
        @Index(name = "idx_goal_emp", columnList = "employee_id"),
        @Index(name = "idx_goal_year", columnList = "`year`")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"employee"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_id")
    @EqualsAndHashCode.Include
    private Integer goalId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Employee employee;
    @Column(nullable = false, length = 200)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(name = "`year`", nullable = false)
    private Integer year;
    @Column(nullable = false)
    private LocalDate deadline;
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private GoalPriority priority = GoalPriority.MEDIUM;
    @Enumerated(EnumType.STRING)
    @Column(length = 15)
    @Builder.Default
    private GoalStatus status = GoalStatus.NOT_STARTED;
    @Column
    @Builder.Default
    private Integer progress = 0;
    @Column(name = "manager_comments", columnDefinition = "TEXT")
    private String managerComments;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/model/Holiday.java`**
```java
package com.revworkforce.employeemanagementservice.model;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "holiday", indexes = {@Index(name = "idx_holiday_year", columnList = "`year`")})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Holiday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "holiday_id")
    private Integer holidayId;
    @Column(name = "holiday_name", nullable = false, length = 200)
    private String holidayName;
    @Column(name = "holiday_date", nullable = false, unique = true)
    private LocalDate holidayDate;
    @Column(length = 500)
    private String description;
    @Column(name = "`year`", nullable = false)
    private Integer year;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/model/LeaveApplication.java`**
```java
package com.revworkforce.employeemanagementservice.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import com.revworkforce.employeemanagementservice.model.enums.LeaveStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "leave_application", indexes = {
        @Index(name = "idx_leave_emp", columnList = "employee_id"),
        @Index(name = "idx_leave_status", columnList = "status"),
        @Index(name = "idx_leave_dates", columnList = "start_date, end_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"employee", "leaveType", "actionedBy"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class LeaveApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "leave_id")
    @EqualsAndHashCode.Include
    private Integer leaveId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Employee employee;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "leave_type_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private LeaveType leaveType;
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
    @Column(name = "total_days", nullable = false)
    private Integer totalDays;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String reason;
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private LeaveStatus status = LeaveStatus.PENDING;
    @Column(name = "manager_comments", columnDefinition = "TEXT")
    private String managerComments;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "actioned_by")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Employee actionedBy;
    @Column(name = "applied_date", updatable = false)
    private LocalDateTime appliedDate;
    @Column(name = "action_date")
    private LocalDateTime actionDate;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/model/LeaveBalance.java`**
```java
package com.revworkforce.employeemanagementservice.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "leave_balance", uniqueConstraints = {
        @UniqueConstraint(name = "uk_emp_leave_year", columnNames = {"employee_id", "leave_type_id", "`year`"})
}, indexes = {@Index(name = "idx_balance_year", columnList = "`year`")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"employee", "leaveType", "adjustedBy"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class LeaveBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "balance_id")
    @EqualsAndHashCode.Include
    private Integer balanceId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Employee employee;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "leave_type_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private LeaveType leaveType;
    @Column(name = "`year`", nullable = false)
    private Integer year;
    @Column(name = "total_leaves")
    @Builder.Default
    private Integer totalLeaves = 0;
    @Column(name = "used_leaves")
    @Builder.Default
    private Integer usedLeaves = 0;
    @Transient
    public Integer getAvailableBalance() {
        return (totalLeaves != null ? totalLeaves : 0) - (usedLeaves != null ? usedLeaves : 0);
    }
    @Column(name = "adjustment_reason", length = 500)
    private String adjustmentReason;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "adjusted_by")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Employee adjustedBy;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/model/LeaveType.java`**
```java
package com.revworkforce.employeemanagementservice.model;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name="leave_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class LeaveType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "leave_type_id")
    @EqualsAndHashCode.Include
    private Integer leaveTypeId;

    @Column(name = "leave_type_name", nullable = false, unique = true, length = 50)
    private String leaveTypeName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "default_days")
    @Builder.Default
    private Integer defaultDays = 0;

    @Column(name = "is_paid_leave")
    @Builder.Default
    private Boolean isPaidLeave = true;

    @Column(name = "is_carry_forward_enabled")
    @Builder.Default
    private Boolean isCarryForwardEnabled = false;

    @Column(name = "max_carry_forward_days")
    @Builder.Default
    private Integer maxCarryForwardDays = 0;

    @Column(name = "is_loss_of_pay")
    @Builder.Default
    private Boolean isLossOfPay = false;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/model/Notification.java`**
```java
package com.revworkforce.employeemanagementservice.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import com.revworkforce.employeemanagementservice.model.enums.NotificationType;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification", indexes = {
        @Index(name = "idx_notif_recipient", columnList = "recipient_id"),
        @Index(name = "idx_notif_read", columnList = "is_read")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"recipient"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    @EqualsAndHashCode.Include
    private Integer notificationId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recipient_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Employee recipient;
    @Column(nullable = false, length = 200)
    private String title;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NotificationType type;
    @Column(name = "is_read")
    @Builder.Default
    private Boolean isRead = false;
    @Column(name = "reference_id")
    private Integer referenceId;
    @Column(name = "reference_type", length = 50)
    private String referenceType;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/model/OfficeLocation.java`**
```java
package com.revworkforce.employeemanagementservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "office_location", indexes = {
        @Index(name = "idx_office_location_active", columnList = "is_active")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OfficeLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    @EqualsAndHashCode.Include
    private Integer locationId;

    @Column(name = "location_name", nullable = false, length = 100)
    private String locationName;

    @Column(name = "address", length = 500)
    private String address;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "radius_meters", nullable = false)
    @Builder.Default
    private Integer radiusMeters = 200;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/model/PerformanceReview.java`**
```java
package com.revworkforce.employeemanagementservice.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import com.revworkforce.employeemanagementservice.model.enums.ReviewStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "performance_review", indexes = {
        @Index(name = "idx_review_emp", columnList = "employee_id"),
        @Index(name = "idx_review_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"employee", "reviewer"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PerformanceReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    @EqualsAndHashCode.Include
    private Integer reviewId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Employee employee;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reviewer_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Employee reviewer;
    @Column(name = "review_period", nullable = false, length = 50)
    private String reviewPeriod;
    @Column(name = "key_deliverables", columnDefinition = "TEXT")
    private String keyDeliverables;
    @Column(columnDefinition = "TEXT")
    private String accomplishments;
    @Column(name = "areas_of_improvement", columnDefinition = "TEXT")
    private String areasOfImprovement;
    @Column(name = "self_assessment_rating")
    private Integer selfAssessmentRating;
    @Column(name = "manager_rating")
    private Integer managerRating;
    @Column(name = "manager_feedback", columnDefinition = "TEXT")
    private String managerFeedback;
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private ReviewStatus status = ReviewStatus.DRAFT;
    @Column(name = "submitted_date")
    private LocalDateTime submittedDate;
    @Column(name = "reviewed_date")
    private LocalDateTime reviewedDate;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/model/enums/AttendanceStatus.java`**
```java
package com.revworkforce.employeemanagementservice.model.enums;

public enum AttendanceStatus {
    PRESENT,
    ABSENT,
    HALF_DAY,
    ON_LEAVE,
    HOLIDAY,
    WEEKEND
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/model/enums/ExpenseCategory.java`**
```java
package com.revworkforce.employeemanagementservice.model.enums;

public enum ExpenseCategory {
    TRAVEL,
    MEALS,
    ACCOMMODATION,
    OFFICE_SUPPLIES,
    EQUIPMENT,
    SOFTWARE,
    TRAINING,
    CLIENT_ENTERTAINMENT,
    COMMUNICATION,
    MEDICAL,
    TRANSPORTATION,
    OTHER
}


```

#### **`src/main/java/com/revworkforce/employeemanagementservice/model/enums/ExpenseStatus.java`**
```java
package com.revworkforce.employeemanagementservice.model.enums;

public enum ExpenseStatus {
    DRAFT,
    SUBMITTED,
    MANAGER_APPROVED,
    FINANCE_APPROVED,
    REJECTED,
    REIMBURSED
}


```

#### **`src/main/java/com/revworkforce/employeemanagementservice/model/enums/Gender.java`**
```java
package com.revworkforce.employeemanagementservice.model.enums;

public enum Gender {
    MALE,
    FEMALE,
    OTHER
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/model/enums/GoalPriority.java`**
```java
package com.revworkforce.employeemanagementservice.model.enums;

public enum GoalPriority {
    HIGH,
    MEDIUM,
    LOW
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/model/enums/GoalStatus.java`**
```java
package com.revworkforce.employeemanagementservice.model.enums;

public enum GoalStatus {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/model/enums/LeaveStatus.java`**
```java
package com.revworkforce.employeemanagementservice.model.enums;

public enum LeaveStatus {
    PENDING,
    APPROVED,
    REJECTED,
    CANCELLED
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/model/enums/MessageType.java`**
```java
package com.revworkforce.employeemanagementservice.model.enums;

public enum MessageType {
    TEXT,
    IMAGE,
    FILE
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/model/enums/NotificationType.java`**
```java
package com.revworkforce.employeemanagementservice.model.enums;

public enum NotificationType {
    LEAVE_APPLIED,
    LEAVE_APPROVED,
    LEAVE_REJECTED,
    LEAVE_CANCELLED,
    REVIEW_SUBMITTED,
    REVIEW_FEEDBACK,
    GOAL_UPDATED,
    GOAL_COMMENT,
    ANNOUNCEMENT,
    CHAT_MESSAGE,
    EXPENSE_SUBMITTED,
    EXPENSE_APPROVED,
    EXPENSE_REJECTED,
    EXPENSE_REIMBURSED,
    GENERAL
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/model/enums/ReviewStatus.java`**
```java
package com.revworkforce.employeemanagementservice.model.enums;

public enum ReviewStatus {
    DRAFT,
    SUBMITTED,
    REVIEWED
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/model/enums/Role.java`**
```java
package com.revworkforce.employeemanagementservice.model.enums;

public enum Role {
    EMPLOYEE,
    MANAGER,
    ADMIN
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/repository/ActivityLogRepository.java`**
```java
package com.revworkforce.employeemanagementservice.repository;

import com.revworkforce.employeemanagementservice.model.ActivityLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Integer> {
    List<ActivityLog> findByEntityTypeAndEntityIdOrderByCreatedAtDesc(String entityType, Integer entityId);

    List<ActivityLog> findByPerformedBy_EmployeeIdOrderByCreatedAtDesc(Integer employeeId);

    Page<ActivityLog> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<ActivityLog> findByEntityTypeOrderByCreatedAtDesc(String entityType, Pageable pageable);

    Page<ActivityLog> findByPerformedBy_EmployeeIdOrderByCreatedAtDesc(Integer employeeId, Pageable pageable);

    Page<ActivityLog> findByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/repository/AllowedIpRangeRepository.java`**
```java
package com.revworkforce.employeemanagementservice.repository;

import com.revworkforce.employeemanagementservice.model.AllowedIpRange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AllowedIpRangeRepository extends JpaRepository<AllowedIpRange, Integer> {
    List<AllowedIpRange> findByIsActiveTrue();

    boolean existsByIpRange(String ipRange);

    List<AllowedIpRange> findAllByOrderByCreatedAtDesc();
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/repository/AnnouncementRepository.java`**
```java
package com.revworkforce.employeemanagementservice.repository;
import com.revworkforce.employeemanagementservice.model.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {
    Page<Announcement> findByIsActiveOrderByCreatedAtDesc(Boolean isActive, Pageable pageable);
    Page<Announcement> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Announcement> findByCreatedBy_EmployeeIdOrderByCreatedAtDesc(Integer employeeId, Pageable pageable);
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/repository/AttendanceRepository.java`**
```java
package com.revworkforce.employeemanagementservice.repository;

import com.revworkforce.employeemanagementservice.model.Attendance;
import com.revworkforce.employeemanagementservice.model.enums.AttendanceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    Optional<Attendance> findByEmployee_EmployeeIdAndAttendanceDate(Integer employeeId, LocalDate attendanceDate);

    Page<Attendance> findByEmployee_EmployeeIdOrderByAttendanceDateDesc(Integer employeeId, Pageable pageable);

    Page<Attendance> findByEmployee_EmployeeIdAndAttendanceDateBetweenOrderByAttendanceDateDesc(
            Integer employeeId, LocalDate startDate, LocalDate endDate, Pageable pageable);

    List<Attendance> findByEmployee_EmployeeIdAndAttendanceDateBetween(
            Integer employeeId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT a FROM Attendance a WHERE a.employee.manager.employeeCode = :managerCode " +
           "AND a.attendanceDate = :date ORDER BY a.checkInTime ASC")
    List<Attendance> findTeamAttendanceByDate(
            @Param("managerCode") String managerCode, @Param("date") LocalDate date);

    @Query("SELECT a FROM Attendance a WHERE a.employee.manager.employeeCode = :managerCode " +
           "AND a.attendanceDate BETWEEN :startDate AND :endDate ORDER BY a.attendanceDate DESC, a.checkInTime ASC")
    List<Attendance> findTeamAttendanceBetween(
            @Param("managerCode") String managerCode,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.employee.employeeId = :employeeId " +
           "AND a.attendanceDate BETWEEN :startDate AND :endDate AND a.status = :status")
    long countByEmployeeAndDateRangeAndStatus(
            @Param("employeeId") Integer employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("status") AttendanceStatus status);

    @Query("SELECT COALESCE(SUM(a.totalHours), 0) FROM Attendance a WHERE a.employee.employeeId = :employeeId " +
           "AND a.attendanceDate BETWEEN :startDate AND :endDate")
    Double getTotalHoursByEmployeeAndDateRange(
            @Param("employeeId") Integer employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    boolean existsByEmployee_EmployeeIdAndAttendanceDate(Integer employeeId, LocalDate attendanceDate);

    @Query("SELECT a FROM Attendance a WHERE a.attendanceDate = :date ORDER BY a.employee.firstName ASC")
    Page<Attendance> findAllByDate(@Param("date") LocalDate date, Pageable pageable);

    @Query("SELECT a.employee.employeeId, COUNT(a) FROM Attendance a " +
           "WHERE a.attendanceDate BETWEEN :startDate AND :endDate AND a.isLate = true " +
           "GROUP BY a.employee.employeeId")
    List<Object[]> countLateArrivalsPerEmployee(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/repository/ChatConversationRepository.java`**
```java
package com.revworkforce.employeemanagementservice.repository;

import com.revworkforce.employeemanagementservice.model.ChatConversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatConversationRepository extends JpaRepository<ChatConversation, Long> {
    @Query("SELECT c FROM ChatConversation c WHERE " +
            "(c.participant1.employeeId = :empId OR c.participant2.employeeId = :empId) " +
            "ORDER BY c.lastMessageAt DESC NULLS LAST")
    List<ChatConversation> findAllByParticipant(@Param("empId") Integer empId);

    @Query("SELECT c FROM ChatConversation c WHERE " +
            "(c.participant1.employeeId = :emp1 AND c.participant2.employeeId = :emp2) OR " +
            "(c.participant1.employeeId = :emp2 AND c.participant2.employeeId = :emp1)")
    Optional<ChatConversation> findByParticipants(@Param("emp1") Integer emp1, @Param("emp2") Integer emp2);
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/repository/ChatMessageRepository.java`**
```java
package com.revworkforce.employeemanagementservice.repository;

import com.revworkforce.employeemanagementservice.model.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    Page<ChatMessage> findByConversation_ConversationIdOrderByCreatedAtDesc(Long conversationId, Pageable pageable);

    long countByConversation_ConversationIdAndIsReadAndSender_EmployeeIdNot(
            Long conversationId, Boolean isRead, Integer senderId);

    @Query("SELECT COUNT(m) FROM ChatMessage m WHERE m.conversation.conversationId IN " +
            "(SELECT c.conversationId FROM ChatConversation c WHERE " +
            "c.participant1.employeeId = :empId OR c.participant2.employeeId = :empId) " +
            "AND m.isRead = false AND m.sender.employeeId != :empId")
    long countTotalUnreadForUser(@Param("empId") Integer empId);

    @Modifying
    @Query("UPDATE ChatMessage m SET m.isRead = true WHERE m.conversation.conversationId = :convId " +
            "AND m.sender.employeeId != :readerId AND m.isRead = false")
    int markConversationAsRead(@Param("convId") Long conversationId, @Param("readerId") Integer readerId);
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/repository/DepartmentRepository.java`**
```java
package com.revworkforce.employeemanagementservice.repository;
import com.revworkforce.employeemanagementservice.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    Optional<Department> findByDepartmentName(String departmentName);
    boolean existsByDepartmentName(String departmentName);
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/repository/DesignationRepository.java`**
```java
package com.revworkforce.employeemanagementservice.repository;
import com.revworkforce.employeemanagementservice.model.Designation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DesignationRepository extends JpaRepository<Designation, Integer> {
    Optional<Designation> findByDesignationName(String designationName);
    boolean existsByDesignationName(String designationName);
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/repository/EmployeeRepository.java`**
```java
package com.revworkforce.employeemanagementservice.repository;
import com.revworkforce.employeemanagementservice.model.Employee;
import com.revworkforce.employeemanagementservice.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Optional<Employee> findByEmail(String email);
    Optional<Employee> findByEmployeeCode(String employeeCode);
    boolean existsByEmail(String email);
    boolean existsByEmployeeCode(String employeeCode);
    boolean existsByRole(Role role);
    @Query(value = "SELECT employee_code FROM employee WHERE employee_code LIKE CONCAT(:prefix, '%') ORDER BY employee_code DESC LIMIT 1", nativeQuery = true)
    Optional<String> findLatestEmployeeCodeByPrefix(@Param("prefix") String prefix);
    Page<Employee> findByIsActive(Boolean isActive, Pageable pageable);
    Page<Employee> findByRole(Role role, Pageable pageable);
    Page<Employee> findByDepartment_DepartmentId(Integer departmentId, Pageable pageable);
    @Query("SELECT e FROM Employee e WHERE LOWER(e.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(e.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(e.employeeCode) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Employee> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
    Page<Employee> findByManager_EmployeeCode(String employeeCode, Pageable pageable);
    List<Employee> findByManager_EmployeeCode(String employeeCode);
    long countByIsActive(Boolean isActive);
    long countByRole(Role role);
    long countByRoleAndIsActive(Role role, Boolean isActive);
    @Query("SELECT e.department.departmentName, COUNT(e) FROM Employee e WHERE e.department IS NOT NULL AND e.isActive = true GROUP BY e.department.departmentName")
    List<Object[]> countActiveByDepartment();
    long countByDepartment_DepartmentId(Integer departmentId);

    @Query("SELECT FUNCTION('YEAR', e.joiningDate), FUNCTION('MONTH', e.joiningDate), COUNT(e) FROM Employee e WHERE e.isActive = true GROUP BY FUNCTION('YEAR', e.joiningDate), FUNCTION('MONTH', e.joiningDate) ORDER BY FUNCTION('YEAR', e.joiningDate) DESC, FUNCTION('MONTH', e.joiningDate) DESC")
    List<Object[]> getJoiningTrends();

    @Query("SELECT e.role, COUNT(e) FROM Employee e WHERE e.isActive = true GROUP BY e.role")
    List<Object[]> countActiveByRole();

    List<Employee> findByRoleAndIsActive(Role role, Boolean isActive);
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/repository/ExpenseRepository.java`**
```java
package com.revworkforce.employeemanagementservice.repository;

import com.revworkforce.employeemanagementservice.model.Expense;
import com.revworkforce.employeemanagementservice.model.enums.ExpenseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
    Page<Expense> findByEmployeeEmployeeIdAndStatusIn(Integer employeeId, List<ExpenseStatus> statuses, Pageable pageable);
    Page<Expense> findByEmployeeEmployeeId(Integer employeeId, Pageable pageable);

    @Query("SELECT e FROM Expense e WHERE e.employee.manager.employeeId = :managerId AND e.status = :status")
    Page<Expense> findTeamExpensesByStatus(@Param("managerId") Integer managerId,
                                           @Param("status") ExpenseStatus status,
                                           Pageable pageable);

    Page<Expense> findByStatus(ExpenseStatus status, Pageable pageable);

    Page<Expense> findByStatusIn(List<ExpenseStatus> statuses, Pageable pageable);

    @Query("SELECT COUNT(e) FROM Expense e WHERE e.employee.employeeId = :empId AND e.status = :status")
    long countByEmployeeAndStatus(@Param("empId") Integer employeeId, @Param("status") ExpenseStatus status);

    @Query("SELECT COALESCE(SUM(e.totalAmount), 0) FROM Expense e WHERE e.employee.employeeId = :empId AND e.status = :status")
    BigDecimal sumAmountByEmployeeAndStatus(@Param("empId") Integer employeeId, @Param("status") ExpenseStatus status);

    @Query("SELECT COALESCE(SUM(e.totalAmount), 0) FROM Expense e WHERE e.employee.employeeId = :empId " +
           "AND e.status = 'REIMBURSED' AND e.expenseDate BETWEEN :start AND :end")
    BigDecimal sumReimbursedInPeriod(@Param("empId") Integer employeeId,
                                     @Param("start") LocalDate start,
                                     @Param("end") LocalDate end);

    long countByStatus(ExpenseStatus status);

    @Query("SELECT COALESCE(SUM(e.totalAmount), 0) FROM Expense e WHERE e.status = :status")
    BigDecimal sumTotalAmountByStatus(@Param("status") ExpenseStatus status);
}


```

#### **`src/main/java/com/revworkforce/employeemanagementservice/repository/GoalRepository.java`**
```java
package com.revworkforce.employeemanagementservice.repository;

import com.revworkforce.employeemanagementservice.model.Goal;
import com.revworkforce.employeemanagementservice.model.enums.GoalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Integer> {
    Page<Goal> findByEmployee_EmployeeId(Integer employeeId, Pageable pageable);

    Page<Goal> findByEmployee_EmployeeIdAndYear(Integer employeeId, Integer year, Pageable pageable);

    Page<Goal> findByEmployee_EmployeeIdAndStatus(Integer employeeId, GoalStatus status, Pageable pageable);

    @Query("select g from Goal g where g.employee.employeeCode = :employeeCode and g.employee.manager.employeeCode = :managerCode")
    Page<Goal> findByEmployeeCodeAndManagerCode(@Param("employeeCode") String employeeCode,
                                                @Param("managerCode") String managerCode,
                                                Pageable pageable);

    @Query("select g from Goal g where g.employee.manager.employeeCode = :managerCode")
    Page<Goal> findByManagerCode(@Param("managerCode") String managerCode, Pageable pageable);
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/repository/HolidayRepository.java`**
```java
package com.revworkforce.employeemanagementservice.repository;
import com.revworkforce.employeemanagementservice.model.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Integer>{
    List<Holiday> findByYearOrderByHolidayDateAsc(Integer year);
    boolean existsByHolidayDate(LocalDate holidayDate);
    List<Holiday> findByHolidayDateBetween(LocalDate startDate, LocalDate endDate);
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/repository/LeaveApplicationRepository.java`**
```java
package com.revworkforce.employeemanagementservice.repository;
import com.revworkforce.employeemanagementservice.model.LeaveApplication;
import com.revworkforce.employeemanagementservice.model.enums.LeaveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication, Integer>{
    Page<LeaveApplication> findByEmployee_EmployeeId(Integer employeeId, Pageable pageable);
    Page<LeaveApplication> findByEmployee_EmployeeIdAndStatus(Integer employeeId, LeaveStatus status, Pageable pageable);
    @Query("select la from LeaveApplication la where la.employee.manager.employeeCode = :managerCode")
    Page<LeaveApplication> findByManagerCode(@Param("managerCode") String managerCode, Pageable pageable);
    @Query("select la from LeaveApplication la where la.employee.manager.employeeCode = :managerCode AND la.status = :status")
    Page<LeaveApplication> findByManagerCodeAndStatus(@Param("managerCode") String managerCode, @Param("status") LeaveStatus status, Pageable pageable);
    @Query("select la from LeaveApplication la where la.employee.employeeId = :employeeId and la.status <> :cancelledStatus and la.status <> :rejectedStatus and la.startDate <= :endDate and la.endDate >= :startDate")
    List<LeaveApplication> findOverlappingLeaves(@Param("employeeId") Integer employeeId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("cancelledStatus") LeaveStatus cancelledStatus, @Param("rejectedStatus") LeaveStatus rejectedStatus);
    long countByStatus(LeaveStatus status);
    @Query("SELECT la FROM LeaveApplication la WHERE la.status = :status AND la.startDate <= :today AND la.endDate >= :today")
    List<LeaveApplication> findActiveLeavesToday(@Param("status") LeaveStatus status, @Param("today") LocalDate today);
    @Query("SELECT la FROM LeaveApplication la WHERE la.employee.department.departmentId = :departmentId")
    Page<LeaveApplication> findByDepartmentId(@Param("departmentId") Integer departmentId, Pageable pageable);

    long countByEmployee_EmployeeIdAndStatus(Integer employeeId, LeaveStatus status);

    Page<LeaveApplication> findByStatus(LeaveStatus status, Pageable pageable);

    List<LeaveApplication> findByEmployeeEmployeeId(Integer employeeId);

    @Query("SELECT COUNT(DISTINCT la.employee.employeeId) FROM LeaveApplication la " +
           "WHERE la.employee.manager.employeeId = :managerId AND la.status = :status " +
           "AND la.startDate <= :date AND la.endDate >= :date")
    int countTeamOnLeave(@Param("managerId") Integer managerId,
                         @Param("status") LeaveStatus status,
                         @Param("date") LocalDate date);

    @Query("SELECT la FROM LeaveApplication la WHERE la.employee.manager.employeeCode = :managerCode AND la.status = :status AND la.startDate <= :endDate AND la.endDate >= :startDate")
    List<LeaveApplication> findTeamLeavesBetween(@Param("managerCode") String managerCode, @Param("status") LeaveStatus status, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("select la from LeaveApplication la where la.employee.manager.employeeCode = :managerCode AND la.employee.role <> com.revworkforce.employeemanagementservice.model.enums.Role.MANAGER")
    Page<LeaveApplication> findByManagerCodeExcludingManagers(@Param("managerCode") String managerCode, Pageable pageable);

    @Query("select la from LeaveApplication la where la.employee.manager.employeeCode = :managerCode AND la.status = :status AND la.employee.role <> com.revworkforce.employeemanagementservice.model.enums.Role.MANAGER")
    Page<LeaveApplication> findByManagerCodeAndStatusExcludingManagers(@Param("managerCode") String managerCode, @Param("status") LeaveStatus status, Pageable pageable);
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/repository/LeaveBalanceRepository.java`**
```java
package com.revworkforce.employeemanagementservice.repository;
import com.revworkforce.employeemanagementservice.model.LeaveBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Integer> {
    List<LeaveBalance> findByEmployee_EmployeeIdAndYear(Integer employeeId, Integer year);
    Optional<LeaveBalance> findByEmployee_EmployeeIdAndLeaveType_LeaveTypeIdAndYear(Integer employeeId, Integer leaveTypeId, Integer year);
    boolean existsByEmployee_EmployeeIdAndLeaveType_LeaveTypeIdAndYear(Integer employeeId, Integer leaveTypeId, Integer year);
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/repository/LeaveTypeRepository.java`**
```java
package com.revworkforce.employeemanagementservice.repository;
import com.revworkforce.employeemanagementservice.model.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveTypeRepository extends JpaRepository<LeaveType, Integer> {
    Optional<LeaveType> findByLeaveTypeName(String leaveTypeName);
    List<LeaveType> findByIsActive(Boolean isActive);
    boolean existsByLeaveTypeName(String leaveTypeName);
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/repository/NotificationRepository.java`**
```java
package com.revworkforce.employeemanagementservice.repository;
import com.revworkforce.employeemanagementservice.model.Notification;
import com.revworkforce.employeemanagementservice.model.enums.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    Page<Notification> findByRecipient_EmployeeIdOrderByCreatedAtDesc(Integer employeeId, Pageable pageable);
    Page<Notification> findByRecipient_EmployeeIdAndIsReadOrderByCreatedAtDesc(Integer employeeId, Boolean isRead, Pageable pageable);
    long countByRecipient_EmployeeIdAndIsRead(Integer employeeId, Boolean isRead);
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.recipient.employeeId = :employeeId AND n.isRead = false")
    int markAllAsRead(@Param("employeeId") Integer employeeId);
    Page<Notification> findByRecipient_EmployeeIdAndTypeOrderByCreatedAtDesc(Integer employeeId, NotificationType type, Pageable pageable);
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/repository/OfficeLocationRepository.java`**
```java
package com.revworkforce.employeemanagementservice.repository;

import com.revworkforce.employeemanagementservice.model.OfficeLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfficeLocationRepository extends JpaRepository<OfficeLocation, Integer> {
    List<OfficeLocation> findByIsActiveTrue();

    List<OfficeLocation> findAllByOrderByCreatedAtDesc();

    boolean existsByLocationName(String locationName);
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/repository/PerformanceReviewRepository.java`**
```java
package com.revworkforce.employeemanagementservice.repository;

import com.revworkforce.employeemanagementservice.model.PerformanceReview;
import com.revworkforce.employeemanagementservice.model.enums.ReviewStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PerformanceReviewRepository extends JpaRepository<PerformanceReview, Integer> {
    Page<PerformanceReview> findByEmployee_EmployeeId(Integer employeeId, Pageable pageable);
    Page<PerformanceReview> findByEmployee_EmployeeIdAndStatus(Integer employeeId, ReviewStatus status, Pageable pageable);

    List<PerformanceReview> findByEmployeeEmployeeId(Integer employeeId);
    Optional<PerformanceReview> findByEmployee_EmployeeIdAndReviewPeriod(Integer employeeId, String reviewPeriod);
    @Query("SELECT pr from PerformanceReview pr where pr.employee.manager.employeeCode = :managerCode AND pr.status = :status")
    Page<PerformanceReview> findByManagerCodeAndStatus(@Param("managerCode") String managerCode, @Param("status") ReviewStatus status, Pageable pageable);
    @Query("select pr from PerformanceReview pr where pr.employee.manager.employeeCode = :managerCode")
    Page<PerformanceReview> findByManagerCode(@Param("managerCode") String managerCode, Pageable pageable);

    @Query("select pr from PerformanceReview pr where pr.employee.manager.employeeCode = :managerCode AND pr.status <> :status")
    Page<PerformanceReview> findByManagerCodeAndStatusNot(@Param("managerCode") String managerCode, @Param("status") ReviewStatus status, Pageable pageable);

    long countByEmployee_EmployeeIdAndStatus(Integer employeeId, ReviewStatus status);

    Page<PerformanceReview> findByStatus(ReviewStatus status, Pageable pageable);
    Page<PerformanceReview> findByStatusNot(ReviewStatus status, Pageable pageable);
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/service/AIService.java`**
```java
package com.revworkforce.employeemanagementservice.service;

import com.revworkforce.employeemanagementservice.dto.*;
import com.revworkforce.employeemanagementservice.integration.OllamaClient;
import com.revworkforce.employeemanagementservice.model.*;
import com.revworkforce.employeemanagementservice.model.enums.LeaveStatus;
import com.revworkforce.employeemanagementservice.model.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class AIService {
    @Autowired private OllamaClient ollamaClient;
    @Autowired private LeaveService leaveService;
    @Autowired private AttendanceService attendanceService;
    @Autowired private EmployeeService employeeService;
    @Autowired private DashboardService dashboardService;
    @Autowired private AnnouncementService announcementService;
    @Autowired private PerformanceService performanceService;
    @Autowired private NotificationService notificationService;

    private final ConcurrentHashMap<String, ConversationContext> activeFlows = new ConcurrentHashMap<>();

    private static class ConversationContext {
        String flow;
        int step;
        Map<String, String> data = new HashMap<>();
        long lastActivity = System.currentTimeMillis();

        ConversationContext(String flow) {
            this.flow = flow;
        }

        boolean isExpired() {
            return System.currentTimeMillis() - lastActivity > 600_000;
        }

        void touch() {
            lastActivity = System.currentTimeMillis();
        }
    }

    public AIChatResponse processMessage(String email, AIChatRequest request) {
        String message = request.getMessage().trim();
        String lower = message.toLowerCase();

        Role userRole = getUserRole(email);

        activeFlows.entrySet().removeIf(e -> e.getValue().isExpired());

        ConversationContext ctx = activeFlows.get(email);
        if (ctx != null && !ctx.isExpired()) {
            if (isAbortCommand(lower)) {
                activeFlows.remove(email);
                return reply("No problem, I've cancelled that. What else can I help you with?",
                        getDefaultQuickReplies(userRole));
            }
            ctx.touch();
            return continueFlow(email, ctx, message);
        }

        String intent = detectIntent(lower, userRole);
        if (intent != null) {
            return startAction(email, intent, message, userRole);
        }

        return handleGeneralChat(request, userRole);
    }

    private Role getUserRole(String email) {
        try {
            Employee emp = employeeService.getEmployeeByEmail(email);
            return emp.getRole();
        } catch (Exception e) {
            return Role.EMPLOYEE;
        }
    }

    private String[] getDefaultQuickReplies(Role role) {
        return switch (role) {
            case ADMIN -> new String[]{"Admin Dashboard", "All Leaves", "Team Attendance", "My Profile", "Help"};
            case MANAGER -> new String[]{"Team Leaves", "Team Attendance", "Apply Leave", "My Profile", "Help"};
            default -> new String[]{"Apply Leave", "My Leaves", "Check In", "My Profile", "Help"};
        };
    }

    private String detectIntent(String msg, Role userRole) {
        if (userRole == Role.MANAGER || userRole == Role.ADMIN) {
            if (containsAny(msg, "team leaves", "team leave", "team's leave",
                    "pending team", "my team leaves", "team requests")) {
                return "GET_TEAM_LEAVES";
            }
            if (containsAny(msg, "approve leave", "approve the leave", "accept leave")) {
                return "APPROVE_LEAVE";
            }
            if (containsAny(msg, "reject leave", "reject the leave", "decline leave", "deny leave")) {
                return "REJECT_LEAVE";
            }
            if (containsAny(msg, "team attendance", "team's attendance", "my team attendance",
                    "who is present", "team present", "who checked in")) {
                return "GET_TEAM_ATTENDANCE";
            }
            if (containsAny(msg, "team reviews", "team performance", "team review")) {
                return "GET_TEAM_REVIEWS";
            }
        }

        if (userRole == Role.ADMIN) {
            if (containsAny(msg, "admin dashboard", "admin stats", "admin overview",
                    "system dashboard", "workforce stats", "overall stats")) {
                return "GET_ADMIN_DASHBOARD";
            }
            if (containsAny(msg, "all leaves", "all leave applications", "all leave requests",
                    "company leaves", "everyone's leave")) {
                return "GET_ALL_LEAVES";
            }
        }

        if (containsAny(msg, "apply leave", "apply for leave", "request leave",
                "want leave", "need leave", "take leave", "apply sick",
                "apply casual", "apply earned", "want a leave", "need a leave",
                "take a leave", "apply a leave", "leave for me", "book leave",
                "submit leave", "apply for a leave", "leave apply", "leave request",
                "can you apply", "please apply", "i want to apply")) {
            return "APPLY_LEAVE";
        }

        if (containsAny(msg, "cancel leave", "cancel my leave", "withdraw leave",
                "revoke leave", "delete leave", "cancel a leave")) {
            return "CANCEL_LEAVE";
        }

        if (containsAny(msg, "check in", "checkin", "check-in", "clock in",
                "punch in", "mark attendance", "start work", "mark my attendance")) {
            if (!containsAny(msg, "did i check", "have i check", "am i check",
                    "today check", "my check-in", "check in today", "checked in")) {
                return "CHECK_IN";
            }
        }

        if (containsAny(msg, "check out", "checkout", "check-out", "clock out",
                "punch out", "end work", "done for today", "log off", "sign off")) {
            return "CHECK_OUT";
        }

        if (containsAny(msg, "update profile", "update my profile", "change my phone",
                "change phone", "update phone", "change address", "update address",
                "update emergency", "change emergency", "edit profile", "edit my profile")) {
            return "UPDATE_PROFILE";
        }

        if (containsAny(msg, "create goal", "new goal", "add goal", "set goal",
                "create a goal", "add a goal", "set a goal")) {
            return "CREATE_GOAL";
        }

        if (containsAny(msg, "leave balance", "how many leaves", "my leaves balance",
                "leave details", "leaves do i have", "remaining leaves",
                "available leaves", "check my leave", "show leave balance",
                "leave count", "leave remaining")) {
            return "GET_LEAVE_BALANCE";
        }

        if (containsAny(msg, "my leaves", "my leave applications", "my leave requests",
                "show my leaves", "list my leaves", "pending leaves",
                "my pending leave", "leave history", "leave applications")) {
            return "GET_MY_LEAVES";
        }

        if (containsAny(msg, "today attendance", "today's attendance", "did i check",
                "attendance today", "am i checked in", "have i checked in",
                "my check-in today", "today status", "checked in today")) {
            return "GET_ATTENDANCE_TODAY";
        }

        if (containsAny(msg, "attendance summary", "attendance report", "monthly attendance",
                "attendance this month", "my attendance", "attendance record",
                "attendance history", "show attendance", "present days", "absent days")) {
            return "GET_ATTENDANCE_SUMMARY";
        }

        if (containsAny(msg, "my dashboard", "dashboard summary", "show dashboard",
                "dashboard details", "my overview", "my summary")) {
            return "GET_MY_DASHBOARD";
        }

        if (containsAny(msg, "announcement", "announcements", "any announcement",
                "company news", "latest news", "notices", "company update",
                "recent announcements")) {
            return "GET_ANNOUNCEMENTS";
        }

        if (containsAny(msg, "my profile", "profile details", "show profile",
                "my details", "my information", "my info", "about me",
                "employee details", "show my profile", "view profile")) {
            return "GET_MY_PROFILE";
        }

        if (containsAny(msg, "holiday", "holidays", "upcoming holiday",
                "public holiday", "holiday list", "holiday dates", "next holiday",
                "upcoming holidays", "show holidays")) {
            return "GET_HOLIDAYS";
        }

        if (containsAny(msg, "my goals", "show goals", "show my goals",
                "goal progress", "my targets", "list goals", "view goals")) {
            return "GET_MY_GOALS";
        }

        if (containsAny(msg, "my reviews", "performance review", "my performance",
                "show reviews", "show my reviews", "review status",
                "my performance reviews")) {
            return "GET_MY_REVIEWS";
        }

        if (containsAny(msg, "notifications", "my notifications", "unread notifications",
                "notification count", "any notifications", "show notifications")) {
            return "GET_NOTIFICATIONS";
        }

        if (containsAny(msg, "help", "what can you do", "capabilities",
                "what do you do", "how to", "commands", "features",
                "what all can you", "guide me")) {
            return "HELP";
        }

        if (containsAny(msg, "hello", "hi", "hey", "good morning",
                "good afternoon", "good evening")) {
            return "GREETING";
        }
        return null;
    }

    private AIChatResponse startAction(String email, String intent, String message, Role userRole) {
        try {
            switch (intent) {
                case "APPLY_LEAVE":    return startApplyLeaveFlow(email, message);
                case "CANCEL_LEAVE":   return startCancelLeaveFlow(email);
                case "CHECK_IN":       return startCheckInFlow(email);
                case "CHECK_OUT":      return startCheckOutFlow(email);
                case "UPDATE_PROFILE": return startUpdateProfileFlow(email);
                case "CREATE_GOAL":    return startCreateGoalFlow(email, message);

                case "GET_LEAVE_BALANCE":     return getLeaveBalance(email);
                case "GET_MY_LEAVES":         return getMyLeaves(email);
                case "GET_ATTENDANCE_TODAY":   return getAttendanceToday(email);
                case "GET_ATTENDANCE_SUMMARY": return getAttendanceSummary(email);
                case "GET_MY_DASHBOARD":      return getDashboard(email);
                case "GET_ANNOUNCEMENTS":     return getAnnouncements();
                case "GET_MY_PROFILE":        return getProfile(email);
                case "GET_HOLIDAYS":          return getHolidays();
                case "GET_MY_GOALS":          return getMyGoals(email);
                case "GET_MY_REVIEWS":        return getMyReviews(email);
                case "GET_NOTIFICATIONS":     return getNotifications(email);
                case "HELP":                  return getHelp(userRole);
                case "GREETING":              return getGreeting(email, userRole);

                case "GET_TEAM_LEAVES":       return getTeamLeaves(email, userRole);
                case "APPROVE_LEAVE":         return startApproveLeavFlow(email, userRole);
                case "REJECT_LEAVE":          return startRejectLeaveFlow(email, userRole);
                case "GET_TEAM_ATTENDANCE":   return getTeamAttendance(email, userRole);
                case "GET_TEAM_REVIEWS":      return getTeamReviews(email, userRole);

                case "GET_ADMIN_DASHBOARD":   return getAdminDashboard(email, userRole);
                case "GET_ALL_LEAVES":        return getAllLeaves(email, userRole);

                default:
                    return reply("I'm not sure how to help with that. Type 'help' to see what I can do.",
                            getDefaultQuickReplies(userRole));
            }
        } catch (Exception e) {
            return reply("Sorry, something went wrong: " + e.getMessage(),
                    getDefaultQuickReplies(userRole));
        }
    }

    private AIChatResponse continueFlow(String email, ConversationContext ctx, String message) {
        try {
            switch (ctx.flow) {
                case "APPLY_LEAVE":    return continueApplyLeave(email, ctx, message);
                case "CANCEL_LEAVE":   return continueCancelLeave(email, ctx, message);
                case "CHECK_IN":       return continueCheckIn(email, ctx, message);
                case "CHECK_OUT":      return continueCheckOut(email, ctx, message);
                case "UPDATE_PROFILE": return continueUpdateProfile(email, ctx, message);
                case "CREATE_GOAL":    return continuteCreateGoal(email, ctx, message);
                case "APPROVE_LEAVE":  return continueApproveRejectLeave(email, ctx, message, "APPROVED");
                case "REJECT_LEAVE":   return continueApproveRejectLeave(email, ctx, message, "REJECTED");
                default:
                    activeFlows.remove(email);
                    return reply("Something went wrong. Let's start over.",
                            "Help", "Apply Leave", "My Profile");
            }
        } catch (Exception e) {
            activeFlows.remove(email);
            return reply("Sorry, an error occurred: " + e.getMessage() + "\nPlease try again.",
                    "Help", "Apply Leave");
        }
    }

    private AIChatResponse startApplyLeaveFlow(String email, String message) {
        ConversationContext ctx = new ConversationContext("APPLY_LEAVE");
        String lower = message.toLowerCase();

        List<LocalDate> dates = extractDates(message);
        if (dates.size() >= 1) {
            ctx.data.put("startDate", dates.get(0).toString());
        }
        if (dates.size() >= 2) {
            ctx.data.put("endDate", dates.get(1).toString());
        }

        Integer typeId = detectLeaveTypeFromMessage(lower);
        if (typeId != null) {
            ctx.data.put("leaveTypeId", typeId.toString());
            try {
                List<LeaveType> types = leaveService.getAllLeaveType();
                types.stream().filter(t -> t.getLeaveTypeId().equals(typeId)).findFirst()
                        .ifPresent(t -> ctx.data.put("leaveTypeName", t.getLeaveTypeName()));
            } catch (Exception ignored) {}
        }

        String reason = extractReason(message);
        if (reason != null) {
            ctx.data.put("reason", reason);
        }

        activeFlows.put(email, ctx);
        return askNextLeaveField(ctx);
    }

    private AIChatResponse continueApplyLeave(String email, ConversationContext ctx, String msg) {
        String lower = msg.toLowerCase().trim();
        int step = ctx.step;

        if (step == 0 && !ctx.data.containsKey("leaveTypeId")) {
            Integer typeId = detectLeaveTypeFromInput(lower);
            if (typeId == null) {
                return reply("I didn't recognize that leave type. Please choose one:",
                        getLeaveTypeNames().toArray(new String[0]));
            }
            ctx.data.put("leaveTypeId", typeId.toString());
            try {
                List<LeaveType> types = leaveService.getAllLeaveType();
                types.stream().filter(t -> t.getLeaveTypeId().equals(typeId)).findFirst()
                        .ifPresent(t -> ctx.data.put("leaveTypeName", t.getLeaveTypeName()));
            } catch (Exception ignored) {
                ctx.data.put("leaveTypeName", "Leave Type #" + typeId);
            }
            return askNextLeaveField(ctx);
        }

        if (step == 1 && !ctx.data.containsKey("startDate")) {
            LocalDate date = parseNaturalDate(msg);
            if (date == null) {
                return reply("I couldn't understand that date. Please try a format like:\n• March 15\n• 2026-03-15\n• tomorrow\n• next Monday");
            }
            if (date.isBefore(LocalDate.now())) {
                return reply("That date is in the past. Please provide a future date.");
            }
            ctx.data.put("startDate", date.toString());
            return askNextLeaveField(ctx);
        }

        if (step == 2 && !ctx.data.containsKey("endDate")) {
            if (lower.equals("same day") || lower.equals("single day") || lower.equals("1 day")
                    || lower.equals("one day") || lower.equals("same")) {
                ctx.data.put("endDate", ctx.data.get("startDate"));
            } else {
                LocalDate date = parseNaturalDate(msg);
                if (date == null) {
                    return reply("I couldn't understand that date. Please try again or select:",
                            "Same day");
                }
                LocalDate start = LocalDate.parse(ctx.data.get("startDate"));
                if (date.isBefore(start)) {
                    return reply("End date can't be before start date (" + formatDate(start) + "). Please try again.",
                            "Same day");
                }
                ctx.data.put("endDate", date.toString());
            }
            return askNextLeaveField(ctx);
        }

        if (step == 3 && !ctx.data.containsKey("reason")) {
            if (msg.length() < 2) {
                return reply("Please provide a brief reason for your leave.");
            }
            ctx.data.put("reason", msg);
            return askNextLeaveField(ctx);
        }

        if (step == 4) {
            if (lower.contains("confirm") || lower.contains("yes") || lower.contains("submit")
                    || lower.contains("apply") || lower.contains("proceed")) {
                return executeApplyLeave(email, ctx);
            } else if (lower.contains("cancel") || lower.contains("no") || lower.contains("discard")) {
                activeFlows.remove(email);
                return reply("Leave application cancelled. What else can I help with?",
                        "Apply Leave", "My Leaves", "Help");
            } else {
                return reply("Please confirm or cancel your leave application.",
                        "✅ Confirm", "❌ Cancel");
            }
        }

        return askNextLeaveField(ctx);
    }

    private AIChatResponse askNextLeaveField(ConversationContext ctx) {
        if (!ctx.data.containsKey("leaveTypeId")) {
            ctx.step = 0;
            StringBuilder sb = new StringBuilder("Sure! I'll help you apply for leave.");
            if (ctx.data.containsKey("startDate")) {
                sb.append("\n📅 Starting: ").append(formatDate(LocalDate.parse(ctx.data.get("startDate"))));
            }
            sb.append("\n\nWhat type of leave would you like?");
            return reply(sb.toString(), getLeaveTypeNames().toArray(new String[0]));
        }

        if (!ctx.data.containsKey("startDate")) {
            ctx.step = 1;
            return reply("Got it, " + ctx.data.get("leaveTypeName") + ".\n\nFrom which date?\n(e.g., March 15, tomorrow, next Monday)",
                    "Tomorrow", "Next Monday");
        }

        if (!ctx.data.containsKey("endDate")) {
            ctx.step = 2;
            String startStr = formatDate(LocalDate.parse(ctx.data.get("startDate")));
            return reply(ctx.data.get("leaveTypeName") + " from " + startStr + ".\n\nTill which date?",
                    "Same day", formatDate(LocalDate.parse(ctx.data.get("startDate")).plusDays(1)),
                    formatDate(LocalDate.parse(ctx.data.get("startDate")).plusDays(2)));
        }

        if (!ctx.data.containsKey("reason")) {
            ctx.step = 3;
            return reply("What's the reason for your leave?",
                    "Personal work", "Not feeling well", "Family commitment", "Medical appointment");
        }

        ctx.step = 4;
        LocalDate start = LocalDate.parse(ctx.data.get("startDate"));
        LocalDate end = LocalDate.parse(ctx.data.get("endDate"));
        long days = java.time.temporal.ChronoUnit.DAYS.between(start, end) + 1;

        String confirmation = String.format(
                "Please confirm your leave application:\n\n" +
                "📋 Type: %s\n" +
                "📅 From: %s\n" +
                "📅 To: %s\n" +
                "📆 Days: %d\n" +
                "📝 Reason: %s",
                ctx.data.get("leaveTypeName"),
                formatDate(start), formatDate(end), days,
                ctx.data.get("reason"));

        return reply(confirmation, "✅ Confirm", "❌ Cancel");
    }

    private AIChatResponse executeApplyLeave(String email, ConversationContext ctx) {
        activeFlows.remove(email);
        try {
            LeaveApplyRequest request = new LeaveApplyRequest();
            request.setLeaveTypeId(Integer.parseInt(ctx.data.get("leaveTypeId")));
            request.setStartDate(LocalDate.parse(ctx.data.get("startDate")));
            request.setEndDate(LocalDate.parse(ctx.data.get("endDate")));
            request.setReason(ctx.data.get("reason"));

            LeaveApplication leave = leaveService.applyLeave(email, request);

            String successMsg = String.format(
                    "✅ Leave applied successfully!\n\n" +
                    "• Leave ID: #%d\n" +
                    "• Type: %s\n" +
                    "• From: %s\n" +
                    "• To: %s\n" +
                    "• Working Days: %d\n" +
                    "• Status: %s\n\n" +
                    "Your manager has been notified.",
                    leave.getLeaveId(), leave.getLeaveType().getLeaveTypeName(),
                    formatDate(leave.getStartDate()), formatDate(leave.getEndDate()),
                    leave.getTotalDays(), leave.getStatus());

            return AIChatResponse.builder()
                    .reply(successMsg)
                    .action("APPLY_LEAVE")
                    .actionPerformed(true)
                    .quickReplies(List.of("My Leaves", "Leave Balance", "Help"))
                    .build();
        } catch (Exception e) {
            return reply("❌ Could not apply leave: " + e.getMessage(),
                    "Try Again", "Leave Balance", "Help");
        }
    }

    private AIChatResponse startCancelLeaveFlow(String email) {
        try {
            Page<LeaveApplication> pending = leaveService.getMyLeaves(email, LeaveStatus.PENDING,
                    PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "appliedDate")));

            if (pending.isEmpty()) {
                return reply("You don't have any pending leave applications to cancel.",
                        "My Leaves", "Apply Leave", "Help");
            }

            ConversationContext ctx = new ConversationContext("CANCEL_LEAVE");
            activeFlows.put(email, ctx);

            StringBuilder sb = new StringBuilder("Here are your pending leave applications:\n\n");
            List<String> quickReplies = new ArrayList<>();
            for (LeaveApplication la : pending.getContent()) {
                sb.append(String.format("🔹 #%d — %s (%s to %s) - %s\n",
                        la.getLeaveId(), la.getLeaveType().getLeaveTypeName(),
                        formatDate(la.getStartDate()), formatDate(la.getEndDate()),
                        la.getReason() != null ? la.getReason() : "No reason"));
                quickReplies.add("#" + la.getLeaveId());
            }
            sb.append("\nWhich leave would you like to cancel? (Enter the ID number)");

            return reply(sb.toString(), quickReplies.toArray(new String[0]));
        } catch (Exception e) {
            return reply("Error fetching your leaves: " + e.getMessage(), "Help");
        }
    }

    private AIChatResponse continueCancelLeave(String email, ConversationContext ctx, String msg) {
        String lower = msg.toLowerCase().trim();

        if (ctx.step == 0) {
            Pattern p = Pattern.compile("\\d+");
            Matcher m = p.matcher(msg);
            if (!m.find()) {
                return reply("Please enter a valid leave ID number (e.g., #5 or 5).");
            }
            String leaveId = m.group();
            ctx.data.put("leaveId", leaveId);
            ctx.step = 1;
            return reply("Cancel leave #" + leaveId + "? This cannot be undone.",
                    "✅ Yes, cancel it", "❌ No, keep it");
        }

        if (ctx.step == 1) {
            if (lower.contains("yes") || lower.contains("confirm") || lower.contains("cancel it")) {
                activeFlows.remove(email);
                try {
                    Integer leaveId = Integer.parseInt(ctx.data.get("leaveId"));
                    LeaveApplication cancelled = leaveService.cancelLeave(email, leaveId);
                    return AIChatResponse.builder()
                            .reply("✅ Leave #" + leaveId + " has been cancelled successfully.\nStatus: " + cancelled.getStatus())
                            .action("CANCEL_LEAVE")
                            .actionPerformed(true)
                            .quickReplies(List.of("My Leaves", "Apply Leave", "Help"))
                            .build();
                } catch (Exception e) {
                    return reply("❌ Could not cancel: " + e.getMessage(), "My Leaves", "Help");
                }
            } else {
                activeFlows.remove(email);
                return reply("Okay, your leave remains unchanged.", "My Leaves", "Help");
            }
        }

        activeFlows.remove(email);
        return reply("Something went wrong. Please try again.", "Cancel Leave", "Help");
    }

    private AIChatResponse startCheckInFlow(String email) {
        try {
            var status = attendanceService.getTodayStatus(email);
            if (status.getCheckInTime() != null) {
                return reply("You've already checked in today at " + status.getCheckInTime().toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm a")) + ".",
                        "Check Out", "My Attendance", "Help");
            }
        } catch (Exception ignored) {}

        ConversationContext ctx = new ConversationContext("CHECK_IN");
        activeFlows.put(email, ctx);

        return reply("Ready to check in for today (" + formatDate(LocalDate.now()) + ")?\nWould you like to add any notes?",
                "✅ Check In Now", "Add notes first");
    }

    private AIChatResponse continueCheckIn(String email, ConversationContext ctx, String msg) {
        String lower = msg.toLowerCase().trim();

        if (ctx.step == 0) {
            if (lower.contains("check in now") || lower.contains("yes") || lower.contains("proceed")
                    || lower.contains("check in") || lower.contains("confirm")) {
                return executeCheckIn(email, null);
            } else if (lower.contains("add notes") || lower.contains("note")) {
                ctx.step = 1;
                return reply("Enter your notes for today's check-in:");
            } else {
                ctx.data.put("notes", msg);
                return executeCheckIn(email, msg);
            }
        }

        if (ctx.step == 1) {
            return executeCheckIn(email, msg);
        }

        activeFlows.remove(email);
        return reply("Something went wrong. Please try again.", "Check In", "Help");
    }

    private AIChatResponse executeCheckIn(String email, String notes) {
        activeFlows.remove(email);
        try {
            CheckInRequest request = new CheckInRequest();
            request.setNotes(notes);
            var result = attendanceService.checkIn(email, request, "chatbot");

            String time = result.getCheckInTime() != null
                    ? result.getCheckInTime().toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm a")) : "now";

            return AIChatResponse.builder()
                    .reply("✅ Checked in successfully!\n\n• Time: " + time + "\n• Date: " + formatDate(LocalDate.now())
                            + "\n• Status: " + result.getStatus()
                            + (Boolean.TRUE.equals(result.getIsLate()) ? "\n⚠️ Marked as late arrival" : ""))
                    .action("CHECK_IN")
                    .actionPerformed(true)
                    .quickReplies(List.of("Check Out", "My Attendance", "Help"))
                    .build();
        } catch (Exception e) {
            return reply("❌ Check-in failed: " + e.getMessage(), "My Attendance", "Help");
        }
    }

    private AIChatResponse startCheckOutFlow(String email) {
        try {
            var status = attendanceService.getTodayStatus(email);
            if (status.getCheckInTime() == null || "NOT_CHECKED_IN".equals(status.getStatus())) {
                return reply("You haven't checked in yet today. Please check in first.",
                        "Check In", "Help");
            }
            if (status.getCheckOutTime() != null) {
                return reply("You've already checked out today at " + status.getCheckOutTime().toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm a")) + ".",
                        "My Attendance", "Help");
            }
        } catch (Exception ignored) {}

        ConversationContext ctx = new ConversationContext("CHECK_OUT");
        activeFlows.put(email, ctx);

        return reply("Ready to check out for today?\nWould you like to add any notes?",
                "✅ Check Out Now", "Add notes first");
    }

    private AIChatResponse continueCheckOut(String email, ConversationContext ctx, String msg) {
        String lower = msg.toLowerCase().trim();

        if (ctx.step == 0) {
            if (lower.contains("check out now") || lower.contains("yes") || lower.contains("proceed")
                    || lower.contains("check out") || lower.contains("confirm")) {
                return executeCheckOut(email, null);
            } else if (lower.contains("add notes") || lower.contains("note")) {
                ctx.step = 1;
                return reply("Enter your notes for today's check-out:");
            } else {
                return executeCheckOut(email, msg);
            }
        }

        if (ctx.step == 1) {
            return executeCheckOut(email, msg);
        }

        activeFlows.remove(email);
        return reply("Something went wrong. Please try again.", "Check Out", "Help");
    }

    private AIChatResponse executeCheckOut(String email, String notes) {
        activeFlows.remove(email);
        try {
            CheckOutRequest request = new CheckOutRequest();
            request.setNotes(notes);
            var result = attendanceService.checkOut(email, request, "chatbot");

            String time = result.getCheckOutTime() != null
                    ? result.getCheckOutTime().toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm a")) : "now";
            String hours = result.getTotalHours() != null ? String.format("%.1f", result.getTotalHours()) : "N/A";

            return AIChatResponse.builder()
                    .reply("✅ Checked out successfully!\n\n• Time: " + time + "\n• Total Hours: " + hours
                            + (Boolean.TRUE.equals(result.getIsEarlyDeparture()) ? "\n⚠️ Marked as early departure" : "")
                            + "\n\nGreat work today! 👏")
                    .action("CHECK_OUT")
                    .actionPerformed(true)
                    .quickReplies(List.of("My Attendance", "My Dashboard", "Help"))
                    .build();
        } catch (Exception e) {
            return reply("❌ Check-out failed: " + e.getMessage(), "My Attendance", "Help");
        }
    }

    private AIChatResponse startUpdateProfileFlow(String email) {
        ConversationContext ctx = new ConversationContext("UPDATE_PROFILE");
        activeFlows.put(email, ctx);

        return reply("What would you like to update?\n\n• Phone number\n• Address\n• Emergency contact name\n• Emergency contact phone",
                "Phone", "Address", "Emergency Contact Name", "Emergency Contact Phone");
    }

    private AIChatResponse continueUpdateProfile(String email, ConversationContext ctx, String msg) {
        String lower = msg.toLowerCase().trim();

        if (ctx.step == 0) {
            String field = null;
            if (lower.contains("phone") && !lower.contains("emergency")) field = "phone";
            else if (lower.contains("address")) field = "address";
            else if (lower.contains("emergency") && lower.contains("name")) field = "emergencyContactName";
            else if (lower.contains("emergency") && lower.contains("phone")) field = "emergencyContactPhone";
            else if (lower.contains("emergency")) field = "emergencyContactName";

            if (field == null) {
                return reply("Please choose what to update:", "Phone", "Address", "Emergency Contact Name", "Emergency Contact Phone");
            }

            ctx.data.put("field", field);
            ctx.step = 1;

            String fieldLabel = switch (field) {
                case "phone" -> "phone number";
                case "address" -> "address";
                case "emergencyContactName" -> "emergency contact name";
                case "emergencyContactPhone" -> "emergency contact phone";
                default -> field;
            };
            return reply("Enter your new " + fieldLabel + ":");
        }

        if (ctx.step == 1) {
            ctx.data.put("value", msg);
            ctx.step = 2;
            return reply("Update " + ctx.data.get("field") + " to: " + msg + "?",
                    "✅ Confirm", "❌ Cancel");
        }

        if (ctx.step == 2) {
            if (lower.contains("confirm") || lower.contains("yes")) {
                return executeUpdateProfile(email, ctx);
            }
            activeFlows.remove(email);
            return reply("Profile update cancelled.", "My Profile", "Help");
        }

        activeFlows.remove(email);
        return reply("Something went wrong. Please try again.", "Update Profile", "Help");
    }

    private AIChatResponse executeUpdateProfile(String email, ConversationContext ctx) {
        activeFlows.remove(email);
        try {
            UpdateProfileRequest request = new UpdateProfileRequest();
            String field = ctx.data.get("field");
            String value = ctx.data.get("value");

            switch (field) {
                case "phone" -> request.setPhone(value);
                case "address" -> request.setAddress(value);
                case "emergencyContactName" -> request.setEmergencyContactName(value);
                case "emergencyContactPhone" -> request.setEmergencyContactPhone(value);
            }

            employeeService.updateProfileWithResponse(email, request);

            return AIChatResponse.builder()
                    .reply("✅ Profile updated successfully!\n\n• " + field + " → " + value)
                    .action("UPDATE_PROFILE")
                    .actionPerformed(true)
                    .quickReplies(List.of("My Profile", "Help"))
                    .build();
        } catch (Exception e) {
            return reply("❌ Could not update profile: " + e.getMessage(), "My Profile", "Help");
        }
    }

    private AIChatResponse startCreateGoalFlow(String email, String message) {
        ConversationContext ctx = new ConversationContext("CREATE_GOAL");
        activeFlows.put(email, ctx);
        ctx.step = 0;
        return reply("Let's create a new goal! What's the title?");
    }

    private AIChatResponse continuteCreateGoal(String email, ConversationContext ctx, String msg) {
        String lower = msg.toLowerCase().trim();

        if (ctx.step == 0) {
            ctx.data.put("title", msg);
            ctx.step = 1;
            return reply("Brief description? (or type 'skip' to leave it empty)", "Skip");
        }

        if (ctx.step == 1) {
            if (!lower.equals("skip")) {
                ctx.data.put("description", msg);
            }
            ctx.step = 2;
            return reply("What's the deadline? (e.g., March 30, 2026-06-30)");
        }

        if (ctx.step == 2) {
            LocalDate deadline = parseNaturalDate(msg);
            if (deadline == null) {
                return reply("I couldn't parse that date. Please try: March 30 or 2026-06-30");
            }
            ctx.data.put("deadline", deadline.toString());
            ctx.step = 3;
            return reply("Priority level?", "HIGH", "MEDIUM", "LOW");
        }

        if (ctx.step == 3) {
            String priority = lower.contains("high") ? "HIGH" : lower.contains("low") ? "LOW" : "MEDIUM";
            ctx.data.put("priority", priority);
            ctx.step = 4;

            String confirmation = String.format(
                    "Please confirm your new goal:\n\n" +
                    "🎯 Title: %s\n" +
                    "📝 Description: %s\n" +
                    "📅 Deadline: %s\n" +
                    "⚡ Priority: %s",
                    ctx.data.get("title"),
                    ctx.data.getOrDefault("description", "N/A"),
                    formatDate(LocalDate.parse(ctx.data.get("deadline"))),
                    priority);
            return reply(confirmation, "✅ Confirm", "❌ Cancel");
        }

        if (ctx.step == 4) {
            if (lower.contains("confirm") || lower.contains("yes")) {
                return executeCreateGoal(email, ctx);
            }
            activeFlows.remove(email);
            return reply("Goal creation cancelled.", "My Goals", "Help");
        }

        activeFlows.remove(email);
        return reply("Something went wrong. Please try again.", "Create Goal", "Help");
    }

    private AIChatResponse executeCreateGoal(String email, ConversationContext ctx) {
        activeFlows.remove(email);
        try {
            GoalRequest request = new GoalRequest();
            request.setTitle(ctx.data.get("title"));
            request.setDescription(ctx.data.getOrDefault("description", null));
            request.setDeadline(LocalDate.parse(ctx.data.get("deadline")));
            request.setPriority(ctx.data.get("priority"));

            Goal goal = performanceService.createGoal(email, request);

            return AIChatResponse.builder()
                    .reply("✅ Goal created successfully!\n\n• Goal ID: #" + goal.getGoalId()
                            + "\n• Title: " + goal.getTitle()
                            + "\n• Priority: " + goal.getPriority()
                            + "\n• Deadline: " + formatDate(goal.getDeadline())
                            + "\n• Status: " + goal.getStatus())
                    .action("CREATE_GOAL")
                    .actionPerformed(true)
                    .quickReplies(List.of("My Goals", "Help"))
                    .build();
        } catch (Exception e) {
            return reply("❌ Could not create goal: " + e.getMessage(), "My Goals", "Help");
        }
    }

    private AIChatResponse getLeaveBalance(String email) {
        List<LeaveBalance> balances = leaveService.getMyLeaveBalance(email);
        if (balances.isEmpty()) {
            return reply("No leave balance records found.", "Apply Leave", "Help");
        }

        StringBuilder sb = new StringBuilder("📊 Your Leave Balance:\n\n");
        for (LeaveBalance b : balances) {
            int available = b.getAvailableBalance();
            String bar = progressBar(b.getUsedLeaves(), b.getTotalLeaves());
            sb.append(String.format("• %s: %d available (%d used / %d total) %s\n",
                    b.getLeaveType().getLeaveTypeName(), available,
                    b.getUsedLeaves(), b.getTotalLeaves(), bar));
        }
        return AIChatResponse.builder()
                .reply(sb.toString())
                .action("GET_LEAVE_BALANCE")
                .actionPerformed(true)
                .quickReplies(List.of("Apply Leave", "My Leaves", "Help"))
                .build();
    }

    private AIChatResponse getMyLeaves(String email) {
        Page<LeaveApplication> leaves = leaveService.getMyLeaves(email, null,
                PageRequest.of(0, 8, Sort.by(Sort.Direction.DESC, "appliedDate")));

        if (leaves.isEmpty()) {
            return reply("You don't have any leave applications yet.", "Apply Leave", "Help");
        }

        StringBuilder sb = new StringBuilder("📋 Your Leave Applications:\n\n");
        for (LeaveApplication la : leaves.getContent()) {
            String statusIcon = switch (la.getStatus()) {
                case PENDING -> "🟡";
                case APPROVED -> "🟢";
                case REJECTED -> "🔴";
                case CANCELLED -> "⚪";
            };
            sb.append(String.format("%s #%d — %s | %s to %s | %s\n",
                    statusIcon, la.getLeaveId(), la.getLeaveType().getLeaveTypeName(),
                    formatDate(la.getStartDate()), formatDate(la.getEndDate()),
                    la.getStatus()));
        }

        return AIChatResponse.builder()
                .reply(sb.toString())
                .action("GET_MY_LEAVES")
                .actionPerformed(true)
                .quickReplies(List.of("Apply Leave", "Cancel Leave", "Leave Balance", "Help"))
                .build();
    }

    private AIChatResponse getAttendanceToday(String email) {
        var status = attendanceService.getTodayStatus(email);

        StringBuilder sb = new StringBuilder("📅 Today's Attendance (" + formatDate(LocalDate.now()) + "):\n\n");
        sb.append("• Status: ").append(status.getStatus()).append("\n");
        sb.append("• Check-in: ").append(status.getCheckInTime() != null
                ? status.getCheckInTime().toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm a")) : "Not yet").append("\n");
        sb.append("• Check-out: ").append(status.getCheckOutTime() != null
                ? status.getCheckOutTime().toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm a")) : "Not yet").append("\n");
        if (status.getTotalHours() != null) {
            sb.append("• Hours: ").append(String.format("%.1f", status.getTotalHours())).append("\n");
        }

        List<String> actions = new ArrayList<>();
        if (status.getCheckInTime() == null || "NOT_CHECKED_IN".equals(status.getStatus())) {
            actions.add("Check In");
        } else if (status.getCheckOutTime() == null) {
            actions.add("Check Out");
        }
        actions.add("Attendance Summary");
        actions.add("Help");

        return AIChatResponse.builder()
                .reply(sb.toString())
                .action("GET_ATTENDANCE_TODAY")
                .actionPerformed(true)
                .quickReplies(actions)
                .build();
    }

    private AIChatResponse getAttendanceSummary(String email) {
        AttendanceSummaryResponse s = attendanceService.getMySummary(email, null, null);

        String summary = String.format(
                "📊 Attendance Summary — %s %d\n\n" +
                "• Present: %d days\n" +
                "• Absent: %d days\n" +
                "• Half Day: %d\n" +
                "• On Leave: %d\n" +
                "• Late Arrivals: %d\n" +
                "• Early Departures: %d\n" +
                "• Total Hours: %s",
                s.getMonth(), s.getYear(),
                s.getTotalPresent(), s.getTotalAbsent(), s.getTotalHalfDay(), s.getTotalOnLeave(),
                s.getTotalLateArrivals(), s.getTotalEarlyDepartures(),
                s.getTotalHoursWorked() != null ? String.format("%.1f", s.getTotalHoursWorked()) : "0");

        return AIChatResponse.builder()
                .reply(summary)
                .action("GET_ATTENDANCE_SUMMARY")
                .actionPerformed(true)
                .quickReplies(List.of("Today's Attendance", "Check In", "Help"))
                .build();
    }

    private AIChatResponse getDashboard(String email) {
        EmployeeDashboardResponse d = dashboardService.getEmployeeDashboard(email);

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("🏠 Dashboard — %s (%s)\n\n", d.getEmployeeName(), d.getEmployeeCode()));
        sb.append(String.format("• Department: %s\n• Designation: %s\n", d.getDepartmentName(), d.getDesignationTitle()));
        sb.append(String.format("• Pending Leaves: %d\n• Approved Leaves: %d\n• Notifications: %d\n",
                d.getPendingLeaveRequests(), d.getApprovedLeaves(), d.getUnreadNotifications()));

        if (d.getLeaveBalances() != null && !d.getLeaveBalances().isEmpty()) {
            sb.append("\n📊 Leave Balance:\n");
            for (var lb : d.getLeaveBalances()) {
                sb.append(String.format("  • %s: %d available\n", lb.getLeaveTypeName(), lb.getAvailableBalance()));
            }
        }
        if (d.getUpcomingHolidays() != null && !d.getUpcomingHolidays().isEmpty()) {
            sb.append("\n🗓️ Upcoming Holidays:\n");
            for (var h : d.getUpcomingHolidays()) {
                sb.append(String.format("  • %s — %s\n", formatDate(h.getHolidayDate()), h.getHolidayName()));
            }
        }

        return AIChatResponse.builder()
                .reply(sb.toString())
                .action("GET_MY_DASHBOARD")
                .actionPerformed(true)
                .quickReplies(List.of("My Profile", "Leave Balance", "Check In", "Help"))
                .build();
    }

    private AIChatResponse getAnnouncements() {
        Page<Announcement> page = announcementService.getActiveAnnouncements(PageRequest.of(0, 5));
        if (page.isEmpty()) {
            return reply("No active announcements at the moment.", "My Dashboard", "Help");
        }

        StringBuilder sb = new StringBuilder("📢 Recent Announcements:\n\n");
        for (Announcement a : page.getContent()) {
            sb.append(String.format("📌 %s\n%s\n%s\n\n",
                    a.getTitle(),
                    a.getContent() != null && a.getContent().length() > 120
                            ? a.getContent().substring(0, 120) + "..." : a.getContent(),
                    a.getCreatedAt() != null ? formatDate(a.getCreatedAt().toLocalDate()) : ""));
        }

        return AIChatResponse.builder()
                .reply(sb.toString())
                .action("GET_ANNOUNCEMENTS")
                .actionPerformed(true)
                .quickReplies(List.of("My Dashboard", "Holidays", "Help"))
                .build();
    }

    private AIChatResponse getProfile(String email) {
        var p = employeeService.getEmployeeProfileByEmail(email);

        String profile = String.format(
                "👤 Your Profile\n\n" +
                "• Name: %s %s\n" +
                "• Code: %s\n" +
                "• Email: %s\n" +
                "• Phone: %s\n" +
                "• Department: %s\n" +
                "• Designation: %s\n" +
                "• Role: %s\n" +
                "• Joining Date: %s\n" +
                "• Status: %s",
                p.getFirstName(), p.getLastName(),
                p.getEmployeeCode(), p.getEmail(),
                p.getPhone() != null ? p.getPhone() : "Not set",
                p.getDepartmentName() != null ? p.getDepartmentName() : "N/A",
                p.getDesignationTitle() != null ? p.getDesignationTitle() : "N/A",
                p.getRole(),
                p.getJoiningDate() != null ? formatDate(p.getJoiningDate()) : "N/A",
                p.getIsActive() ? "✅ Active" : "❌ Inactive");

        return AIChatResponse.builder()
                .reply(profile)
                .action("GET_MY_PROFILE")
                .actionPerformed(true)
                .quickReplies(List.of("Update Profile", "My Dashboard", "Help"))
                .build();
    }

    private AIChatResponse getHolidays() {
        List<Holiday> holidays = leaveService.getHolidays(LocalDate.now().getYear());
        if (holidays.isEmpty()) {
            return reply("No holidays found for " + LocalDate.now().getYear() + ".", "Help");
        }

        StringBuilder sb = new StringBuilder("🗓️ Holidays — " + LocalDate.now().getYear() + "\n\n");
        LocalDate today = LocalDate.now();
        for (Holiday h : holidays) {
            String icon = h.getHolidayDate().isBefore(today) ? "✓" : "•";
            sb.append(String.format("%s %s — %s\n", icon, formatDate(h.getHolidayDate()), h.getHolidayName()));
        }

        long upcoming = holidays.stream().filter(h -> !h.getHolidayDate().isBefore(today)).count();
        sb.append("\n").append(upcoming).append(" upcoming holidays remaining");

        return AIChatResponse.builder()
                .reply(sb.toString())
                .action("GET_HOLIDAYS")
                .actionPerformed(true)
                .quickReplies(List.of("Apply Leave", "My Dashboard", "Help"))
                .build();
    }

    private AIChatResponse getMyGoals(String email) {
        Page<Goal> goals = performanceService.getMyGoals(email, null, null,
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt")));

        if (goals.isEmpty()) {
            return reply("You don't have any goals yet.", "Create Goal", "Help");
        }

        StringBuilder sb = new StringBuilder("🎯 Your Goals:\n\n");
        for (Goal g : goals.getContent()) {
            String statusIcon = switch (g.getStatus()) {
                case NOT_STARTED -> "⬜";
                case IN_PROGRESS -> "🔵";
                case COMPLETED -> "✅";
            };
            sb.append(String.format("%s %s — %s | %d%% | Due: %s\n",
                    statusIcon, g.getTitle(), g.getPriority(), g.getProgress(),
                    formatDate(g.getDeadline())));
        }

        return AIChatResponse.builder()
                .reply(sb.toString())
                .action("GET_MY_GOALS")
                .actionPerformed(true)
                .quickReplies(List.of("Create Goal", "My Reviews", "Help"))
                .build();
    }

    private AIChatResponse getMyReviews(String email) {
        Page<PerformanceReview> reviews = performanceService.getMyReviews(email, null,
                PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt")));

        if (reviews.isEmpty()) {
            return reply("No performance reviews found.", "My Goals", "Help");
        }

        StringBuilder sb = new StringBuilder("📝 Your Performance Reviews:\n\n");
        for (PerformanceReview r : reviews.getContent()) {
            sb.append(String.format("• %s — %s | Self: %s | Manager: %s\n",
                    r.getReviewPeriod(), r.getStatus(),
                    r.getSelfAssessmentRating() != null ? r.getSelfAssessmentRating().toString() : "N/A",
                    r.getManagerRating() != null ? r.getManagerRating().toString() : "Pending"));
        }

        return AIChatResponse.builder()
                .reply(sb.toString())
                .action("GET_MY_REVIEWS")
                .actionPerformed(true)
                .quickReplies(List.of("My Goals", "My Dashboard", "Help"))
                .build();
    }

    private AIChatResponse getNotifications(String email) {
        long unread = notificationService.getUnreadCount(email);

        String msg = unread == 0
                ? "🔔 You're all caught up! No unread notifications."
                : "🔔 You have " + unread + " unread notification" + (unread > 1 ? "s" : "") + ".\nCheck the notification bell for details.";

        return AIChatResponse.builder()
                .reply(msg)
                .action("GET_NOTIFICATIONS")
                .actionPerformed(true)
                .quickReplies(List.of("My Dashboard", "My Leaves", "Help"))
                .build();
    }

    private AIChatResponse getGreeting(String email, Role role) {
        String name;
        try {
            var p = employeeService.getEmployeeProfileByEmail(email);
            name = p.getFirstName();
        } catch (Exception e) {
            name = "there";
        }

        int hour = LocalTime.now().getHour();
        String timeGreeting = hour < 12 ? "Good morning" : hour < 17 ? "Good afternoon" : "Good evening";
        String roleLabel = role == Role.ADMIN ? "Admin" : role == Role.MANAGER ? "Manager" : "Employee";

        StringBuilder sb = new StringBuilder();
        sb.append(timeGreeting).append(", ").append(name).append("! 👋\n");
        sb.append("Role: ").append(roleLabel).append("\n\n");
        sb.append("I'm your WorkForce AI assistant. Here's what I can help with:\n\n");
        sb.append("📝 Apply or cancel leaves\n");
        sb.append("⏰ Check in/out attendance\n");
        sb.append("📊 View leave balance, dashboard\n");
        sb.append("👤 View/update your profile\n");
        sb.append("🎯 Goals & performance reviews\n");
        if (role == Role.MANAGER || role == Role.ADMIN) {
            sb.append("\n👥 TEAM ACTIONS:\n");
            sb.append("📋 Team Leaves — view & approve/reject\n");
            sb.append("📅 Team Attendance — who's present today\n");
            sb.append("📝 Team Reviews — team performance\n");
        }
        if (role == Role.ADMIN) {
            sb.append("\n🔧 ADMIN ACTIONS:\n");
            sb.append("🏢 Admin Dashboard — system-wide stats\n");
            sb.append("📋 All Leaves — company-wide leave list\n");
        }
        sb.append("\nWhat would you like to do?");

        return reply(sb.toString(), getDefaultQuickReplies(role));
    }

    private AIChatResponse getHelp(Role role) {
        StringBuilder sb = new StringBuilder();
        sb.append("🤖 Here's what I can do for you:\n\n");
        sb.append("📝 LEAVE MANAGEMENT\n");
        sb.append("  • Apply Leave — multi-step guided flow\n");
        sb.append("  • Cancel Leave — cancel pending/approved leaves\n");
        sb.append("  • Leave Balance — view your leave balance\n");
        sb.append("  • My Leaves — list your leave applications\n\n");
        sb.append("⏰ ATTENDANCE\n");
        sb.append("  • Check In / Check Out\n");
        sb.append("  • Today's Attendance — today's status\n");
        sb.append("  • Attendance Summary — monthly summary\n\n");
        sb.append("👤 PROFILE & INFO\n");
        sb.append("  • My Profile / Update Profile\n");
        sb.append("  • My Dashboard — overview\n\n");
        sb.append("🎯 GOALS & REVIEWS\n");
        sb.append("  • My Goals / Create Goal\n");
        sb.append("  • My Reviews — performance reviews\n\n");
        sb.append("📢 OTHER\n");
        sb.append("  • Announcements • Holidays • Notifications\n");

        if (role == Role.MANAGER || role == Role.ADMIN) {
            sb.append("\n👥 TEAM (Manager/Admin):\n");
            sb.append("  • Team Leaves — pending team requests\n");
            sb.append("  • Approve Leave / Reject Leave\n");
            sb.append("  • Team Attendance — today's team status\n");
            sb.append("  • Team Reviews — team performance\n");
        }
        if (role == Role.ADMIN) {
            sb.append("\n🔧 ADMIN ONLY:\n");
            sb.append("  • Admin Dashboard — workforce stats\n");
            sb.append("  • All Leaves — company-wide list\n");
        }
        sb.append("\n💡 Just type naturally — I understand!");

        return reply(sb.toString(), getDefaultQuickReplies(role));
    }

    private AIChatResponse getTeamLeaves(String email, Role role) {
        try {
            Page<LeaveApplication> leaves;
            if (role == Role.ADMIN) {
                leaves = leaveService.getAllLeaveApplications(LeaveStatus.PENDING,
                        PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "appliedDate")));
            } else {
                leaves = leaveService.getTeamLeaves(email, LeaveStatus.PENDING,
                        PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "appliedDate")));
            }
            if (leaves.isEmpty()) {
                return reply("✅ No pending leave requests from your team!",
                        getDefaultQuickReplies(role));
            }
            StringBuilder sb = new StringBuilder("📋 Pending Team Leaves:\n\n");
            for (LeaveApplication la : leaves.getContent()) {
                sb.append(String.format("🔹 #%d — %s %s\n   %s | %s to %s | %d day(s)\n   Reason: %s\n\n",
                        la.getLeaveId(),
                        la.getEmployee().getFirstName(), la.getEmployee().getLastName(),
                        la.getLeaveType().getLeaveTypeName(),
                        formatDate(la.getStartDate()), formatDate(la.getEndDate()),
                        la.getTotalDays(),
                        la.getReason() != null ? la.getReason() : "N/A"));
            }
            sb.append("Say 'approve leave' or 'reject leave' to take action.");
            return AIChatResponse.builder()
                    .reply(sb.toString()).action("GET_TEAM_LEAVES").actionPerformed(true)
                    .quickReplies(List.of("Approve Leave", "Reject Leave", "Team Attendance", "Help"))
                    .build();
        } catch (Exception e) {
            return reply("Error: " + e.getMessage(), getDefaultQuickReplies(role));
        }
    }

    private AIChatResponse startApproveLeavFlow(String email, Role role) {
        ConversationContext ctx = new ConversationContext("APPROVE_LEAVE");
        activeFlows.put(email, ctx);
        return reply("Enter the Leave ID to approve (e.g. 5):");
    }

    private AIChatResponse startRejectLeaveFlow(String email, Role role) {
        ConversationContext ctx = new ConversationContext("REJECT_LEAVE");
        activeFlows.put(email, ctx);
        return reply("Enter the Leave ID to reject (e.g. 5):");
    }

    private AIChatResponse continueApproveRejectLeave(String email, ConversationContext ctx, String msg, String action) {
        String lower = msg.toLowerCase().trim();
        Role role = getUserRole(email);

        if (ctx.step == 0) {
            Pattern p = Pattern.compile("\\d+");
            Matcher m = p.matcher(msg);
            if (!m.find()) {
                return reply("Please enter a valid leave ID number.");
            }
            ctx.data.put("leaveId", m.group());
            if ("REJECTED".equals(action)) {
                ctx.step = 1;
                return reply("Reason for rejection?");
            }
            ctx.step = 2;
            return reply(action.equals("APPROVED") ? "Approve" : "Reject" + " leave #" + m.group() + "?",
                    "✅ Yes", "❌ No");
        }

        if (ctx.step == 1) {
            ctx.data.put("comments", msg);
            ctx.step = 2;
            return reply("Reject leave #" + ctx.data.get("leaveId") + " with reason: \"" + msg + "\"?",
                    "✅ Yes", "❌ No");
        }

        if (ctx.step == 2) {
            if (lower.contains("yes") || lower.contains("confirm") || lower.contains("✅")) {
                activeFlows.remove(email);
                try {
                    Integer leaveId = Integer.parseInt(ctx.data.get("leaveId"));
                    LeaveActionRequest req = new LeaveActionRequest();
                    req.setAction(action);
                    req.setComments(ctx.data.getOrDefault("comments", null));

                    LeaveApplication result;
                    if (role == Role.ADMIN) {
                        result = leaveService.adminActionLeave(email, leaveId, req);
                    } else {
                        result = leaveService.actionLeave(email, leaveId, req);
                    }
                    String emoji = "APPROVED".equals(action) ? "✅" : "❌";
                    return AIChatResponse.builder()
                            .reply(emoji + " Leave #" + leaveId + " has been " + action.toLowerCase() + ".\n" +
                                    "• Employee: " + result.getEmployee().getFirstName() + " " + result.getEmployee().getLastName() + "\n" +
                                    "• Type: " + result.getLeaveType().getLeaveTypeName() + "\n" +
                                    "• Dates: " + formatDate(result.getStartDate()) + " to " + formatDate(result.getEndDate()))
                            .action("APPROVED".equals(action) ? "APPROVE_LEAVE" : "REJECT_LEAVE")
                            .actionPerformed(true)
                            .quickReplies(List.of("Team Leaves", "Team Attendance", "Help"))
                            .build();
                } catch (Exception e) {
                    return reply("❌ Failed: " + e.getMessage(), "Team Leaves", "Help");
                }
            }
            activeFlows.remove(email);
            return reply("Cancelled.", getDefaultQuickReplies(role));
        }
        activeFlows.remove(email);
        return reply("Something went wrong.", getDefaultQuickReplies(role));
    }

    private AIChatResponse getTeamAttendance(String email, Role role) {
        try {
            List<AttendanceResponse> team = attendanceService.getTeamAttendanceToday(email);
            if (team.isEmpty()) {
                return reply("No team attendance records for today yet.",
                        getDefaultQuickReplies(role));
            }
            StringBuilder sb = new StringBuilder("👥 Team Attendance Today:\n\n");
            long present = team.stream().filter(a -> !"NOT_CHECKED_IN".equals(a.getStatus())).count();
            sb.append("Present: ").append(present).append(" / ").append(team.size()).append("\n\n");
            for (AttendanceResponse a : team) {
                String icon = "NOT_CHECKED_IN".equals(a.getStatus()) ? "⬜" :
                        a.getCheckOutTime() != null ? "✅" : "🟢";
                sb.append(String.format("%s %s — %s", icon, a.getEmployeeName(), a.getStatus()));
                if (a.getCheckInTime() != null) {
                    sb.append(" (in: ").append(a.getCheckInTime().toLocalTime().format(
                            DateTimeFormatter.ofPattern("hh:mm a"))).append(")");
                }
                sb.append("\n");
            }
            return AIChatResponse.builder()
                    .reply(sb.toString()).action("GET_TEAM_ATTENDANCE").actionPerformed(true)
                    .quickReplies(List.of("Team Leaves", "My Attendance", "Help"))
                    .build();
        } catch (Exception e) {
            return reply("Error: " + e.getMessage(), getDefaultQuickReplies(role));
        }
    }

    private AIChatResponse getTeamReviews(String email, Role role) {
        try {
            Page<PerformanceReview> reviews = performanceService.getTeamReviews(email, null,
                    PageRequest.of(0, 8, Sort.by(Sort.Direction.DESC, "createdAt")));
            if (reviews.isEmpty()) {
                return reply("No team performance reviews found.", getDefaultQuickReplies(role));
            }
            StringBuilder sb = new StringBuilder("📝 Team Performance Reviews:\n\n");
            for (PerformanceReview r : reviews.getContent()) {
                sb.append(String.format("• %s %s — %s | %s | Self: %s\n",
                        r.getEmployee().getFirstName(), r.getEmployee().getLastName(),
                        r.getReviewPeriod(), r.getStatus(),
                        r.getSelfAssessmentRating() != null ? r.getSelfAssessmentRating().toString() : "N/A"));
            }
            return AIChatResponse.builder()
                    .reply(sb.toString()).action("GET_TEAM_REVIEWS").actionPerformed(true)
                    .quickReplies(List.of("Team Leaves", "My Reviews", "Help"))
                    .build();
        } catch (Exception e) {
            return reply("Error: " + e.getMessage(), getDefaultQuickReplies(role));
        }
    }

    private AIChatResponse getAdminDashboard(String email, Role role) {
        try {
            DashboardResponse d = dashboardService.getDashboard();
            String summary = String.format(
                    "🏢 Admin Dashboard\n\n" +
                    "👥 Total Employees: %d\n" +
                    "  • Active: %d  |  Inactive: %d\n" +
                    "  • Admins: %d  |  Managers: %d  |  Employees: %d\n\n" +
                    "🏬 Departments: %d  |  Designations: %d\n" +
                    "📝 Pending Leaves: %d  |  On Leave Today: %d",
                    d.getTotalEmployees(),
                    d.getActiveEmployees(), d.getInactiveEmployees(),
                    d.getTotalAdmins(), d.getTotalManagers(), d.getTotalRegularEmployees(),
                    d.getTotalDepartments(), d.getTotalDesignations(),
                    d.getPendingLeaves(), d.getApprovedLeavesToday());
            return AIChatResponse.builder()
                    .reply(summary).action("GET_ADMIN_DASHBOARD").actionPerformed(true)
                    .quickReplies(List.of("All Leaves", "Team Attendance", "Announcements", "Help"))
                    .build();
        } catch (Exception e) {
            return reply("Error: " + e.getMessage(), getDefaultQuickReplies(role));
        }
    }

    private AIChatResponse getAllLeaves(String email, Role role) {
        try {
            Page<LeaveApplication> leaves = leaveService.getAllLeaveApplications(null,
                    PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "appliedDate")));
            if (leaves.isEmpty()) {
                return reply("No leave applications in the system.", getDefaultQuickReplies(role));
            }
            StringBuilder sb = new StringBuilder("📋 All Leave Applications (latest 10):\n\n");
            for (LeaveApplication la : leaves.getContent()) {
                String statusIcon = switch (la.getStatus()) {
                    case PENDING -> "🟡";
                    case APPROVED -> "🟢";
                    case REJECTED -> "🔴";
                    case CANCELLED -> "⚪";
                };
                sb.append(String.format("%s #%d — %s %s | %s | %s to %s | %s\n",
                        statusIcon, la.getLeaveId(),
                        la.getEmployee().getFirstName(), la.getEmployee().getLastName(),
                        la.getLeaveType().getLeaveTypeName(),
                        formatDate(la.getStartDate()), formatDate(la.getEndDate()),
                        la.getStatus()));
            }
            return AIChatResponse.builder()
                    .reply(sb.toString()).action("GET_ALL_LEAVES").actionPerformed(true)
                    .quickReplies(List.of("Approve Leave", "Reject Leave", "Admin Dashboard", "Help"))
                    .build();
        } catch (Exception e) {
            return reply("Error: " + e.getMessage(), getDefaultQuickReplies(role));
        }
    }

    private AIChatResponse handleGeneralChat(AIChatRequest request, Role userRole) {
        if (!ollamaClient.isAvailable()) {
            return reply("I'm not sure I understand that. Type 'help' to see what I can do for you.",
                    getDefaultQuickReplies(userRole));
        }

        try {
            StringBuilder prompt = new StringBuilder();
            prompt.append("You are a concise HR assistant. Reply in 1-2 sentences. No markdown.\n\n");

            if (request.getHistory() != null) {
                List<AIChatRequest.ChatHistoryEntry> recent = request.getHistory();
                int start = Math.max(0, recent.size() - 4);
                for (int i = start; i < recent.size(); i++) {
                    AIChatRequest.ChatHistoryEntry entry = recent.get(i);
                    prompt.append(entry.getRole().equals("user") ? "User: " : "Assistant: ");
                    prompt.append(entry.getContent()).append("\n");
                }
            }
            prompt.append("User: ").append(request.getMessage()).append("\nAssistant:");

            String llmReply = ollamaClient.generate(prompt.toString(), 100);
            if (llmReply != null && !llmReply.isBlank() && !llmReply.startsWith("Error")) {
                return reply(llmReply.trim(), getDefaultQuickReplies(userRole));
            }
        } catch (Exception ignored) {}

        return reply("I'm not sure I understand that. Type 'help' to see what I can do for you.",
                getDefaultQuickReplies(userRole));
    }

    private AIChatResponse reply(String message, String... quickReplies) {
        return AIChatResponse.builder()
                .reply(message)
                .actionPerformed(false)
                .quickReplies(quickReplies != null && quickReplies.length > 0
                        ? List.of(quickReplies) : null)
                .build();
    }

    private boolean isAbortCommand(String msg) {
        return containsAny(msg, "abort", "stop", "nevermind", "never mind",
                "forget it", "quit", "exit flow", "start over");
    }

    private boolean containsAny(String text, String... keywords) {
        for (String keyword : keywords) {
            if (text.contains(keyword)) return true;
        }
        return false;
    }

    private String progressBar(int used, int total) {
        if (total == 0) return "";
        int filled = (int) Math.round((double) used / total * 5);
        return "▓".repeat(Math.min(filled, 5)) + "░".repeat(Math.max(5 - filled, 0));
    }

    private String formatDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
    }

    private List<String> getLeaveTypeNames() {
        try {
            return leaveService.getAllLeaveType().stream()
                    .filter(LeaveType::getIsActive)
                    .map(LeaveType::getLeaveTypeName)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of("Sick Leave", "Casual Leave", "Earned Leave");
        }
    }

    private Integer detectLeaveTypeFromMessage(String lower) {
        try {
            List<LeaveType> types = leaveService.getAllLeaveType();
            for (LeaveType t : types) {
                String name = t.getLeaveTypeName().toLowerCase();

                String[] words = name.split("\\s+");
                for (String word : words) {
                    if (word.length() > 3 && lower.contains(word)) {
                        return t.getLeaveTypeId();
                    }
                }
            }
        } catch (Exception ignored) {}

        if (lower.contains("sick")) return findLeaveTypeId("sick");
        if (lower.contains("casual")) return findLeaveTypeId("casual");
        if (lower.contains("earned")) return findLeaveTypeId("earned");
        if (lower.contains("comp")) return findLeaveTypeId("comp");
        if (lower.contains("lop") || lower.contains("loss of pay")) return findLeaveTypeId("loss");
        return null;
    }

    private Integer detectLeaveTypeFromInput(String lower) {
        try {
            List<LeaveType> types = leaveService.getAllLeaveType();
            for (LeaveType t : types) {
                if (lower.contains(t.getLeaveTypeName().toLowerCase())) {
                    return t.getLeaveTypeId();
                }
            }

            for (LeaveType t : types) {
                String name = t.getLeaveTypeName().toLowerCase();
                for (String word : name.split("\\s+")) {
                    if (word.length() > 3 && lower.contains(word)) {
                        return t.getLeaveTypeId();
                    }
                }
            }
        } catch (Exception ignored) {}

        try {
            int id = Integer.parseInt(lower.replaceAll("[^0-9]", ""));
            return id > 0 ? id : null;
        } catch (Exception ignored) {}

        return null;
    }

    private Integer findLeaveTypeId(String keyword) {
        try {
            return leaveService.getAllLeaveType().stream()
                    .filter(t -> t.getLeaveTypeName().toLowerCase().contains(keyword))
                    .findFirst().map(LeaveType::getLeaveTypeId).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    private LocalDate parseNaturalDate(String input) {
        if (input == null || input.isBlank()) return null;
        String text = input.trim().toLowerCase()
                .replaceAll("[,.]", " ").replaceAll("\\s+", " ").trim();

        LocalDate today = LocalDate.now();

        if (text.equals("today")) return today;
        if (text.equals("tomorrow") || text.equals("tmrw") || text.equals("tmr")) return today.plusDays(1);
        if (text.equals("day after tomorrow")) return today.plusDays(2);

        if (text.startsWith("next ") || text.startsWith("this ")) {
            String dayName = text.substring(5).trim();
            DayOfWeek dow = parseDayOfWeek(dayName);
            if (dow != null) {
                return text.startsWith("next ")
                        ? today.with(TemporalAdjusters.next(dow))
                        : today.with(TemporalAdjusters.nextOrSame(dow));
            }
        }

        try {
            return LocalDate.parse(text);
        } catch (Exception ignored) {}

        Pattern p1 = Pattern.compile("(jan(?:uary)?|feb(?:ruary)?|mar(?:ch)?|apr(?:il)?|may|jun(?:e)?|jul(?:y)?|aug(?:ust)?|sep(?:tember)?|oct(?:ober)?|nov(?:ember)?|dec(?:ember)?)\\s+(\\d{1,2})(?:\\s+(\\d{4}))?");
        Matcher m1 = p1.matcher(text);
        if (m1.find()) {
            int month = parseMonth(m1.group(1));
            int day = Integer.parseInt(m1.group(2));
            int year = m1.group(3) != null ? Integer.parseInt(m1.group(3)) : today.getYear();
            try {
                LocalDate parsed = LocalDate.of(year, month, day);

                if (parsed.isBefore(today) && m1.group(3) == null) {
                    parsed = LocalDate.of(year + 1, month, day);
                }
                return parsed;
            } catch (Exception ignored) {}
        }

        Pattern p2 = Pattern.compile("(\\d{1,2})\\s+(jan(?:uary)?|feb(?:ruary)?|mar(?:ch)?|apr(?:il)?|may|jun(?:e)?|jul(?:y)?|aug(?:ust)?|sep(?:tember)?|oct(?:ober)?|nov(?:ember)?|dec(?:ember)?)(?:\\s+(\\d{4}))?");
        Matcher m2 = p2.matcher(text);
        if (m2.find()) {
            int day = Integer.parseInt(m2.group(1));
            int month = parseMonth(m2.group(2));
            int year = m2.group(3) != null ? Integer.parseInt(m2.group(3)) : today.getYear();
            try {
                LocalDate parsed = LocalDate.of(year, month, day);
                if (parsed.isBefore(today) && m2.group(3) == null) {
                    parsed = LocalDate.of(year + 1, month, day);
                }
                return parsed;
            } catch (Exception ignored) {}
        }

        Pattern p3 = Pattern.compile("(\\d{1,2})[/\\-](\\d{1,2})(?:[/\\-](\\d{2,4}))?");
        Matcher m3 = p3.matcher(text);
        if (m3.find()) {
            int a = Integer.parseInt(m3.group(1));
            int b = Integer.parseInt(m3.group(2));
            int year = m3.group(3) != null ? Integer.parseInt(m3.group(3)) : today.getYear();
            if (year < 100) year += 2000;

            try {
                return LocalDate.of(year, b, a);
            } catch (Exception ignored) {}

            try {
                return LocalDate.of(year, a, b);
            } catch (Exception ignored) {}
        }

        return null;
    }

    private List<LocalDate> extractDates(String message) {
        List<LocalDate> dates = new ArrayList<>();
        String text = message.toLowerCase().replaceAll("[,.]", " ");

        Pattern monthDay = Pattern.compile("(jan(?:uary)?|feb(?:ruary)?|mar(?:ch)?|apr(?:il)?|may|jun(?:e)?|jul(?:y)?|aug(?:ust)?|sep(?:tember)?|oct(?:ober)?|nov(?:ember)?|dec(?:ember)?)\\s+(\\d{1,2})(?:\\s+(\\d{4}))?");
        Matcher m = monthDay.matcher(text);
        while (m.find()) {
            int month = parseMonth(m.group(1));
            int day = Integer.parseInt(m.group(2));
            int year = m.group(3) != null ? Integer.parseInt(m.group(3)) : LocalDate.now().getYear();
            try { dates.add(LocalDate.of(year, month, day)); } catch (Exception ignored) {}
        }

        Pattern iso = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})");
        Matcher mi = iso.matcher(message);
        while (mi.find()) {
            try { dates.add(LocalDate.parse(mi.group())); } catch (Exception ignored) {}
        }

        if (text.contains("tomorrow")) dates.add(LocalDate.now().plusDays(1));

        return dates;
    }

    private String extractReason(String message) {
        String lower = message.toLowerCase();
        if (lower.contains("reason:")) {
            return message.substring(lower.indexOf("reason:") + 7).trim();
        }
        if (lower.contains("because ")) {
            return message.substring(lower.indexOf("because ") + 8).trim();
        }
        if (lower.contains(" for ") && lower.indexOf(" for ") > lower.length() / 2) {
            String after = message.substring(lower.lastIndexOf(" for ") + 5).trim();
            if (after.length() > 5 && !after.toLowerCase().matches("\\d+ day.*|a day.*|leave.*|me.*")) {
                return after;
            }
        }
        return null;
    }

    private int parseMonth(String name) {
        return switch (name.substring(0, 3)) {
            case "jan" -> 1; case "feb" -> 2; case "mar" -> 3; case "apr" -> 4;
            case "may" -> 5; case "jun" -> 6; case "jul" -> 7; case "aug" -> 8;
            case "sep" -> 9; case "oct" -> 10; case "nov" -> 11; case "dec" -> 12;
            default -> 1;
        };
    }

    private DayOfWeek parseDayOfWeek(String name) {
        return switch (name.toLowerCase().trim()) {
            case "monday", "mon" -> DayOfWeek.MONDAY;
            case "tuesday", "tue", "tues" -> DayOfWeek.TUESDAY;
            case "wednesday", "wed" -> DayOfWeek.WEDNESDAY;
            case "thursday", "thu", "thurs" -> DayOfWeek.THURSDAY;
            case "friday", "fri" -> DayOfWeek.FRIDAY;
            case "saturday", "sat" -> DayOfWeek.SATURDAY;
            case "sunday", "sun" -> DayOfWeek.SUNDAY;
            default -> null;
        };
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/service/AnnouncementService.java`**
```java
package com.revworkforce.employeemanagementservice.service;

import com.revworkforce.employeemanagementservice.dto.AnnouncementRequest;
import com.revworkforce.employeemanagementservice.exception.InvalidActionException;
import com.revworkforce.employeemanagementservice.exception.ResourceNotFoundException;
import com.revworkforce.employeemanagementservice.model.Announcement;
import com.revworkforce.employeemanagementservice.model.Employee;
import com.revworkforce.employeemanagementservice.repository.AnnouncementRepository;
import com.revworkforce.employeemanagementservice.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnnouncementService {
    @Autowired
    private AnnouncementRepository announcementRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private NotificationService notificationService;

    @Transactional
    public Announcement createAnnouncement(String adminEmail, AnnouncementRequest request) {
        Employee admin = employeeRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with email: " + adminEmail));
        Announcement announcement = Announcement.builder()
                .title(request.getTitle()).content(request.getContent()).createdBy(admin).build();
        Announcement saved = announcementRepository.save(announcement);
        List<Employee> activeEmployees = employeeRepository.findAll().stream()
                .filter(e -> e.getIsActive() && !e.getEmployeeId().equals(admin.getEmployeeId()))
                .toList();
        for (Employee emp : activeEmployees) {
            notificationService.notifyAnnouncement(emp, saved.getAnnouncementId(), saved.getTitle());
        }
        return saved;
    }

    public Announcement updateAnnouncement(Integer announcementId, AnnouncementRequest request) {
        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new ResourceNotFoundException("Announcement not found with id: " + announcementId));
        announcement.setTitle(request.getTitle());
        announcement.setContent(request.getContent());
        return announcementRepository.save(announcement);
    }

    public Announcement deactivateAnnouncement(Integer announcementId) {
        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new ResourceNotFoundException("Announcement not found with id: " + announcementId));
        if (!announcement.getIsActive()) {
            throw new InvalidActionException("Announcement is already deactivated");
        }
        announcement.setIsActive(false);
        return announcementRepository.save(announcement);
    }

    public Announcement activateAnnouncement(Integer announcementId) {
        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new ResourceNotFoundException("Announcement not found with id: " + announcementId));
        if (announcement.getIsActive()) {
            throw new InvalidActionException("Announcement is already active");
        }
        announcement.setIsActive(true);
        return announcementRepository.save(announcement);
    }

    public Page<Announcement> getActiveAnnouncements(Pageable pageable) {
        return announcementRepository.findByIsActiveOrderByCreatedAtDesc(true, pageable);
    }

    public Page<Announcement> getAllAnnouncements(Pageable pageable) {
        return announcementRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    public Announcement getAnnouncementById(Integer announcementId) {
        return announcementRepository.findById(announcementId)
                .orElseThrow(() -> new ResourceNotFoundException("Announcement not found with id: " + announcementId));
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/service/AttendanceService.java`**
```java
package com.revworkforce.employeemanagementservice.service;

import com.revworkforce.employeemanagementservice.dto.AttendanceResponse;
import com.revworkforce.employeemanagementservice.dto.AttendanceSummaryResponse;
import com.revworkforce.employeemanagementservice.dto.CheckInRequest;
import com.revworkforce.employeemanagementservice.dto.CheckOutRequest;
import com.revworkforce.employeemanagementservice.exception.*;
import com.revworkforce.employeemanagementservice.model.Attendance;
import com.revworkforce.employeemanagementservice.model.Employee;
import com.revworkforce.employeemanagementservice.model.enums.AttendanceStatus;
import com.revworkforce.employeemanagementservice.repository.AttendanceRepository;
import com.revworkforce.employeemanagementservice.repository.EmployeeRepository;
import com.revworkforce.employeemanagementservice.service.GeoAttendanceService;
import com.revworkforce.employeemanagementservice.service.GeoAttendanceService.GeoVerificationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceService {
    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private GeoAttendanceService geoAttendanceService;

    @Value("${attendance.office-start-time:09:00}")
    private String officeStartTime;

    @Value("${attendance.office-end-time:18:00}")
    private String officeEndTime;

    @Value("${attendance.late-threshold-minutes:15}")
    private int lateThresholdMinutes;

    @Value("${attendance.early-departure-threshold-minutes:30}")
    private int earlyDepartureThresholdMinutes;

    @Transactional
    public AttendanceResponse checkIn(String email, CheckInRequest request, String ipAddress) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with email: " + email));

        LocalDate today = LocalDate.now();
        if (attendanceRepository.existsByEmployee_EmployeeIdAndAttendanceDate(employee.getEmployeeId(), today)) {
            Attendance existing = attendanceRepository
                    .findByEmployee_EmployeeIdAndAttendanceDate(employee.getEmployeeId(), today)
                    .orElseThrow(() -> new ResourceNotFoundException("Attendance record not found"));
            if (existing.getCheckInTime() != null) {
                throw new DuplicateResourceException("You have already checked in today at " + existing.getCheckInTime());
            }
        }

        LocalDateTime now = LocalDateTime.now();
        LocalTime startTime = LocalTime.parse(officeStartTime);
        boolean isLate = now.toLocalTime().isAfter(startTime.plusMinutes(lateThresholdMinutes));

        boolean locationVerified = false;
        Double checkInDistance = null;
        String officeLocationName = null;

        if (request != null && request.getLatitude() != null && request.getLongitude() != null) {
            GeoVerificationResult geoResult = geoAttendanceService.verifyLocation(
                    request.getLatitude(), request.getLongitude());

            if (!geoResult.withinFence()) {
                throw new InvalidActionException("Geo-fence check failed: " + geoResult.message());
            }

            locationVerified = true;
            checkInDistance = geoResult.distanceMeters();
            officeLocationName = geoResult.message();
        }

        Attendance attendance = Attendance.builder()
                .employee(employee)
                .attendanceDate(today)
                .checkInTime(now)
                .status(AttendanceStatus.PRESENT)
                .checkInIp(ipAddress)
                .checkInLatitude(request != null ? request.getLatitude() : null)
                .checkInLongitude(request != null ? request.getLongitude() : null)
                .locationVerified(locationVerified)
                .checkInDistanceMeters(checkInDistance)
                .officeLocationName(officeLocationName)
                .notes(request != null ? request.getNotes() : null)
                .isLate(isLate)
                .build();

        Attendance saved = attendanceRepository.save(attendance);
        return mapToResponse(saved);
    }

    @Transactional
    public AttendanceResponse checkOut(String email, CheckOutRequest request, String ipAddress) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with email: " + email));

        LocalDate today = LocalDate.now();
        Attendance attendance = attendanceRepository
                .findByEmployee_EmployeeIdAndAttendanceDate(employee.getEmployeeId(), today)
                .orElseThrow(() -> new ResourceNotFoundException("No check-in record found for today. Please check in first."));

        if (attendance.getCheckInTime() == null) {
            throw new InvalidActionException("You must check in before checking out.");
        }
        if (attendance.getCheckOutTime() != null) {
            throw new DuplicateResourceException("You have already checked out today at " + attendance.getCheckOutTime());
        }

        LocalDateTime now = LocalDateTime.now();
        LocalTime endTime = LocalTime.parse(officeEndTime);
        boolean isEarlyDeparture = now.toLocalTime().isBefore(endTime.minusMinutes(earlyDepartureThresholdMinutes));

        if (request != null && request.getLatitude() != null && request.getLongitude() != null) {
            GeoVerificationResult geoResult = geoAttendanceService.verifyLocation(
                    request.getLatitude(), request.getLongitude());

            attendance.setCheckOutLatitude(request.getLatitude());
            attendance.setCheckOutLongitude(request.getLongitude());
            attendance.setCheckOutDistanceMeters(geoResult.distanceMeters());
        }

        attendance.setCheckOutTime(now);
        attendance.setCheckOutIp(ipAddress);
        attendance.setIsEarlyDeparture(isEarlyDeparture);
        attendance.setTotalHours(attendance.getCalculatedHours());

        if (request != null && request.getNotes() != null) {
            String existingNotes = attendance.getNotes() != null ? attendance.getNotes() + " | " : "";
            attendance.setNotes(existingNotes + request.getNotes());
        }

        if (attendance.getTotalHours() != null && attendance.getTotalHours() < 4.0) {
            attendance.setStatus(AttendanceStatus.HALF_DAY);
        }

        Attendance saved = attendanceRepository.save(attendance);
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public AttendanceResponse getTodayStatus(String email) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with email: " + email));

        LocalDate today = LocalDate.now();
        Attendance attendance = attendanceRepository
                .findByEmployee_EmployeeIdAndAttendanceDate(employee.getEmployeeId(), today)
                .orElse(null);

        if (attendance == null) {
            return AttendanceResponse.builder()
                    .employeeId(employee.getEmployeeId())
                    .employeeCode(employee.getEmployeeCode())
                    .employeeName(employee.getFirstName() + " " + employee.getLastName())
                    .attendanceDate(today)
                    .status("NOT_CHECKED_IN")
                    .build();
        }
        return mapToResponse(attendance);
    }

    @Transactional(readOnly = true)
    public Page<AttendanceResponse> getMyAttendance(String email, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with email: " + email));

        Page<Attendance> attendancePage;
        if (startDate != null && endDate != null) {
            attendancePage = attendanceRepository.findByEmployee_EmployeeIdAndAttendanceDateBetweenOrderByAttendanceDateDesc(
                    employee.getEmployeeId(), startDate, endDate, pageable);
        } else {
            attendancePage = attendanceRepository.findByEmployee_EmployeeIdOrderByAttendanceDateDesc(
                    employee.getEmployeeId(), pageable);
        }
        return attendancePage.map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public AttendanceSummaryResponse getMySummary(String email, Integer month, Integer year) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with email: " + email));

        int targetYear = year != null ? year : LocalDate.now().getYear();
        int targetMonth = month != null ? month : LocalDate.now().getMonthValue();

        LocalDate startDate = LocalDate.of(targetYear, targetMonth, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        return buildSummary(employee, startDate, endDate, targetMonth, targetYear);
    }

    @Transactional(readOnly = true)
    public List<AttendanceResponse> getTeamAttendanceToday(String managerEmail) {
        Employee manager = employeeRepository.findByEmail(managerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found with email: " + managerEmail));

        List<Attendance> teamAttendance = attendanceRepository.findTeamAttendanceByDate(
                manager.getEmployeeCode(), LocalDate.now());

        return teamAttendance.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AttendanceResponse> getTeamAttendanceBetween(String managerEmail, LocalDate startDate, LocalDate endDate) {
        Employee manager = employeeRepository.findByEmail(managerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found with email: " + managerEmail));

        if (startDate == null) startDate = LocalDate.now().withDayOfMonth(1);
        if (endDate == null) endDate = LocalDate.now();

        List<Attendance> teamAttendance = attendanceRepository.findTeamAttendanceBetween(
                manager.getEmployeeCode(), startDate, endDate);

        return teamAttendance.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<AttendanceResponse> getAllAttendanceByDate(LocalDate date, Pageable pageable) {
        if (date == null) date = LocalDate.now();
        Page<Attendance> attendancePage = attendanceRepository.findAllByDate(date, pageable);
        return attendancePage.map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public AttendanceSummaryResponse getEmployeeSummary(String employeeCode, Integer month, Integer year) {
        Employee employee = employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with code: " + employeeCode));

        int targetYear = year != null ? year : LocalDate.now().getYear();
        int targetMonth = month != null ? month : LocalDate.now().getMonthValue();

        LocalDate startDate = LocalDate.of(targetYear, targetMonth, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        return buildSummary(employee, startDate, endDate, targetMonth, targetYear);
    }

    private AttendanceSummaryResponse buildSummary(Employee employee, LocalDate startDate, LocalDate endDate,
                                                    int month, int year) {
        long present = attendanceRepository.countByEmployeeAndDateRangeAndStatus(
                employee.getEmployeeId(), startDate, endDate, AttendanceStatus.PRESENT);
        long absent = attendanceRepository.countByEmployeeAndDateRangeAndStatus(
                employee.getEmployeeId(), startDate, endDate, AttendanceStatus.ABSENT);
        long halfDay = attendanceRepository.countByEmployeeAndDateRangeAndStatus(
                employee.getEmployeeId(), startDate, endDate, AttendanceStatus.HALF_DAY);
        long onLeave = attendanceRepository.countByEmployeeAndDateRangeAndStatus(
                employee.getEmployeeId(), startDate, endDate, AttendanceStatus.ON_LEAVE);

        Double totalHours = attendanceRepository.getTotalHoursByEmployeeAndDateRange(
                employee.getEmployeeId(), startDate, endDate);

        List<Attendance> records = attendanceRepository.findByEmployee_EmployeeIdAndAttendanceDateBetween(
                employee.getEmployeeId(), startDate, endDate);
        long lateCount = records.stream().filter(a -> Boolean.TRUE.equals(a.getIsLate())).count();
        long earlyDepartureCount = records.stream().filter(a -> Boolean.TRUE.equals(a.getIsEarlyDeparture())).count();

        return AttendanceSummaryResponse.builder()
                .employeeCode(employee.getEmployeeCode())
                .employeeName(employee.getFirstName() + " " + employee.getLastName())
                .totalPresent(present)
                .totalAbsent(absent)
                .totalHalfDay(halfDay)
                .totalOnLeave(onLeave)
                .totalLateArrivals(lateCount)
                .totalEarlyDepartures(earlyDepartureCount)
                .totalHoursWorked(totalHours)
                .month(java.time.Month.of(month).name())
                .year(year)
                .build();
    }

    private AttendanceResponse mapToResponse(Attendance attendance) {
        return AttendanceResponse.builder()
                .attendanceId(attendance.getAttendanceId())
                .employeeId(attendance.getEmployee().getEmployeeId())
                .employeeCode(attendance.getEmployee().getEmployeeCode())
                .employeeName(attendance.getEmployee().getFirstName() + " " + attendance.getEmployee().getLastName())
                .attendanceDate(attendance.getAttendanceDate())
                .checkInTime(attendance.getCheckInTime())
                .checkOutTime(attendance.getCheckOutTime())
                .totalHours(attendance.getTotalHours())
                .status(attendance.getStatus().name())
                .checkInIp(attendance.getCheckInIp())
                .checkOutIp(attendance.getCheckOutIp())

                .checkInLatitude(attendance.getCheckInLatitude())
                .checkInLongitude(attendance.getCheckInLongitude())
                .checkOutLatitude(attendance.getCheckOutLatitude())
                .checkOutLongitude(attendance.getCheckOutLongitude())
                .locationVerified(attendance.getLocationVerified())
                .checkInDistanceMeters(attendance.getCheckInDistanceMeters())
                .checkOutDistanceMeters(attendance.getCheckOutDistanceMeters())
                .officeLocationName(attendance.getOfficeLocationName())
                .notes(attendance.getNotes())
                .isLate(attendance.getIsLate())
                .isEarlyDeparture(attendance.getIsEarlyDeparture())
                .createdAt(attendance.getCreatedAt())
                .build();
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/service/ChatService.java`**
```java
package com.revworkforce.employeemanagementservice.service;

import com.revworkforce.employeemanagementservice.dto.ChatMessageRequest;
import com.revworkforce.employeemanagementservice.dto.ChatMessageResponse;
import com.revworkforce.employeemanagementservice.dto.ConversationResponse;
import com.revworkforce.employeemanagementservice.exception.AccessDeniedException;
import com.revworkforce.employeemanagementservice.exception.ResourceNotFoundException;
import com.revworkforce.employeemanagementservice.model.ChatConversation;
import com.revworkforce.employeemanagementservice.model.ChatMessage;
import com.revworkforce.employeemanagementservice.model.Employee;
import com.revworkforce.employeemanagementservice.model.enums.MessageType;
import com.revworkforce.employeemanagementservice.repository.ChatConversationRepository;
import com.revworkforce.employeemanagementservice.repository.ChatMessageRepository;
import com.revworkforce.employeemanagementservice.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {
    @Autowired
    private ChatConversationRepository conversationRepository;
    @Autowired
    private ChatMessageRepository messageRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private PresenceService presenceService;
    @Autowired
    private WebSocketNotificationService wsNotificationService;

    @Transactional
    public ConversationResponse getOrCreateConversation(String currentEmail, Integer otherEmployeeId) {
        Employee currentUser = findEmployeeByEmail(currentEmail);
        Employee otherUser = employeeRepository.findById(otherEmployeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + otherEmployeeId));

        if (currentUser.getEmployeeId().equals(otherEmployeeId)) {
            throw new AccessDeniedException("Cannot create conversation with yourself");
        }

        ChatConversation conversation = conversationRepository
                .findByParticipants(currentUser.getEmployeeId(), otherEmployeeId)
                .orElseGet(() -> {
                    Employee p1, p2;
                    if (currentUser.getEmployeeId() < otherEmployeeId) {
                        p1 = currentUser;
                        p2 = otherUser;
                    } else {
                        p1 = otherUser;
                        p2 = currentUser;
                    }
                    ChatConversation newConv = ChatConversation.builder()
                            .participant1(p1)
                            .participant2(p2)
                            .build();
                    return conversationRepository.save(newConv);
                });

        return mapToConversationResponse(conversation, currentUser.getEmployeeId());
    }

    public List<ConversationResponse> getMyConversations(String email) {
        Employee employee = findEmployeeByEmail(email);
        List<ChatConversation> conversations = conversationRepository.findAllByParticipant(employee.getEmployeeId());

        return conversations.stream()
                .map(conv -> mapToConversationResponse(conv, employee.getEmployeeId()))
                .collect(Collectors.toList());
    }

    public Page<ChatMessageResponse> getMessages(String email, Long conversationId, int page, int size) {
        Employee employee = findEmployeeByEmail(email);
        ChatConversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found"));

        if (!isParticipant(conversation, employee.getEmployeeId())) {
            throw new AccessDeniedException("You are not a participant of this conversation");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<ChatMessage> messages = messageRepository
                .findByConversation_ConversationIdOrderByCreatedAtDesc(conversationId, pageable);

        return messages.map(msg -> mapToMessageResponse(msg, conversation, employee.getEmployeeId()));
    }

    @Transactional
    public ChatMessageResponse sendMessage(String senderEmail, ChatMessageRequest request) {
        Employee sender = findEmployeeByEmail(senderEmail);

        ChatConversation conversation;
        Employee recipient;

        if (request.getConversationId() != null) {
            conversation = conversationRepository.findById(request.getConversationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Conversation not found"));
            if (!isParticipant(conversation, sender.getEmployeeId())) {
                throw new AccessDeniedException("You are not a participant of this conversation");
            }
            recipient = getOtherParticipant(conversation, sender.getEmployeeId());
        } else if (request.getRecipientId() != null) {
            recipient = employeeRepository.findById(request.getRecipientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Recipient not found"));

            Employee p1, p2;
            if (sender.getEmployeeId() < recipient.getEmployeeId()) {
                p1 = sender; p2 = recipient;
            } else {
                p1 = recipient; p2 = sender;
            }
            conversation = conversationRepository
                    .findByParticipants(sender.getEmployeeId(), recipient.getEmployeeId())
                    .orElseGet(() -> conversationRepository.save(
                            ChatConversation.builder().participant1(p1).participant2(p2).build()));
        } else {
            throw new AccessDeniedException("Either conversationId or recipientId must be provided");
        }

        MessageType messageType = MessageType.TEXT;
        if (request.getMessageType() != null) {
            try {
                messageType = MessageType.valueOf(request.getMessageType());
            } catch (IllegalArgumentException ignored) {}
        }

        ChatMessage message = ChatMessage.builder()
                .conversation(conversation)
                .sender(sender)
                .content(request.getContent())
                .messageType(messageType)
                .fileUrl(request.getFileUrl())
                .fileName(request.getFileName())
                .build();

        message = messageRepository.save(message);

        String preview = request.getContent();
        if (preview != null && preview.length() > 100) {
            preview = preview.substring(0, 100) + "...";
        }
        conversation.setLastMessageText(preview);
        conversation.setLastMessageAt(message.getCreatedAt() != null ? message.getCreatedAt() : LocalDateTime.now());
        conversation.setLastSenderId(sender.getEmployeeId());
        conversationRepository.save(conversation);

        ChatMessageResponse response = mapToMessageResponse(message, conversation, sender.getEmployeeId());
        response.setRecipientId(recipient.getEmployeeId());

        wsNotificationService.sendChatMessage(recipient.getEmail(), response);

        long unreadCount = messageRepository.countTotalUnreadForUser(recipient.getEmployeeId());
        wsNotificationService.sendUnreadChatCount(recipient.getEmail(), unreadCount);

        return response;
    }

    @Transactional
    public int markConversationAsRead(String email, Long conversationId) {
        Employee employee = findEmployeeByEmail(email);
        ChatConversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found"));

        if (!isParticipant(conversation, employee.getEmployeeId())) {
            throw new AccessDeniedException("You are not a participant of this conversation");
        }

        int updated = messageRepository.markConversationAsRead(conversationId, employee.getEmployeeId());

        if (updated > 0) {
            long newUnreadCount = messageRepository.countTotalUnreadForUser(employee.getEmployeeId());
            wsNotificationService.sendUnreadChatCount(employee.getEmail(), newUnreadCount);
        }

        return updated;
    }

    public long getTotalUnreadCount(String email) {
        Employee employee = findEmployeeByEmail(email);
        return messageRepository.countTotalUnreadForUser(employee.getEmployeeId());
    }

    public Employee getOtherParticipantByConversation(Long conversationId, Integer currentEmpId) {
        ChatConversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found"));
        return getOtherParticipant(conversation, currentEmpId);
    }

    private Employee findEmployeeByEmail(String email) {
        return employeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with email: " + email));
    }

    private boolean isParticipant(ChatConversation conv, Integer empId) {
        return conv.getParticipant1().getEmployeeId().equals(empId)
                || conv.getParticipant2().getEmployeeId().equals(empId);
    }

    private Employee getOtherParticipant(ChatConversation conv, Integer currentEmpId) {
        if (conv.getParticipant1().getEmployeeId().equals(currentEmpId)) {
            return conv.getParticipant2();
        }
        return conv.getParticipant1();
    }

    private ConversationResponse mapToConversationResponse(ChatConversation conv, Integer currentEmpId) {
        Employee other = getOtherParticipant(conv, currentEmpId);
        long unread = messageRepository.countByConversation_ConversationIdAndIsReadAndSender_EmployeeIdNot(
                conv.getConversationId(), false, currentEmpId);

        return ConversationResponse.builder()
                .conversationId(conv.getConversationId())
                .otherParticipantId(other.getEmployeeId())
                .otherParticipantEmail(other.getEmail())
                .otherParticipantName(other.getFirstName() + " " + other.getLastName())
                .otherParticipantDesignation(other.getDesignation() != null ? other.getDesignation().getDesignationName() : "")
                .otherParticipantCode(other.getEmployeeCode())
                .otherParticipantRole(other.getRole() != null ? other.getRole().name() : "")
                .otherParticipantDepartment(other.getDepartment() != null ? other.getDepartment().getDepartmentName() : "")
                .lastMessageText(conv.getLastMessageText())
                .lastSenderId(conv.getLastSenderId())
                .lastMessageAt(conv.getLastMessageAt())
                .unreadCount(unread)
                .online(presenceService.isOnline(other.getEmail()))
                .build();
    }

    private ChatMessageResponse mapToMessageResponse(ChatMessage msg, ChatConversation conv, Integer currentEmpId) {
        Employee other = getOtherParticipant(conv, msg.getSender().getEmployeeId());
        return ChatMessageResponse.builder()
                .messageId(msg.getMessageId())
                .conversationId(conv.getConversationId())
                .senderId(msg.getSender().getEmployeeId())
                .senderName(msg.getSender().getFirstName() + " " + msg.getSender().getLastName())
                .senderCode(msg.getSender().getEmployeeCode())
                .recipientId(other.getEmployeeId())
                .content(msg.getContent())
                .messageType(msg.getMessageType().name())
                .fileUrl(msg.getFileUrl())
                .fileName(msg.getFileName())
                .isRead(msg.getIsRead())
                .createdAt(msg.getCreatedAt())
                .build();
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/service/DashboardService.java`**
```java
package com.revworkforce.employeemanagementservice.service;

import com.revworkforce.employeemanagementservice.dto.DashboardResponse;
import com.revworkforce.employeemanagementservice.dto.EmployeeDashboardResponse;
import com.revworkforce.employeemanagementservice.dto.EmployeeReportResponse;
import com.revworkforce.employeemanagementservice.dto.LeaveReportResponse;
import com.revworkforce.employeemanagementservice.exception.ResourceNotFoundException;
import com.revworkforce.employeemanagementservice.model.Employee;
import com.revworkforce.employeemanagementservice.model.Holiday;
import com.revworkforce.employeemanagementservice.model.LeaveBalance;
import com.revworkforce.employeemanagementservice.model.enums.LeaveStatus;
import com.revworkforce.employeemanagementservice.model.enums.Role;
import com.revworkforce.employeemanagementservice.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;
    @Autowired
    private LeaveBalanceRepository leaveBalanceRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private DesignationRepository designationRepository;
    @Autowired
    private HolidayRepository holidayRepository;
    @Autowired
    private PerformanceReviewRepository performanceReviewRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    public DashboardResponse getDashboard() {
        long totalEmployees = employeeRepository.count();
        long activeEmployees = employeeRepository.countByIsActive(true);
        long inactiveEmployees = employeeRepository.countByIsActive(false);
        long totalManagers = employeeRepository.countByRoleAndIsActive(Role.MANAGER, true);
        long totalAdmins = employeeRepository.countByRoleAndIsActive(Role.ADMIN, true);
        long totalRegular = employeeRepository.countByRoleAndIsActive(Role.EMPLOYEE, true);
        long pendingLeaves = leaveApplicationRepository.countByStatus(LeaveStatus.PENDING);
        long approvedToday = leaveApplicationRepository.findActiveLeavesToday(LeaveStatus.APPROVED, LocalDate.now()).size();
        long totalDepartments = departmentRepository.count();
        long totalDesignations = designationRepository.count();
        Map<String, Long> employeesByDept = new LinkedHashMap<>();
        List<Object[]> deptCounts = employeeRepository.countActiveByDepartment();
        for (Object[] row : deptCounts) {
            employeesByDept.put((String) row[0], (Long) row[1]);
        }
        return DashboardResponse.builder()
                .totalEmployees(totalEmployees).activeEmployees(activeEmployees)
                .inactiveEmployees(inactiveEmployees).totalManagers(totalManagers)
                .totalAdmins(totalAdmins).totalRegularEmployees(totalRegular)
                .pendingLeaves(pendingLeaves).approvedLeavesToday(approvedToday)
                .totalDepartments(totalDepartments).totalDesignations(totalDesignations)
                .employeesByDepartment(employeesByDept).build();
    }

    public List<LeaveReportResponse> getLeaveReport(Integer year) {
        int reportYear = year != null ? year : LocalDate.now().getYear();
        List<LeaveBalance> allBalances = leaveBalanceRepository.findAll();

        return allBalances.stream()
                .filter(b -> b.getYear().equals(reportYear))
                .map(b -> LeaveReportResponse.builder()
                        .employeeCode(b.getEmployee().getEmployeeCode())
                        .employeeName(b.getEmployee().getFirstName() + " " + b.getEmployee().getLastName())
                        .departmentName(b.getEmployee().getDepartment() != null ? b.getEmployee().getDepartment().getDepartmentName() : "N/A")
                        .leaveTypeName(b.getLeaveType().getLeaveTypeName())
                        .totalLeaves(b.getTotalLeaves())
                        .usedLeaves(b.getUsedLeaves())
                        .availableBalance(b.getAvailableBalance())
                        .year(b.getYear())
                        .build())
                .collect(Collectors.toList());
    }

    public List<LeaveReportResponse> getLeaveReportByDepartment(Integer departmentId, Integer year) {
        int reportYear = year != null ? year : LocalDate.now().getYear();
        List<LeaveBalance> allBalances = leaveBalanceRepository.findAll();

        return allBalances.stream()
                .filter(b -> b.getYear().equals(reportYear))
                .filter(b -> b.getEmployee().getDepartment() != null && b.getEmployee().getDepartment().getDepartmentId().equals(departmentId))
                .map(b -> LeaveReportResponse.builder()
                        .employeeCode(b.getEmployee().getEmployeeCode())
                        .employeeName(b.getEmployee().getFirstName() + " " + b.getEmployee().getLastName())
                        .departmentName(b.getEmployee().getDepartment().getDepartmentName())
                        .leaveTypeName(b.getLeaveType().getLeaveTypeName())
                        .totalLeaves(b.getTotalLeaves())
                        .usedLeaves(b.getUsedLeaves())
                        .availableBalance(b.getAvailableBalance())
                        .year(b.getYear())
                        .build())
                .collect(Collectors.toList());
    }

    public List<LeaveReportResponse> getLeaveReportByEmployee(String employeeCode, Integer year) {
        int reportYear = year != null ? year : LocalDate.now().getYear();
        List<LeaveBalance> allBalances = leaveBalanceRepository.findAll();

        return allBalances.stream()
                .filter(b -> b.getYear().equals(reportYear))
                .filter(b -> b.getEmployee().getEmployeeCode().equals(employeeCode))
                .map(b -> LeaveReportResponse.builder()
                        .employeeCode(b.getEmployee().getEmployeeCode())
                        .employeeName(b.getEmployee().getFirstName() + " " + b.getEmployee().getLastName())
                        .departmentName(b.getEmployee().getDepartment() != null ? b.getEmployee().getDepartment().getDepartmentName() : "N/A")
                        .leaveTypeName(b.getLeaveType().getLeaveTypeName())
                        .totalLeaves(b.getTotalLeaves()).usedLeaves(b.getUsedLeaves())
                        .availableBalance(b.getAvailableBalance()).year(b.getYear()).build())
                .collect(Collectors.toList());
    }

    public EmployeeDashboardResponse getEmployeeDashboard(String email) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with email: " + email));

        int currentYear = LocalDate.now().getYear();

        List<LeaveBalance> balances = leaveBalanceRepository
                .findByEmployee_EmployeeIdAndYear(employee.getEmployeeId(), currentYear);
        List<EmployeeDashboardResponse.LeaveBalanceSummary> leaveBalanceSummaries = balances.stream()
                .map(b -> EmployeeDashboardResponse.LeaveBalanceSummary.builder()
                        .leaveTypeName(b.getLeaveType().getLeaveTypeName())
                        .totalLeaves(b.getTotalLeaves())
                        .usedLeaves(b.getUsedLeaves())
                        .availableBalance(b.getAvailableBalance())
                        .build())
                .collect(Collectors.toList());

        long pendingLeaves = leaveApplicationRepository.countByEmployee_EmployeeIdAndStatus(
                employee.getEmployeeId(), LeaveStatus.PENDING);
        long approvedLeaves = leaveApplicationRepository.countByEmployee_EmployeeIdAndStatus(
                employee.getEmployeeId(), LeaveStatus.APPROVED);

        LocalDate today = LocalDate.now();
        List<Holiday> holidays = holidayRepository.findByHolidayDateBetween(today, today.plusDays(30));
        List<EmployeeDashboardResponse.UpcomingHolidaySummary> upcomingHolidays = holidays.stream()
                .map(h -> EmployeeDashboardResponse.UpcomingHolidaySummary.builder()
                        .holidayName(h.getHolidayName())
                        .holidayDate(h.getHolidayDate())
                        .description(h.getDescription())
                        .build())
                .collect(Collectors.toList());

        long unreadNotifications = notificationRepository
                .countByRecipient_EmployeeIdAndIsRead(employee.getEmployeeId(), false);

        return EmployeeDashboardResponse.builder()
                .employeeName(employee.getFirstName() + " " + employee.getLastName())
                .employeeCode(employee.getEmployeeCode())
                .departmentName(employee.getDepartment() != null ? employee.getDepartment().getDepartmentName() : "N/A")
                .designationTitle(employee.getDesignation() != null ? employee.getDesignation().getDesignationName() : "N/A")
                .pendingLeaveRequests(pendingLeaves)
                .approvedLeaves(approvedLeaves)
                .unreadNotifications(unreadNotifications)
                .leaveBalances(leaveBalanceSummaries)
                .upcomingHolidays(upcomingHolidays)
                .build();
    }

    public EmployeeReportResponse getEmployeeReport() {
        long totalEmployees = employeeRepository.count();
        long activeEmployees = employeeRepository.countByIsActive(true);
        long inactiveEmployees = employeeRepository.countByIsActive(false);

        Map<String, Long> headcountByDept = new LinkedHashMap<>();
        for (Object[] row : employeeRepository.countActiveByDepartment()) {
            headcountByDept.put((String) row[0], (Long) row[1]);
        }

        Map<String, Long> headcountByRole = new LinkedHashMap<>();
        for (Object[] row : employeeRepository.countActiveByRole()) {
            headcountByRole.put(row[0].toString(), (Long) row[1]);
        }

        List<EmployeeReportResponse.JoiningTrend> joiningTrends = new ArrayList<>();
        for (Object[] row : employeeRepository.getJoiningTrends()) {
            int year = ((Number) row[0]).intValue();
            int month = ((Number) row[1]).intValue();
            long count = ((Number) row[2]).longValue();
            joiningTrends.add(EmployeeReportResponse.JoiningTrend.builder()
                    .period(String.format("%d-%02d", year, month))
                    .count(count)
                    .build());
        }

        List<Employee> activeEmps = employeeRepository.findAll().stream()
                .filter(Employee::getIsActive)
                .filter(e -> e.getJoiningDate() != null)
                .toList();
        double avgTenure = 0;
        if (!activeEmps.isEmpty()) {
            LocalDate today = LocalDate.now();
            long totalMonths = activeEmps.stream()
                    .mapToLong(e -> ChronoUnit.MONTHS.between(e.getJoiningDate(), today))
                    .sum();
            avgTenure = Math.round((double) totalMonths / activeEmps.size() * 100.0) / 100.0;
        }

        return EmployeeReportResponse.builder()
                .totalEmployees(totalEmployees)
                .activeEmployees(activeEmployees)
                .inactiveEmployees(inactiveEmployees)
                .headcountByDepartment(headcountByDept)
                .headcountByRole(headcountByRole)
                .joiningTrends(joiningTrends)
                .averageTenureMonths(avgTenure)
                .build();
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/service/DepartmentService.java`**
```java
package com.revworkforce.employeemanagementservice.service;

import com.revworkforce.employeemanagementservice.dto.DepartmentRequest;
import com.revworkforce.employeemanagementservice.exception.BadRequestException;
import com.revworkforce.employeemanagementservice.exception.DuplicateResourceException;
import com.revworkforce.employeemanagementservice.exception.InvalidActionException;
import com.revworkforce.employeemanagementservice.exception.ResourceNotFoundException;
import com.revworkforce.employeemanagementservice.model.Department;
import com.revworkforce.employeemanagementservice.repository.DepartmentRepository;
import com.revworkforce.employeemanagementservice.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    public Department createDepartment(DepartmentRequest request) {
        if (departmentRepository.existsByDepartmentName(request.getDepartmentName())) {
            throw new DuplicateResourceException("Department '" + request.getDepartmentName() + "' already exists");
        }
        Department department = Department.builder()
                .departmentName(request.getDepartmentName())
                .description(request.getDescription())
                .build();
        return departmentRepository.save(department);
    }

    public Department updateDepartment(Integer departmentId, DepartmentRequest request) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + departmentId));
        departmentRepository.findByDepartmentName(request.getDepartmentName()).ifPresent(existing -> {
            if (!existing.getDepartmentId().equals(departmentId)) {
                throw new DuplicateResourceException("Department '" + request.getDepartmentName() + "' already exists");
            }
        });
        department.setDepartmentName(request.getDepartmentName());
        department.setDescription(request.getDescription());
        return departmentRepository.save(department);
    }

    public Department deactivateDepartment(Integer departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + departmentId));
        long employeeCount = employeeRepository.countByDepartment_DepartmentId(departmentId);
        if (employeeCount > 0) {
            throw new BadRequestException("Cannot deactivate department. " + employeeCount + " employee(s) are assigned to it.");
        }
        if (!department.getIsActive()) {
            throw new InvalidActionException("Department is already deactivated");
        }
        department.setIsActive(false);
        return departmentRepository.save(department);
    }

    public Department activateDepartment(Integer departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + departmentId));
        if (department.getIsActive()) {
            throw new InvalidActionException("Department is already active");
        }
        department.setIsActive(true);
        return departmentRepository.save(department);
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department getDepartmentById(Integer departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + departmentId));
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/service/DesignationService.java`**
```java
package com.revworkforce.employeemanagementservice.service;

import com.revworkforce.employeemanagementservice.dto.DesignationRequest;
import com.revworkforce.employeemanagementservice.exception.DuplicateResourceException;
import com.revworkforce.employeemanagementservice.exception.InvalidActionException;
import com.revworkforce.employeemanagementservice.exception.ResourceNotFoundException;
import com.revworkforce.employeemanagementservice.model.Designation;
import com.revworkforce.employeemanagementservice.repository.DesignationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DesignationService {
    @Autowired
    private DesignationRepository designationRepository;

    public Designation createDesignation(DesignationRequest request) {
        if (designationRepository.existsByDesignationName(request.getDesignationName())) {
            throw new DuplicateResourceException("Designation '" + request.getDesignationName() + "' already exists");
        }
        Designation designation = Designation.builder()
                .designationName(request.getDesignationName())
                .description(request.getDescription())
                .build();
        return designationRepository.save(designation);
    }

    public Designation updateDesignation(Integer designationId, DesignationRequest request) {
        Designation designation = designationRepository.findById(designationId)
                .orElseThrow(() -> new ResourceNotFoundException("Designation not found with id: " + designationId));
        designationRepository.findByDesignationName(request.getDesignationName()).ifPresent(existing -> {
            if (!existing.getDesignationId().equals(designationId)) {
                throw new DuplicateResourceException("Designation '" + request.getDesignationName() + "' already exists");
            }
        });
        designation.setDesignationName(request.getDesignationName());
        designation.setDescription(request.getDescription());
        return designationRepository.save(designation);
    }

    public Designation deactivateDesignation(Integer designationId) {
        Designation designation = designationRepository.findById(designationId)
                .orElseThrow(() -> new ResourceNotFoundException("Designation not found with id: " + designationId));
        if (!designation.getIsActive()) {
            throw new InvalidActionException("Designation is already deactivated");
        }
        designation.setIsActive(false);
        return designationRepository.save(designation);
    }

    public Designation activateDesignation(Integer designationId) {
        Designation designation = designationRepository.findById(designationId)
                .orElseThrow(() -> new ResourceNotFoundException("Designation not found with id: " + designationId));
        if (designation.getIsActive()) {
            throw new InvalidActionException("Designation is already active");
        }
        designation.setIsActive(true);
        return designationRepository.save(designation);
    }

    public List<Designation> getAllDesignations() {
        return designationRepository.findAll();
    }

    public Designation getDesignationById(Integer designationId) {
        return designationRepository.findById(designationId)
                .orElseThrow(() -> new ResourceNotFoundException("Designation not found with id: " + designationId));
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/service/EmailService.java`**
```java
package com.revworkforce.employeemanagementservice.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String fromEmail;

    @Value("${otp.expiry-minutes:5}")
    private int otpExpiryMinutes;

    @Async
    public void sendOtpEmail(String toEmail, String employeeName, String otp) {
        if (mailSender == null || fromEmail == null || fromEmail.isBlank()) {
            log.warn("SMTP is not configured; skipping OTP email for {}", toEmail);
            return;
        }
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("WorkForce HRMS — Your Login Verification Code");
            helper.setText(buildOtpEmailTemplate(employeeName, otp), true);

            mailSender.send(message);
            log.info("OTP email sent successfully to: {}", toEmail);
        } catch (MessagingException e) {
            log.error("Failed to send OTP email to {}: {}", toEmail, e.getMessage());
            throw new RuntimeException("Failed to send OTP email. Please try again.");
        }
    }

    @Async
    public void sendWelcomeEmail(String toEmail, String employeeName, String employeeCode,
                                  String password, String role) {
        if (mailSender == null || fromEmail == null || fromEmail.isBlank()) {
            log.warn("SMTP is not configured; skipping welcome email for {}", toEmail);
            return;
        }
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Welcome to WorkForce HRMS — Your Account is Ready!");
            helper.setText(buildWelcomeEmailTemplate(employeeName, employeeCode, toEmail, password, role), true);

            mailSender.send(message);
            log.info("Welcome email sent successfully to: {}", toEmail);
        } catch (MessagingException e) {
            log.error("Failed to send welcome email to {}: {}", toEmail, e.getMessage());
        }
    }

    private String buildWelcomeEmailTemplate(String employeeName, String employeeCode,
                                              String email, String password, String role) {
        String roleBadgeColor;
        switch (role.toUpperCase()) {
            case "ADMIN":   roleBadgeColor = "#8b5cf6"; break;
            case "MANAGER": roleBadgeColor = "#3b82f6"; break;
            default:        roleBadgeColor = "#10b981"; break;
        }

        return "<!DOCTYPE html>" +
            "<html><head><meta charset=\"UTF-8\"></head>" +
            "<body style=\"margin:0;padding:0;background-color:#f8fafc;font-family:'Segoe UI',Roboto,Helvetica,Arial,sans-serif;\">" +
            "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"background-color:#f8fafc;padding:40px 0;\">" +
            "<tr><td align=\"center\">" +
            "<table width=\"520\" cellpadding=\"0\" cellspacing=\"0\" style=\"background-color:#ffffff;border-radius:16px;overflow:hidden;box-shadow:0 4px 6px rgba(0,0,0,0.07);\">" +

            "<tr><td style=\"background:linear-gradient(135deg,#1e3a5f 0%,#2563eb 100%);padding:36px 40px;text-align:center;\">" +
            "<h1 style=\"margin:0;font-size:28px;font-weight:700;color:#ffffff;letter-spacing:0.5px;\">WorkForce</h1>" +
            "<p style=\"margin:4px 0 0;font-size:12px;color:#93c5fd;letter-spacing:2px;text-transform:uppercase;\">HRMS Portal</p>" +
            "</td></tr>" +

            "<tr><td style=\"padding:40px 40px 0;text-align:center;\">" +
            "<div style=\"width:64px;height:64px;margin:0 auto 20px;border-radius:50%;background:linear-gradient(135deg,#dbeafe,#eff6ff);display:flex;align-items:center;justify-content:center;\">" +
            "<span style=\"font-size:32px;\">&#127881;</span>" +
            "</div>" +
            "<h2 style=\"margin:0 0 8px;font-size:22px;font-weight:700;color:#1e293b;\">Welcome aboard, " + employeeName + "!</h2>" +
            "<p style=\"margin:0 0 28px;font-size:14px;color:#64748b;line-height:1.6;\">" +
            "Your WorkForce HRMS account has been created. Below are your login credentials to get started.</p>" +
            "</td></tr>" +

            "<tr><td style=\"padding:0 40px;\">" +
            "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"background:#f8fafc;border:1px solid #e2e8f0;border-radius:12px;overflow:hidden;\">" +
            "<tr><td style=\"padding:20px 24px;border-bottom:1px solid #e2e8f0;\">" +
            "<table width=\"100%\"><tr>" +
            "<td style=\"font-size:13px;color:#64748b;font-weight:500;\">Employee Code</td>" +
            "<td style=\"font-size:14px;color:#1e293b;font-weight:700;text-align:right;font-family:monospace;letter-spacing:1px;\">" + employeeCode + "</td>" +
            "</tr></table></td></tr>" +
            "<tr><td style=\"padding:20px 24px;border-bottom:1px solid #e2e8f0;\">" +
            "<table width=\"100%\"><tr>" +
            "<td style=\"font-size:13px;color:#64748b;font-weight:500;\">Email</td>" +
            "<td style=\"font-size:14px;color:#1e293b;font-weight:600;text-align:right;\">" + email + "</td>" +
            "</tr></table></td></tr>" +
            "<tr><td style=\"padding:20px 24px;border-bottom:1px solid #e2e8f0;\">" +
            "<table width=\"100%\"><tr>" +
            "<td style=\"font-size:13px;color:#64748b;font-weight:500;\">Password</td>" +
            "<td style=\"font-size:14px;color:#1e293b;font-weight:700;text-align:right;font-family:monospace;letter-spacing:1px;\">" + password + "</td>" +
            "</tr></table></td></tr>" +
            "<tr><td style=\"padding:20px 24px;\">" +
            "<table width=\"100%\"><tr>" +
            "<td style=\"font-size:13px;color:#64748b;font-weight:500;\">Role</td>" +
            "<td style=\"text-align:right;\">" +
            "<span style=\"display:inline-block;padding:4px 14px;border-radius:20px;font-size:12px;font-weight:700;" +
            "color:#ffffff;background-color:" + roleBadgeColor + ";letter-spacing:0.5px;\">" + role.toUpperCase() + "</span>" +
            "</td></tr></table></td></tr>" +
            "</table></td></tr>" +

            "<tr><td style=\"padding:24px 40px 0;\">" +
            "<div style=\"padding:16px 20px;background-color:#fef3c7;border-left:4px solid #f59e0b;border-radius:0 8px 8px 0;\">" +
            "<p style=\"margin:0;font-size:13px;color:#92400e;line-height:1.5;\">" +
            "<strong>&#128274; Security Tip:</strong> Please change your password after your first login to keep your account secure.</p>" +
            "</div></td></tr>" +

            "<tr><td style=\"padding:24px 40px 0;\">" +
            "<p style=\"margin:0 0 12px;font-size:14px;font-weight:600;color:#1e293b;\">What you can do on WorkForce:</p>" +
            "<table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">" +
            "<tr><td style=\"padding:6px 0;font-size:13px;color:#475569;\">&#128197; &nbsp;Mark attendance &amp; track hours</td></tr>" +
            "<tr><td style=\"padding:6px 0;font-size:13px;color:#475569;\">&#128221; &nbsp;Apply for leaves &amp; view balance</td></tr>" +
            "<tr><td style=\"padding:6px 0;font-size:13px;color:#475569;\">&#127919; &nbsp;Set goals &amp; track performance</td></tr>" +
            "<tr><td style=\"padding:6px 0;font-size:13px;color:#475569;\">&#128276; &nbsp;Get real-time notifications</td></tr>" +
            "<tr><td style=\"padding:6px 0;font-size:13px;color:#475569;\">&#129302; &nbsp;Chat with AI assistant for quick help</td></tr>" +
            "</table></td></tr>" +

            "<tr><td style=\"padding:32px 40px;\">" +
            "<div style=\"border-top:1px solid #e2e8f0;padding-top:24px;text-align:center;\">" +
            "<p style=\"margin:0;font-size:12px;color:#94a3b8;\">This is an automated message from WorkForce HRMS. Please do not reply.</p>" +
            "<p style=\"margin:8px 0 0;font-size:11px;color:#cbd5e1;\">&copy; 2026 WorkForce HRMS. All rights reserved.</p>" +
            "</div></td></tr>" +

            "</table></td></tr></table></body></html>";
    }

    private String buildOtpEmailTemplate(String employeeName, String otp) {
        StringBuilder otpBoxes = new StringBuilder();
        for (char digit : otp.toCharArray()) {
            otpBoxes.append(
                "<td style=\"width:48px;height:56px;text-align:center;font-size:28px;font-weight:700;" +
                "color:#1e293b;background-color:#f1f5f9;border:2px solid #e2e8f0;border-radius:12px;" +
                "font-family:'Segoe UI',Roboto,sans-serif;letter-spacing:2px;\">"
                + digit + "</td><td style=\"width:8px;\"></td>"
            );
        }

        return "<!DOCTYPE html>" +
            "<html><head><meta charset=\"UTF-8\"></head>" +
            "<body style=\"margin:0;padding:0;background-color:#f8fafc;font-family:'Segoe UI',Roboto,Helvetica,Arial,sans-serif;\">" +
            "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"background-color:#f8fafc;padding:40px 0;\">" +
            "<tr><td align=\"center\">" +
            "<table width=\"480\" cellpadding=\"0\" cellspacing=\"0\" style=\"background-color:#ffffff;border-radius:16px;overflow:hidden;box-shadow:0 4px 6px rgba(0,0,0,0.07);\">" +

            "<tr><td style=\"background:linear-gradient(135deg,#1e3a5f 0%,#2563eb 100%);padding:32px 40px;text-align:center;\">" +
            "<h1 style=\"margin:0;font-size:24px;font-weight:700;color:#ffffff;letter-spacing:0.5px;\">WorkForce</h1>" +
            "<p style=\"margin:4px 0 0;font-size:12px;color:#93c5fd;letter-spacing:2px;text-transform:uppercase;\">HRMS Portal</p>" +
            "</td></tr>" +

            "<tr><td style=\"padding:40px;\">" +
            "<p style=\"margin:0 0 8px;font-size:16px;color:#1e293b;\">Hello <strong>" + employeeName + "</strong>,</p>" +
            "<p style=\"margin:0 0 28px;font-size:14px;color:#64748b;line-height:1.6;\">" +
            "We received a login request for your WorkForce account. Use the verification code below to complete your sign-in:</p>" +

            "<table cellpadding=\"0\" cellspacing=\"0\" style=\"margin:0 auto 28px;\">" +
            "<tr>" + otpBoxes.toString() + "</tr>" +
            "</table>" +

            "<div style=\"text-align:center;margin-bottom:28px;\">" +
            "<span style=\"display:inline-block;padding:8px 20px;background-color:#fef3c7;color:#92400e;border-radius:8px;font-size:13px;font-weight:500;\">" +
            "&#9200; This code expires in " + otpExpiryMinutes + " minutes" +
            "</span></div>" +

            "<div style=\"padding:16px 20px;background-color:#f0f9ff;border-left:4px solid #3b82f6;border-radius:0 8px 8px 0;margin-bottom:8px;\">" +
            "<p style=\"margin:0;font-size:13px;color:#1e40af;line-height:1.5;\">" +
            "<strong>Security Notice:</strong> If you did not request this code, please ignore this email and ensure your account credentials are secure.</p>" +
            "</div>" +
            "</td></tr>" +

            "<tr><td style=\"padding:24px 40px;background-color:#f8fafc;border-top:1px solid #e2e8f0;text-align:center;\">" +
            "<p style=\"margin:0;font-size:12px;color:#94a3b8;\">This is an automated message from WorkForce HRMS. Please do not reply.</p>" +
            "<p style=\"margin:8px 0 0;font-size:11px;color:#cbd5e1;\">&copy; 2026 WorkForce HRMS. All rights reserved.</p>" +
            "</td></tr>" +

            "</table></td></tr></table></body></html>";
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/service/EmployeeService.java`**
```java
package com.revworkforce.employeemanagementservice.service;

import com.revworkforce.employeemanagementservice.dto.EmployeeProfileResponse;
import com.revworkforce.employeemanagementservice.dto.RegisterEmployeeRequest;
import com.revworkforce.employeemanagementservice.dto.UpdateEmployeeRequest;
import com.revworkforce.employeemanagementservice.dto.UpdateProfileRequest;
import com.revworkforce.employeemanagementservice.exception.*;
import com.revworkforce.employeemanagementservice.model.*;
import com.revworkforce.employeemanagementservice.model.enums.*;
import com.revworkforce.employeemanagementservice.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.revworkforce.employeemanagementservice.dto.ChangePasswordRequest;

@Service
@Transactional
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private DesignationRepository designationRepository;
    @Autowired
    private ActivityLogRepository activityLogRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;

    public Employee registerEmployee(RegisterEmployeeRequest request) {
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + request.getEmail());
        }
        Role role = Role.EMPLOYEE;
        if (request.getRole() != null && !request.getRole().isBlank()) {
            try {
                role = Role.valueOf(request.getRole().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid role value: " + request.getRole() + ". Allowed values: EMPLOYEE, MANAGER, ADMIN");
            }
        }
        String employeeCode = generateEmployeeCode(role);

        boolean enforce2FA = (role == Role.EMPLOYEE || role == Role.MANAGER);

        Employee employee = Employee.builder()
                .firstName(request.getFirstName()).lastName(request.getLastName())
                .email(request.getEmail()).passwordHash(passwordEncoder.encode(request.getPassword()))
                .employeeCode(employeeCode).phone(request.getPhone()).dateOfBirth(request.getDateOfBirth())
                .address(request.getAddress()).emergencyContactName(request.getEmergencyContactName())
                .emergencyContactPhone(request.getEmergencyContactPhone()).joiningDate(request.getJoiningDate())
                .salary(request.getSalary()).role(role).twoFactorEnabled(enforce2FA).build();
        if (request.getGender() != null && !request.getGender().isBlank()) {
            try {
                employee.setGender(Gender.valueOf(request.getGender().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid gender value: " + request.getGender() + ". Allowed values: MALE, FEMALE, OTHER");
            }
        }
        if (request.getDepartmentId() != null) {
            Department dept = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + request.getDepartmentId()));
            employee.setDepartment(dept);
        }
        if (request.getDesignationId() != null) {
            Designation desig = designationRepository.findById(request.getDesignationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Designation not found with id: " + request.getDesignationId()));
            employee.setDesignation(desig);
        }
        if (role == Role.EMPLOYEE) {
            if (request.getManagerCode() == null || request.getManagerCode().isBlank()) {
                throw new BadRequestException("Manager code is required for EMPLOYEE role. Provide a valid manager code (e.g. MG001).");
            }
            Employee manager = employeeRepository.findByEmployeeCode(request.getManagerCode())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager not found with code: " + request.getManagerCode()));
            if (manager.getRole() != Role.MANAGER && manager.getRole() != Role.ADMIN) {
                throw new BadRequestException("Employee with code " + request.getManagerCode() + " is not a MANAGER or ADMIN. Only managers/admins can be assigned as a manager.");
            }
            employee.setManager(manager);
        }
        Employee savedEmployee = employeeRepository.save(employee);

        try {
            emailService.sendWelcomeEmail(
                    savedEmployee.getEmail(),
                    savedEmployee.getFirstName() + " " + savedEmployee.getLastName(),
                    savedEmployee.getEmployeeCode(),
                    request.getPassword(),
                    savedEmployee.getRole().name()
            );
        } catch (Exception e) {
        }

        return savedEmployee;
    }

    private String generateEmployeeCode(Role role) {
        String prefix = switch (role) {
            case ADMIN -> "ADM";
            case MANAGER -> "MG";
            case EMPLOYEE -> "EMP";
        };
        var latestCode = employeeRepository.findLatestEmployeeCodeByPrefix(prefix);
        int nextNumber = 1;
        if (latestCode.isPresent()) {
            String numericPart = latestCode.get().substring(prefix.length());
            nextNumber = Integer.parseInt(numericPart) + 1;
        }
        return String.format("%s%03d", prefix, nextNumber);
    }

    public Employee getEmployeeByEmail(String email) {
        return employeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with email: " + email));
    }

    public Employee updateProfile(Integer employeeId, UpdateProfileRequest request) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));
        StringBuilder changes = new StringBuilder();
        if (request.getPhone() != null && !request.getPhone().equals(employee.getPhone())) {
            changes.append(String.format("Phone: '%s' -> '%s'; ",
                    employee.getPhone() != null ? employee.getPhone() : "null", request.getPhone()));
            employee.setPhone(request.getPhone());
        }
        if (request.getAddress() != null && !request.getAddress().equals(employee.getAddress())) {
            changes.append(String.format("Address: '%s' -> '%s'; ",
                    employee.getAddress() != null ? employee.getAddress() : "null", request.getAddress()));
            employee.setAddress(request.getAddress());
        }
        if (request.getEmergencyContactName() != null
                && !request.getEmergencyContactName().equals(employee.getEmergencyContactName())) {
            changes.append(String.format("EmergencyContactName: '%s' -> '%s'; ",
                    employee.getEmergencyContactName() != null ? employee.getEmergencyContactName() : "null",
                    request.getEmergencyContactName()));
            employee.setEmergencyContactName(request.getEmergencyContactName());
        }
        if (request.getEmergencyContactPhone() != null
                && !request.getEmergencyContactPhone().equals(employee.getEmergencyContactPhone())) {
            changes.append(String.format("EmergencyContactPhone: '%s' -> '%s'; ",
                    employee.getEmergencyContactPhone() != null ? employee.getEmergencyContactPhone() : "null",
                    request.getEmergencyContactPhone()));
            employee.setEmergencyContactPhone(request.getEmergencyContactPhone());
        }
        Employee savedEmployee = employeeRepository.save(employee);
        if (!changes.isEmpty()) {
            ActivityLog log = ActivityLog.builder()
                    .performedBy(employee)
                    .action("PROFILE_UPDATE")
                    .entityType("EMPLOYEE")
                    .entityId(employeeId)
                    .details(changes.toString())
                    .build();
            activityLogRepository.save(log);
        }
        return savedEmployee;
    }

    @Transactional(readOnly = true)
    public EmployeeProfileResponse getEmployeeProfileByEmail(String email) {
        Employee employee = getEmployeeByEmail(email);
        return mapToProfileResponse(employee);
    }

    public EmployeeProfileResponse updateProfileWithResponse(String email, UpdateProfileRequest request) {
        Employee employee = getEmployeeByEmail(email);
        Employee updatedEmployee = updateProfile(employee.getEmployeeId(), request);
        return mapToProfileResponse(updatedEmployee);
    }

    private EmployeeProfileResponse mapToProfileResponse(Employee employee) {
        EmployeeProfileResponse.ManagerInfo managerInfo = null;
        if (employee.getManager() != null) {
            Employee manager = employee.getManager();
            managerInfo = EmployeeProfileResponse.ManagerInfo.builder()
                    .managerId(manager.getEmployeeId())
                    .managerCode(manager.getEmployeeCode())
                    .managerName(manager.getFirstName() + " " + manager.getLastName())
                    .managerEmail(manager.getEmail())
                    .managerPhone(manager.getPhone())
                    .build();
        }
        return EmployeeProfileResponse.builder()
                .employeeId(employee.getEmployeeId())
                .employeeCode(employee.getEmployeeCode())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .phone(employee.getPhone())
                .dateOfBirth(employee.getDateOfBirth())
                .gender(employee.getGender() != null ? employee.getGender().name() : null)
                .address(employee.getAddress())
                .emergencyContactName(employee.getEmergencyContactName())
                .emergencyContactPhone(employee.getEmergencyContactPhone())
                .departmentId(employee.getDepartment() != null ? employee.getDepartment().getDepartmentId() : null)
                .departmentName(employee.getDepartment() != null ? employee.getDepartment().getDepartmentName() : null)
                .designationId(employee.getDesignation() != null ? employee.getDesignation().getDesignationId() : null)
                .designationTitle(employee.getDesignation() != null ? employee.getDesignation().getDesignationName() : null)
                .joiningDate(employee.getJoiningDate())
                .salary(employee.getSalary())
                .role(employee.getRole().name())
                .isActive(employee.getIsActive())
                .twoFactorEnabled(employee.getTwoFactorEnabled())
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .manager(managerInfo)
                .build();
    }

    @Transactional(readOnly = true)
    public EmployeeProfileResponse getEmployeeByCode(String employeeCode) {
        Employee employee = employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with code: " + employeeCode));
        return mapToProfileResponse(employee);
    }

    @Transactional(readOnly = true)
    public Page<EmployeeProfileResponse> getEmployees(String keyword, Integer departmentId, String role, Boolean isActive, Pageable pageable) {
        Page<Employee> employees;
        if (keyword != null && !keyword.isBlank()) {
            employees = employeeRepository.searchByKeyword(keyword.trim(), pageable);
        } else if (departmentId != null) {
            employees = employeeRepository.findByDepartment_DepartmentId(departmentId, pageable);
        } else if (role != null && !role.isBlank()) {
            try {
                Role roleEnum = Role.valueOf(role.toUpperCase());
                employees = employeeRepository.findByRole(roleEnum, pageable);
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid role: " + role + ". Allowed: EMPLOYEE, MANAGER, ADMIN");
            }
        } else if (isActive != null) {
            employees = employeeRepository.findByIsActive(isActive, pageable);
        } else {
            employees = employeeRepository.findAll(pageable);
        }
        return employees.map(this::mapToProfileResponse);
    }

    public EmployeeProfileResponse updateEmployeeByAdmin(String employeeCode, UpdateEmployeeRequest request, String adminEmail) {
        Employee employee = employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with code: " + employeeCode));
        Employee admin = getEmployeeByEmail(adminEmail);
        StringBuilder changes = new StringBuilder();
        if (request.getFirstName() != null && !request.getFirstName().equals(employee.getFirstName())) {
            changes.append("FirstName: '").append(employee.getFirstName()).append("' -> '").append(request.getFirstName()).append("'; ");
            employee.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null && !request.getLastName().equals(employee.getLastName())) {
            changes.append("LastName: '").append(employee.getLastName()).append("' -> '").append(request.getLastName()).append("'; ");
            employee.setLastName(request.getLastName());
        }
        if (request.getEmail() != null && !request.getEmail().equals(employee.getEmail())) {
            if (employeeRepository.existsByEmail(request.getEmail())) {
                throw new DuplicateResourceException("Email already in use: " + request.getEmail());
            }
            changes.append("Email: '").append(employee.getEmail()).append("' -> '").append(request.getEmail()).append("'; ");
            employee.setEmail(request.getEmail());
        }
        if (request.getPhone() != null && !request.getPhone().equals(employee.getPhone())) {
            changes.append("Phone: '").append(employee.getPhone() != null ? employee.getPhone() : "null").append("' -> '").append(request.getPhone()).append("'; ");
            employee.setPhone(request.getPhone());
        }
        if (request.getDateOfBirth() != null && !request.getDateOfBirth().equals(employee.getDateOfBirth())) {
            changes.append("DateOfBirth: '").append(employee.getDateOfBirth() != null ? employee.getDateOfBirth() : "null").append("' -> '").append(request.getDateOfBirth()).append("'; ");
            employee.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getGender() != null && !request.getGender().isBlank()) {
            try {
                Gender newGender = Gender.valueOf(request.getGender().toUpperCase());
                if (employee.getGender() != newGender) {
                    changes.append("Gender: '").append(employee.getGender() != null ? employee.getGender() : "null").append("' -> '").append(newGender).append("'; ");
                }
                employee.setGender(newGender);
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid gender: " + request.getGender() + ". Allowed: MALE, FEMALE, OTHER");
            }
        }
        if (request.getAddress() != null && !request.getAddress().equals(employee.getAddress())) {
            changes.append("Address: '").append(employee.getAddress() != null ? employee.getAddress() : "null").append("' -> '").append(request.getAddress()).append("'; ");
            employee.setAddress(request.getAddress());
        }
        if (request.getEmergencyContactName() != null && !request.getEmergencyContactName().equals(employee.getEmergencyContactName())) {
            changes.append("EmergencyContactName: '").append(employee.getEmergencyContactName() != null ? employee.getEmergencyContactName() : "null").append("' -> '").append(request.getEmergencyContactName()).append("'; ");
            employee.setEmergencyContactName(request.getEmergencyContactName());
        }
        if (request.getEmergencyContactPhone() != null && !request.getEmergencyContactPhone().equals(employee.getEmergencyContactPhone())) {
            changes.append("EmergencyContactPhone: '").append(employee.getEmergencyContactPhone() != null ? employee.getEmergencyContactPhone() : "null").append("' -> '").append(request.getEmergencyContactPhone()).append("'; ");
            employee.setEmergencyContactPhone(request.getEmergencyContactPhone());
        }
        if (request.getDepartmentId() != null) {
            Department dept = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + request.getDepartmentId()));
            changes.append("Department changed; ");
            employee.setDepartment(dept);
        }
        if (request.getDesignationId() != null) {
            Designation desig = designationRepository.findById(request.getDesignationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Designation not found with id: " + request.getDesignationId()));
            changes.append("Designation changed; ");
            employee.setDesignation(desig);
        }
        if (request.getJoiningDate() != null && !request.getJoiningDate().equals(employee.getJoiningDate())) {
            changes.append("JoiningDate: '").append(employee.getJoiningDate() != null ? employee.getJoiningDate() : "null").append("' -> '").append(request.getJoiningDate()).append("'; ");
            employee.setJoiningDate(request.getJoiningDate());
        }
        if (request.getSalary() != null && !request.getSalary().equals(employee.getSalary())) {
            changes.append("Salary: '").append(employee.getSalary() != null ? employee.getSalary() : "null").append("' -> '").append(request.getSalary()).append("'; ");
            employee.setSalary(request.getSalary());
        }
        if (request.getRole() != null && !request.getRole().isBlank()) {
            try {
                Role newRole = Role.valueOf(request.getRole().toUpperCase());
                changes.append("Role: '").append(employee.getRole()).append("' -> '").append(newRole).append("'; ");
                employee.setRole(newRole);

                if ((newRole == Role.EMPLOYEE || newRole == Role.MANAGER) && !Boolean.TRUE.equals(employee.getTwoFactorEnabled())) {
                    employee.setTwoFactorEnabled(true);
                    changes.append("2FA: auto-enabled (mandatory for ").append(newRole).append("); ");
                }
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid role: " + request.getRole() + ". Allowed: EMPLOYEE, MANAGER, ADMIN");
            }
        }
        Employee saved = employeeRepository.save(employee);
        if (!changes.isEmpty()) {
            activityLogRepository.save(ActivityLog.builder()
                    .performedBy(admin).action("ADMIN_UPDATE_EMPLOYEE").entityType("EMPLOYEE")
                    .entityId(employee.getEmployeeId()).details(changes.toString()).build());
        }
        return mapToProfileResponse(saved);
    }

    public EmployeeProfileResponse deactivateEmployee(String employeeCode, String adminEmail) {
        Employee employee = employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with code: " + employeeCode));
        if (!employee.getIsActive()) {
            throw new InvalidActionException("Employee is already deactivated");
        }
        employee.setIsActive(false);
        Employee saved = employeeRepository.save(employee);
        Employee admin = getEmployeeByEmail(adminEmail);
        activityLogRepository.save(ActivityLog.builder()
                .performedBy(admin).action("DEACTIVATE_EMPLOYEE").entityType("EMPLOYEE")
                .entityId(employee.getEmployeeId()).details("Deactivated employee: " + employeeCode).build());
        return mapToProfileResponse(saved);
    }

    public EmployeeProfileResponse activateEmployee(String employeeCode, String adminEmail) {
        Employee employee = employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with code: " + employeeCode));
        if (employee.getIsActive()) {
            throw new InvalidActionException("Employee is already active");
        }
        employee.setIsActive(true);
        Employee saved = employeeRepository.save(employee);
        Employee admin = getEmployeeByEmail(adminEmail);
        activityLogRepository.save(ActivityLog.builder()
                .performedBy(admin).action("ACTIVATE_EMPLOYEE").entityType("EMPLOYEE")
                .entityId(employee.getEmployeeId()).details("Reactivated employee: " + employeeCode).build());
        return mapToProfileResponse(saved);
    }

    public void changePassword(String email, ChangePasswordRequest request) {
        Employee employee = getEmployeeByEmail(email);

        if (!passwordEncoder.matches(request.getCurrentPassword(), employee.getPasswordHash())) {
            throw new BadRequestException("Current password is incorrect");
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("New password and confirm password do not match");
        }
        if (passwordEncoder.matches(request.getNewPassword(), employee.getPasswordHash())) {
            throw new BadRequestException("New password must be different from the current password");
        }

        employee.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        employeeRepository.save(employee);

        activityLogRepository.save(ActivityLog.builder()
                .performedBy(employee).action("PASSWORD_CHANGE").entityType("EMPLOYEE")
                .entityId(employee.getEmployeeId()).details("Employee changed their own password").build());
    }

    public void forceResetPassword(String employeeCode, String newPassword, String adminEmail) {
        Employee employee = employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with code: " + employeeCode));
        Employee admin = getEmployeeByEmail(adminEmail);

        employee.setPasswordHash(passwordEncoder.encode(newPassword));
        employeeRepository.save(employee);

        activityLogRepository.save(ActivityLog.builder()
                .performedBy(admin).action("ADMIN_FORCE_RESET_PASSWORD").entityType("EMPLOYEE")
                .entityId(employee.getEmployeeId())
                .details("Admin force-reset password for employee: " + employeeCode).build());
    }

    public void enable2FA(String employeeCode, String adminEmail) {
        Employee employee = employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with code: " + employeeCode));
        if (Boolean.TRUE.equals(employee.getTwoFactorEnabled())) {
            throw new InvalidActionException("2FA is already enabled for this employee");
        }
        employee.setTwoFactorEnabled(true);
        employeeRepository.save(employee);
        Employee admin = getEmployeeByEmail(adminEmail);
        activityLogRepository.save(ActivityLog.builder()
                .performedBy(admin).action("ENABLE_2FA").entityType("EMPLOYEE")
                .entityId(employee.getEmployeeId())
                .details("Admin enabled 2FA for employee: " + employeeCode).build());
    }

    public void disable2FA(String employeeCode, String adminEmail) {
        Employee employee = employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with code: " + employeeCode));

        if (employee.getRole() == Role.EMPLOYEE || employee.getRole() == Role.MANAGER) {
            throw new InvalidActionException("Two-factor authentication is mandatory for "
                    + employee.getRole().name() + " role and cannot be disabled.");
        }

        if (!Boolean.TRUE.equals(employee.getTwoFactorEnabled())) {
            throw new InvalidActionException("2FA is already disabled for this employee");
        }
        employee.setTwoFactorEnabled(false);
        employeeRepository.save(employee);
        Employee admin = getEmployeeByEmail(adminEmail);
        activityLogRepository.save(ActivityLog.builder()
                .performedBy(admin).action("DISABLE_2FA").entityType("EMPLOYEE")
                .entityId(employee.getEmployeeId())
                .details("Admin disabled 2FA for employee: " + employeeCode).build());
    }

    public int forceEnable2FAForAll(String adminEmail) {
        Employee admin = getEmployeeByEmail(adminEmail);

        var employees = employeeRepository.findAll();
        int count = 0;
        for (Employee emp : employees) {
            if ((emp.getRole() == Role.EMPLOYEE || emp.getRole() == Role.MANAGER)
                    && !Boolean.TRUE.equals(emp.getTwoFactorEnabled())) {
                emp.setTwoFactorEnabled(true);
                employeeRepository.save(emp);
                count++;
            }
        }
        activityLogRepository.save(ActivityLog.builder()
                .performedBy(admin).action("FORCE_ENABLE_2FA_ALL").entityType("SYSTEM")
                .entityId(admin.getEmployeeId())
                .details("Admin force-enabled 2FA for " + count + " employees/managers").build());
        return count;
    }

    public EmployeeProfileResponse assignManager(String employeeCode, String managerCode, String adminEmail) {
        Employee employee = employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with code: " + employeeCode));
        Employee manager = employeeRepository.findByEmployeeCode(managerCode)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found with code: " + managerCode));
        if (employeeCode.equals(managerCode)) {
            throw new BadRequestException("Employee cannot be their own manager");
        }
        if (manager.getRole() != Role.MANAGER && manager.getRole() != Role.ADMIN) {
            throw new BadRequestException(managerCode + " is not a MANAGER or ADMIN. Only managers/admins can be assigned.");
        }
        String oldManager = employee.getManager() != null ? employee.getManager().getEmployeeCode() : "None";
        employee.setManager(manager);
        Employee saved = employeeRepository.save(employee);
        Employee admin = getEmployeeByEmail(adminEmail);
        activityLogRepository.save(ActivityLog.builder()
                .performedBy(admin).action("CHANGE_MANAGER").entityType("EMPLOYEE")
                .entityId(employee.getEmployeeId())
                .details("Manager: '" + oldManager + "' -> '" + managerCode + "' ").build());
        return mapToProfileResponse(saved);
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/service/ExpenseService.java`**
```java
package com.revworkforce.employeemanagementservice.service;

import com.revworkforce.employeemanagementservice.dto.ExpenseActionRequest;
import com.revworkforce.employeemanagementservice.dto.ExpenseRequest;
import com.revworkforce.employeemanagementservice.exception.BadRequestException;
import com.revworkforce.employeemanagementservice.exception.ResourceNotFoundException;
import com.revworkforce.employeemanagementservice.model.Employee;
import com.revworkforce.employeemanagementservice.model.Expense;
import com.revworkforce.employeemanagementservice.model.ExpenseItem;
import com.revworkforce.employeemanagementservice.model.enums.ExpenseCategory;
import com.revworkforce.employeemanagementservice.model.enums.ExpenseStatus;
import com.revworkforce.employeemanagementservice.model.enums.NotificationType;
import com.revworkforce.employeemanagementservice.model.enums.Role;
import com.revworkforce.employeemanagementservice.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class ExpenseService {
    @Autowired private ExpenseRepository expenseRepository;
    @Autowired private EmployeeService employeeService;
    @Autowired private NotificationService notificationService;
    @Value("${app.expense.receipt-dir:uploads/expense-receipts}")
    private String receiptDir;

    @Transactional
    public Expense createExpense(String email, ExpenseRequest request) {
        Employee employee = employeeService.getEmployeeByEmail(email);

        Expense expense = Expense.builder()
                .employee(employee)
                .title(request.getTitle())
                .description(request.getDescription())
                .category(parseCategory(request.getCategory()))
                .totalAmount(request.getTotalAmount())
                .currency(request.getCurrency() != null ? request.getCurrency() : "INR")
                .expenseDate(request.getExpenseDate())
                .vendorName(request.getVendorName())
                .invoiceNumber(request.getInvoiceNumber())
                .receiptFileName(request.getReceiptFileName())
                .status(ExpenseStatus.DRAFT)
                .build();

        if (request.getItems() != null) {
            for (ExpenseRequest.ExpenseItemRequest itemReq : request.getItems()) {
                ExpenseItem item = ExpenseItem.builder()
                        .description(itemReq.getDescription())
                        .amount(itemReq.getAmount())
                        .quantity(itemReq.getQuantity() != null ? itemReq.getQuantity() : 1)
                        .build();
                expense.addItem(item);
            }
        }

        Expense saved = expenseRepository.save(expense);
        saveReceiptFile(saved, request.getReceiptBase64(), request.getReceiptFileName());
        return expenseRepository.save(saved);
    }

    @Transactional
    public Expense submitExpense(String email, Integer expenseId) {
        Employee employee = employeeService.getEmployeeByEmail(email);
        Expense expense = getExpenseById(expenseId);

        validateOwnership(expense, employee);
        if (expense.getStatus() != ExpenseStatus.DRAFT) {
            throw new BadRequestException("Only draft expenses can be submitted.");
        }

        expense.setStatus(ExpenseStatus.SUBMITTED);
        expense.setSubmittedDate(LocalDateTime.now());
        Expense saved = expenseRepository.save(expense);

        if (employee.getManager() != null) {
            notificationService.sendNotification(
                    employee.getManager(), "Expense Submitted",
                    "New expense claim from " + employee.getFirstName() + " " + employee.getLastName()
                            + " — ₹" + expense.getTotalAmount(),
                    NotificationType.EXPENSE_SUBMITTED, saved.getExpenseId(), "EXPENSE");
        }

        return saved;
    }

    public Page<Expense> getMyExpenses(String email, Pageable pageable) {
        Employee employee = employeeService.getEmployeeByEmail(email);
        return expenseRepository.findByEmployeeEmployeeId(employee.getEmployeeId(), pageable);
    }

    public Page<Expense> getTeamExpenses(String email, Pageable pageable) {
        Employee manager = employeeService.getEmployeeByEmail(email);
        return expenseRepository.findTeamExpensesByStatus(
                manager.getEmployeeId(), ExpenseStatus.SUBMITTED, pageable);
    }

    @Transactional
    public Expense managerAction(String email, Integer expenseId, ExpenseActionRequest request) {
        Employee manager = employeeService.getEmployeeByEmail(email);
        Expense expense = getExpenseById(expenseId);

        if (expense.getStatus() != ExpenseStatus.SUBMITTED) {
            throw new BadRequestException("This expense is not pending manager approval.");
        }

        Employee expenseOwner = expense.getEmployee();
        if (expenseOwner.getManager() == null ||
                !expenseOwner.getManager().getEmployeeId().equals(manager.getEmployeeId())) {
            if (manager.getRole() != Role.ADMIN) {
                throw new BadRequestException("You are not authorized to action this expense.");
            }
        }

        if ("APPROVED".equalsIgnoreCase(request.getAction())) {
            expense.setStatus(ExpenseStatus.MANAGER_APPROVED);
            expense.setManagerComments(request.getComments());
            expense.setActionedBy(manager);
            expense.setManagerActionDate(LocalDateTime.now());

            notificationService.sendNotification(expenseOwner, "Expense Approved",
                    "Your expense '" + expense.getTitle() + "' was approved by manager. Pending finance review.",
                    NotificationType.EXPENSE_APPROVED, expenseId, "EXPENSE");
        } else if ("REJECTED".equalsIgnoreCase(request.getAction())) {
            expense.setStatus(ExpenseStatus.REJECTED);
            expense.setRejectionReason(request.getComments());
            expense.setActionedBy(manager);
            expense.setManagerActionDate(LocalDateTime.now());

            notificationService.sendNotification(expenseOwner, "Expense Rejected",
                    "Your expense '" + expense.getTitle() + "' was rejected by manager: " + request.getComments(),
                    NotificationType.EXPENSE_REJECTED, expenseId, "EXPENSE");
        } else {
            throw new BadRequestException("Invalid action. Use APPROVED or REJECTED.");
        }

        return expenseRepository.save(expense);
    }

    public Page<Expense> getFinancePendingExpenses(Pageable pageable) {
        return expenseRepository.findByStatus(ExpenseStatus.MANAGER_APPROVED, pageable);
    }

    public Page<Expense> getAllExpenses(ExpenseStatus status, Pageable pageable) {
        if (status != null) {
            return expenseRepository.findByStatus(status, pageable);
        }
        return expenseRepository.findAll(pageable);
    }

    @Transactional
    public Expense financeAction(String email, Integer expenseId, ExpenseActionRequest request) {
        Employee financeUser = employeeService.getEmployeeByEmail(email);
        Expense expense = getExpenseById(expenseId);

        if (expense.getStatus() != ExpenseStatus.MANAGER_APPROVED
                && expense.getStatus() != ExpenseStatus.FINANCE_APPROVED) {
            throw new BadRequestException("This expense is not pending finance action.");
        }

        Employee expenseOwner = expense.getEmployee();

        switch (request.getAction().toUpperCase()) {
            case "APPROVED" -> {
                expense.setStatus(ExpenseStatus.FINANCE_APPROVED);
                expense.setFinanceComments(request.getComments());
                expense.setFinanceActionedBy(financeUser);
                expense.setFinanceActionDate(LocalDateTime.now());
                notificationService.sendNotification(expenseOwner, "Expense Finance Approved",
                        "Your expense '" + expense.getTitle() + "' has been approved by finance.",
                        NotificationType.EXPENSE_APPROVED, expenseId, "EXPENSE");
            }
            case "REJECTED" -> {
                expense.setStatus(ExpenseStatus.REJECTED);
                expense.setRejectionReason(request.getComments());
                expense.setFinanceActionedBy(financeUser);
                expense.setFinanceActionDate(LocalDateTime.now());
                notificationService.sendNotification(expenseOwner, "Expense Rejected",
                        "Your expense '" + expense.getTitle() + "' was rejected by finance: " + request.getComments(),
                        NotificationType.EXPENSE_REJECTED, expenseId, "EXPENSE");
            }
            case "REIMBURSED" -> {
                expense.setStatus(ExpenseStatus.REIMBURSED);
                expense.setFinanceComments(request.getComments());
                expense.setFinanceActionedBy(financeUser);
                expense.setReimbursedDate(LocalDateTime.now());
                notificationService.sendNotification(expenseOwner, "Expense Reimbursed",
                        "Your expense '" + expense.getTitle() + "' of ₹" + expense.getTotalAmount() + " has been reimbursed!",
                        NotificationType.EXPENSE_REIMBURSED, expenseId, "EXPENSE");
            }
            default -> throw new BadRequestException("Invalid action. Use APPROVED, REJECTED, or REIMBURSED.");
        }

        return expenseRepository.save(expense);
    }

    public Expense getExpenseById(Integer id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public ReceiptFileData getExpenseReceipt(String email, Integer expenseId) {
        Employee requester = employeeService.getEmployeeByEmail(email);
        Expense expense = getExpenseById(expenseId);

        ensureReceiptAccess(requester, expense);

        if (expense.getReceiptUrl() == null || expense.getReceiptUrl().isBlank()) {
            throw new ResourceNotFoundException("No uploaded receipt found for this expense.");
        }

        try {
            Path receiptPath = Paths.get(expense.getReceiptUrl()).normalize();
            if (!Files.exists(receiptPath)) {
                throw new ResourceNotFoundException("Uploaded receipt file not found.");
            }

            Resource resource = new UrlResource(receiptPath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new ResourceNotFoundException("Uploaded receipt file is not accessible.");
            }

            String contentType = Files.probeContentType(receiptPath);
            if (contentType == null || contentType.isBlank()) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            String fileName = expense.getReceiptFileName() != null && !expense.getReceiptFileName().isBlank()
                    ? expense.getReceiptFileName()
                    : receiptPath.getFileName().toString();

            return new ReceiptFileData(resource, fileName, contentType);
        } catch (MalformedURLException e) {
            throw new ResourceNotFoundException("Uploaded receipt is unavailable.");
        } catch (IOException e) {
            throw new BadRequestException("Unable to read uploaded receipt file.");
        }
    }

    private void validateOwnership(Expense expense, Employee employee) {
        if (!expense.getEmployee().getEmployeeId().equals(employee.getEmployeeId())) {
            throw new BadRequestException("You can only modify your own expenses.");
        }
    }

    private ExpenseCategory parseCategory(String category) {
        if (category == null) return ExpenseCategory.OTHER;
        try {
            return ExpenseCategory.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ExpenseCategory.OTHER;
        }
    }

    private void saveReceiptFile(Expense expense, String receiptBase64, String originalFileName) {
        if (receiptBase64 == null || receiptBase64.isBlank()) {
            return;
        }

        try {
            String[] parts = receiptBase64.split(",", 2);
            String meta = parts.length > 1 ? parts[0] : "";
            String payload = parts.length > 1 ? parts[1] : parts[0];

            byte[] data = Base64.getDecoder().decode(payload);

            String extension = ".bin";
            if (meta.contains("image/png")) extension = ".png";
            else if (meta.contains("image/jpeg") || meta.contains("image/jpg")) extension = ".jpg";
            else if (meta.contains("application/pdf")) extension = ".pdf";
            else if (meta.contains("image/webp")) extension = ".webp";

            Path directory = Paths.get(receiptDir).toAbsolutePath().normalize();
            Files.createDirectories(directory);

            String fileName = "expense-" + expense.getExpenseId() + "-" + System.currentTimeMillis() + extension;
            Path target = directory.resolve(fileName).normalize();
            Files.write(target, data);

            expense.setReceiptUrl(target.toString());
            if (originalFileName != null && !originalFileName.isBlank()) {
                expense.setReceiptFileName(originalFileName);
            } else {
                expense.setReceiptFileName(fileName);
            }
        } catch (Exception e) {
            throw new BadRequestException("Unable to save uploaded receipt file.");
        }
    }

    private void ensureReceiptAccess(Employee requester, Expense expense) {
        Employee owner = expense.getEmployee();
        if (owner.getEmployeeId().equals(requester.getEmployeeId())) {
            return;
        }

        if (requester.getRole() == Role.ADMIN) {
            return;
        }

        if (requester.getRole() == Role.MANAGER
                && owner.getManager() != null
                && owner.getManager().getEmployeeId().equals(requester.getEmployeeId())) {
            return;
        }

        throw new BadRequestException("You are not authorized to view this receipt.");
    }

    public record ReceiptFileData(Resource resource, String fileName, String contentType) {}
}


```

#### **`src/main/java/com/revworkforce/employeemanagementservice/service/GeoAttendanceService.java`**
```java
package com.revworkforce.employeemanagementservice.service;

import com.revworkforce.employeemanagementservice.model.OfficeLocation;
import com.revworkforce.employeemanagementservice.repository.OfficeLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GeoAttendanceService {
    @Autowired
    private OfficeLocationRepository officeLocationRepository;

    private static final double EARTH_RADIUS_METERS = 6_371_000;

    @Transactional(readOnly = true)
    public GeoVerificationResult verifyLocation(double latitude, double longitude) {
        List<OfficeLocation> activeLocations = officeLocationRepository.findByIsActiveTrue();

        if (activeLocations.isEmpty()) {
            return new GeoVerificationResult(true, null, 0,
                    "Geo-fencing not configured — attendance allowed from any location");
        }

        OfficeLocation nearest = null;
        double nearestDistance = Double.MAX_VALUE;

        for (OfficeLocation loc : activeLocations) {
            double dist = haversineDistance(latitude, longitude,
                    loc.getLatitude(), loc.getLongitude());
            if (dist < nearestDistance) {
                nearestDistance = dist;
                nearest = loc;
            }
        }

        if (nearest != null && nearestDistance <= nearest.getRadiusMeters()) {
            return new GeoVerificationResult(
                    true,
                    nearest.getLocationId(),
                    nearestDistance,
                    "Within " + nearest.getLocationName()
                            + " (" + Math.round(nearestDistance) + "m away)"
            );
        }

        String msg = "You are " + Math.round(nearestDistance) + "m away from the nearest office ("
                + (nearest != null ? nearest.getLocationName() : "unknown")
                + "). Maximum allowed: "
                + (nearest != null ? nearest.getRadiusMeters() : 0) + "m";

        return new GeoVerificationResult(
                false,
                nearest != null ? nearest.getLocationId() : null,
                nearestDistance,
                msg
        );
    }

    public double haversineDistance(double lat1, double lon1,
                                   double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_METERS * c;
    }

    public record GeoVerificationResult(
            boolean withinFence,
            Integer officeLocationId,
            double distanceMeters,
            String message
    ) {}
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/service/InvoiceParserService.java`**
```java
package com.revworkforce.employeemanagementservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import com.revworkforce.employeemanagementservice.dto.InvoiceParseResponse;
import com.revworkforce.employeemanagementservice.integration.OllamaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class InvoiceParserService {
    private static final Logger log = LoggerFactory.getLogger(InvoiceParserService.class);

    @Autowired
    private OllamaClient ollamaClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Set<String> VALID_CATEGORIES = Set.of(
            "TRAVEL", "MEALS", "ACCOMMODATION", "OFFICE_SUPPLIES", "EQUIPMENT",
            "SOFTWARE", "TRAINING", "CLIENT_ENTERTAINMENT", "COMMUNICATION",
            "MEDICAL", "TRANSPORTATION", "OTHER"
    );

    public InvoiceParseResponse parseUploadedFile(String base64Data, String fileType) {
        if (base64Data == null || base64Data.isBlank()) {
            return InvoiceParseResponse.builder()
                    .success(false).errorMessage("No file provided").build();
        }

        String cleanBase64 = base64Data.contains(",")
                ? base64Data.substring(base64Data.indexOf(",") + 1)
                : base64Data;

        boolean isPdf = fileType != null && fileType.toLowerCase().contains("pdf");

        if (isPdf) {
            return parsePdf(cleanBase64);
        } else {
            return parseImage(cleanBase64);
        }
    }

    private InvoiceParseResponse parsePdf(String base64Pdf) {
        try {
            byte[] pdfBytes = Base64.getDecoder().decode(base64Pdf);
            String extractedText;

            try (PDDocument doc = Loader.loadPDF(pdfBytes)) {
                PDFTextStripper stripper = new PDFTextStripper();
                extractedText = stripper.getText(doc);
            }

            if (extractedText == null || extractedText.isBlank()) {
                return InvoiceParseResponse.builder()
                        .success(false).errorMessage("Could not extract text from PDF. The PDF might be image-based.").build();
            }

            log.info("PDF text extracted ({} chars). Running instant regex parser first...", extractedText.length());
            log.debug("Extracted PDF text (first 1500 chars):\n{}", extractedText.substring(0, Math.min(extractedText.length(), 1500)));

            InvoiceParseResponse regexResult = regexParse(extractedText);
            if (regexResult.isSuccess()) {
                boolean hasUsefulData = regexResult.getTotalAmount() != null
                        || regexResult.getVendorName() != null
                        || regexResult.getInvoiceNumber() != null;
                if (hasUsefulData) {
                    log.info("✅ Regex parser extracted (instant): vendor={}, amount={}, date={}, invoice={}, items={}",
                            regexResult.getVendorName(), regexResult.getTotalAmount(),
                            regexResult.getInvoiceDate(), regexResult.getInvoiceNumber(),
                            regexResult.getItems() != null ? regexResult.getItems().size() : 0);
                    return regexResult;
                }
            }

            if (ollamaClient.isAvailable()) {
                log.info("Regex incomplete — trying AI-powered extraction (truncated text)...");
                try {
                    String truncatedText = truncateForAi(extractedText, 1500);
                    InvoiceParseResponse aiResult = parseInvoice(truncatedText);
                    if (aiResult.isSuccess()) {
                        log.info("AI extraction succeeded: vendor={}, amount={}", aiResult.getVendorName(), aiResult.getTotalAmount());
                        return aiResult;
                    }
                } catch (Exception e) {
                    log.warn("AI extraction failed: {}", e.getMessage());
                }
            }

            if (regexResult.isSuccess()) {
                return regexResult;
            }

            log.warn("Both regex and AI could not extract enough data from PDF.");
            return InvoiceParseResponse.builder()
                    .success(false)
                    .errorMessage("Could not extract data from this PDF. Please fill the form manually.")
                    .rawText(extractedText)
                    .build();
        } catch (IllegalArgumentException e) {
            log.error("Invalid base64 data: {}", e.getMessage());
            return InvoiceParseResponse.builder()
                    .success(false).errorMessage("Invalid file data. Please re-upload the PDF.").build();
        } catch (Exception e) {
            log.error("PDF processing failed: {}", e.getMessage(), e);
            return InvoiceParseResponse.builder()
                    .success(false).errorMessage("PDF processing failed: " + e.getMessage()).build();
        }
    }

    private String truncateForAi(String text, int maxChars) {
        if (text.length() <= maxChars) return text;

        int headerLen = (int) (maxChars * 0.6);
        int footerLen = maxChars - headerLen;
        String header = text.substring(0, headerLen);
        String footer = text.substring(text.length() - footerLen);
        return header + "\n...[truncated]...\n" + footer;
    }

    private InvoiceParseResponse parseImage(String base64Image) {
        if (!ollamaClient.isAvailable()) {
            log.warn("Ollama is not reachable — cannot process image invoice");
            return InvoiceParseResponse.builder()
                    .success(false)
                    .errorMessage("AI vision service is not available. Please upload a PDF instead for instant extraction, or start the Ollama service (ollama serve) and pull the vision model (ollama pull llava).")
                    .build();
        }

        try {
            String prompt = "Read this invoice/receipt. Reply with JSON only, no explanation.\n" +
                    "{\"title\":\"short title\",\"vendorName\":\"str\",\"invoiceNumber\":\"str\",\"invoiceDate\":\"YYYY-MM-DD\"," +
                    "\"totalAmount\":number,\"category\":\"TRAVEL|MEALS|ACCOMMODATION|OFFICE_SUPPLIES|EQUIPMENT|SOFTWARE|TRAINING|MEDICAL|TRANSPORTATION|OTHER\"," +
                    "\"description\":\"brief\",\"items\":[{\"description\":\"str\",\"amount\":number,\"quantity\":number}]}\nJSON:";

            String aiResponse = ollamaClient.generateWithImage(prompt, base64Image);

            if (aiResponse != null && !aiResponse.startsWith("Error")) {
                return parseAiResponse(aiResponse, "image-upload");
            }

            return InvoiceParseResponse.builder()
                    .success(false)
                    .errorMessage("Vision model could not read this image. Try uploading a PDF instead, or ensure the llava model is installed (ollama pull llava).")
                    .build();
        } catch (Exception e) {
            log.error("Image analysis failed: {}", e.getMessage());
            return InvoiceParseResponse.builder()
                    .success(false).errorMessage("Image analysis failed: " + e.getMessage()).build();
        }
    }

    public InvoiceParseResponse parseInvoice(String invoiceText) {
        if (invoiceText == null || invoiceText.isBlank()) {
            return InvoiceParseResponse.builder()
                    .success(false)
                    .errorMessage("No invoice text provided")
                    .build();
        }

        try {
            String prompt = buildExtractionPrompt(invoiceText);
            String aiResponse = ollamaClient.generate(prompt, 300);

            if (aiResponse == null || aiResponse.startsWith("Error")) {
                log.error("AI processing failed: {}", aiResponse);
                return InvoiceParseResponse.builder()
                        .success(false)
                        .errorMessage("AI processing failed: " + aiResponse)
                        .rawText(invoiceText)
                        .build();
            }

            return parseAiResponse(aiResponse, invoiceText);
        } catch (Exception e) {
            log.error("Failed to parse invoice: {}", e.getMessage());
            return InvoiceParseResponse.builder()
                    .success(false)
                    .errorMessage("Failed to parse invoice: " + e.getMessage())
                    .rawText(invoiceText)
                    .build();
        }
    }

    private String buildExtractionPrompt(String invoiceText) {
        return "Extract invoice data as JSON only. No explanation.\n" +
               "{\"title\":\"short title\",\"vendorName\":\"str\",\"invoiceNumber\":\"str\",\"invoiceDate\":\"YYYY-MM-DD\"," +
               "\"totalAmount\":number,\"category\":\"TRAVEL|MEALS|ACCOMMODATION|OFFICE_SUPPLIES|EQUIPMENT|SOFTWARE|TRAINING|MEDICAL|TRANSPORTATION|OTHER\"," +
               "\"description\":\"brief\",\"items\":[{\"description\":\"str\",\"amount\":number,\"quantity\":number}]}\n\n" +
               "Invoice:\n" + invoiceText + "\n\nJSON:";
    }

    private InvoiceParseResponse parseAiResponse(String aiResponse, String rawText) {
        try {
            String json = extractJson(aiResponse);
            log.info("Extracted JSON from AI response: {}", json.substring(0, Math.min(json.length(), 500)));
            JsonNode root = objectMapper.readTree(json);

            List<InvoiceParseResponse.ParsedItem> items = new ArrayList<>();
            if (root.has("items") && root.get("items").isArray()) {
                for (JsonNode itemNode : root.get("items")) {
                    items.add(InvoiceParseResponse.ParsedItem.builder()
                            .description(getTextOrNull(itemNode, "description"))
                            .amount(getDecimalOrNull(itemNode, "amount"))
                            .quantity(itemNode.has("quantity") ? itemNode.get("quantity").asInt(1) : 1)
                            .build());
                }
            }

            String category = getTextOrNull(root, "category");
            if (category != null) {
                category = category.toUpperCase().replace(" ", "_");
                if (!VALID_CATEGORIES.contains(category)) {
                    category = guessCategory(rawText);
                }
            }

            return InvoiceParseResponse.builder()
                    .success(true)
                    .title(getTextOrNull(root, "title"))
                    .vendorName(getTextOrNull(root, "vendorName"))
                    .invoiceNumber(getTextOrNull(root, "invoiceNumber"))
                    .invoiceDate(getTextOrNull(root, "invoiceDate"))
                    .totalAmount(getDecimalOrNull(root, "totalAmount"))
                    .currency(root.has("currency") ? root.get("currency").asText("INR") : "INR")
                    .category(category)
                    .description(getTextOrNull(root, "description"))
                    .items(items)
                    .rawText(rawText)
                    .build();
        } catch (Exception e) {
            log.error("Could not parse AI response as JSON: {}", e.getMessage());
            return InvoiceParseResponse.builder()
                    .success(false)
                    .errorMessage("Could not parse AI response as structured data. Please fill fields manually.")
                    .rawText(rawText)
                    .build();
        }
    }

    private InvoiceParseResponse regexParse(String text) {
        log.info("Running regex-based extraction on text ({} chars)", text.length());

        String vendorName = extractVendorName(text);
        String invoiceNumber = extractInvoiceNumber(text);
        String invoiceDate = extractDate(text);
        BigDecimal totalAmount = extractTotalAmount(text);
        String category = guessCategory(text);
        List<InvoiceParseResponse.ParsedItem> items = extractLineItems(text);

        if (invoiceNumber == null) {
            Matcher orderMatcher = Pattern.compile("(?i)(?:ORDER[_\\s-]*(?:INVOICE)?[_\\s-]*)([A-Z0-9]{5,30})")
                    .matcher(text);
            if (orderMatcher.find()) {
                invoiceNumber = orderMatcher.group(1);
            }
        }

        if (totalAmount == null && !items.isEmpty()) {
            totalAmount = items.stream()
                    .map(i -> i.getAmount() != null ? i.getAmount().multiply(BigDecimal.valueOf(i.getQuantity() != null ? i.getQuantity() : 1)) : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        String title = generateTitle(vendorName, category, totalAmount);

        String description = generateDescription(text, vendorName);

        int fieldsFound = 0;
        if (vendorName != null) fieldsFound++;
        if (invoiceNumber != null) fieldsFound++;
        if (invoiceDate != null) fieldsFound++;
        if (totalAmount != null) fieldsFound++;
        if (!items.isEmpty()) fieldsFound++;

        log.info("Regex extracted {} fields: vendor={}, invoice={}, date={}, amount={}, items={}",
                fieldsFound, vendorName, invoiceNumber, invoiceDate, totalAmount, items.size());

        if (fieldsFound == 0) {
            return InvoiceParseResponse.builder()
                    .success(false)
                    .errorMessage("Could not extract data from this PDF. Please fill the fields manually.")
                    .rawText(text)
                    .build();
        }

        return InvoiceParseResponse.builder()
                .success(true)
                .title(title)
                .vendorName(vendorName)
                .invoiceNumber(invoiceNumber)
                .invoiceDate(invoiceDate)
                .totalAmount(totalAmount)
                .currency("INR")
                .category(category)
                .description(description)
                .items(items)
                .rawText(text)
                .build();
    }

    private String generateDescription(String text, String vendorName) {
        String[] lines = text.split("\\r?\\n");
        List<String> meaningful = new ArrayList<>();
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.length() > 5 && trimmed.length() < 100 && meaningful.size() < 3
                    && !trimmed.matches("^[\\d\\s./-]+$")
                    && (vendorName == null || !trimmed.equals(vendorName))) {
                meaningful.add(trimmed);
            }
        }
        return meaningful.isEmpty() ? null : String.join("; ", meaningful);
    }

    private String extractVendorName(String text) {
        String lower = text.toLowerCase();
        if (lower.contains("rapido")) return "Rapido";
        if (lower.contains("amazon")) return "Amazon";
        if (lower.contains("flipkart")) return "Flipkart";
        if (lower.contains("myntra")) return "Myntra";
        if (lower.contains("swiggy")) return "Swiggy";
        if (lower.contains("zomato")) return "Zomato";
        if (lower.contains("uber eats")) return "Uber Eats";
        if (lower.contains("uber")) return "Uber";
        if (lower.contains("makemytrip") || lower.contains("make my trip")) return "MakeMyTrip";
        if (lower.contains("bigbasket") || lower.contains("big basket")) return "BigBasket";
        if (lower.contains("dunzo")) return "Dunzo";
        if (lower.contains("zepto")) return "Zepto";
        if (lower.contains("blinkit") || lower.contains("grofers")) return "Blinkit";
        if (lower.contains("ola cabs") || (lower.contains("ola") && containsAny(lower, "ride", "cab", "trip", "fare"))) return "Ola";
        if (lower.contains("phonepe")) return "PhonePe";
        if (lower.contains("paytm")) return "Paytm";
        if (lower.contains("meesho")) return "Meesho";
        if (lower.contains("nykaa")) return "Nykaa";
        if (lower.contains("jiomart")) return "JioMart";
        if (lower.contains("reliance")) return "Reliance";
        if (lower.contains("irctc")) return "IRCTC";
        if (lower.contains("redbus")) return "RedBus";
        if (lower.contains("cleartrip")) return "Cleartrip";
        if (lower.contains("goibibo")) return "Goibibo";
        if (lower.contains("dominos") || lower.contains("domino's")) return "Dominos";
        if (lower.contains("mcdonald")) return "McDonald's";
        if (lower.contains("starbucks")) return "Starbucks";

        String[] vendorPatterns = {
                "(?i)(?:sold\\s*by|seller\\s*(?:name)?|vendor\\s*(?:name)?|bill\\s*from|billed?\\s*by|merchant)\\s*[:\\-]?\\s*([^\\r\\n]+)",
                "(?i)(?:from|company|store|shop|restaurant|retailer)\\s*[:\\-]\\s*([^\\r\\n]+)"
        };

        for (String pattern : vendorPatterns) {
            Matcher m = Pattern.compile(pattern).matcher(text);
            if (m.find()) {
                String name = m.group(1).trim();
                name = name.split(",")[0].trim();
                name = name.replaceAll("\\s*\\(.*\\)\\s*$", "").trim();
                name = name.replaceAll("(?i)\\s*GST.*$", "").trim();

                if (!name.isEmpty() && name.length() >= 2 && name.length() < 60
                        && !name.toLowerCase().contains("not by")
                        && !name.toLowerCase().contains("private limited")
                        && !name.toLowerCase().contains("services")) {
                    return name;
                }
            }
        }

        String[] lines = text.split("\\r?\\n");
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.length() > 3 && trimmed.length() < 80
                    && !trimmed.matches("(?i).*(?:invoice|receipt|bill\\s*no|tax|date|order\\s*id|order\\s*no|page|gst|total|amount|qty|hsn|payment|summary|\\d{2}/\\d{2}/\\d{4}).*")
                    && !trimmed.matches("^[\\d\\s.,/-]+$")
                    && !trimmed.matches("(?i)^(tax\\s+invoice|original|duplicate|copy|ride\\s*id|time|distance|duration).*")) {
                return trimmed;
            }
        }

        return null;
    }

    private String extractInvoiceNumber(String text) {
        Set<String> rejected = Set.of("details", "date", "summary", "amount", "total",
                "charge", "charges", "fee", "fees", "payment", "address", "name", "type",
                "number", "invoice", "bill", "receipt", "tax", "order", "ride", "booking",
                "category", "supply", "state", "using");

        Matcher m1 = Pattern.compile("(?i)invoice\\s+no\\.?\\s*[:\\-]?\\s*([0-9][A-Za-z0-9]{4,49})").matcher(text);
        while (m1.find()) {
            String num = m1.group(1).trim();
            if (isValidInvoiceNumber(num, rejected)) return num;
        }

        Matcher m2 = Pattern.compile("(?i)(?:ride|order|booking|txn)\\s*id\\s*[:\\-]?\\s*([A-Za-z0-9\\-/.]{5,50})").matcher(text);
        while (m2.find()) {
            String num = m2.group(1).trim();
            if (isValidInvoiceNumber(num, rejected)) return num;
        }

        Matcher m3 = Pattern.compile("(?i)(?:bill|receipt|ref|reference|txn)\\s*(?:no|number|num|#|id)\\.?\\s*[:\\-]?\\s*([A-Za-z0-9\\-/]{3,50})").matcher(text);
        while (m3.find()) {
            String num = m3.group(1).trim();
            if (isValidInvoiceNumber(num, rejected)) return num;
        }

        Matcher m4 = Pattern.compile("(?m)^\\s*([0-9]{2,}[A-Z]+[A-Z0-9]{3,})\\s*$").matcher(text);
        while (m4.find()) {
            String num = m4.group(1).trim();
            if (num.length() >= 8 && num.length() <= 50) return num;
        }

        return null;
    }

    private boolean isValidInvoiceNumber(String num, Set<String> rejected) {
        return num.length() >= 3 && num.length() <= 50
                && num.matches(".*\\d.*")
                && !rejected.contains(num.toLowerCase())
                && !num.matches("(?i)^(details|date|summary|amount|total|charge|invoice|bill|tax|Feb|Mar|Jan).*");
    }

    private String extractDate(String text) {
        String dateContext = text;
        Matcher dateLine = Pattern.compile("(?i)(?:date|dated|invoice\\s*date|bill\\s*date|order\\s*date|time\\s*of\\s*ride)[:\\s]*(.{0,80})").matcher(text);
        if (dateLine.find()) {
            dateContext = dateLine.group(1);
        }

        String result = tryExtractDate(dateContext);
        if (result != null) return result;

        if (!dateContext.equals(text)) {
            result = tryExtractDate(text);
        }
        return result;
    }

    private String tryExtractDate(String text) {
        Matcher mOrd1 = Pattern.compile(
                "(?i)(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)[a-z]*\\s+(\\d{1,2})(?:st|nd|rd|th)?[,]?\\s+(\\d{4})"
        ).matcher(text);
        if (mOrd1.find()) {
            try {
                int day = Integer.parseInt(mOrd1.group(2));
                int month = monthToNumber(mOrd1.group(1));
                int year = Integer.parseInt(mOrd1.group(3));
                if (day >= 1 && day <= 31 && month >= 1 && month <= 12 && year >= 2000) {
                    return String.format("%d-%02d-%02d", year, month, day);
                }
            } catch (Exception ignored) {}
        }

        Matcher mOrd2 = Pattern.compile(
                "(?i)(\\d{1,2})(?:st|nd|rd|th)?\\s+(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)[a-z]*[,]?\\s+(\\d{4})"
        ).matcher(text);
        if (mOrd2.find()) {
            try {
                int day = Integer.parseInt(mOrd2.group(1));
                int month = monthToNumber(mOrd2.group(2));
                int year = Integer.parseInt(mOrd2.group(3));
                if (day >= 1 && day <= 31 && month >= 1 && month <= 12 && year >= 2000) {
                    return String.format("%d-%02d-%02d", year, month, day);
                }
            } catch (Exception ignored) {}
        }

        Matcher m1 = Pattern.compile("(\\d{1,2})[/\\-](\\d{1,2})[/\\-](\\d{4})").matcher(text);
        if (m1.find()) {
            try {
                int a = Integer.parseInt(m1.group(1));
                int b = Integer.parseInt(m1.group(2));
                int year = Integer.parseInt(m1.group(3));
                if (year >= 2000) {
                    if (a > 12) return String.format("%d-%02d-%02d", year, b, a);
                    else if (b > 12) return String.format("%d-%02d-%02d", year, a, b);
                    else return String.format("%d-%02d-%02d", year, b, a);
                }
            } catch (Exception ignored) {}
        }

        Matcher m2 = Pattern.compile("(\\d{4})[/\\-](\\d{1,2})[/\\-](\\d{1,2})").matcher(text);
        if (m2.find()) {
            int year = Integer.parseInt(m2.group(1));
            if (year >= 2000) {
                return String.format("%d-%02d-%02d", year, Integer.parseInt(m2.group(2)), Integer.parseInt(m2.group(3)));
            }
        }

        return null;
    }

    private int monthToNumber(String monthStr) {
        return switch (monthStr.substring(0, 3).toLowerCase()) {
            case "jan" -> 1; case "feb" -> 2; case "mar" -> 3; case "apr" -> 4;
            case "may" -> 5; case "jun" -> 6; case "jul" -> 7; case "aug" -> 8;
            case "sep" -> 9; case "oct" -> 10; case "nov" -> 11; case "dec" -> 12;
            default -> -1;
        };
    }

    private BigDecimal extractTotalAmount(String text) {
        String[] tier1Patterns = {
                "(?i)(?:grand\\s*total|total\\s*amount|total\\s*payable|order\\s*total|invoice\\s*total|bill\\s*total|final\\s*amount|you\\s*pay)[^\\d₹]{0,20}(?:₹|Rs\\.?|INR)\\s*([\\d,]+\\.\\d{2})",
                "(?i)(?:grand\\s*total|total\\s*amount|total\\s*payable|order\\s*total|invoice\\s*total|bill\\s*total|final\\s*amount|you\\s*pay)[^\\d₹]{0,20}([\\d,]+\\.\\d{2})",
        };

        String[] tier2Patterns = {
                "(?i)(?:^|\\n)\\s*total[^\\d₹]{0,15}(?:₹|Rs\\.?|INR)\\s*([\\d,]+\\.\\d{2})",
                "(?i)(?:^|\\n)\\s*total[^\\d₹]{0,15}([\\d,]+\\.\\d{2})",
        };

        String tier3Pattern = "(?:₹|Rs\\.?)\\s*([\\d,]+\\.\\d{2})";

        BigDecimal best = findBestAmount(text, tier1Patterns);
        if (best != null) return best;

        best = findBestAmount(text, tier2Patterns);
        if (best != null) return best;

        BigDecimal maxAmount = null;
        Matcher m3 = Pattern.compile(tier3Pattern).matcher(text);
        while (m3.find()) {
            try {
                BigDecimal amt = parseAmount(m3.group(1));
                if (amt != null && isReasonableAmount(amt)) {
                    int endPos = m3.end();
                    if (endPos < text.length() && text.charAt(endPos) == '/') continue;
                    if (maxAmount == null || amt.compareTo(maxAmount) > 0) {
                        maxAmount = amt;
                    }
                }
            } catch (Exception ignored) {}
        }

        return maxAmount;
    }

    private BigDecimal findBestAmount(String text, String[] patterns) {
        BigDecimal best = null;
        for (String pattern : patterns) {
            Matcher m = Pattern.compile(pattern).matcher(text);
            while (m.find()) {
                try {
                    BigDecimal amt = parseAmount(m.group(1));
                    if (amt != null && isReasonableAmount(amt)) {
                        int endPos = m.end();
                        if (endPos < text.length() && text.charAt(endPos) == '/') continue;
                        if (best == null || amt.compareTo(best) > 0) {
                            best = amt;
                        }
                    }
                } catch (Exception ignored) {}
            }
            if (best != null) return best;
        }
        return best;
    }

    private BigDecimal parseAmount(String amtStr) {
        if (amtStr == null || amtStr.isBlank()) return null;
        String cleaned = amtStr.replace(",", "").trim();
        if (cleaned.isEmpty()) return null;
        try {
            return new BigDecimal(cleaned);
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isReasonableAmount(BigDecimal amt) {
        return amt.compareTo(BigDecimal.ZERO) > 0 && amt.compareTo(new BigDecimal("1000000")) < 0;
    }

    private String guessCategory(String text) {
        String lower = text.toLowerCase();

        if (containsAny(lower, "flight", "airline", "airport", "travel", "ticket", "railway", "train", "irctc"))
            return "TRAVEL";
        if (containsAny(lower, "restaurant", "food", "meal", "lunch", "dinner", "breakfast", "cafe", "catering", "zomato", "swiggy"))
            return "MEALS";
        if (containsAny(lower, "hotel", "accommodation", "stay", "room", "lodge", "resort", "oyo", "airbnb"))
            return "ACCOMMODATION";
        if (containsAny(lower, "stationery", "office supply", "office supplies", "pen", "paper", "printer"))
            return "OFFICE_SUPPLIES";
        if (containsAny(lower, "laptop", "computer", "monitor", "keyboard", "mouse", "equipment", "hardware"))
            return "EQUIPMENT";
        if (containsAny(lower, "software", "license", "subscription", "saas", "aws", "azure", "cloud"))
            return "SOFTWARE";
        if (containsAny(lower, "training", "course", "seminar", "workshop", "conference", "certification"))
            return "TRAINING";
        if (containsAny(lower, "hospital", "medical", "pharmacy", "medicine", "doctor", "clinic", "health"))
            return "MEDICAL";
        if (containsAny(lower, "uber", "ola", "taxi", "cab", "fuel", "petrol", "diesel", "parking", "toll", "metro", "bus", "rapido", "ride charge", "ride id", "captain"))
            return "TRANSPORTATION";
        if (containsAny(lower, "phone", "mobile", "internet", "broadband", "telecom", "airtel", "jio"))
            return "COMMUNICATION";
        if (containsAny(lower, "amazon", "flipkart", "myntra", "online", "shopping", "ecommerce"))
            return "OTHER";

        return "OTHER";
    }

    private boolean containsAny(String text, String... keywords) {
        for (String kw : keywords) {
            if (text.contains(kw)) return true;
        }
        return false;
    }

    private List<InvoiceParseResponse.ParsedItem> extractLineItems(String text) {
        List<InvoiceParseResponse.ParsedItem> items = new ArrayList<>();
        Set<String> seenDescs = new HashSet<>();

        Pattern exclusionPattern = Pattern.compile(
                "(?i)(?:^total$|total\\s*amount|total\\s*payable|grand\\s*total|final\\s*amount|" +
                "sub\\s*total|subtotal|sub-total|^total\\s*₹|" +
                "gst|cgst|sgst|igst|cess|" +
                "discount|coupon|promo|saving|cashback|round\\s*off|" +
                "balance|paid|you\\s*pay|net\\s*amount|inclusive|" +
                "tax\\s*category|place\\s*of\\s*supply|gst\\s*number|vehicle\\s*number|captain\\s*name|customer\\s*name|" +
                "address|india|tamil\\s*nadu|karnataka|maharashtra|andhra|telangana|" +
                "pin\\s*code|state\\s|invoice\\s*date|invoice\\s*no|ride\\s*id|time\\s*of|" +
                "qr\\s*pay|upi|wallet|passengers\\s*n\\.?e\\.?c|n\\.?e\\.?c\\.?|" +
                "duration|distance|kms|mins|minutes|kilometers)");

        Pattern currencyDescPattern = Pattern.compile("^(?:₹|Rs\\.?|INR)\\s*\\d");

        Pattern numericDescPattern = Pattern.compile("^[\\d\\s.,₹/]+$");

        String[] itemPatterns = {
                "(?m)^\\s*(?:\\d+[.)]?\\s+)?(.{4,80})\\s+(?:₹|Rs\\.?|INR)\\s*([\\d,]+\\.\\d{2})\\s*$",

                "(?m)^\\s*(?:\\d+[.)]?\\s+)?(.{5,70})\\s{2,}([\\d,]+\\.\\d{2})\\s*$",

                "(?m)^\\s*\\d+[.)]?\\s+(.{3,80})\\s+(\\d+)\\s+(?:₹|Rs\\.?|INR)?\\s*([\\d,]+\\.\\d{2})\\s*$",

                "(?m)^\\s*(?:\\d+[.)]?\\s+)?(.{3,60})\\s+(\\d+)\\s+[\\d,.]+\\s+(?:₹|Rs\\.?|INR)?\\s*([\\d,]+\\.\\d{0,2})\\s*$"
        };

        for (String pattern : itemPatterns) {
            Matcher m = Pattern.compile(pattern).matcher(text);
            while (m.find() && items.size() < 20) {
                try {
                    String desc = m.group(1).trim();

                    if (exclusionPattern.matcher(desc).find()) continue;
                    if (currencyDescPattern.matcher(desc).find()) continue;
                    if (numericDescPattern.matcher(desc).matches()) continue;
                    if (desc.length() < 3 || seenDescs.contains(desc.toLowerCase())) continue;

                    if (desc.matches("(?i).*(?:description|item\\s*name|particulars|s\\.?\\s*no|qty|quantity|^rate$|^price$|^amount$|hsn|sac|bill\\s*details|payment\\s*summary).*"))
                        continue;

                    if (desc.matches("^\\d{4,}.*")) continue;

                    int qty = 1;
                    String amtStr;
                    if (m.groupCount() >= 3 && m.group(3) != null) {
                        try { qty = Integer.parseInt(m.group(2)); } catch (Exception ignored) {}
                        amtStr = m.group(3).replace(",", "");
                    } else {
                        amtStr = m.group(2).replace(",", "");
                    }

                    BigDecimal amount = new BigDecimal(amtStr);
                    if (amount.compareTo(BigDecimal.ZERO) > 0 && amount.compareTo(new BigDecimal("999999")) < 0) {
                        seenDescs.add(desc.toLowerCase());
                        items.add(InvoiceParseResponse.ParsedItem.builder()
                                .description(desc)
                                .amount(amount)
                                .quantity(qty)
                                .build());
                    }
                } catch (Exception ignored) {}
            }
            if (!items.isEmpty()) break;
        }

        return items;
    }

    private String generateTitle(String vendorName, String category, BigDecimal amount) {
        List<String> parts = new ArrayList<>();
        if (vendorName != null && !vendorName.isEmpty()) {
            parts.add(vendorName);
        }
        if (category != null && !category.equals("OTHER")) {
            parts.add(category.replace("_", " ").substring(0, 1).toUpperCase()
                    + category.replace("_", " ").substring(1).toLowerCase());
        }
        if (!parts.isEmpty()) {
            return String.join(" — ", parts);
        }
        if (amount != null) {
            return "Expense ₹" + amount.toPlainString();
        }
        return "Expense";
    }

    private String extractJson(String text) {
        int start = text.indexOf('{');
        int end = text.lastIndexOf('}');
        if (start >= 0 && end > start) {
            return text.substring(start, end + 1);
        }
        return text;
    }

    private String getTextOrNull(JsonNode node, String field) {
        if (node.has(field) && !node.get(field).isNull()) {
            String val = node.get(field).asText();
            return "null".equalsIgnoreCase(val) || val.isBlank() ? null : val;
        }
        return null;
    }

    private BigDecimal getDecimalOrNull(JsonNode node, String field) {
        if (node.has(field) && !node.get(field).isNull()) {
            if (node.get(field).isNumber()) {
                return node.get(field).decimalValue();
            }

            try {
                String val = node.get(field).asText().replaceAll("[^\\d.]", "");
                if (!val.isEmpty()) {
                    return new BigDecimal(val);
                }
            } catch (Exception ignored) {}
        }
        return null;
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/service/IpAccessControlService.java`**
```java
package com.revworkforce.employeemanagementservice.service;

import com.revworkforce.employeemanagementservice.dto.IpRangeRequest;
import com.revworkforce.employeemanagementservice.exception.BadRequestException;
import com.revworkforce.employeemanagementservice.exception.DuplicateResourceException;
import com.revworkforce.employeemanagementservice.exception.ResourceNotFoundException;
import com.revworkforce.employeemanagementservice.model.AllowedIpRange;
import com.revworkforce.employeemanagementservice.repository.AllowedIpRangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.revworkforce.employeemanagementservice.util.NetworkIpUtil;

import java.net.InetAddress;
import java.util.List;

@Service
public class IpAccessControlService {
    @Autowired
    private AllowedIpRangeRepository ipRangeRepository;

    @Transactional(readOnly = true)
    public List<AllowedIpRange> getAllIpRanges() {
        return ipRangeRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public List<AllowedIpRange> getActiveIpRanges() {
        return ipRangeRepository.findByIsActiveTrue();
    }

    @Transactional
    public AllowedIpRange addIpRange(IpRangeRequest request) {
        validateIpOrCidr(request.getIpRange());

        if (ipRangeRepository.existsByIpRange(request.getIpRange().trim())) {
            throw new DuplicateResourceException("IP range '" + request.getIpRange() + "' is already whitelisted");
        }

        AllowedIpRange ipRange = AllowedIpRange.builder()
                .ipRange(request.getIpRange().trim())
                .description(request.getDescription().trim())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();

        return ipRangeRepository.save(ipRange);
    }

    @Transactional
    public AllowedIpRange updateIpRange(Integer id, IpRangeRequest request) {
        AllowedIpRange existing = ipRangeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("IP range not found with id: " + id));

        if (request.getIpRange() != null && !request.getIpRange().isBlank()) {
            validateIpOrCidr(request.getIpRange());
            existing.setIpRange(request.getIpRange().trim());
        }
        if (request.getDescription() != null && !request.getDescription().isBlank()) {
            existing.setDescription(request.getDescription().trim());
        }
        if (request.getIsActive() != null) {
            existing.setIsActive(request.getIsActive());
        }

        return ipRangeRepository.save(existing);
    }

    @Transactional
    public AllowedIpRange toggleIpRange(Integer id) {
        AllowedIpRange existing = ipRangeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("IP range not found with id: " + id));
        existing.setIsActive(!existing.getIsActive());
        return ipRangeRepository.save(existing);
    }

    @Transactional
    public void deleteIpRange(Integer id) {
        if (!ipRangeRepository.existsById(id)) {
            throw new ResourceNotFoundException("IP range not found with id: " + id);
        }
        ipRangeRepository.deleteById(id);
    }

    public boolean isIpAllowed(String clientIp) {
        List<AllowedIpRange> activeRanges = ipRangeRepository.findByIsActiveTrue();

        if (activeRanges.isEmpty()) {
            return true;
        }

        String normalizedIp = normalizeIp(clientIp);

        for (AllowedIpRange range : activeRanges) {
            if (isIpInRange(normalizedIp, range.getIpRange().trim())) {
                return true;
            }
        }
        return false;
    }

    private boolean isIpInRange(String clientIp, String ipRange) {
        try {
            if (ipRange.contains("/")) {
                String[] parts = ipRange.split("/");
                String networkAddress = parts[0];
                int prefixLength = Integer.parseInt(parts[1]);

                InetAddress clientAddr = InetAddress.getByName(clientIp);
                InetAddress networkAddr = InetAddress.getByName(networkAddress);

                byte[] clientBytes = clientAddr.getAddress();
                byte[] networkBytes = networkAddr.getAddress();

                if (clientBytes.length != networkBytes.length) {
                    return false;
                }

                int fullBytes = prefixLength / 8;
                int remainingBits = prefixLength % 8;

                for (int i = 0; i < fullBytes; i++) {
                    if (clientBytes[i] != networkBytes[i]) return false;
                }

                if (remainingBits > 0 && fullBytes < clientBytes.length) {
                    int mask = 0xFF << (8 - remainingBits);
                    if ((clientBytes[fullBytes] & mask) != (networkBytes[fullBytes] & mask)) {
                        return false;
                    }
                }
                return true;
            } else {
                String normalizedRange = normalizeIp(ipRange);
                return clientIp.equals(normalizedRange);
            }
        } catch (Exception e) {
            return false;
        }
    }

    private String normalizeIp(String ip) {
        if (ip == null) return "";
        if (NetworkIpUtil.isLoopback(ip)) {
            String networkIp = NetworkIpUtil.getLocalNetworkIp();
            return networkIp != null ? networkIp : ip;
        }
        return ip;
    }

    private void validateIpOrCidr(String input) {
        if (input == null || input.isBlank()) {
            throw new BadRequestException("IP range cannot be empty");
        }

        String trimmed = input.trim();

        try {
            if (trimmed.contains("/")) {
                String[] parts = trimmed.split("/");
                if (parts.length != 2) {
                    throw new BadRequestException("Invalid CIDR format: " + trimmed);
                }
                InetAddress.getByName(parts[0]);
                int prefix = Integer.parseInt(parts[1]);
                InetAddress addr = InetAddress.getByName(parts[0]);
                int maxPrefix = addr.getAddress().length == 4 ? 32 : 128;
                if (prefix < 0 || prefix > maxPrefix) {
                    throw new BadRequestException("CIDR prefix must be between 0 and " + maxPrefix);
                }
            } else {
                InetAddress.getByName(trimmed);
            }
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("Invalid IP address or CIDR range: " + trimmed);
        }
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/service/LeaveAnalysisService.java`**
```java
package com.revworkforce.employeemanagementservice.service;

import com.revworkforce.employeemanagementservice.dto.LeaveAnalysisResponse;
import com.revworkforce.employeemanagementservice.integration.OllamaClient;
import com.revworkforce.employeemanagementservice.model.*;
import com.revworkforce.employeemanagementservice.model.enums.LeaveStatus;
import com.revworkforce.employeemanagementservice.repository.LeaveApplicationRepository;
import com.revworkforce.employeemanagementservice.repository.LeaveBalanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LeaveAnalysisService {
    private static final Logger log = LoggerFactory.getLogger(LeaveAnalysisService.class);

    @Autowired private LeaveApplicationRepository leaveApplicationRepository;
    @Autowired private LeaveBalanceRepository leaveBalanceRepository;
    @Autowired private OllamaClient ollamaClient;

    public LeaveAnalysisResponse analyzeLeaveRequest(Integer leaveId) {
        LeaveApplication request = leaveApplicationRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave application not found: " + leaveId));

        Employee employee = request.getEmployee();
        LocalDate today = LocalDate.now();
        int currentYear = today.getYear();

        List<LeaveApplication> allLeaves = leaveApplicationRepository
                .findByEmployeeEmployeeId(employee.getEmployeeId().intValue());

        List<LeaveApplication> thisYearLeaves = allLeaves.stream()
                .filter(l -> l.getStartDate().getYear() == currentYear)
                .filter(l -> l.getStatus() == LeaveStatus.APPROVED || l.getStatus() == LeaveStatus.PENDING)
                .toList();

        List<LeaveApplication> lastYearLeaves = allLeaves.stream()
                .filter(l -> l.getStartDate().getYear() == currentYear - 1)
                .filter(l -> l.getStatus() == LeaveStatus.APPROVED)
                .toList();

        Map<String, Integer> leavesByType = thisYearLeaves.stream()
                .filter(l -> l.getStatus() == LeaveStatus.APPROVED)
                .collect(Collectors.groupingBy(
                        l -> l.getLeaveType().getLeaveTypeName(),
                        Collectors.summingInt(LeaveApplication::getTotalDays)));

        Map<String, Integer> leavesByMonth = thisYearLeaves.stream()
                .filter(l -> l.getStatus() == LeaveStatus.APPROVED)
                .collect(Collectors.groupingBy(
                        l -> l.getStartDate().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                        Collectors.summingInt(LeaveApplication::getTotalDays)));

        double avgDuration = thisYearLeaves.stream()
                .filter(l -> l.getStatus() == LeaveStatus.APPROVED)
                .mapToInt(LeaveApplication::getTotalDays)
                .average().orElse(0);

        List<LeaveBalance> balances = leaveBalanceRepository
                .findByEmployee_EmployeeIdAndYear(employee.getEmployeeId(), currentYear);
        Map<String, Integer> currentBalances = balances.stream()
                .collect(Collectors.toMap(
                        b -> b.getLeaveType().getLeaveTypeName(),
                        LeaveBalance::getAvailableBalance));

        String requestedType = request.getLeaveType().getLeaveTypeName();
        int currentBalance = currentBalances.getOrDefault(requestedType, 0);
        int balanceAfterApproval = currentBalance - request.getTotalDays();

        List<String> patterns = detectPatterns(thisYearLeaves);

        int thisYearCount = thisYearLeaves.stream()
                .filter(l -> l.getStatus() == LeaveStatus.APPROVED)
                .mapToInt(LeaveApplication::getTotalDays).sum();
        int lastYearCount = lastYearLeaves.stream()
                .mapToInt(LeaveApplication::getTotalDays).sum();
        String trend = thisYearCount > lastYearCount * 1.2 ? "INCREASING" :
                thisYearCount < lastYearCount * 0.8 ? "DECREASING" : "STABLE";

        int teamOnLeave = 0;
        if (employee.getManager() != null) {
            teamOnLeave = countTeamMembersOnLeave(employee.getManager().getEmployeeId(), today);
        }

        long pendingCount = allLeaves.stream()
                .filter(l -> l.getStatus() == LeaveStatus.PENDING)
                .count();

        LeaveAnalysisResponse response = LeaveAnalysisResponse.builder()
                .employeeName(employee.getFirstName() + " " + employee.getLastName())
                .department(employee.getDepartment() != null ? employee.getDepartment().getDepartmentName() : "N/A")
                .designation(employee.getDesignation() != null ? employee.getDesignation().getDesignationName() : "N/A")
                .totalLeavesTakenThisYear(thisYearCount)
                .totalLeavesTakenLastYear(lastYearCount)
                .leavesByType(leavesByType)
                .leavesByMonth(leavesByMonth)
                .pendingLeaveRequests((int) pendingCount)
                .averageLeaveDuration(Math.round(avgDuration * 10.0) / 10.0)
                .currentBalances(currentBalances)
                .patterns(patterns)
                .frequencyTrend(trend)
                .teamMembersOnLeaveToday(teamOnLeave)
                .requestedDays(request.getTotalDays())
                .requestedType(requestedType)
                .balanceAfterApproval(balanceAfterApproval)
                .build();

        generateAiAnalysis(response, request);

        return response;
    }

    private List<String> detectPatterns(List<LeaveApplication> leaves) {
        List<String> patterns = new ArrayList<>();
        List<LeaveApplication> approved = leaves.stream()
                .filter(l -> l.getStatus() == LeaveStatus.APPROVED).toList();

        if (approved.isEmpty()) {
            patterns.add("No approved leave history this year");
            return patterns;
        }

        long mondayFriday = approved.stream()
                .filter(l -> {
                    DayOfWeek dow = l.getStartDate().getDayOfWeek();
                    return dow == DayOfWeek.MONDAY || dow == DayOfWeek.FRIDAY;
                }).count();
        if (mondayFriday > approved.size() * 0.5 && approved.size() >= 3) {
            patterns.add("Frequently takes leave on Mondays/Fridays (" + mondayFriday + "/" + approved.size() + " requests)");
        }

        long singleDays = approved.stream().filter(l -> l.getTotalDays() == 1).count();
        if (singleDays > 3) {
            patterns.add("Takes many single-day leaves (" + singleDays + " this year)");
        }

        Map<Integer, Long> byMonth = approved.stream()
                .collect(Collectors.groupingBy(l -> l.getStartDate().getMonthValue(), Collectors.counting()));
        byMonth.entrySet().stream()
                .filter(e -> e.getValue() >= 3)
                .forEach(e -> patterns.add("High leave frequency in " +
                        java.time.Month.of(e.getKey()).getDisplayName(TextStyle.FULL, Locale.ENGLISH)));

        if (patterns.isEmpty()) {
            patterns.add("No unusual patterns detected");
        }

        return patterns;
    }

    private int countTeamMembersOnLeave(Integer managerId, LocalDate date) {
        return leaveApplicationRepository.countTeamOnLeave(managerId, LeaveStatus.APPROVED, date);
    }

    private void generateAiAnalysis(LeaveAnalysisResponse response, LeaveApplication request) {
        if (!ollamaClient.isAvailable()) {
            log.info("Ollama not available — using data-driven analysis fallback");
            setDefaultAnalysis(response);
            return;
        }

        try {
            String prompt = String.format("""
                    Analyze this leave request concisely (2-3 sentences).
                    Employee: %s | Dept: %s | Request: %d days %s
                    Balance after: %d | Trend: %s | Team on leave: %d
                    Patterns: %s
                    Respond EXACTLY as:
                    SUMMARY: [analysis]
                    RECOMMENDATION: [APPROVE or REVIEW_FURTHER]
                    REASONS: [reason1, reason2]
                    """,
                    response.getEmployeeName(), response.getDepartment(),
                    request.getTotalDays(), response.getRequestedType(),
                    response.getBalanceAfterApproval(),
                    response.getFrequencyTrend(), response.getTeamMembersOnLeaveToday(),
                    String.join("; ", response.getPatterns()));

            String aiResponse = ollamaClient.generate(prompt, 150);

            if (aiResponse != null && !aiResponse.startsWith("Error")) {
                String summary = extractField(aiResponse, "SUMMARY:");
                String recommendation = extractField(aiResponse, "RECOMMENDATION:");
                String reasons = extractField(aiResponse, "REASONS:");

                response.setAiSummary(summary != null ? summary : aiResponse.trim());
                response.setAiRecommendation(recommendation != null && recommendation.contains("REVIEW") ?
                        "REVIEW_FURTHER" : "APPROVE");
                response.setAiReasons(reasons != null ?
                        Arrays.asList(reasons.split(",\\s*")) :
                        List.of("Based on available data"));
            } else {
                setDefaultAnalysis(response);
            }
        } catch (Exception e) {
            setDefaultAnalysis(response);
        }
    }

    private void setDefaultAnalysis(LeaveAnalysisResponse response) {
        List<String> reasons = new ArrayList<>();
        StringBuilder summary = new StringBuilder();

        boolean recommend = true;

        if (response.getBalanceAfterApproval() >= 0) {
            reasons.add("Sufficient leave balance (" + (response.getBalanceAfterApproval() + response.getRequestedDays()) + " available, " + response.getBalanceAfterApproval() + " remaining after)");
        } else {
            recommend = false;
            reasons.add("Insufficient leave balance (would be " + response.getBalanceAfterApproval() + " after approval)");
        }

        if (response.getTeamMembersOnLeaveToday() >= 3) {
            recommend = false;
            reasons.add("High team absence — " + response.getTeamMembersOnLeaveToday() + " team members already on leave today");
        } else if (response.getTeamMembersOnLeaveToday() > 0) {
            reasons.add(response.getTeamMembersOnLeaveToday() + " team member(s) on leave today — manageable impact");
        } else {
            reasons.add("No team members currently on leave — minimal team impact");
        }

        if ("INCREASING".equals(response.getFrequencyTrend()) && response.getTotalLeavesTakenThisYear() > 10) {
            reasons.add("Leave frequency is increasing compared to last year (" + response.getTotalLeavesTakenThisYear() + " vs " + response.getTotalLeavesTakenLastYear() + " days)");
        } else if ("STABLE".equals(response.getFrequencyTrend()) || "DECREASING".equals(response.getFrequencyTrend())) {
            reasons.add("Leave usage trend is " + response.getFrequencyTrend().toLowerCase() + " compared to last year");
        }

        boolean hasWarningPattern = response.getPatterns() != null &&
                response.getPatterns().stream().anyMatch(p -> p.contains("Frequently") || p.contains("High leave frequency"));
        if (hasWarningPattern) {
            reasons.add("Some leave patterns detected — review patterns section for details");
        }

        if (response.getPendingLeaveRequests() > 3) {
            reasons.add(response.getPendingLeaveRequests() + " pending leave requests — consider reviewing all together");
        }

        summary.append(response.getEmployeeName()).append(" (").append(response.getDepartment()).append(") ")
                .append("is requesting ").append(response.getRequestedDays()).append(" day(s) of ").append(response.getRequestedType()).append(". ");

        if (response.getBalanceAfterApproval() >= 0) {
            summary.append("They have sufficient balance with ").append(response.getBalanceAfterApproval()).append(" days remaining after approval. ");
        } else {
            summary.append("This would exceed their available balance by ").append(Math.abs(response.getBalanceAfterApproval())).append(" day(s). ");
        }

        summary.append("This year they've taken ").append(response.getTotalLeavesTakenThisYear()).append(" days ")
                .append("(last year: ").append(response.getTotalLeavesTakenLastYear()).append(" days). ");

        if (response.getTeamMembersOnLeaveToday() == 0) {
            summary.append("No team members are on leave today, so team coverage looks fine.");
        } else {
            summary.append(response.getTeamMembersOnLeaveToday()).append(" team member(s) are on leave today.");
        }

        response.setAiSummary(summary.toString());
        response.setAiRecommendation(recommend ? "APPROVE" : "REVIEW_FURTHER");
        response.setAiReasons(reasons);
    }

    private String extractField(String text, String fieldName) {
        int idx = text.indexOf(fieldName);
        if (idx < 0) return null;
        String after = text.substring(idx + fieldName.length()).trim();
        int newline = after.indexOf('\n');
        return newline >= 0 ? after.substring(0, newline).trim() : after.trim();
    }
}


```

#### **`src/main/java/com/revworkforce/employeemanagementservice/service/LeaveService.java`**
```java
package com.revworkforce.employeemanagementservice.service;

import com.revworkforce.employeemanagementservice.dto.*;
import com.revworkforce.employeemanagementservice.exception.*;
import com.revworkforce.employeemanagementservice.model.*;
import com.revworkforce.employeemanagementservice.model.enums.LeaveStatus;
import com.revworkforce.employeemanagementservice.model.enums.Role;
import com.revworkforce.employeemanagementservice.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LeaveService {
    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;
    @Autowired
    private LeaveBalanceRepository leaveBalanceRepository;
    @Autowired
    private LeaveTypeRepository leaveTypeRepository;
    @Autowired
    private HolidayRepository holidayRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private NotificationService notificationService;

    @Transactional
    public LeaveApplication applyLeave(String email, LeaveApplyRequest request) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with email: " + email));
        LeaveType leaveType = leaveTypeRepository.findById(request.getLeaveTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Leave type not found with id: " + request.getLeaveTypeId()));
        if (!leaveType.getIsActive()) {
            throw new InvalidActionException("This leave type is not active");
        }
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new BadRequestException("End date cannot be before start date");
        }
        if (request.getStartDate().isBefore(LocalDate.now())) {
            throw new BadRequestException("Cannot apply leave for past dates");
        }
        List<LeaveApplication> overlapping = leaveApplicationRepository.findOverlappingLeaves(
                employee.getEmployeeId(), request.getStartDate(), request.getEndDate(),
                LeaveStatus.CANCELLED, LeaveStatus.REJECTED);
        if (!overlapping.isEmpty()) {
            throw new DuplicateResourceException("You already have a leave application overlapping with these dates");
        }
        int totalDays = calculateWorkingDays(request.getStartDate(), request.getEndDate());
        if (totalDays <= 0) {
            throw new BadRequestException("No working days in the selected date range");
        }
        int currentYear = request.getStartDate().getYear();

        LeaveBalance balance = leaveBalanceRepository
                .findByEmployee_EmployeeIdAndLeaveType_LeaveTypeIdAndYear(
                        employee.getEmployeeId(), leaveType.getLeaveTypeId(), currentYear)
                .orElseGet(() -> {
                    Integer defaultTotal = leaveType.getIsLossOfPay() ? 0 :
                            (leaveType.getDefaultDays() != null ? leaveType.getDefaultDays() : 0);

                    LeaveBalance newBalance = LeaveBalance.builder()
                            .employee(employee)
                            .leaveType(leaveType)
                            .year(currentYear)
                            .totalLeaves(defaultTotal)
                            .usedLeaves(0)
                            .adjustmentReason("Auto-created on leave application")
                            .build();
                    return leaveBalanceRepository.save(newBalance);
                });

        if (!leaveType.getIsLossOfPay() && balance.getAvailableBalance() < totalDays) {
            throw new InsufficientBalanceException(
                    "Insufficient leave balance. Available: " + balance.getAvailableBalance() + ", Requested: " + totalDays);
        }
        LeaveApplication leave = LeaveApplication.builder()
                .employee(employee).leaveType(leaveType)
                .startDate(request.getStartDate()).endDate(request.getEndDate())
                .totalDays(totalDays).reason(request.getReason())
                .status(LeaveStatus.PENDING).appliedDate(LocalDateTime.now())
                .build();
        LeaveApplication savedLeave = leaveApplicationRepository.save(leave);
        notificationService.notifyLeaveApplied(employee);
        return savedLeave;
    }

    public Page<LeaveApplication> getMyLeaves(String email, LeaveStatus status, Pageable pageable) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with email: " + email));
        if (status != null) {
            return leaveApplicationRepository.findByEmployee_EmployeeIdAndStatus(employee.getEmployeeId(), status, pageable);
        }
        return leaveApplicationRepository.findByEmployee_EmployeeId(employee.getEmployeeId(), pageable);
    }

    @Transactional
    public LeaveApplication cancelLeave(String email, Integer leaveId) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with email: " + email));
        LeaveApplication leave = leaveApplicationRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave application not found with id: " + leaveId));
        if (!leave.getEmployee().getEmployeeId().equals(employee.getEmployeeId())) {
            throw new AccessDeniedException("You can only cancel your own leave applications");
        }
        if (leave.getStatus() != LeaveStatus.PENDING && leave.getStatus() != LeaveStatus.APPROVED) {
            throw new InvalidActionException("Only pending or approved leaves can be cancelled. Current status: " + leave.getStatus());
        }

        if (leave.getStatus() == LeaveStatus.APPROVED) {
            int year = leave.getStartDate().getYear();
            leaveBalanceRepository.findByEmployee_EmployeeIdAndLeaveType_LeaveTypeIdAndYear(
                    employee.getEmployeeId(), leave.getLeaveType().getLeaveTypeId(), year)
                    .ifPresent(balance -> {
                        int restored = Math.max(balance.getUsedLeaves() - leave.getTotalDays(), 0);
                        balance.setUsedLeaves(restored);
                        leaveBalanceRepository.save(balance);
                    });
        }

        leave.setStatus(LeaveStatus.CANCELLED);
        LeaveApplication cancelledLeave = leaveApplicationRepository.save(leave);
        notificationService.notifyLeaveCancelled(employee, leaveId);
        return cancelledLeave;
    }

    public List<LeaveBalance> getMyLeaveBalance(String email) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with email: " + email));
        int currentYear = LocalDate.now().getYear();

        List<LeaveBalance> balances = leaveBalanceRepository.findByEmployee_EmployeeIdAndYear(employee.getEmployeeId(), currentYear);

        List<LeaveType> allActiveLeaveTypes = leaveTypeRepository.findByIsActive(true);

        Set<Integer> existingLeaveTypeIds = balances.stream()
                .map(b -> b.getLeaveType().getLeaveTypeId())
                .collect(Collectors.toSet());

        for (LeaveType leaveType : allActiveLeaveTypes) {
            if (!existingLeaveTypeIds.contains(leaveType.getLeaveTypeId())) {
                LeaveBalance zeroBalance = LeaveBalance.builder()
                        .employee(employee)
                        .leaveType(leaveType)
                        .year(currentYear)
                        .totalLeaves(leaveType.getDefaultDays() != null ? leaveType.getDefaultDays() : 0)
                        .usedLeaves(0)
                        .build();
                balances.add(zeroBalance);
            }
        }

        return balances;
    }

    public List<Holiday> getHolidays(Integer year) {
        return holidayRepository.findByYearOrderByHolidayDateAsc(year != null ? year : LocalDate.now().getYear());
    }

    public Page<LeaveApplication> getTeamLeaves(String managerEmail, LeaveStatus status, Pageable pageable) {
        Employee manager = employeeRepository.findByEmail(managerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found with email: " + managerEmail));

        if (status != null) {
            return leaveApplicationRepository.findByManagerCodeAndStatusExcludingManagers(manager.getEmployeeCode(), status, pageable);
        }
        return leaveApplicationRepository.findByManagerCodeExcludingManagers(manager.getEmployeeCode(), pageable);
    }

    @Transactional
    public LeaveApplication actionLeave(String managerEmail, Integer leaveId, LeaveActionRequest request) {
        Employee manager = employeeRepository.findByEmail(managerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found with email: " + managerEmail));
        LeaveApplication leave = leaveApplicationRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave application not found with id: " + leaveId));
        if (leave.getEmployee().getManager() == null ||
                !leave.getEmployee().getManager().getEmployeeCode().equals(manager.getEmployeeCode())) {
            throw new AccessDeniedException("This leave application does not belong to your team");
        }

        if (leave.getEmployee().getRole() == Role.MANAGER) {
            throw new AccessDeniedException("Manager leave applications can only be actioned by an admin");
        }
        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new InvalidActionException("Only pending leaves can be approved/rejected. Current status: " + leave.getStatus());
        }
        LeaveStatus newStatus;
        if ("APPROVED".equalsIgnoreCase(request.getAction())) {
            newStatus = LeaveStatus.APPROVED;
        } else if ("REJECTED".equalsIgnoreCase(request.getAction())) {
            if (request.getComments() == null || request.getComments().isBlank()) {
                throw new BadRequestException("Comments are mandatory when rejecting a leave");
            }
            newStatus = LeaveStatus.REJECTED;
        } else {
            throw new BadRequestException("Invalid action. Use APPROVED or REJECTED");
        }
        leave.setStatus(newStatus);
        leave.setManagerComments(request.getComments());
        leave.setActionedBy(manager);
        leave.setActionDate(LocalDateTime.now());
        if (newStatus == LeaveStatus.APPROVED) {
            int year = leave.getStartDate().getYear();
            LeaveBalance balance = leaveBalanceRepository
                    .findByEmployee_EmployeeIdAndLeaveType_LeaveTypeIdAndYear(
                            leave.getEmployee().getEmployeeId(), leave.getLeaveType().getLeaveTypeId(), year)
                    .orElseThrow(() -> new ResourceNotFoundException("Leave balance not found for employee"));
            balance.setUsedLeaves(balance.getUsedLeaves() + leave.getTotalDays());
            leaveBalanceRepository.save(balance);
        }
        LeaveApplication actionedLeave = leaveApplicationRepository.save(leave);
        if (newStatus == LeaveStatus.APPROVED) {
            notificationService.notifyLeaveApproved(leave.getEmployee(), actionedLeave.getLeaveId());
        } else if (newStatus == LeaveStatus.REJECTED) {
            notificationService.notifyLeaveRejected(leave.getEmployee(), actionedLeave.getLeaveId());
        }
        return actionedLeave;
    }

    @Transactional
    public LeaveApplication adminActionLeave(String adminEmail, Integer leaveId, LeaveActionRequest request) {
        Employee admin = employeeRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with email: " + adminEmail));
        LeaveApplication leave = leaveApplicationRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave application not found with id: " + leaveId));
        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new InvalidActionException("Only pending leaves can be approved/rejected. Current status: " + leave.getStatus());
        }
        LeaveStatus newStatus;
        if ("APPROVED".equalsIgnoreCase(request.getAction())) {
            newStatus = LeaveStatus.APPROVED;
        } else if ("REJECTED".equalsIgnoreCase(request.getAction())) {
            if (request.getComments() == null || request.getComments().isBlank()) {
                throw new BadRequestException("Comments are mandatory when rejecting a leave");
            }
            newStatus = LeaveStatus.REJECTED;
        } else {
            throw new BadRequestException("Invalid action. Use APPROVED or REJECTED");
        }
        leave.setStatus(newStatus);
        leave.setManagerComments(request.getComments());
        leave.setActionedBy(admin);
        leave.setActionDate(LocalDateTime.now());
        if (newStatus == LeaveStatus.APPROVED) {
            int year = leave.getStartDate().getYear();
            LeaveBalance balance = leaveBalanceRepository
                    .findByEmployee_EmployeeIdAndLeaveType_LeaveTypeIdAndYear(
                            leave.getEmployee().getEmployeeId(), leave.getLeaveType().getLeaveTypeId(), year)
                    .orElseThrow(() -> new ResourceNotFoundException("Leave balance not found for employee"));
            balance.setUsedLeaves(balance.getUsedLeaves() + leave.getTotalDays());
            leaveBalanceRepository.save(balance);
        }
        LeaveApplication actionedLeave = leaveApplicationRepository.save(leave);
        if (newStatus == LeaveStatus.APPROVED) {
            notificationService.notifyLeaveApproved(leave.getEmployee(), actionedLeave.getLeaveId());
        } else if (newStatus == LeaveStatus.REJECTED) {
            notificationService.notifyLeaveRejected(leave.getEmployee(), actionedLeave.getLeaveId());
        }
        return actionedLeave;
    }

    public List<LeaveBalance> getTeamMemberBalance(String managerEmail, String employeeCode) {
        Employee manager = employeeRepository.findByEmail(managerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found with email: " + managerEmail));
        Employee employee = employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with code: " + employeeCode));
        if (employee.getManager() == null ||
                !employee.getManager().getEmployeeCode().equals(manager.getEmployeeCode())) {
            throw new AccessDeniedException("This employee is not in your team");
        }
        int currentYear = LocalDate.now().getYear();
        return leaveBalanceRepository.findByEmployee_EmployeeIdAndYear(employee.getEmployeeId(), currentYear);
    }

    public LeaveType createLeaveType(LeaveTypeRequest request) {
        if (leaveTypeRepository.existsByLeaveTypeName(request.getLeaveTypeName())) {
            throw new DuplicateResourceException("Leave type '" + request.getLeaveTypeName() + "' already exists");
        }
        LeaveType leaveType = LeaveType.builder()
                .leaveTypeName(request.getLeaveTypeName())
                .description(request.getDescription())
                .defaultDays(request.getDefaultDays())
                .isPaidLeave(request.getIsPaidLeave() != null ? request.getIsPaidLeave() : true)
                .isCarryForwardEnabled(request.getIsCarryForwardEnabled() != null ? request.getIsCarryForwardEnabled() : false)
                .maxCarryForwardDays(request.getMaxCarryForwardDays() != null ? request.getMaxCarryForwardDays() : 0)
                .isLossOfPay(request.getIsLossOfPay() != null ? request.getIsLossOfPay() : false)
                .build();
        return leaveTypeRepository.save(leaveType);
    }

    public List<LeaveType> getAllLeaveType() {
        return leaveTypeRepository.findAll();
    }

    public LeaveType updateLeaveType(Integer leaveTypeId, LeaveTypeRequest request) {
        LeaveType leaveType = leaveTypeRepository.findById(leaveTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave type not found with id: " + leaveTypeId));
        leaveTypeRepository.findByLeaveTypeName(request.getLeaveTypeName()).ifPresent(existing -> {
            if (!existing.getLeaveTypeId().equals(leaveTypeId)) {
                throw new DuplicateResourceException("Leave type '" + request.getLeaveTypeName() + "' already exists");
            }
        });
        leaveType.setLeaveTypeName(request.getLeaveTypeName());
        leaveType.setDescription(request.getDescription());
        leaveType.setDefaultDays(request.getDefaultDays());
        if (request.getIsPaidLeave() != null) {
            leaveType.setIsPaidLeave(request.getIsPaidLeave());
        }
        if (request.getIsCarryForwardEnabled() != null) {
            leaveType.setIsCarryForwardEnabled(request.getIsCarryForwardEnabled());
        }
        if (request.getMaxCarryForwardDays() != null) {
            leaveType.setMaxCarryForwardDays(request.getMaxCarryForwardDays());
        }
        if (request.getIsLossOfPay() != null) {
            leaveType.setIsLossOfPay(request.getIsLossOfPay());
        }
        return leaveTypeRepository.save(leaveType);
    }

    @Transactional
    public LeaveBalance assignLeaveQuota(String employeeCode, AdjustLeaveBalanceRequest request, String adminEmail) {
        Employee employee = employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with code: " + employeeCode));
        Employee admin = employeeRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with email: " + adminEmail));
        LeaveType leaveType = leaveTypeRepository.findById(request.getLeaveTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Leave type not found with id: " + request.getLeaveTypeId()));
        int currentYear = LocalDate.now().getYear();
        LeaveBalance balance = leaveBalanceRepository
                .findByEmployee_EmployeeIdAndLeaveType_LeaveTypeIdAndYear(
                        employee.getEmployeeId(), leaveType.getLeaveTypeId(), currentYear)
                .orElse(LeaveBalance.builder()
                        .employee(employee).leaveType(leaveType).year(currentYear).usedLeaves(0)
                        .build());
        balance.setTotalLeaves(request.getTotalLeaves());
        balance.setAdjustmentReason(request.getReason());
        balance.setAdjustedBy(admin);
        return leaveBalanceRepository.save(balance);
    }

    public List<LeaveBalance> getEmployeeBalance(String employeeCode) {
        Employee employee = employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with code: " + employeeCode));
        int currentYear = LocalDate.now().getYear();
        return leaveBalanceRepository.findByEmployee_EmployeeIdAndYear(employee.getEmployeeId(), currentYear);
    }

    public Holiday createHoliday(HolidayRequest request) {
        if (holidayRepository.existsByHolidayDate(request.getHolidayDate())) {
            throw new DuplicateResourceException("A holiday already exists on date: " + request.getHolidayDate());
        }
        Holiday holiday = Holiday.builder()
                .holidayName(request.getHolidayName())
                .holidayDate(request.getHolidayDate())
                .description(request.getDescription())
                .year(request.getHolidayDate().getYear())
                .build();
        return holidayRepository.save(holiday);
    }

    public Holiday updateHoliday(Integer holidayId, HolidayRequest request) {
        Holiday holiday = holidayRepository.findById(holidayId)
                .orElseThrow(() -> new ResourceNotFoundException("Holiday not found with id: " + holidayId));
        if (!holiday.getHolidayDate().equals(request.getHolidayDate()) &&
                holidayRepository.existsByHolidayDate(request.getHolidayDate())) {
            throw new DuplicateResourceException("A holiday already exists on date: " + request.getHolidayDate());
        }
        holiday.setHolidayName(request.getHolidayName());
        holiday.setHolidayDate(request.getHolidayDate());
        holiday.setDescription(request.getDescription());
        holiday.setYear(request.getHolidayDate().getYear());
        return holidayRepository.save(holiday);
    }

    public void deleteHoliday(Integer holidayId) {
        if (!holidayRepository.existsById(holidayId)) {
            throw new ResourceNotFoundException("Holiday not found with id: " + holidayId);
        }
        holidayRepository.deleteById(holidayId);
    }

    public List<TeamLeaveCalendarEntry> getTeamLeaveCalendar(String managerEmail, LocalDate startDate, LocalDate endDate) {
        Employee manager = employeeRepository.findByEmail(managerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found with email: " + managerEmail));

        if (startDate == null) startDate = LocalDate.now().withDayOfMonth(1);
        if (endDate == null) endDate = startDate.plusMonths(1).minusDays(1);

        List<LeaveApplication> approvedLeaves = leaveApplicationRepository.findTeamLeavesBetween(
                manager.getEmployeeCode(), LeaveStatus.APPROVED, startDate, endDate);

        return approvedLeaves.stream()
                .map(la -> TeamLeaveCalendarEntry.builder()
                        .employeeCode(la.getEmployee().getEmployeeCode())
                        .employeeName(la.getEmployee().getFirstName() + " " + la.getEmployee().getLastName())
                        .leaveTypeName(la.getLeaveType().getLeaveTypeName())
                        .startDate(la.getStartDate())
                        .endDate(la.getEndDate())
                        .totalDays(la.getTotalDays())
                        .status(la.getStatus().name())
                        .build())
                .collect(Collectors.toList());
    }

    public Page<LeaveApplication> getAllLeaveApplications(LeaveStatus status, Pageable pageable) {
        if (status != null) {
            return leaveApplicationRepository.findByStatus(status, pageable);
        }
        return leaveApplicationRepository.findAll(pageable);
    }

    private int calculateWorkingDays(LocalDate startDate, LocalDate endDate) {
        List<Holiday> holidays = holidayRepository.findByHolidayDateBetween(startDate, endDate);
        int workingDays = 0;
        LocalDate date = startDate;
        while (!date.isAfter(endDate)) {
            DayOfWeek day = date.getDayOfWeek();
            if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY) {
                LocalDate findDate = date;
                boolean isHoliday = holidays.stream().anyMatch(h -> h.getHolidayDate().equals(findDate));
                if (!isHoliday) {
                    workingDays++;
                }
            }
            date = date.plusDays(1);
        }
        return workingDays;
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/service/NotificationService.java`**
```java
package com.revworkforce.employeemanagementservice.service;

import com.revworkforce.employeemanagementservice.exception.AccessDeniedException;
import com.revworkforce.employeemanagementservice.exception.ResourceNotFoundException;
import com.revworkforce.employeemanagementservice.model.Employee;
import com.revworkforce.employeemanagementservice.model.Notification;
import com.revworkforce.employeemanagementservice.model.enums.NotificationType;
import com.revworkforce.employeemanagementservice.model.enums.Role;
import com.revworkforce.employeemanagementservice.repository.EmployeeRepository;
import com.revworkforce.employeemanagementservice.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private WebSocketNotificationService wsNotificationService;

    public void sendNotification(Employee recipient, String title, String message, NotificationType type, Integer referenceId, String referenceType) {
        Notification notification = Notification.builder()
                .recipient(recipient)
                .title(title)
                .message(message).type(type).referenceId(referenceId).referenceType(referenceType).build();
        notification = notificationRepository.save(notification);

        try {
            long unreadCount = notificationRepository.countByRecipient_EmployeeIdAndIsRead(recipient.getEmployeeId(), false);
            wsNotificationService.pushNotification(recipient.getEmail(), java.util.Map.of(
                    "notificationId", notification.getNotificationId(),
                    "title", title,
                    "message", message,
                    "type", type.name(),
                    "referenceId", referenceId != null ? referenceId : "",
                    "referenceType", referenceType != null ? referenceType : "",
                    "unreadCount", unreadCount
            ));
        } catch (Exception e) {
        }
    }

    public Page<Notification> getMyNotifications(String email, Boolean isRead, Pageable pageable) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with email: " + email));
        if (isRead != null) {
            return notificationRepository.findByRecipient_EmployeeIdAndIsReadOrderByCreatedAtDesc(employee.getEmployeeId(), isRead, pageable);
        }
        return notificationRepository.findByRecipient_EmployeeIdOrderByCreatedAtDesc(employee.getEmployeeId(), pageable);
    }

    public long getUnreadCount(String email) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with email: " + email));
        return notificationRepository.countByRecipient_EmployeeIdAndIsRead(employee.getEmployeeId(), false);
    }

    @Transactional
    public Notification markAsRead(String email, Integer notificationId) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with email: " + email));
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + notificationId));
        if (!notification.getRecipient().getEmployeeId().equals(employee.getEmployeeId())) {
            throw new AccessDeniedException("You can only mark your own notifications as read");
        }
        notification.setIsRead(true);
        return notificationRepository.save(notification);
    }

    @Transactional
    public int markAllAsRead(String email) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with email: " + email));
        return notificationRepository.markAllAsRead(employee.getEmployeeId());
    }

    public void notifyLeaveApplied(Employee employee) {
        String empName = employee.getFirstName() + " " + employee.getLastName();

        if (employee.getManager() != null) {
            sendNotification(employee.getManager(), "New Leave Application",
                    empName + " has applied for leave.",
                    NotificationType.LEAVE_APPLIED, null, "LEAVE_APPLICATION");
        }

        List<Employee> admins = employeeRepository.findByRoleAndIsActive(Role.ADMIN, true);
        for (Employee admin : admins) {
            if (employee.getManager() != null
                    && admin.getEmployeeId().equals(employee.getManager().getEmployeeId())) {
                continue;
            }
            sendNotification(admin, "New Leave Application",
                    empName + " has applied for leave.",
                    NotificationType.LEAVE_APPLIED, null, "LEAVE_APPLICATION");
        }
    }

    public void notifyLeaveApproved(Employee employee, Integer leaveId) {
        sendNotification(employee, "Leave Approved", "Your leave application has been approved.",
                NotificationType.LEAVE_APPROVED, leaveId, "LEAVE_APPLICATION");
    }

    public void notifyLeaveRejected(Employee employee, Integer leaveId) {
        sendNotification(employee, "Leave Rejected",
                "Your leave application has been rejected. Please check the comments.",
                NotificationType.LEAVE_REJECTED, leaveId, "LEAVE_APPLICATION");
    }

    public void notifyLeaveCancelled(Employee employee, Integer leaveId) {
        if (employee.getManager() != null) {
            sendNotification(employee.getManager(), "Leave Cancelled",
                    employee.getFirstName() + " " + employee.getLastName() + " has cancelled a leave application.",
                    NotificationType.LEAVE_CANCELLED, leaveId, "LEAVE_APPLICATION");
        }
    }

    public void notifyReviewSubmitted(Employee employee, Integer reviewId) {
        if (employee.getManager() != null) {
            sendNotification(employee.getManager(), "Performance Review Submitted",
                    employee.getFirstName() + " " + employee.getLastName() + " has submitted a performance review.",
                    NotificationType.REVIEW_SUBMITTED, reviewId, "PERFORMANCE_REVIEW");
        }
    }

    public void notifyReviewFeedback(Employee employee, Integer reviewId) {
        sendNotification(employee, "Manager Feedback Received",
                "Your manager has provided feedback on your performance review.",
                NotificationType.REVIEW_FEEDBACK, reviewId, "PERFORMANCE_REVIEW");
    }

    public void notifyGoalComment(Employee employee, Integer goalId) {
        sendNotification(employee, "Goal Comment from Manager",
                "Your manager has commented on your goal.",
                NotificationType.GOAL_COMMENT, goalId, "GOAL");
    }

    public void notifyAnnouncement(Employee employee, Integer announcementId, String announcementTitle) {
        sendNotification(employee, "New Announcement", "New announcement: " + announcementTitle,
                NotificationType.ANNOUNCEMENT, announcementId, "ANNOUNCEMENT");
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/service/OfficeLocationService.java`**
```java
package com.revworkforce.employeemanagementservice.service;

import com.revworkforce.employeemanagementservice.dto.OfficeLocationRequest;
import com.revworkforce.employeemanagementservice.exception.DuplicateResourceException;
import com.revworkforce.employeemanagementservice.exception.ResourceNotFoundException;
import com.revworkforce.employeemanagementservice.model.OfficeLocation;
import com.revworkforce.employeemanagementservice.repository.OfficeLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OfficeLocationService {
    @Autowired
    private OfficeLocationRepository officeLocationRepository;

    @Transactional(readOnly = true)
    public List<OfficeLocation> getAllLocations() {
        return officeLocationRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public List<OfficeLocation> getActiveLocations() {
        return officeLocationRepository.findByIsActiveTrue();
    }

    @Transactional(readOnly = true)
    public OfficeLocation getLocationById(Integer id) {
        return officeLocationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Office location not found with id: " + id));
    }

    @Transactional
    public OfficeLocation addLocation(OfficeLocationRequest request) {
        if (officeLocationRepository.existsByLocationName(request.getLocationName())) {
            throw new DuplicateResourceException("Office location with name '" + request.getLocationName() + "' already exists");
        }

        validateCoordinates(request.getLatitude(), request.getLongitude());

        OfficeLocation location = OfficeLocation.builder()
                .locationName(request.getLocationName())
                .address(request.getAddress())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .radiusMeters(request.getRadiusMeters() != null ? request.getRadiusMeters() : 200)
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();

        return officeLocationRepository.save(location);
    }

    @Transactional
    public OfficeLocation updateLocation(Integer id, OfficeLocationRequest request) {
        OfficeLocation existing = getLocationById(id);

        officeLocationRepository.findAllByOrderByCreatedAtDesc().stream()
                .filter(loc -> !loc.getLocationId().equals(id)
                        && loc.getLocationName().equalsIgnoreCase(request.getLocationName()))
                .findFirst()
                .ifPresent(loc -> {
                    throw new DuplicateResourceException("Office location with name '" + request.getLocationName() + "' already exists");
                });

        validateCoordinates(request.getLatitude(), request.getLongitude());

        existing.setLocationName(request.getLocationName());
        existing.setAddress(request.getAddress());
        existing.setLatitude(request.getLatitude());
        existing.setLongitude(request.getLongitude());
        if (request.getRadiusMeters() != null) {
            existing.setRadiusMeters(request.getRadiusMeters());
        }
        if (request.getIsActive() != null) {
            existing.setIsActive(request.getIsActive());
        }

        return officeLocationRepository.save(existing);
    }

    @Transactional
    public OfficeLocation toggleLocation(Integer id) {
        OfficeLocation location = getLocationById(id);
        location.setIsActive(!location.getIsActive());
        return officeLocationRepository.save(location);
    }

    @Transactional
    public void deleteLocation(Integer id) {
        OfficeLocation location = getLocationById(id);
        officeLocationRepository.delete(location);
    }

    private void validateCoordinates(Double latitude, Double longitude) {
        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90");
        }
        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180");
        }
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/service/PerformanceService.java`**
```java
package com.revworkforce.employeemanagementservice.service;

import com.revworkforce.employeemanagementservice.dto.*;
import com.revworkforce.employeemanagementservice.exception.*;
import com.revworkforce.employeemanagementservice.model.Employee;
import com.revworkforce.employeemanagementservice.model.Goal;
import com.revworkforce.employeemanagementservice.model.PerformanceReview;
import com.revworkforce.employeemanagementservice.model.enums.GoalPriority;
import com.revworkforce.employeemanagementservice.model.enums.GoalStatus;
import com.revworkforce.employeemanagementservice.model.enums.ReviewStatus;
import com.revworkforce.employeemanagementservice.repository.EmployeeRepository;
import com.revworkforce.employeemanagementservice.repository.GoalRepository;
import com.revworkforce.employeemanagementservice.repository.PerformanceReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PerformanceService {
    @Autowired
    private PerformanceReviewRepository reviewRepository;
    @Autowired
    private GoalRepository goalRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private NotificationService notificationService;

    @Transactional
    public PerformanceReview createReview(String email, PerformanceReviewRequest request) {
        Employee employee = getEmployeeByEmail(email);
        reviewRepository.findByEmployee_EmployeeIdAndReviewPeriod(employee.getEmployeeId(), request.getReviewPeriod())
                .ifPresent(existing -> {
                    throw new DuplicateResourceException("A performance review already exists for period: " + request.getReviewPeriod());
                });
        PerformanceReview review = PerformanceReview.builder()
                .employee(employee).reviewPeriod(request.getReviewPeriod())
                .keyDeliverables(request.getKeyDeliverables())
                .accomplishments(request.getAccomplishments())
                .areasOfImprovement(request.getAreasOfImprovement())
                .selfAssessmentRating(request.getSelfAssessmentRating())
                .status(ReviewStatus.DRAFT).build();
        return reviewRepository.save(review);
    }

    @Transactional
    public PerformanceReview updateReview(String email, Integer reviewId, PerformanceReviewRequest request) {
        Employee employee = getEmployeeByEmail(email);
        PerformanceReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Performance review not found with id: " + reviewId));
        if (!review.getEmployee().getEmployeeId().equals(employee.getEmployeeId())) {
            throw new AccessDeniedException("You can only update your own performance reviews");
        }
        if (review.getStatus() != ReviewStatus.DRAFT) {
            throw new InvalidActionException("Only draft reviews can be updated. Current status: " + review.getStatus());
        }
        review.setKeyDeliverables(request.getKeyDeliverables());
        review.setAccomplishments(request.getAccomplishments());
        review.setAreasOfImprovement(request.getAreasOfImprovement());
        review.setSelfAssessmentRating(request.getSelfAssessmentRating());
        return reviewRepository.save(review);
    }

    @Transactional
    public PerformanceReview submitReview(String email, Integer reviewId) {
        Employee employee = getEmployeeByEmail(email);
        PerformanceReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Performance review not found with id: " + reviewId));
        if (!review.getEmployee().getEmployeeId().equals(employee.getEmployeeId())) {
            throw new AccessDeniedException("You can only submit your own performance reviews");
        }
        if (review.getStatus() != ReviewStatus.DRAFT) {
            throw new InvalidActionException("Only draft reviews can be submitted. Current status: " + review.getStatus());
        }
        if (review.getKeyDeliverables() == null || review.getKeyDeliverables().isBlank()) {
            throw new BadRequestException("Key deliverables are required before submitting");
        }
        if (review.getSelfAssessmentRating() == null) {
            throw new BadRequestException("Self assessment rating is required before submitting");
        }
        review.setStatus(ReviewStatus.SUBMITTED);
        review.setSubmittedDate(LocalDateTime.now());
        PerformanceReview savedReview = reviewRepository.save(review);
        notificationService.notifyReviewSubmitted(employee, savedReview.getReviewId());
        return savedReview;
    }

    public Page<PerformanceReview> getMyReviews(String email, ReviewStatus status, Pageable pageable) {
        Employee employee = getEmployeeByEmail(email);
        if (status != null) {
            return reviewRepository.findByEmployee_EmployeeIdAndStatus(employee.getEmployeeId(), status, pageable);
        }
        return reviewRepository.findByEmployee_EmployeeId(employee.getEmployeeId(), pageable);
    }

    public PerformanceReview getReviewById(String email, Integer reviewId) {
        Employee employee = getEmployeeByEmail(email);
        PerformanceReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Performance review not found with id: " + reviewId));
        if (!review.getEmployee().getEmployeeId().equals(employee.getEmployeeId())) {
            throw new AccessDeniedException("You can only view your own performance reviews");
        }
        return review;
    }

    @Transactional
    public Goal createGoal(String email, GoalRequest request) {
        Employee employee = getEmployeeByEmail(email);
        GoalPriority priority;
        try {
            priority = GoalPriority.valueOf(request.getPriority().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid priority: " + request.getPriority() + ". Use HIGH, MEDIUM, or LOW");
        }
        Goal goal = Goal.builder()
                .employee(employee).title(request.getTitle())
                .description(request.getDescription())
                .year(request.getDeadline().getYear())
                .deadline(request.getDeadline())
                .priority(priority).status(GoalStatus.NOT_STARTED).progress(0).build();
        return goalRepository.save(goal);
    }

    public Page<Goal> getMyGoals(String email, Integer year, GoalStatus status, Pageable pageable) {
        Employee employee = getEmployeeByEmail(email);
        if (status != null) {
            return goalRepository.findByEmployee_EmployeeIdAndStatus(employee.getEmployeeId(), status, pageable);
        }
        if (year != null) {
            return goalRepository.findByEmployee_EmployeeIdAndYear(employee.getEmployeeId(), year, pageable);
        }
        return goalRepository.findByEmployee_EmployeeId(employee.getEmployeeId(), pageable);
    }

    @Transactional
    public Goal updateGoalProgress(String email, Integer goalId, GoalProgressRequest request) {
        Employee employee = getEmployeeByEmail(email);
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found with id: " + goalId));
        if (!goal.getEmployee().getEmployeeId().equals(employee.getEmployeeId())) {
            throw new AccessDeniedException("You can only update your own goals");
        }
        if (goal.getStatus() == GoalStatus.COMPLETED) {
            throw new InvalidActionException("This goal is already marked as completed");
        }
        goal.setProgress(request.getProgress());
        if (request.getProgress() == 100) {
            goal.setStatus(GoalStatus.COMPLETED);
        } else if (request.getProgress() > 0) {
            goal.setStatus(GoalStatus.IN_PROGRESS);
        } else {
            goal.setStatus(GoalStatus.NOT_STARTED);
        }
        if (request.getStatus() != null && !request.getStatus().isBlank()) {
            try {
                goal.setStatus(GoalStatus.valueOf(request.getStatus().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid status: " + request.getStatus() + ". Use NOT_STARTED, IN_PROGRESS, or COMPLETED");
            }
        }
        return goalRepository.save(goal);
    }

    public Page<PerformanceReview> getTeamReviews(String managerEmail, ReviewStatus status, Pageable pageable) {
        Employee manager = getEmployeeByEmail(managerEmail);
        if (status != null) {
            return reviewRepository.findByManagerCodeAndStatus(manager.getEmployeeCode(), status, pageable);
        }

        return reviewRepository.findByManagerCodeAndStatusNot(manager.getEmployeeCode(), ReviewStatus.DRAFT, pageable);
    }

    public PerformanceReview getTeamReviewById(String managerEmail, Integer reviewId) {
        Employee manager = getEmployeeByEmail(managerEmail);
        PerformanceReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Performance review not found with id: " + reviewId));
        if (review.getEmployee().getManager() == null ||
                !review.getEmployee().getManager().getEmployeeCode().equals(manager.getEmployeeCode())) {
            throw new AccessDeniedException("This review does not belong to your team member");
        }
        return review;
    }

    @Transactional
    public PerformanceReview provideReviewFeedback(String managerEmail, Integer reviewId, ManagerFeedbackRequest request) {
        Employee manager = getEmployeeByEmail(managerEmail);
        PerformanceReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Performance review not found with id: " + reviewId));
        if (review.getEmployee().getManager() == null ||
                !review.getEmployee().getManager().getEmployeeCode().equals(manager.getEmployeeCode())) {
            throw new AccessDeniedException("This review does not belong to your team member");
        }
        if (review.getStatus() != ReviewStatus.SUBMITTED) {
            throw new InvalidActionException("Only submitted reviews can be reviewed. Current status: " + review.getStatus());
        }
        review.setReviewer(manager);
        review.setManagerRating(request.getManagerRating());
        review.setManagerFeedback(request.getManagerFeedback());
        review.setStatus(ReviewStatus.REVIEWED);
        review.setReviewedDate(LocalDateTime.now());
        PerformanceReview savedReview = reviewRepository.save(review);
        notificationService.notifyReviewFeedback(review.getEmployee(), savedReview.getReviewId());
        return savedReview;
    }

    public Page<Goal> getTeamMemberGoals(String managerEmail, String employeeCode, Pageable pageable) {
        Employee manager = getEmployeeByEmail(managerEmail);
        return goalRepository.findByEmployeeCodeAndManagerCode(employeeCode, manager.getEmployeeCode(), pageable);
    }

    public Page<Goal> getAllTeamGoals(String managerEmail, Pageable pageable) {
        Employee manager = getEmployeeByEmail(managerEmail);
        return goalRepository.findByManagerCode(manager.getEmployeeCode(), pageable);
    }

    @Transactional
    public Goal commentOnGoal(String managerEmail, Integer goalId, ManagerGoalCommentRequest request) {
        Employee manager = getEmployeeByEmail(managerEmail);
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found with id: " + goalId));
        if (goal.getEmployee().getManager() == null ||
                !goal.getEmployee().getManager().getEmployeeCode().equals(manager.getEmployeeCode())) {
            throw new AccessDeniedException("This goal does not belong to your team member");
        }
        goal.setManagerComments(request.getManagerComments());
        Goal savedGoal = goalRepository.save(goal);
        notificationService.notifyGoalComment(goal.getEmployee(), savedGoal.getGoalId());
        return savedGoal;
    }

    public Page<PerformanceReview> getAllReviews(ReviewStatus status, Pageable pageable) {
        if (status != null) {
            return reviewRepository.findByStatus(status, pageable);
        }

        return reviewRepository.findByStatusNot(ReviewStatus.DRAFT, pageable);
    }

    public PerformanceReview getAdminReviewById(Integer reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Performance review not found with id: " + reviewId));
    }

    @Transactional
    public PerformanceReview provideAdminReviewFeedback(String adminEmail, Integer reviewId, ManagerFeedbackRequest request) {
        Employee admin = getEmployeeByEmail(adminEmail);
        PerformanceReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Performance review not found with id: " + reviewId));
        if (review.getStatus() != ReviewStatus.SUBMITTED) {
            throw new InvalidActionException("Only submitted reviews can be reviewed. Current status: " + review.getStatus());
        }
        review.setReviewer(admin);
        review.setManagerRating(request.getManagerRating());
        review.setManagerFeedback(request.getManagerFeedback());
        review.setStatus(ReviewStatus.REVIEWED);
        review.setReviewedDate(LocalDateTime.now());
        PerformanceReview savedReview = reviewRepository.save(review);
        notificationService.notifyReviewFeedback(review.getEmployee(), savedReview.getReviewId());
        return savedReview;
    }

    private Employee getEmployeeByEmail(String email) {
        return employeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with email: " + email));
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/service/PresenceService.java`**
```java
package com.revworkforce.employeemanagementservice.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PresenceService {
    private static final long ONLINE_TTL_SECONDS = 150;

    private final Set<String> websocketOnlineUsers = ConcurrentHashMap.newKeySet();
    private final ConcurrentHashMap<String, Instant> lastSeen = new ConcurrentHashMap<>();

    public void userConnected(String email) {
        if (email == null || email.isBlank()) {
            return;
        }
        websocketOnlineUsers.add(email);
        touchUser(email);
    }

    public void userDisconnected(String email) {
        if (email == null || email.isBlank()) {
            return;
        }
        websocketOnlineUsers.remove(email);
        touchUser(email);
    }

    public void touchUser(String email) {
        if (email == null || email.isBlank()) {
            return;
        }
        lastSeen.put(email, Instant.now());
    }

    public boolean isOnline(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        if (websocketOnlineUsers.contains(email)) {
            return true;
        }
        Instant seenAt = lastSeen.get(email);
        if (seenAt == null) {
            return false;
        }
        return seenAt.isAfter(Instant.now().minusSeconds(ONLINE_TTL_SECONDS));
    }

    public Set<String> getOnlineUsers() {
        Instant cutoff = Instant.now().minusSeconds(ONLINE_TTL_SECONDS);
        Set<String> online = new HashSet<>(websocketOnlineUsers);
        lastSeen.forEach((email, seenAt) -> {
            if (seenAt != null && seenAt.isAfter(cutoff)) {
                online.add(email);
            }
        });
        return Collections.unmodifiableSet(online);
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/service/WebSocketNotificationService.java`**
```java
package com.revworkforce.employeemanagementservice.service;

import com.revworkforce.employeemanagementservice.dto.ChatMessageResponse;
import com.revworkforce.employeemanagementservice.dto.TypingIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WebSocketNotificationService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendChatMessage(String recipientEmail, ChatMessageResponse message) {
        messagingTemplate.convertAndSendToUser(
                recipientEmail, "/queue/messages", message);
    }

    public void sendTypingIndicator(String recipientEmail, TypingIndicator indicator) {
        messagingTemplate.convertAndSendToUser(
                recipientEmail, "/queue/typing", indicator);
    }

    public void pushNotification(String recipientEmail, Map<String, Object> notification) {
        messagingTemplate.convertAndSendToUser(
                recipientEmail, "/queue/notifications", notification);
    }

    public void sendUnreadChatCount(String recipientEmail, long count) {
        messagingTemplate.convertAndSendToUser(
                recipientEmail, "/queue/chat-unread", Map.of("unreadCount", count));
    }
}

```

#### **`src/main/java/com/revworkforce/employeemanagementservice/util/NetworkIpUtil.java`**
```java
package com.revworkforce.employeemanagementservice.util;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class NetworkIpUtil {
    private static final Logger log = LoggerFactory.getLogger(NetworkIpUtil.class);

    private static volatile String cachedNetworkIp = null;
    private static volatile long cacheTimestamp = 0;
    private static final long CACHE_TTL_MS = 30_000;

    public static String getLocalNetworkIp() {
        long now = System.currentTimeMillis();
        if (cachedNetworkIp != null && (now - cacheTimestamp) < CACHE_TTL_MS) {
            return cachedNetworkIp;
        }

        String detectedIp = detectNetworkIp();
        if (detectedIp != null) {
            cachedNetworkIp = detectedIp;
            cacheTimestamp = now;
            log.info("[NetworkIpUtil] Network IP resolved: {}", detectedIp);
        } else {
            log.warn("[NetworkIpUtil] No suitable network IP found");
        }
        return detectedIp;
    }

    private static String detectNetworkIp() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces == null) return null;

            String wifiIp = null;
            String ethernetIp = null;

            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();

                if (ni.isLoopback() || !ni.isUp()) continue;

                String displayName = ni.getDisplayName().toLowerCase();
                String name = ni.getName().toLowerCase();

                if (isVirtualAdapter(ni, displayName, name)) {
                    log.debug("[NetworkIpUtil] SKIPPING virtual adapter: {} ({})", ni.getDisplayName(), name);
                    continue;
                }

                boolean isWifi = isWifiAdapter(displayName, name);

                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();

                    if (addr instanceof Inet4Address && !addr.isLinkLocalAddress()) {
                        String ip = addr.getHostAddress();

                        if (isWifi && wifiIp == null) {
                            wifiIp = ip;
                            log.info("[NetworkIpUtil] WiFi IP detected: {} (interface: {})",
                                    ip, ni.getDisplayName());
                        } else if (!isWifi && ethernetIp == null) {
                            ethernetIp = ip;
                            log.info("[NetworkIpUtil] Ethernet IP detected: {} (interface: {})",
                                    ip, ni.getDisplayName());
                        }
                    }
                }
            }

            if (wifiIp != null) {
                return wifiIp;
            }
            return ethernetIp;
        } catch (SocketException e) {
            log.warn("[NetworkIpUtil] Failed to scan network interfaces: {}", e.getMessage());
        }

        return null;
    }

    private static boolean isVirtualAdapter(NetworkInterface ni, String displayNameLower, String nameLower) {
        if (ni.isVirtual()) return true;

        if (displayNameLower.contains("docker")) return true;

        if (displayNameLower.contains("vethernet")) return true;
        if (displayNameLower.contains("hyper-v")) return true;
        if (displayNameLower.contains("wsl")) return true;

        if (displayNameLower.contains("vmware")) return true;
        if (displayNameLower.contains("vmnet")) return true;

        if (displayNameLower.contains("virtualbox")) return true;
        if (displayNameLower.contains("vboxnet")) return true;

        if (displayNameLower.contains("virtual")) return true;

        if (displayNameLower.contains("tunnel")) return true;
        if (displayNameLower.contains("teredo")) return true;
        if (displayNameLower.contains("isatap")) return true;

        if (nameLower.startsWith("docker")) return true;
        if (nameLower.startsWith("veth")) return true;
        if (nameLower.startsWith("br-")) return true;

        if (nameLower.startsWith("virbr")) return true;

        return false;
    }

    private static boolean isWifiAdapter(String displayNameLower, String nameLower) {
        if (displayNameLower.contains("wi-fi")) return true;
        if (displayNameLower.contains("wifi")) return true;
        if (displayNameLower.contains("wireless")) return true;
        if (displayNameLower.contains("wlan")) return true;
        if (displayNameLower.contains("802.11")) return true;

        if (nameLower.startsWith("wlan")) return true;
        if (nameLower.startsWith("wlp")) return true;
        if (nameLower.equals("en0")) return true;

        return false;
    }

    public static boolean isLoopback(String ip) {
        if (ip == null) return false;
        return "127.0.0.1".equals(ip)
                || "::1".equals(ip)
                || "0:0:0:0:0:0:0:1".equals(ip);
    }

    public static String resolveClientIp(HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();

        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isEmpty()) {
            String firstIp = xff.split(",")[0].trim();

            if (!isLoopback(firstIp)) {
                return firstIp;
            }
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !isLoopback(xRealIp.trim())) {
            return xRealIp.trim();
        }

        if (isLoopback(remoteAddr)) {
            String networkIp = getLocalNetworkIp();
            if (networkIp != null) {
                log.debug("[NetworkIpUtil] Resolved loopback {} → WiFi/LAN IP {}", remoteAddr, networkIp);
                return networkIp;
            }
        }

        return remoteAddr;
    }

    public static String resolveLoopbackToLanIp(String ip) {
        if (ip == null) return "";
        if (isLoopback(ip)) {
            String lanIp = getLocalNetworkIp();
            return lanIp != null ? lanIp : ip;
        }
        return ip;
    }
}

```
