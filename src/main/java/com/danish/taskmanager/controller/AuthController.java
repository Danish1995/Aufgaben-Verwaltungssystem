package com.danish.taskmanager.controller;


import com.danish.taskmanager.dto.LoginDTO;
import com.danish.taskmanager.dto.UserRequestDTO;
import com.danish.taskmanager.entity.User;
import com.danish.taskmanager.repository.UserRepository;
import com.danish.taskmanager.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

@Controller
@RequestMapping("/auth")
public class AuthController {

    UserRepository userRepository;
    UserService userService;

    public AuthController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/loginForm")
    public String addUser(Model model) {
        UserRequestDTO newUser = new UserRequestDTO();
        model.addAttribute("loginUser", newUser);
        return "login-user";
    }

    @PostMapping("/register")
    public String register(UserRequestDTO dto) {
        userService.registerUser(dto);
        return "redirect:/users";
    }

    @GetMapping("/registerNewUserForm")
    public String registerNewUserForm(Model model) {
        UserRequestDTO newUser = new UserRequestDTO();
        model.addAttribute("registerUser", newUser);
        return "register-user";
    }

    @GetMapping("/login")
    public String login(LoginDTO dto) {
        User byEmail = userRepository.findByEmail(dto.getEmail()); // call service layer here
        if (Objects.equals(byEmail.getPassword(), dto.getPassword())) {
            return "redirect:/users";// later → JWT token
        } else {
            throw new RuntimeException("Invalid password");
        }
    }

}
