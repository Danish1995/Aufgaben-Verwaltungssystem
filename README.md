# Aufgaben-Verwaltungssystem (Task Management System)

A RESTful backend API built with Spring Boot — a mini Jira-like task management system
where users can register, log in, and manage tasks.

## Tech Stack
- Java 17
- Spring Boot
- Spring Security (JWT)
- Spring Data JPA
- Hibernate
- MySQL / PostgreSQL
- Maven

## Features
- User registration and login with JWT authentication
- Create, update, delete, and view tasks
- Role-based access control
- Global exception handling

## How to Run Locally
1. Clone the repository
   git clone https://github.com/Danish1995/Aufgaben-Verwaltungssystem.git
2. Configure your database in src/main/resources/application.properties
3. Run the app via IntelliJ or:
   ./mvnw spring-boot:run

## API Endpoints
| Method | Endpoint            | Description          |
|--------|---------------------|----------------------|
| POST   | /api/auth/register  | Register a new user  |
| POST   | /api/auth/login     | Login and get token  |
| GET    | /api/tasks          | Get all tasks        |
| POST   | /api/tasks          | Create a task        |
| PUT    | /api/tasks/{id}     | Update a task        |
| DELETE | /api/tasks/{id}     | Delete a task        |