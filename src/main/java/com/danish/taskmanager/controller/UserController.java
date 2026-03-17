package com.danish.taskmanager.controller;

import com.danish.taskmanager.dto.UserRequestDTO;
import com.danish.taskmanager.entity.User;
import com.danish.taskmanager.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/users/{userId}")
    public User getSingleUser(@PathVariable int userId) {
        return userService.findUser(userId);
    }


    @PostMapping("/users")
    public User addUser(@RequestBody UserRequestDTO dto) {

        return userService.addUser(dto);
    }


}