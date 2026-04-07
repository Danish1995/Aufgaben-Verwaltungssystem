package com.danish.taskmanager.service;

import com.danish.taskmanager.entity.Task;
import com.danish.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {


    TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository){
        this.taskRepository=taskRepository;

    }
    public List<Task> allTask(){

        return null;
    }
}
