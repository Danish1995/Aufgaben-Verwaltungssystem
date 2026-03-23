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


    /* When a client requests to get all users, a request is sent to the controller endpoint. The controller handles this
    request and calls the corresponding service method. The service layer then interacts with the repository layer by
    invoking the findAll() method to fetch all user records from the database.

    The repository returns a list of entity objects to the service layer. In the service, these entities are not returned
    directly. Instead, each entity is converted into a UserResponseDTO, and all DTOs are collected into a list. This step
    is important for data security and abstraction, as it prevents exposing internal database structures or sensitive
    fields to higher layers.

    The service then returns the list of DTOs to the controller. The controller adds this list to the Model as an attribute
    and returns the name of a Thymeleaf template (HTML page).

    Finally, in the Thymeleaf view, the list of DTOs is accessed using the model attribute. The template iterates over
        this list and displays the required fields using DTO properties, rendering the data to the client in a
    structured HTML format.*/

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
    public String deleteUser(@PathVariable int userID) {
        userService.deleteUser(userID);
        return "redirect:/users";
    }

//    @GetMapping("users/delete/{userID}")
//    public String deleteUser(@PathVariable int userID) {
//        userService.deleteUser(userID);
//        return "redirect:/users";
//    }

    @PutMapping("/users/{userID}")
    public UserResponseDTO updateUser(@PathVariable int userID, @RequestBody UserRequestDTO dto) {

        return userService.updateUser(userID, dto);
    }


}