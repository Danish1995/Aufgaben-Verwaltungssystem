package com.danish.taskmanager.service;

import com.danish.taskmanager.entity.Task;
import com.danish.taskmanager.repository.TaskRepository;

import java.util.List;

public class TaskService {


    TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository){
        this.taskRepository=taskRepository;

    }
    public List<Task> allTask(){

        return null;
    }
}
