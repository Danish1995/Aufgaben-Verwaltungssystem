# Aufgaben-Verwaltungssystem — Task Management System

A full-stack task management application inspired by Jira, built with Spring Boot 3 and Thymeleaf. Users can register, log in, manage tasks, assign work to team members, and track progress through a secure, role-based web interface.

---

## Screenshots

> Screenshots of dashboard, task list, user management page, and Swagger UI will be added here.

---

## Features

### Authentication & Security

* Session-based authentication using Spring Security
* Login and logout functionality
* BCrypt password hashing
* Role-based access control (`ADMIN`, `MANAGER`, `MEMBER`)
* Protected routes with automatic redirection to login for unauthenticated users

### User Management

* Create, view, update, and delete users
* User profile page for authenticated users
* Email uniqueness validation
* Password encoding during registration
* Role assignment and management

### Task Management

* Create, view, update, and delete tasks
* Assign tasks to team members
* Task status tracking (`TODO`, `IN_PROGRESS`, `DONE`)
* Priority levels (`LOW`, `MEDIUM`, `HIGH`)
* Dynamic filtering by status, priority, assigned user, and keyword
* Pagination and sorting support

### Validation & Error Handling

* Jakarta Bean Validation (`@NotBlank`, `@Email`, `@NotNull`)
* Global exception handling using `@RestControllerAdvice`
* Inline validation messages in Thymeleaf forms

### REST API & Documentation

* REST API endpoints for users and tasks
* Interactive Swagger UI documentation
* OpenAPI 3.0 integration via SpringDoc

### Docker & Containerization

* Multi-stage Docker build using Java 21 (Eclipse Temurin)
* Docker Compose setup for application and MySQL database
* Automated container networking and service orchestration
* Persistent MySQL storage using Docker volumes
* Environment-based configuration via Docker Compose variables
* Spring Boot Actuator health checks for container monitoring
* Pre-seeded demo users and tasks for Docker profile
* Isolated and reproducible development environment
### Testing

* Controller tests using MockMvc
* Service layer unit tests with Mockito and JUnit 5
* Isolated testing using `@MockBean`

---

## Technology Stack

| Layer       | Technology                 |
| ----------- | -------------------------- |
| Language    | Java 21                    |
| Framework   | Spring Boot 3.3.5          |
| Security    | Spring Security            |
| Persistence | Spring Data JPA, Hibernate |
| Database    | MySQL 8                    |
| Frontend    | Thymeleaf, Bootstrap 5     |
| API Docs    | SpringDoc OpenAPI 3.0      |
| Validation  | Jakarta Bean Validation    |
| Testing     | JUnit 5, Mockito, MockMvc  |
| Build Tool  | Maven                      |

---

## Project Architecture

The application follows a layered architecture:

```text
Controller Layer   → Handles HTTP requests
Service Layer      → Business logic
Repository Layer   → Data access using Spring Data JPA
DTO Layer          → Request/Response contracts
Mapper Layer       → Entity ↔ DTO conversion
Exception Layer    → Global exception handling
Security Layer     → Authentication & authorization
```

---

## Web Application Endpoints (Thymeleaf)

### Authentication

| Method | Endpoint                    | Description               |
| ------ | --------------------------- | ------------------------- |
| GET    | `/auth/loginForm`           | Display login page        |
| GET    | `/auth/registerNewUserForm` | Display registration page |
| POST   | `/auth/register`            | Register a new user       |

---

### User Management

| Method | Endpoint             | Description                    |
| ------ | -------------------- | ------------------------------ |
| GET    | `/users`             | Display all users              |
| GET    | `/registerUserForm`  | Display create-user form       |
| GET    | `/users/{id}`        | Display edit-user form         |
| POST   | `/users`             | Create or update a user        |
| DELETE | `/users/delete/{id}` | Delete a user                  |
| GET    | `/userProfile`       | Display logged-in user profile |

---

### Task Management

| Method | Endpoint          | Description                                     |
| ------ | ----------------- | ----------------------------------------------- |
| GET    | `/api/tasks`      | Display all tasks with filtering and pagination |
| GET    | `/api/add`        | Display create-task form                        |
| GET    | `/api/tasks/{id}` | Display edit-task form                          |
| POST   | `/api/tasks`      | Create or update a task                         |
| DELETE | `/api/tasks/{id}` | Delete a task                                   |

#### Task Filter Parameters

| Parameter        | Type    | Description                    |
| ---------------- | ------- | ------------------------------ |
| `status`         | String  | Filter by task status          |
| `priority`       | String  | Filter by task priority        |
| `assignedUserId` | Long    | Filter by assigned user        |
| `keyword`        | String  | Search by title or description |
| `page`           | Integer | Page number (default: 0)       |
| `size`           | Integer | Page size (default: 5)         |

---

## REST API Endpoints

### Task APIs

| Method | Endpoint             | Description    |
| ------ | -------------------- | -------------- |
| GET    | `/api/v1/tasks`      | Get all tasks  |
| GET    | `/api/v1/tasks/{id}` | Get task by ID |
| POST   | `/api/v1/tasks`      | Create a task  |
| PUT    | `/api/v1/tasks/{id}` | Update a task  |
| DELETE | `/api/v1/tasks/{id}` | Delete a task  |

### User APIs

| Method | Endpoint             | Description    |
| ------ | -------------------- | -------------- |
| GET    | `/api/v1/users`      | Get all users  |
| GET    | `/api/v1/users/{id}` | Get user by ID |
| POST   | `/api/v1/users`      | Create a user  |
| PUT    | `/api/v1/users/{id}` | Update a user  |
| DELETE | `/api/v1/users/{id}` | Delete a user  |

---

## API Documentation

Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI JSON:

```text
http://localhost:8080/v3/api-docs
```

---

## Running Locally

### Prerequisites

* Java 21
* Maven
* MySQL 8

### Clone the Repository

```bash
git clone https://github.com/Danish1995/Aufgaben-Verwaltungssystem.git
cd Aufgaben-Verwaltungssystem
```

### Create Database

```sql
CREATE DATABASE taskmanager_db;
```

### Configure Application Properties

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/taskmanager_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Run the Application

```bash
./mvnw spring-boot:run
```

### Open in Browser

```text
http://localhost:8080/auth/loginForm
```

---

## Running Tests

```bash
./mvnw test
```

---

## Project Structure

```text
src/main/java/com/danish/taskmanager/
├── config/
├── controller/
├── dto/
├── entity/
├── exception/
├── mapper/
├── repository/
├── service/
└── specification/
```

### Package Overview

| Package       | Description                        |
| ------------- | ---------------------------------- |
| config        | Security and OpenAPI configuration |
| controller    | MVC and REST controllers           |
| dto           | Request and response DTOs          |
| entity        | JPA entities                       |
| exception     | Global exception handling          |
| mapper        | DTO mapping logic                  |
| repository    | Spring Data JPA repositories       |
| service       | Business logic layer               |
| specification | Dynamic filtering specifications   |

---

## Future Improvements

* Docker and Docker Compose support
* GitHub Actions CI/CD pipeline
* Task analytics dashboard
* Project management module
* Email notifications using Spring Mail
* Event-driven notifications with Kafka
* Microservices architecture
* Kubernetes deployment

---

## Author

**Danish**

GitHub: https://github.com/Danish1995
