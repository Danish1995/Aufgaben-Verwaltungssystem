package com.danish.taskmanager.service;

import com.danish.taskmanager.dto.TaskFilter;
import com.danish.taskmanager.dto.TaskRequestDTO;
import com.danish.taskmanager.dto.TaskResponseDTO;
import com.danish.taskmanager.entity.Task;
import com.danish.taskmanager.entity.User;
import com.danish.taskmanager.mapper.TaskMapper;
import com.danish.taskmanager.repository.TaskRepository;
import com.danish.taskmanager.repository.UserRepository;
import com.danish.taskmanager.specification.TaskSpecification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public List<TaskResponseDTO> getAllTask(){

        List<Task> all = taskRepository.findAll();
        List<TaskResponseDTO> listAllTask= new ArrayList<>();
        for(Task task : all)
        {
           listAllTask.add(taskMapper.toDTO(task));
        }

        return listAllTask;
    }

    public void deleteTask(Long taskID){
        taskRepository.deleteById(taskID);
    }

    public Task save(TaskRequestDTO dto) {

        User byId = userRepository.findById(dto.getAssignedUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        if (dto.getId() != null) {
            // Update task conditions

            Task task = taskRepository.findById(dto.getId()).orElseThrow(() -> new RuntimeException("Task not found"));

            task.setId(dto.getId());
            task.setTitle(dto.getTitle());
            task.setDescription(dto.getDescription());
            task.setStatus(Task.Status.valueOf(dto.getStatus()));
            task.setPriority(Task.Priority.valueOf(dto.getPriority()));
            task.setDueDate(dto.getDueDate());
            task.setAssignedUser(byId);

            return   taskRepository.save(task);

        } else {

            Task entity = taskMapper.toEntity(dto);
            entity.setAssignedUser(byId);
            return taskRepository.save(entity);
        }

    }


    public TaskRequestDTO taskUpdateValue(Long id){

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found: " + id));
        return taskMapper.toRequestDTO(task);
    }
}
