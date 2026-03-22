# Config Server (Centralized Configuration)

## 📌 Overview
The **Config Server** acts as the central hub for the external properties of applications across all environments. In a microservices ecosystem, managing configurations (like database credentials, feature flags, or endpoints) for multiple small, distinct applications can become chaotic.

By using Spring Cloud Config, you extract the configurations out of each microservice's JAR file (`application.properties` / `application.yml`) and store them securely in one place. Changes to properties can be made without requiring a full rebuild or restart of the individual microservices.

## 🏗️ Architecture & Flow

```mermaid
sequenceDiagram
    participant OS as Other Services (e.g. User Service)
    participant CS as Config Server
    participant Repo as Configuration Repository (Git/Native)

    OS->>CS: Requests configuration on startup
    CS->>Repo: Fetches latest configurations
    Repo-->>CS: Returns configuration data
    CS-->>OS: Sends properties to microservice
    OS->>OS: Service runs using fetched properties
```

### 🔑 Key Responsibilities:
1. **Centralized Management**: Keep configuration files isolated and secure.
2. **Environment Separation**: Serve different configurations depending on the active profile (`dev`, `staging`, `prod`).
3. **Dynamic Reloads**: Clients can refresh their configurations on the fly without restarting.
4. **Consistency**: Ensure all instances of a microservice boot up using the exact same configuration set.

## 💻 Technical Details

### Dependencies (`pom.xml`)
The service includes the Spring Cloud Config Server dependency:
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-config-server</artifactId>
</dependency>
```

### Main Application Class
The application requires the `@EnableConfigServer` annotation to actively function as a configuration repository server:
```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer // Tells Spring this is a central Config Server
public class ConfigServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}
```

### Configuration (`application.properties`)
```properties
spring.application.name=config-server
# Standard Config Server Port
server.port=8888 

# Eureka Client Registration (optional if discovery is needed)
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/

# Instructing to fetch properties from the local file system
spring.profiles.active=native
spring.cloud.config.server.native.search-locations=file:///D:/HRMS/Microservices - HRMS/config-repo
```
*Note: The system points to a Native path. In a real-world scenario, this would likely be an external GitHub repository URL.*

## 🚀 How to Run
**Using Maven:**
```bash
mvn spring-boot:run
```

**Using Docker:**
```bash
docker run -p 8888:8888 config-server:latest
```

## 🌐 Endpoints
The Config Server provides endpoints to retrieve configurations for a target application:
👉 **[http://localhost:8888/{application-name}/{profile}](http://localhost:8888/{application-name}/{profile})**
