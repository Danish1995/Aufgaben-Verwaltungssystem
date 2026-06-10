package com.danish.taskmanager.config;

import com.danish.taskmanager.entity.Task;
import com.danish.taskmanager.entity.User;
import com.danish.taskmanager.repository.TaskRepository;
import com.danish.taskmanager.repository.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Initializes sample data when the application runs with the 'docker' profile.
 * This ensures a containerized demo always has users and tasks available.
 * Idempotent: creates demo records only if they don't already exist by email/title.
 */
@Component
@Profile("docker")
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           TaskRepository taskRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Insert 8 demo users if they don't exist (idempotent)
        createUserIfNotExists("admin@example.com", "Admin User", "adminpass", User.Role.ADMIN);
        createUserIfNotExists("sarah@example.com", "Sarah Johnson", "sarahpass", User.Role.ADMIN);
        createUserIfNotExists("manager@example.com", "Manager User", "managerpass", User.Role.MANAGER);
        createUserIfNotExists("john@example.com", "John Smith", "johnpass", User.Role.MANAGER);
        createUserIfNotExists("michael@example.com", "Michael Brown", "michaelpass", User.Role.MANAGER);
        createUserIfNotExists("member@example.com", "Member User", "memberpass", User.Role.MEMBER);
        createUserIfNotExists("alice@example.com", "Alice Williams", "alicepass", User.Role.MEMBER);
        createUserIfNotExists("bob@example.com", "Bob Davis", "bobpass", User.Role.MEMBER);

        // Insert 10 demo tasks if they don't exist by title
        createTaskIfNotExists("Set up project repository", "Initialize Git, CI and basic project structure", 
                Task.Status.TODO, Task.Priority.HIGH, 7, "admin@example.com");
        createTaskIfNotExists("Implement authentication", "Add form login and role-based access control", 
                Task.Status.IN_PROGRESS, Task.Priority.MEDIUM, 5, "manager@example.com");
        createTaskIfNotExists("Create sample tasks", "Add sample tasks so UI shows data on first run", 
                Task.Status.TODO, Task.Priority.LOW, 10, "member@example.com");
        createTaskIfNotExists("Design database schema", "Create tables for users, tasks, and audit logs", 
                Task.Status.DONE, Task.Priority.HIGH, 3, "sarah@example.com");
        createTaskIfNotExists("Write unit tests", "Ensure 80% code coverage for service layer", 
                Task.Status.IN_PROGRESS, Task.Priority.MEDIUM, 8, "john@example.com");
        createTaskIfNotExists("Deploy to staging", "Push code to staging environment for QA testing", 
                Task.Status.TODO, Task.Priority.HIGH, 4, "michael@example.com");
        createTaskIfNotExists("Document API endpoints", "Create OpenAPI/Swagger documentation", 
                Task.Status.IN_PROGRESS, Task.Priority.LOW, 6, "alice@example.com");
        createTaskIfNotExists("Fix bug in task filter", "Tasks not filtering by status correctly", 
                Task.Status.IN_PROGRESS, Task.Priority.MEDIUM, 2, "bob@example.com");
        createTaskIfNotExists("Optimize database queries", "Add indexes and reduce N+1 queries", 
                Task.Status.TODO, Task.Priority.MEDIUM, 12, "admin@example.com");
        createTaskIfNotExists("Setup CI/CD pipeline", "Configure GitHub Actions for automated builds", 
                Task.Status.DONE, Task.Priority.HIGH, 1, "sarah@example.com");
    }

    private void createUserIfNotExists(String email, String name, String password, User.Role role) {
        if (userRepository.findByEmail(email).isEmpty()) {
            User user = User.builder()
                    .name(name)
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .role(role)
                    .build();
            userRepository.save(user);
        }
    }

    private void createTaskIfNotExists(String title, String description, Task.Status status, 
                                       Task.Priority priority, int dueDaysFromNow, String assignedUserEmail) {
        if (!taskRepository.existsByTitle(title)) {
            User assignedUser = userRepository.findByEmail(assignedUserEmail).orElse(null);
            Task task = Task.builder()
                    .title(title)
                    .description(description)
                    .status(status)
                    .priority(priority)
                    .dueDate(LocalDateTime.now().plusDays(dueDaysFromNow))
                    .assignedUser(assignedUser)
                    .build();
            taskRepository.save(task);
        }
    }
}


