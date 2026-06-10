package com.danish.taskmanager.controller;

import com.danish.taskmanager.dto.*;
import com.danish.taskmanager.entity.User;
import com.danish.taskmanager.repository.UserRepository;
import com.danish.taskmanager.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
//
//    @GetMapping("/users")
//    public String getAllUsers(Model model) {
//        List<UserResponseDTO> allUsers = userService.findAll();
//        model.addAttribute("users", allUsers);
//        return "/user/list-users";
//    }
    @GetMapping("/users")
    public String getAllUsers( @RequestParam(required = false) String name,
                               @RequestParam(required = false) String email,
                               @RequestParam(required = false) String role,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size,
                               Model model ) {


        UserFilter filter = new UserFilter();
        filter.setName(name);
        filter.setEmail(email);
        filter.setRole(role);


        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<UserResponseDTO> userPage = userService.getFilteredUser(filter, pageable);

        model.addAttribute("users", userPage.getContent());
        model.addAttribute("userPage", userPage);

        // Pass filters back so form stays filled
        model.addAttribute("currentName", name);
        model.addAttribute("currentEmail", email);
        model.addAttribute("currentRole", role);

        model.addAttribute("currentSize", size);
        return "user/list-users";
    }

    @PostMapping("/users/register")
    public String registerUser(@ModelAttribute("adduser") UserRequestDTO dto) {
        userService.addUser(dto);
        return "redirect:/auth/loginForm";
    }



    @GetMapping("/registerUserForm")
    public String addUser(Model model) {
        UserRequestDTO newUser = new UserRequestDTO();
        model.addAttribute("adduser", newUser);
        return "user/user-form";
    }

    @GetMapping("/users/{userID}")
    public String getSingleUser(@PathVariable Long userID, Model model) {
        model.addAttribute("adduser", userService.findUserForForm(userID));
        return "user/user-form";
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
            User existingUser = userRepository.findById(dto.getId()).orElseThrow();

            // only check if email changed
            if (!existingUser.getEmail().equals(dto.getEmail()) &&
                    userRepository.existsByEmail(dto.getEmail())) {
                result.rejectValue("email", "error.email", "Email already exists");
            }
        }

        // Operations
        if (result.hasErrors()) {
            return "user/user-form";
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
    public String deleteUser(@PathVariable Long userID) {
        userService.deleteUser(userID);
        return "redirect:/users";
    }

    @PutMapping("/users/update/{userID}")
    public UserResponseDTO updateUser(@PathVariable Long userID, @RequestBody UserRequestDTO dto) {
        return userService.updateUser(userID, dto);
    }

    /* User profile controller*/

    @GetMapping("/userProfile")
    public String userProfile(Model model) {
        // Fetch username from Spring Security
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserResponseDTO byEmail = userService.findBYEmail(authentication.getName());
        model.addAttribute("user", byEmail);

        return "user/user-profile";
    }

}