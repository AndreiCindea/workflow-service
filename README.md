# TechQuarter Workflow Service - Corporate Booking Platform

## 📋 Overview

**Workflow Service** is a high-performance REST API for managing corporate travel and resource bookings. Designed to handle 100+ transactions per second (TPS), it provides a clean, scalable architecture following enterprise best practices.

**Time Constraint:** This solution was implemented within a 3–4 hour design and development window, focusing on architectural clarity, code quality, and extensibility over feature completeness.

---

## 🏗️ Architecture

### High-Level Design

```
┌─────────────────────────────────────────────────────────────┐
│                    React Native Frontend                     │
└────────────────────────────┬────────────────────────────────┘
                             │
                    REST API (Spring Boot)
                             │
     ┌───────────────────────┼───────────────────────┐
     │                       │                       │
  Controllers           Services                  Repositories
  (HTTP Layer)      (Business Logic)           (Data Access)
     │                       │                       │
     └───────────────────────┼───────────────────────┘
                             │
                       H2 / PostgreSQL
```

### Layered Architecture

```
com.techquarter.workflow
│
├── controller/          # REST endpoints (@RestController)
│   ├── EmployeeController
│   └── BookingController
│
├── service/             # Business logic (@Service)
│   ├── EmployeeService
│   └── BookingService
│
├── domain/
│   ├── model/           # JPA entities (@Entity)
│   │   ├── Employee
│   │   ├── Booking
│   │   ├── Appointment
│   │   ├── ResourceType (enum)
│   │   └── BookingStatus (enum)
│   │
│   └── repository/      # Data access (extends JpaRepository)
│       ├── EmployeeRepository
│       ├── BookingRepository
│       └── AppointmentRepository
│
├── dto/                 # Request/Response DTOs
│   ├── CreateEmployeeRequest
│   ├── CreateBookingRequest
│   ├── EmployeeResponse
│   └── BookingResponse
│
├── mapper/              # Entity ↔ DTO conversion
│   ├── EmployeeMapper
│   └── BookingMapper
│
└── exception/           # Exception handling
    ├── ResourceNotFoundException
    └── GlobalExceptionHandler
```

---

## 🔌 REST Endpoints

### Employee Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/employees` | Register new employee |
| `GET` | `/employees/{employeeCode}` | Retrieve employee by code |

**Example Request:**
```bash
curl -X POST http://localhost:8080/employees \
  -H "Content-Type: application/json" \
  -d '{
    "employeeCode": "EMP9876",
    "name": "John Doe",
    "email": "john@example.com",
    "costCenter": "CC-456"
  }'
```

### Booking Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/bookings` | Create new booking |
| `GET` | `/bookings/{id}` | Get booking by ID |
| `GET` | `/bookings` | List all bookings |
| `GET` | `/bookings/employee/{employeeCode}` | Get bookings by employee |

**Example Request:**
```bash
curl -X POST http://localhost:8080/bookings \
  -H "Content-Type: application/json" \
  -d '{
    "employeeCode": "EMP9876",
    "resourceType": "FLIGHT",
    "destination": "NYC",
    "departureDate": "2024-11-05T08:00:00",
    "returnDate": "2024-11-08T18:00:00",
    "travelerCount": 1,
    "costCenterRef": "CC-456",
    "tripPurpose": "Client meeting - Acme Corp"
  }'
```

---

## 🛠️ Technology Stack

- **Language:** Java 21
- **Framework:** Spring Boot 3.x (4.0.2)
- **ORM:** Spring Data JPA with Hibernate
- **Database:** H2 (development), PostgreSQL (production)
- **Build:** Maven 3.8+
- **Container:** Docker
- **Testing:** JUnit 5, Mockito (13 tests)
- **Validation:** Jakarta Validation API
- **Logging:** SLF4J with Logback

---

## 🚀 Running Locally

### Prerequisites
- Java 21+ installed
- Maven 3.8+ installed
- Git for version control

### Quick Start

#### 1. Build the Application
```bash
cd /Users/georgianac/Desktop/Learning/workflow-service
mvn clean install -DskipTests
```

#### 2. Run the Application
```bash
# Option A: Using Maven
mvn spring-boot:run

# Option B: Using JAR directly (faster)
java -jar target/workflow-service-0.0.1-SNAPSHOT.jar
```

Application will start on **`http://localhost:8080`**

You should see:
```
Tomcat started on port(s): 8080 (http) with context path ''
```

#### 3. Test with curl
```bash
# Create employee
curl -X POST http://localhost:8080/employees \
  -H "Content-Type: application/json" \
  -d '{
    "employeeCode": "EMP001",
    "name": "John Doe",
    "email": "john@company.com",
    "costCenter": "CC-100"
  }'
```

### Testing with Postman

1. **Import Collection:** Use the endpoints listed in [REST Endpoints](#-rest-endpoints) section
2. **Base URL:** `http://localhost:8080`
3. **Headers:** `Content-Type: application/json`

**Sample Postman Collection:**
- `POST /employees` - Create employee
- `GET /employees/{employeeCode}` - Get employee
- `POST /bookings` - Create booking
- `GET /bookings` - List all bookings
- `GET /bookings/employee/{employeeCode}` - Get employee's bookings

---

## 🧪 Testing

### Unit & Integration Tests
```bash
mvn test
```

**Test Coverage:**
- **13 Total Tests** (all passing ✅)
- **4 Test Classes:**
  - `WorkflowServiceApplicationTests` (1 test) - Context loading
  - `BookingServiceTest` (3 tests) - Service logic validation
  - `EmployeeServiceTest` (5 tests) - Employee operations
  - `BookingControllerIntegrationTest` (4 tests) - Controller behavior

**Test Scenarios Covered:**
- ✅ Happy path: Creating bookings with valid data
- ✅ Validation: Date range validation (returnDate > departureDate)
- ✅ Exception handling: Employee not found
- ✅ Status codes: 201 Created, 404 Not Found, 400 Bad Request

### Test Results
```bash
[INFO] Tests run: 13, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

---

## 🔧 Troubleshooting

### Issue: 404 Not Found on POST /employees

**Cause:** Spring component scanning not finding controllers (usually happens with incorrect package structure)

**Solution:**
Ensure `WorkflowServiceApplication.java` is in the root package:
```
src/main/java/com/techquarter/workflow/WorkflowServiceApplication.java
```

All components must be under:
```
com.techquarter.workflow.*
```

**Verify:**
```bash
# Check logs when starting application
mvn spring-boot:run

# You should see:
# "Mapped GET /employees/..."
# "Mapped POST /bookings..."
```

### Issue: Port 8080 Already in Use

**Solution:**
```bash
# Find process using port 8080
lsof -i :8080

# Kill process (if needed)
kill -9 <PID>

# Or use different port
java -jar target/workflow-service-0.0.1-SNAPSHOT.jar --server.port=8081
```

### Issue: Tests Failing

**Solution:**
```bash
# Clean and rebuild
mvn clean test

# Run specific test
mvn test -Dtest=BookingServiceTest
```

---

## 🐳 Docker Deployment

### Build Image
```bash
mvn clean package
docker build -t workflow-service:latest .
```

### Run Container
```bash
docker run -p 8080:8080 workflow-service:latest
```

---

## 📦 Package Structure

```
com.techquarter.workflow
├── WorkflowServiceApplication.java    # Main entry point
├── controller/                        # REST endpoints
│   ├── EmployeeController
│   └── BookingController
├── service/                           # Business logic
│   ├── EmployeeService
│   └── BookingService
├── domain/
│   ├── model/                         # JPA entities + enums
│   │   ├── Employee
│   │   ├── Booking
│   │   ├── Appointment
│   │   ├── ResourceType (enum)
│   │   └── BookingStatus (enum)
│   └── repository/                    # JPA repositories
│       ├── EmployeeRepository
│       ├── BookingRepository
│       └── AppointmentRepository
├── dto/                               # Request/Response DTOs
│   ├── CreateEmployeeRequest
│   ├── CreateBookingRequest
│   ├── EmployeeResponse
│   └── BookingResponse
├── mapper/                            # Entity ↔ DTO mappers
│   ├── EmployeeMapper
│   └── BookingMapper
└── exception/                         # Exception handling
    ├── GlobalExceptionHandler
    └── ResourceNotFoundException
```

---

## 📊 Design Decisions & Trade-offs

### 1. **Separation of Concerns**
- **DTOs** separate API contracts from domain models
- **Mappers** handle conversion logic
- **Services** encapsulate business logic
- **Controllers** are thin, focusing only on HTTP concerns

**Why:** Ensures flexibility, testability, and ability to evolve API independently of domain.

### 2. **Stateless Service Design**
- No session state in service layer
- Each request is independent
- Enables horizontal scaling

**Why:** Critical for supporting 100 TPS across multiple instances.

### 3. **JPA Entities with Relationships**
- `@ManyToOne` between Booking → Employee
- `@ManyToOne` between Appointment → Booking
- Lazy loading to avoid N+1 queries

**Why:** Allows natural data modeling while maintaining performance.

### 4. **Validation at Multiple Layers**
- **DTO level:** `@Valid` annotations (email, required fields)
- **Service level:** Business rules (returnDate > departureDate)
- **Database level:** Unique constraints (employeeCode)

**Why:** Defense in depth, fail fast, clear error messages.

### 5. **Exception Handling**
- `GlobalExceptionHandler` for centralized error responses
- Custom `ResourceNotFoundException`
- Consistent error format across API

**Why:** Better client experience, easier debugging, professional API.

---

## 📈 Scaling to 100 TPS

Current implementation supports 100 TPS through:

### 1. **Stateless Service Architecture**
```
Load Balancer
├── Service Instance 1
├── Service Instance 2
├── Service Instance n
└── Shared Database
```

### 2. **Connection Pooling**
- HikariCP (default in Spring Boot) configured for optimal pool size
- Connection reuse reduces latency

### 3. **Database Optimization**
- Indexes on `Employee.employeeCode` (unique constraint)
- Indexes on `Booking.employeeId` (foreign key)
- Proper database configuration (connection pool, query optimization)

### 4. **Future Enhancements for Enterprise Scale**

#### Caching Layer (Redis)
```java
@Cacheable("employees")
public EmployeeResponse getEmployee(String employeeCode) { ... }
```

#### Async Processing (Event-Driven)
```java
@Async
public void processBookingAsync(Booking booking) { ... }
```

#### API Rate Limiting
```java
@RateLimiter(name = "bookingApi")
@PostMapping
public ResponseEntity<?> createBooking(...) { ... }
```

#### Database Read Replicas
- Write operations → Primary DB
- Read operations → Read replicas
- Reduces load on primary instance

#### Load Testing & Monitoring
```bash
# Apache JMeter simulation
jmeter -Dserver.ramp-up=10 -Dserver.threads=100
```

---

## ☁️ AWS Deployment Architecture

### Recommended Production Setup

```
┌────────────────────────────────────────────────────────────┐
│                    Route 53 (DNS)                          │
└────────────────┬───────────────────────────────────────────┘
                 │
┌────────────────┴───────────────────────────────────────────┐
│                   CloudFront (CDN)                         │
└────────────────┬───────────────────────────────────────────┘
                 │
┌────────────────┴───────────────────────────────────────────┐
│              Application Load Balancer (ALB)               │
└────────────────┬───────────────────────────────────────────┘
                 │
    ┌────────────┼────────────┐
    │            │            │
┌───▼──┐    ┌────▼───┐  ┌────▼───┐
│ ECS  │    │ ECS    │  │ ECS    │  (Fargate, auto-scaling)
│ Task │    │ Task   │  │ Task   │
└───┬──┘    └────┬───┘  └────┬───┘
    │            │            │
    └────────────┼────────────┘
                 │
        ┌────────▼────────┐
        │  RDS PostgreSQL  │
        │  (Multi-AZ)      │
        └──────────────────┘
```

### Infrastructure as Code (Terraform Example)

```hcl
# AWS ECS Fargate Cluster
resource "aws_ecs_cluster" "workflow_service" {
  name = "workflow-service-cluster"
}

# ECS Task Definition
resource "aws_ecs_task_definition" "workflow_service" {
  family = "workflow-service"
  container_definitions = jsonencode([{
    name      = "workflow-service"
    image     = "account.dkr.ecr.region.amazonaws.com/workflow-service:latest"
    memory    = 512
    cpu       = 256
    portMappings = [{
      containerPort = 8080
      hostPort      = 8080
      protocol      = "tcp"
    }]
  }])
}

# Auto Scaling Group
resource "aws_appautoscaling_target" "ecs_target" {
  max_capacity       = 10
  min_capacity       = 3
  resource_id        = "service/workflow-service-cluster/workflow-service"
  service_namespace  = "ecs"
}

# RDS PostgreSQL Database
resource "aws_db_instance" "postgres" {
  allocated_storage    = 100
  engine              = "postgres"
  engine_version      = "15.3"
  instance_class      = "db.t3.medium"
  multi_az            = true
  backup_retention_period = 30
}

# CloudWatch Alarms
resource "aws_cloudwatch_metric_alarm" "cpu_utilization" {
  alarm_name          = "workflow-service-high-cpu"
  comparison_operator = "GreaterThanThreshold"
  evaluation_periods  = "2"
  metric_name         = "CPUUtilization"
  namespace           = "AWS/ECS"
  period              = "300"
  statistic           = "Average"
  threshold           = "80"
}
```

### CI/CD Pipeline (GitHub Actions)

```yaml
name: Build and Deploy

on: [push]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Build with Maven
        run: mvn clean install
      
      - name: Run Tests
        run: mvn test
      
      - name: Build Docker Image
        run: docker build -t workflow-service:latest .
      
      - name: Push to ECR
        run: aws ecr get-login-password | docker login ...
      
      - name: Deploy to ECS
        run: aws ecs update-service ...
```

---

## 🧪 Testing Strategy

### Current Implementation
- **Unit Tests:** BookingService validation scenarios (3 tests)
  - ✅ Successful booking creation
  - ✅ Invalid date validation
  - ✅ Employee not found handling

### Enterprise Extension
```java
// Integration tests with TestContainers
@Container
static PostgreSQLContainer<?> postgres = 
  new PostgreSQLContainer<>("postgres:15");

// Contract testing (Spring Cloud Contract)
@SpringBootTest
class BookingControllerContractTest { ... }

// End-to-end testing (Cypress for React Native)
describe("Booking Flow", () => {
  it("should create booking and list it", () => { ... });
});

// Load testing (JMeter / Gatling)
// Verify 100+ TPS capacity
```

### Code Coverage Goals
- **Unit tests:** 70%+ of service layer
- **Integration tests:** 40%+ of API layer
- **E2E tests:** Critical workflows only

---

## 🔒 Security Considerations

### Current Implementation
- ✅ Input validation (Jakarta Validation)
- ✅ SQL injection prevention (JPA parameterized queries)
- ✅ Unique constraints on sensitive fields (employeeCode)

### Production Hardening
```java
// JWT Authentication
@Configuration
class SecurityConfig extends WebSecurityConfigurerAdapter {
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
      .antMatchers("/bookings/**").authenticated()
      .and().oauth2Login();
  }
}

// Rate Limiting
@Configuration
class RateLimitConfig {
  @Bean
  public RateLimiter bookingRateLimiter() {
    return RateLimiter.create(100.0); // 100 requests/second
  }
}

// CORS & HTTPS
@Configuration
public class CorsConfig implements WebMvcConfigurer {
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/**")
      .allowedOrigins("https://app.techquarter.com")
      .allowedMethods("GET", "POST", "PUT", "DELETE");
  }
}
```

---

## 📱 React Native Frontend Integration

### API Client Layer
```javascript
// api/client.js
import axios from 'axios';

const API_BASE = 'https://api.techquarter.com';

export const createBooking = async (bookingData) => {
  try {
    const response = await axios.post(`${API_BASE}/bookings`, bookingData);
    return response.data;
  } catch (error) {
    throw error.response.data;
  }
};

export const getBookings = async (employeeCode) => {
  const response = await axios.get(
    `${API_BASE}/bookings/employee/${employeeCode}`
  );
  return response.data;
};
```

### Business Logic Layer
```javascript
// services/BookingService.js
import * as BookingAPI from '../api/BookingAPI';

export const BookingService = {
  async createBooking(employeeCode, flightDetails) {
    // Validation
    if (!employeeCode || !flightDetails) {
      throw new Error('Invalid booking data');
    }
    
    // API Call
    return BookingAPI.createBooking({
      employeeCode,
      ...flightDetails
    });
  },
  
  async getUpcomingBookings(employeeCode) {
    return BookingAPI.getBookings(employeeCode);
  }
};
```

### UI Components
```javascript
// screens/BookingListScreen.js
import React, { useEffect, useState } from 'react';
import { FlatList, Text, View } from 'react-native';
import { BookingService } from '../services/BookingService';

export const BookingListScreen = ({ employeeCode }) => {
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    loadBookings();
  }, []);

  const loadBookings = async () => {
    setLoading(true);
    try {
      const data = await BookingService.getUpcomingBookings(employeeCode);
      setBookings(data);
    } catch (error) {
      console.error('Failed to load bookings', error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <View>
      <FlatList
        data={bookings}
        renderItem={({ item }) => <BookingCard booking={item} />}
        keyExtractor={(item) => item.id.toString()}
      />
    </View>
  );
};
```

---

## 🤖 AI Tooling Strategy (500-word outline)

### Backend Code Generation & Enhancement

**1. DTO and Entity Generation**
- AI can auto-generate boilerplate DTOs from request/response examples
- Generates Lombok annotations, getters/setters, builders
- Reduces manual typing by ~40%, human review ensures quality

**2. Service Layer Scaffolding**
- AI generates CRUD service templates from entity definitions
- Provides method signatures with validation logic
- Engineer validates business logic, adjusts rules, adds edge cases
- Example: `ChatGPT: "Generate BookingService with validation for date ranges"` → outputs skeleton → manually enhance with cost center validation

**3. Test Case Generation**
- AI creates unit test templates with @Mock annotations
- Generates happy-path and error scenarios (null checks, exceptions)
- Saves ~50% of test boilerplate, engineer adds domain-specific cases
- Example: Happy path, validation failures, resource not found

**4. Security Review & Pattern Suggestions**
- AI reviews code for SQL injection risks, proper use of parameterized queries
- Suggests improvements: "Use @Valid instead of manual validation"
- Recommends Design Patterns: "Consider Strategy pattern for ResourceType processing"

**5. API Documentation**
- AI generates OpenAPI/Swagger specs from controller annotations
- Creates endpoint documentation automatically
- Engineer reviews for accuracy and completeness

### Infrastructure as Code (IaC) Acceleration

**1. Terraform Template Generation**
- AI generates complete AWS infrastructure from architecture diagram
- Produces: VPC, Subnets, ECS Cluster, RDS, ALB, Security Groups
- Human validates IAM policies, security groups, cost optimization
- Time saved: 4+ hours of manual Terraform writing

**2. CI/CD Pipeline Scaffolding**
- AI generates GitHub Actions workflow for build, test, deploy
- Includes test gates, Docker push, ECS deployment steps
- Engineer customizes for specific AWS account, repos, branching strategy

**3. Monitoring & Logging Configuration**
- AI generates CloudWatch alarms, dashboards, log groups
- Suggests metrics: CPU, Memory, Request Latency, Error Rates
- Human refines thresholds based on SLA requirements

### React Native UI Development

**1. Form Component Generation**
- AI generates booking form with validation rules matching DTOs
- Produces TextInput, DatePicker, Dropdown components
- Engineer integrates with state management, adjusts styling
- Time saved: 30 min per screen

**2. API Client Layer**
- AI generates Axios client with all endpoints from OpenAPI spec
- Includes request/response interceptors
- Error handling templates
- Human adds auth tokens, retry logic, caching

**3. Test Case Generation**
- AI generates Jest tests for components
- Mock API responses, user interactions
- Coverage suggestions
- Human adds complex interaction scenarios

**4. UI/UX Enhancements**
- AI suggests accessibility improvements (WCAG compliance)
- Proposes layout optimizations for different screen sizes
- Human validates design consistency

### Enterprise Considerations

**Prompt Engineering Strategy:**
- Specific, domain-aware prompts yield best results
- "Generate Spring Boot service for flight booking with validation" → better results
- Always review AI output for correctness, security, performance
- Use AI for acceleration, human expertise for architectural decisions

**Quality Assurance:**
- 100% code review on AI-generated code
- Mandatory testing (unit + integration)
- Security scanning (SAST/DAST)
- Performance profiling

**ROI Calculation:**
- Estimated time savings: 30-40% of total development time
- Remaining time invested in: requirements clarification, testing, optimization
- Quality maintained through human review and validation

**Key Takeaway:** AI is a force multiplier for velocity, not a replacement for engineering judgment. Best results come from strategic use of AI for boilerplate/repetitive work, while humans focus on architecture, security, and business logic.

---

## 🔄 Development Workflow

This project follows the team's standard GitHub-based development process:

1. **Feature Development**: Work on feature branch, organized by package
2. **Code Quality**: 
   - Proper package structure under `com.techquarter.workflow`
   - Separation of concerns (Controller → Service → Repository)
3. **Testing**: 
   - Unit tests for service layer
   - Integration tests for controller behavior
   - All tests must pass before merge (13/13 tests)
4. **Code Review**: Minimum 2 approvals before merge
5. **CI/CD Pipeline**: 
   - Automated tests and build validation
   - Docker image creation
   - Auto-deployed to AWS Dev environment
6. **Staging Review**: Team validation in staging environment
7. **Production Release**: Coordinated release by Release team

The **stateless design** of this service enables **multiple weekly deployments** without downtime.

---

## 📚 Future Enhancements

1. **Event-Driven Architecture**
   - SNS/SQS for async booking notifications
   - Event sourcing for audit trail

2. **Distributed Tracing**
   - AWS X-Ray integration
   - Request correlation IDs across services

3. **Advanced Caching**
   - Redis for employee lookups
   - Cache invalidation strategy

4. **Machine Learning**
   - Booking recommendations based on patterns
   - Cost optimization suggestions

5. **Mobile App Features**
   - Push notifications for booking confirmations
   - Calendar integration (Google Calendar, Outlook)
   - Real-time flight tracking

---

## 📞 Support & Questions

For questions or issues:
1. Check README sections above
2. Review code comments in `/src` directory
3. See unit tests in `/src/test` for usage examples
4. Consult Spring Boot docs: https://spring.io/projects/spring-boot

---

## 📄 License

Internal Use Only - TechQuarter Corporation
