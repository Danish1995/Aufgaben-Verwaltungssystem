package com.danish.taskmanager.controller;

import com.danish.taskmanager.dto.UserRequestDTO;
import com.danish.taskmanager.dto.UserResponseDTO;
import com.danish.taskmanager.entity.User;
import com.danish.taskmanager.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

//    @GetMapping("/users")
//    public List<UserResponseDTO> getAllUsers() {
//        return userService.findAll();
//    }

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        List<UserResponseDTO> allUsers = userService.findAll();
        model.addAttribute("users", allUsers);
        return "list-users";
    }

    @GetMapping("/registerUserForm")
    public String addEmployees(Model model) {
        UserRequestDTO newUser = new UserRequestDTO();
        model.addAttribute("adduser", newUser);
        return "user-form";
    }

    @GetMapping("/users/{userID}")
    public User getSingleUser(@PathVariable int userID) {
        return userService.findUser(userID);
    }


    @PostMapping("/users")
    public String addUser(UserRequestDTO dto) {
        // Spring automatically creates a UserRequestDTO object and fills it field by field:

        userService.addUser(dto);
        return "redirect:/users";
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