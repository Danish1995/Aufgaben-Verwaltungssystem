package com.danish.taskmanager.service;

import com.danish.taskmanager.dto.TaskRequestDTO;
import com.danish.taskmanager.dto.TaskResponseDTO;
import com.danish.taskmanager.entity.Task;
import com.danish.taskmanager.mapper.TaskMapper;
import com.danish.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {


    TaskRepository taskRepository;
    TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper){
        this.taskRepository=taskRepository;
        this.taskMapper= taskMapper;

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
    public void deleteTask(int taskID){
        taskRepository.deleteById(taskID);
    }

    public Task save(TaskRequestDTO dto) {


        if (dto.getId() != null) {

            Task task = taskRepository.findById(dto.getId()).orElseThrow(() -> new RuntimeException("Task not found"));

            task.setId(dto.getId());
            task.setTitle(dto.getTitle());
            task.setDescription(dto.getDescription());
            task.setStatus(Task.Status.valueOf(dto.getStatus()));
            task.setPriority(Task.Priority.valueOf(dto.getPriority()));
            task.setDueDate(dto.getDueDate());
            task.setCreatedAt(LocalDateTime.now());

            return   taskRepository.save(task);

        } else {

            Task entity = taskMapper.toEntity(dto);
            return taskRepository.save(entity);
        }

    }




    public TaskRequestDTO taskUpdateValue(int id){


        Optional<Task> byId = taskRepository.findById(id);

        return taskMapper.toRequestDTO(byId.get());
    }
}
