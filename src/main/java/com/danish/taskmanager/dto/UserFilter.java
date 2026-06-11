package com.danish.taskmanager.dto;

import lombok.Getter;
import lombok.Setter;


    @Getter
    @Setter
    public class UserFilter {
        private String name;
        private String email;
        private String Role;
        private String excludeEmail; // Exclude a specific email from results (e.g., current user)

    }