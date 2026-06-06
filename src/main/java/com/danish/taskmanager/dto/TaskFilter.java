package com.danish.taskmanager.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskFilter {
    private String status;
    private String priority;
    private Long assignedUserId;
    private String keyword;   // searches title + description
}