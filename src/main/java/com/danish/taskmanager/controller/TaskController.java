package com.danish.taskmanager.controller;

import com.danish.taskmanager.entity.User;
import com.danish.taskmanager.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TaskController {

    UserRepository userRepository;

    public TaskController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/tasks")
    public List<User> users() {
        return userRepository.findAll();
    }
}