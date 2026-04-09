package com.danish.taskmanager.mapper;


import com.danish.taskmanager.dto.TaskRequestDTO;
import com.danish.taskmanager.dto.TaskResponseDTO;
import com.danish.taskmanager.entity.Task;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TaskMapper {


    public TaskResponseDTO toDTO(Task task) {

        TaskResponseDTO dto = new TaskResponseDTO();
        dto.setId(task.getId());
        dto.setDescription(task.getDescription());
        dto.setCreatedAt(task.getCreatedAt());

        if (task.getPriority() != null) {
            dto.setPriority(task.getPriority().name());
        }

        dto.setTitle(task.getTitle());

        if (task.getStatus() != null) {
            dto.setStatus(task.getStatus().name());
        }

        dto.setDueDate(task.getDueDate());
        dto.setUpdatedAt(task.getUpdatedAt());


        return dto;

    }

    public Task toEntity(TaskRequestDTO dto) {

        Task task = new Task();


        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(Task.Status.valueOf(dto.getStatus()));
        task.setPriority(Task.Priority.valueOf(dto.getPriority()));
        task.setDueDate(dto.getDueDate());
        task.setCreatedAt(LocalDateTime.now());

        // assigned user to be mapped


        return task;
    }
}
