package com.danish.taskmanager.repository;

import com.danish.taskmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //Spring Data JPA automatically creates query from method name existsByEmail → checks if email exists in DB
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
}
