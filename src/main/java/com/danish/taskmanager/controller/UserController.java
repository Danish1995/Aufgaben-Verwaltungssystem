package com.danish.taskmanager.controller;

import com.danish.taskmanager.dto.UserRequestDTO;
import com.danish.taskmanager.dto.UserResponseDTO;
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

    @GetMapping("/users/{userID}")
    public User getSingleUser(@PathVariable int userID) {
        return userService.findUser(userID);
    }


    @PostMapping("/users")
    public UserResponseDTO addUser(@RequestBody UserRequestDTO dto) {

        return userService.addUser(dto);
    }

    @DeleteMapping("users/{userID}")
    public User deleteUser(@PathVariable int userID) {

        return userService.deleteUser(userID);
    }

    @PutMapping("/users/{userID}")
    public UserResponseDTO updateUser(@PathVariable int userID, @RequestBody UserRequestDTO dto) {

        return userService.updateUser(userID, dto);
    }


}