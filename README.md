# Aufgaben-Verwaltungssystem (Task Management System)

A full-stack task management application inspired by Jira, built with Spring Boot. The system allows users to register, authenticate, manage tasks, assign work to team members, and track task progress through a secure role-based environment.

## Features

### Authentication & Security

* User registration and login
* JWT-based authentication
* Spring Security integration
* Role-based authorization (Admin/Manager/User)
* Protected API endpoints

### User Management

* Full CRUD operations for users
* User profile management
* User search and filtering
* Pagination support

### Task Management

* Full CRUD operations for tasks
* Assign tasks to users
* Task status management
* Priority management
* View assigned tasks
* Task filtering by:

   * Status
   * Priority
   * Assigned User
* Pagination and sorting

### Validation & Error Handling

* Request validation using Bean Validation
* Global exception handling
* Consistent API error responses

### Testing

* Unit testing with JUnit 5
* Mocking with Mockito
* Controller testing with MockMvc
* Service layer testing

### Frontend

* Thymeleaf-based user interface
* User dashboard and profile views

## Technology Stack

* Java 17
* Spring Boot
* Spring Security (JWT)
* Spring Data JPA
* Hibernate
* MySQL
* Maven
* Thymeleaf
* JUnit 5
* Mockito
* MockMvc

## Project Architecture

The application follows a layered architecture:

* Controller Layer
* Service Layer
* Repository Layer
* Security Layer
* Validation Layer

## API Endpoints

### Authentication

| Method | Endpoint           | Description                 |
| ------ | ------------------ | --------------------------- |
| POST   | /api/auth/register | Register a new user         |
| POST   | /api/auth/login    | Login and receive JWT token |

### Tasks

| Method | Endpoint        | Description   |
| ------ | --------------- | ------------- |
| GET    | /api/tasks      | Get all tasks |
| POST   | /api/tasks      | Create task   |
| PUT    | /api/tasks/{id} | Update task   |
| DELETE | /api/tasks/{id} | Delete task   |

### Users

| Method | Endpoint        | Description |
| ------ | --------------- | ----------- |
| GET    | /api/users      | Get users   |
| POST   | /api/users      | Create user |
| PUT    | /api/users/{id} | Update user |
| DELETE | /api/users/{id} | Delete user |

## Running Locally

1. Clone the repository

git clone https://github.com/Danish1995/Aufgaben-Verwaltungssystem.git

2. Configure database credentials in:

src/main/resources/application.properties

3. Run the application

./mvnw spring-boot:run

4. Access the application through your browser or API client.

## Future Improvements

* Docker support
* CI/CD pipeline with GitHub Actions
* Swagger/OpenAPI documentation
* Email notifications
* Audit logging
* Deployment to AWS
