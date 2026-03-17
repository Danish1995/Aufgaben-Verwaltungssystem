package com.danish.taskmanager.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskResponseDTO {

    private Long id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private LocalDateTime dueDate;
    private Long assignedUserId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}