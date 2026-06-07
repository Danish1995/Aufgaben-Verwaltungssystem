package com.danish.taskmanager.repository;

import com.danish.taskmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    //Spring Data JPA automatically creates query from method name existsByEmail → checks if email exists in DB
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);

//    findById() always returns Optional<User> → you can always use .orElseThrow() on it.
//    Optional and orElseThrow are partners — orElseThrow only works on Optional, nothing else.
//    int vs Long has absolutely nothing to do with Optional — it's a completely separate topic.
}
