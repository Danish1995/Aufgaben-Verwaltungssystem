package com.danish.taskmanager.mapper;


import com.danish.taskmanager.dto.TaskResponseDTO;
import com.danish.taskmanager.entity.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {


    public TaskResponseDTO toEntity(Task task) {

        TaskResponseDTO dto = new TaskResponseDTO();
        dto.setId(task.getId());
        dto.setDescription(task.getDescription());
        dto.setCreatedAt(task.getCreatedAt());
        dto.setPriority(task.getPriority().name());
        dto.setTitle(task.getTitle());
        dto.setStatus(task.getStatus().name());
        dto.setDueDate(task.getDueDate());
        dto.setUpdatedAt(task.getUpdatedAt());
        dto.setAssignedUser(task.getAssignedUser());


        return dto;

    }
}
