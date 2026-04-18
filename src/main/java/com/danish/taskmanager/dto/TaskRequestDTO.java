package com.danish.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskRequestDTO {
    private Integer id;
    @NotBlank(message = "title is required")
    private String title;
    @NotBlank(message = "description is required")
    private String description;
    @NotBlank(message = "status is required")
    private String status;
    @NotBlank(message = "    private String priority;\n is required")
    private String priority;
    // user not null for non string values
    @NotNull(message = "Due date is required")
    private LocalDateTime dueDate;
    //    @NotBlank(message = "Name is required")
    private Long assignedUserId;

}
