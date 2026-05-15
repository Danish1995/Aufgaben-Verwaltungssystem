package com.danish.taskmanager.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
@Getter
@Setter
public class UserRequestDTO {

    // to avoid null id error used wrapper class because for create it need to send null id
    private Integer id;

    //Validations (DTO → define rules HTML (Thymeleaf) → bind + show errors Controller → trigger validation)

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Role is required")
    private String role;

    @NotBlank(message = "Password is required")
    private String password;

}