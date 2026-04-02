package com.danish.taskmanager.repository;

import com.danish.taskmanager.dto.UserRequestDTO;
import com.danish.taskmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    //Spring Data JPA automatically creates query from method name existsByEmail → checks if email exists in DB
    boolean existsByEmail(String email);
    User findByEmail(String email);
}
