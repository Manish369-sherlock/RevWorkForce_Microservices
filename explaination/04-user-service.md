# User Service — Authentication & User Management

## Overview
The **User Service** is the most complex and critical business service in the WorkForce ecosystem. It acts as the **authentication authority** for the entire application. It handles user registration, login, JWT issuance, multi-factor authentication (OTP), IP whitelisting, activity logging, and managing the employee directory.

- **Port:** `8087`
- **Type:** Core Identity & Access Management (IAM) Service 
- **Role:** Providing secure JWT tokens, capturing exhaustive activity logs, strictly enforcing IP access boundaries, and resolving user profiles.

---

## Dependencies

| Dependency | Purpose |
|---|---|
| `spring-boot-starter-web` | Exposes REST endpoints (Servlet/MVC) for login and profile adjustments |
| `spring-boot-starter-data-jpa` | ORM for MySQL: Users, Auth Sessions, OTP Tokens, Activity Logs |
| `spring-boot-starter-security` | Spring Security filter chain for endpoints, RBAC, and Context |
| `spring-cloud-starter-netflix-eureka-client` | Dynamic Service Discovery registration |
| `spring-cloud-starter-config` | Syncs runtime configuration properties dynamically |
| `mysql-connector-j` | JDBC Driver mapping entities to MySQL (`workforce` DB) |
| `jjwt-api / jjwt-impl` | Cryptographically signs, issues, and validates JWT Tokens |
| `spring-boot-starter-mail` | Underpins `EmailService` logic sending SMTP OTP codes to employees |
| `spring-boot-starter-websocket` | Emits real-time notification broadcasts directly to active clients |
| `springdoc-openapi` | Auto-generates interactive Swagger/OpenAPI dashboards |
| `lombok` | Generates boilerplate constructors and getters |

---

## Core Services (Key Classes)

### `CustomUserDetailsService`
The entry point bridging Spring Security with the MySQL Employee table. Loads core employee records by email and generates standard Spring `UserDetails` representations used within the filter chain.

### `IpAccessControlService` & `IpAccessControlFilter`
A powerful administrative guardrail. It cross-references incoming request IPs against the `allowed_ip_range` table. Certain secure routes (like accessing manager features or deep employee profiles) are strictly isolated to recognized corporate IPs.

### `ActivityLogService`
Records a detailed audit trail for critical identity/HR events (who updated what, who failed to login, who successfully reset an OTP). It operates asynchronously via `AuthenticationEventListener` to trap bad-password attempts immediately.

### `OtpService`
Generates cryptographically random 6-digit OTP codes required for verifying multi-factor authentication (MFA). It caches them securely in the database (`otp_verification` table), tracks retry attempts, and manages aggressive 5-minute expiries.

### `WebSocketNotificationService`
Directly connects to online sessions via WebSockets, allowing instant system broadcasts seamlessly (typing indicators, connection statuses, or sudden administrative alerts) without waiting for client polling.

---

## Security Flow (Login)
```text
POST /api/auth/login
         │
         ▼
  Verify Email/Password via Spring AuthenticationManager
         │
         ▼
  Valid Credentials? ─── NO ─→ Throw BadCredentials (Logged via EventListener)
         │
         ▼
  Are OTPs Enforced globally? (`otp.enforce-for-all=true`)
         ├── YES → Generate DB OTP, trigger EmailService, Return 200 (Prompt GUI)
         │         │
         │         ▼
         │      Client calls POST /api/auth/verify-otp
         │
         └── NO  → Generate Access + Refresh JWT tokens immediately
         │
         ▼
  Return Tokens (Valid for 24 Hours) to Gateway
```

---

## Key Things to Know

- **Dynamic IP Whitelisting:** Administrators can dynamically update IP Whitelists without restarting the microservice. The next immediate API call evaluates the new lists.
- **WebSocket Broadcasts:** This service doesn't just authenticate; it is heavily real-time oriented, exposing `/ws` layers acting as a chat/alert bus for the front-end network. 
- **Audit Hooks:** Even failed logins (`AuthenticationFailureBadCredentialsEvent`) are aggressively logged. This data is critical for monitoring brute-force attempts.

---

## Interview Focus 🎯

> **Q: How does the User Service implement IP Access Controls?**

We built a custom `IpAccessControlFilter` utilizing Spring's `OncePerRequestFilter`. When traffic targets `/api/employees**` or `/api/manager**`, we calculate the client's original IP using a secure reverse proxy util (`NetworkIpUtil`). That IP is cross-referenced live against database CIDR/Whitelists. If the request originates off-premises or off-VPN, the system forces a strict 403 Forbidden without even parsing the payload.

> **Q: How are JWTs securely dispatched and handled within your microservices?**

The User Service is the absolute authority over tokens. A successful login (and OTP verification) triggers the `JwtUtil` class to build a cryptographically signed HMAC string appending the user's exact Roles/Claims. We grant a short-lived Access token (24H) and a long-lived Refresh token (7D). Downstream microservices don't contact the User Service repeatedly; instead, the API Gateway merely verifies the signature independently using the central `jwt.secret` configuration.

> **Q: What role does Event-Driven architectures play in authentication here?**

To maintain clean code and high performance, we decouple logging from login logic. The moment an attacker fails a login, Spring Security fires an `AuthenticationFailureBadCredentialsEvent`. Our separate `AuthenticationEventListener` component listens for this and natively commits a detailed entry straight into `ActivityLogRepository` completely async. Not only does this stop synchronous delays, but it guarantees compliance monitoring scales effortlessly.


## 🛑 Deep Dive Component Codes & Project Structure
This section contains the full, exhaustive breakdown of the microservice's source code, project structure, and dependencies. It operates as the fundamental source of truth replacing isolated snippets with the actual working code.

### 🌳 Complete Project Tree
```text
user-service/
├── .gitattributes
├── .gitignore
├── Dockerfile
├── hs_err_pid18216.log
├── hs_err_pid19964.log
├── mvnw
├── mvnw.cmd
├── pom.xml
├── replay_pid18216.log
├── replay_pid19964.log
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── revworkforce
    │   │           └── userservice
    │   │               ├── UserServiceApplication.java
    │   │               ├── config
    │   │               │   ├── AuthenticationEventListener.java
    │   │               │   ├── DataSeeder.java
    │   │               │   ├── IpAccessControlFilter.java
    │   │               │   ├── JwtAuthenticationFilter.java
    │   │               │   ├── JwtUtil.java
    │   │               │   ├── OllamaConfig.java
    │   │               │   ├── SecurityConfig.java
    │   │               │   ├── SwaggerConfig.java
    │   │               │   ├── WebSocketAuthInterceptor.java
    │   │               │   ├── WebSocketConfig.java
    │   │               │   └── WebSocketEventListener.java
    │   │               ├── controller
    │   │               │   ├── AdminActivityLogController.java
    │   │               │   ├── AdminDashboardController.java
    │   │               │   ├── AdminDepartmentController.java
    │   │               │   ├── AdminDesignationController.java
    │   │               │   ├── AdminEmployeeController.java
    │   │               │   ├── AdminIpAccessController.java
    │   │               │   ├── AdminOfficeLocationController.java
    │   │               │   ├── AuthController.java
    │   │               │   ├── EmployeeController.java
    │   │               │   ├── EmployeeDirectoryController.java
    │   │               │   └── ManagerTeamController.java
    │   │               ├── dto
    │   │               │   ├── AIChatRequest.java
    │   │               │   ├── AIChatResponse.java
    │   │               │   ├── AdjustLeaveBalanceRequest.java
    │   │               │   ├── AnnouncementRequest.java
    │   │               │   ├── ApiResponse.java
    │   │               │   ├── AssignManagerRequest.java
    │   │               │   ├── AttendanceResponse.java
    │   │               │   ├── AttendanceSummaryResponse.java
    │   │               │   ├── ChangePasswordRequest.java
    │   │               │   ├── ChatMessageRequest.java
    │   │               │   ├── ChatMessageResponse.java
    │   │               │   ├── CheckInRequest.java
    │   │               │   ├── CheckOutRequest.java
    │   │               │   ├── ConversationResponse.java
    │   │               │   ├── DashboardResponse.java
    │   │               │   ├── DepartmentRequest.java
    │   │               │   ├── DesignationRequest.java
    │   │               │   ├── EmployeeDashboardResponse.java
    │   │               │   ├── EmployeeDirectoryResponse.java
    │   │               │   ├── EmployeeProfileResponse.java
    │   │               │   ├── EmployeeReportResponse.java
    │   │               │   ├── ExpenseActionRequest.java
    │   │               │   ├── ExpenseRequest.java
    │   │               │   ├── ForceResetPasswordRequest.java
    │   │               │   ├── GoalProgressRequest.java
    │   │               │   ├── GoalRequest.java
    │   │               │   ├── HolidayRequest.java
    │   │               │   ├── InvoiceParseResponse.java
    │   │               │   ├── IpRangeRequest.java
    │   │               │   ├── IpRangeResponse.java
    │   │               │   ├── LeaveActionRequest.java
    │   │               │   ├── LeaveAnalysisResponse.java
    │   │               │   ├── LeaveApplyRequest.java
    │   │               │   ├── LeaveReportResponse.java
    │   │               │   ├── LeaveTypeRequest.java
    │   │               │   ├── LoginRequest.java
    │   │               │   ├── ManagerFeedbackRequest.java
    │   │               │   ├── ManagerGoalCommentRequest.java
    │   │               │   ├── OfficeLocationRequest.java
    │   │               │   ├── OfficeLocationResponse.java
    │   │               │   ├── PerformanceReportResponse.java
    │   │               │   ├── PerformanceReviewRequest.java
    │   │               │   ├── RefreshTokenRequest.java
    │   │               │   ├── RegisterEmployeeRequest.java
    │   │               │   ├── ResendOtpRequest.java
    │   │               │   ├── TeamLeaveCalendarEntry.java
    │   │               │   ├── TypingIndicator.java
    │   │               │   ├── UpdateEmployeeRequest.java
    │   │               │   ├── UpdateProfileRequest.java
    │   │               │   └── VerifyOtpRequest.java
    │   │               ├── exception
    │   │               │   ├── AccessDeniedException.java
    │   │               │   ├── AccountDeactivatedException.java
    │   │               │   ├── BadRequestException.java
    │   │               │   ├── DuplicateResourceException.java
    │   │               │   ├── GlobalExceptionHandler.java
    │   │               │   ├── InsufficientBalanceException.java
    │   │               │   ├── InvalidActionException.java
    │   │               │   ├── IpBlockedException.java
    │   │               │   ├── ResourceNotFoundException.java
    │   │               │   └── UnauthorizedException.java
    │   │               ├── model
    │   │               │   ├── ActivityLog.java
    │   │               │   ├── AllowedIpRange.java
    │   │               │   ├── Announcement.java
    │   │               │   ├── Attendance.java
    │   │               │   ├── Department.java
    │   │               │   ├── Designation.java
    │   │               │   ├── Employee.java
    │   │               │   ├── Goal.java
    │   │               │   ├── Holiday.java
    │   │               │   ├── LeaveApplication.java
    │   │               │   ├── LeaveBalance.java
    │   │               │   ├── LeaveType.java
    │   │               │   ├── Notification.java
    │   │               │   ├── OfficeLocation.java
    │   │               │   ├── OtpVerification.java
    │   │               │   ├── PerformanceReview.java
    │   │               │   ├── RefreshToken.java
    │   │               │   └── enums
    │   │               │       ├── AttendanceStatus.java
    │   │               │       ├── Gender.java
    │   │               │       ├── GoalPriority.java
    │   │               │       ├── GoalStatus.java
    │   │               │       ├── LeaveStatus.java
    │   │               │       ├── NotificationType.java
    │   │               │       ├── ReviewStatus.java
    │   │               │       └── Role.java
    │   │               ├── repository
    │   │               │   ├── ActivityLogRepository.java
    │   │               │   ├── AllowedIpRangeRepository.java
    │   │               │   ├── AttendanceRepository.java
    │   │               │   ├── DepartmentRepository.java
    │   │               │   ├── DesignationRepository.java
    │   │               │   ├── EmployeeRepository.java
    │   │               │   ├── HolidayRepository.java
    │   │               │   ├── LeaveApplicationRepository.java
    │   │               │   ├── LeaveBalanceRepository.java
    │   │               │   ├── LeaveTypeRepository.java
    │   │               │   ├── NotificationRepository.java
    │   │               │   ├── OfficeLocationRepository.java
    │   │               │   ├── OtpVerificationRepository.java
    │   │               │   ├── PerformanceReviewRepository.java
    │   │               │   └── RefreshTokenRepository.java
    │   │               ├── service
    │   │               │   ├── ActivityLogService.java
    │   │               │   ├── CustomUserDetailsService.java
    │   │               │   ├── DashboardService.java
    │   │               │   ├── DepartmentService.java
    │   │               │   ├── DesignationService.java
    │   │               │   ├── EmailService.java
    │   │               │   ├── EmployeeService.java
    │   │               │   ├── IpAccessControlService.java
    │   │               │   ├── NotificationService.java
    │   │               │   ├── OfficeLocationService.java
    │   │               │   ├── OtpService.java
    │   │               │   ├── PresenceService.java
    │   │               │   ├── RefreshTokenService.java
    │   │               │   └── WebSocketNotificationService.java
    │   │               └── util
    │   │                   └── NetworkIpUtil.java
    │   └── resources
    │       └── application.properties
    └── test
        └── java
            └── com
                └── revworkforce
                    └── userservice
                        └── UserServiceApplicationTests.java
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
    <artifactId>user-service</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>user-service</name>
    <description>Authentication, RBAC, profile management, employee directory</description>
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
            <artifactId>spring-boot-starter-security</artifactId>
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
            <artifactId>spring-boot-starter-mail</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-websocket</artifactId>
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
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
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
spring.application.name=user-service
spring.config.import=optional:configserver:http://localhost:8888
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
server.port=8087

spring.datasource.url=jdbc:mysql://localhost:3306/workforce?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=1234
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

jwt.secret=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
jwt.expiration=86400000
jwt.refresh-expiration=604800000

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=rkonda863@gmail.com
spring.mail.password=zplsqhgqsbptleua
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true

otp.expiry-minutes=5
otp.max-attempts=5
otp.fail-open-on-email-error=false
otp.skip-for-local=false
otp.enforce-for-all=true

springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html

```
