package com.danish.taskmanager.service;

import com.danish.taskmanager.dto.TaskFilter;
import com.danish.taskmanager.dto.TaskRequestDTO;
import com.danish.taskmanager.dto.TaskResponseDTO;
import com.danish.taskmanager.entity.Task;
import com.danish.taskmanager.entity.User;
import com.danish.taskmanager.exception.AppException;
import com.danish.taskmanager.mapper.TaskMapper;
import com.danish.taskmanager.repository.TaskRepository;
import com.danish.taskmanager.repository.UserRepository;
import com.danish.taskmanager.specification.TaskSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {


    TaskRepository taskRepository;
    TaskMapper taskMapper;
    UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.userRepository = userRepository;
    }

    public Page<TaskResponseDTO> getFilteredTasks(TaskFilter filter, Pageable pageable) {
        Specification<Task> spec = TaskSpecification.withFilters(filter);
        return taskRepository.findAll(spec, pageable)
                .map(taskMapper::toDTO);
    }

    public List<TaskResponseDTO> getAllTask() {
        return taskRepository.findAll().stream()
                .map(taskMapper::toDTO)
                .collect(Collectors.toList());
    }


    public void deleteTask(Long taskID) {
        taskRepository.deleteById(taskID);
    }

    public Task save(TaskRequestDTO dto) {

        User byId = userRepository.findById(dto.getAssignedUserId())
                .orElseThrow(() -> new AppException("User not found", "USER_NOT_FOUND", 404));
        if (dto.getId() != null) {
            // Update task conditions

            Task task = taskRepository.findById(dto.getId())
                    .orElseThrow(() -> new AppException("Task not found", "TASK_NOT_FOUND", 404));

            task.setId(dto.getId());
            task.setTitle(dto.getTitle());
            task.setDescription(dto.getDescription());
            task.setStatus(Task.Status.valueOf(dto.getStatus()));
            task.setPriority(Task.Priority.valueOf(dto.getPriority()));
            task.setDueDate(dto.getDueDate());
            task.setAssignedUser(byId);

            return taskRepository.save(task);

        } else {

            Task entity = taskMapper.toEntity(dto);
            entity.setAssignedUser(byId);
            return taskRepository.save(entity);
        }

    }


    public TaskRequestDTO getTaskForEdit(Long id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new AppException("Task not found: " + id, "TASK_NOT_FOUND", 404));
        return taskMapper.toRequestDTO(task);
    }
}
