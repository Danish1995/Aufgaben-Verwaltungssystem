package com.danish.taskmanager.service;

import com.danish.taskmanager.dto.TaskResponseDTO;
import com.danish.taskmanager.entity.Task;
import com.danish.taskmanager.mapper.TaskMapper;
import com.danish.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
           listAllTask.add(taskMapper.toEntity(task));
        }

        return listAllTask;
    }
    public void deleteTask(int taskID){
        taskRepository.deleteById(taskID);
    }
}
