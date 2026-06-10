# Docker Setup for TaskManager

This guide explains how to containerize and run your TaskManager application using Docker and Docker Compose.

## Prerequisites

- Docker (v20.10+)
- Docker Compose (v2.0+)

Install from: https://docs.docker.com/get-docker/

---

## ⚠️ Important Before You Start

Docker uses the same ports as your local setup. Before running Docker:

- **Stop local MySQL** — both use port 3306 and will conflict
- **Stop IntelliJ app** — both use port 8080 and will conflict

```bash
# Stop local MySQL (Windows PowerShell as Admin)
    Stop-Service -Name "MySQL80"
```

---

## Quick Start

### 1. Build and run with Docker Compose

```bash
    docker-compose up --build
```

This will:
- Build the Spring Boot application image
- Start MySQL 8.0 container with database `taskmanager_db`
- Start the TaskManager app container
- Wire them together on a shared network

> **First build takes 3-5 minutes** — Maven downloads all dependencies inside the container.
> Subsequent builds are much faster due to Docker layer caching.

### 2. Wait for startup

The app is ready when you see both of these in the logs:

```
taskmanager-mysql  | ready for connections
taskmanager-app    | Started TaskManagerApplication in X seconds
```

### 3. Access the application

- **Web UI**: http://localhost:8080/auth/loginForm
- **Register**: http://localhost:8080/registerUserForm
- **Swagger/OpenAPI**: http://localhost:8080/swagger-ui.html
- **API**: http://localhost:8080/api/v1/...
- **Health check**: http://localhost:8080/actuator/health

### 4. Demo Credentials and Auto-Seeded Data

The app comes with pre-loaded **demo users and tasks** that seed automatically on first startup.

#### Demo Users (use these to log in immediately):

| Email | Password | Role |
|-------|----------|------|
| admin@example.com | adminpass | ADMIN |
| manager@example.com | managerpass | MANAGER |
| member@example.com | memberpass | MEMBER |

#### Demo Tasks (visible after login):
1. **Set up project repository** (TODO, HIGH) — assigned to Admin
2. **Implement authentication** (IN_PROGRESS, MEDIUM) — assigned to Manager
3. **Create sample tasks** (TODO, LOW) — assigned to Member

#### Login Guide:
1. Open http://localhost:8080/auth/loginForm
2. Enter email: `admin@example.com`
3. Enter password: `adminpass`
4. Click "Login"
5. You'll see the demo tasks and user list

#### Add New Users:
You can also register new users anytime:
```
http://localhost:8080/registerUserForm
```

### 5. Data Persistence

**Important:** Docker persists data across container restarts using the `mysql_data` volume.

- **Demo users/tasks persist** — they're created only once if missing
- **Your custom users/tasks persist** — database survives `docker-compose down`
- **To wipe and reseed demo data:**
  ```bash
  docker-compose down
  docker volume rm Aufgaben-Verwaltungssystem_mysql_data
  docker-compose up --build
  ```

The DataInitializer component (profile: `docker`) checks if demo records exist:
- If email doesn't exist → creates demo user
- If task title doesn't exist → creates demo task
- User-created data is never deleted by the initializer

### 6. Stop containers

```bash
    docker-compose down
```

To also remove the database volume (wipes all data):

```bash
    docker-compose down -v
```

---

## Switching Between Docker and Local Development

| Mode | Steps |
|---|---|
| **Run locally (IntelliJ)** | `docker-compose down` → start MySQL80 service → run from IntelliJ |
| **Run with Docker** | Stop IntelliJ app → `Stop-Service MySQL80` → `docker-compose up` |

**Rule: Never run both at the same time — port conflicts will occur.**

---

## Developer Workflow

```
Daily coding       → IntelliJ (fast, hot reload)
Before pushing     → test with Docker first
Push to GitHub     → only after Docker works
```

After any code change, rebuild Docker image:

```bash
    docker-compose down
    docker-compose up --build
```

---

## Environment Variables (docker-compose.yml)

Customize in `docker-compose.yml`:

```yaml
environment:
  SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/taskmanager_db
  SPRING_DATASOURCE_USERNAME: taskmanager
  SPRING_DATASOURCE_PASSWORD: taskmanager123
  SPRING_JPA_HIBERNATE_DDL_AUTO: update  # or validate, create, create-drop
  SPRING_PROFILES_ACTIVE: docker
```

> **Note:** Environment variables in docker-compose.yml override `application.properties` completely.

---

## Dockerfile Details

- **Base image**: `eclipse-temurin:21-jre-jammy` (Java 21 runtime, optimized)
- **Build**: Multi-stage build (compile with Maven, run lean JAR)
- **Health check**: Spring Boot Actuator `/actuator/health` endpoint
- **Port**: 8080 (default Spring Boot)

---

## Troubleshooting

**Container exits immediately:**
```bash
    docker-compose logs app
```

**MySQL connection refused:**
- Wait 30-60 seconds for MySQL to fully initialize
- Check `docker-compose logs mysql`
- Make sure local MySQL is stopped (port 3306 conflict)

**Port already in use (3306 or 8080):**
```bash
# Stop local MySQL
Stop-Service -Name "MySQL80"

# Or change ports in docker-compose.yml:
ports:
  - "8081:8080"  # Host:Container
  - "3307:3306"  # Host:Container
```

**ERR_TOO_MANY_REDIRECTS in browser:**
- Clear browser cookies or use Incognito window
- Make sure `/auth/loginForm` is in `permitAll()` in SecurityConfig

**Login fails after registration:**
- Docker database is empty — you must register a new user inside Docker
- Local users don't exist in Docker's MySQL

**Rebuild after code changes:**
```bash
    docker-compose down
    docker-compose up --build
```

---

## Clean Rebuild (fix corrupted state)

```bash
    docker-compose down -v
    docker-compose up --build
```

---

## Production Notes

For production deployment:
- Move secrets to a `.env` file (never commit passwords to GitHub)
- Change all default passwords in `docker-compose.yml`
- Use `SPRING_JPA_HIBERNATE_DDL_AUTO: validate` (not `update`)
- Use `SPRING_PROFILES_ACTIVE: prod`
- Add resource limits: `cpus`, `memory`
- Consider Kubernetes instead of Docker Compose for scaling
