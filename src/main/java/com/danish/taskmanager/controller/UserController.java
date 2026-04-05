package com.danish.taskmanager.controller;

import com.danish.taskmanager.dto.UserRequestDTO;
import com.danish.taskmanager.dto.UserResponseDTO;
import com.danish.taskmanager.entity.User;
import com.danish.taskmanager.repository.UserRepository;
import com.danish.taskmanager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {

    UserService userService;
    UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

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
    public String addUser(Model model) {
        UserRequestDTO newUser = new UserRequestDTO();
        model.addAttribute("adduser", newUser);
        return "user-form";
    }

    @GetMapping("/users/{userID}")
    public String getSingleUser(@PathVariable int userID, Model model) {
        UserResponseDTO userResponseDTO = userService.findUser(userID);
        model.addAttribute("adduser", userResponseDTO);
        return "user-form";
    }


    @PostMapping("/users")
    public String addUser(@Valid @ModelAttribute("adduser") UserRequestDTO dto,
                          BindingResult result) {
        // @Valid automatically bind validation result to result

        // Check validation first
        if (dto.getId() == null) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                result.rejectValue("email", "error.email", "Email already exists");
            }
        } else {
            System.out.println("else--");
            User existingUser = userRepository.findById(dto.getId()).orElseThrow();

            // only check if email changed
            if (!existingUser.getEmail().equals(dto.getEmail()) &&
                    userRepository.existsByEmail(dto.getEmail())) {
                result.rejectValue("email", "error.email", "Email already exists");
            }
        }

        // Operations
        if (result.hasErrors()) {
            System.out.println("i am in if");
            return "user-form";
        }
        // Operation after all validation
        if (dto.getId() == null) {
            userService.addUser(dto);

        } else {
            userService.updateUser(dto.getId(), dto);
        }

        return "redirect:/users";
    }

    @DeleteMapping("users/delete/{userID}")
    public String deleteUser(@PathVariable int userID) {
        userService.deleteUser(userID);
        return "redirect:/users";
    }

    @PutMapping("/users/update/{userID}")
    public UserResponseDTO updateUser(@PathVariable int userID, @RequestBody UserRequestDTO dto) {
        return userService.updateUser(userID, dto);
    }

    /* User profile controller*/

    @GetMapping("/userProfile")
    public String userProfile(Model model) {
        // Fetch username from Spring Security
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserResponseDTO byEmail = userService.findBYEmail(authentication.getName());
        model.addAttribute("user", byEmail);

        return "user-profile";
    }

}