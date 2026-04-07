package com.danish.taskmanager.controller;

import com.danish.taskmanager.dto.TaskResponseDTO;
import com.danish.taskmanager.entity.Task;
import com.danish.taskmanager.entity.User;
import com.danish.taskmanager.repository.TaskRepository;
import com.danish.taskmanager.repository.UserRepository;

import com.danish.taskmanager.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
public class TaskController {

    TaskRepository taskRepository;
    TaskService taskService;

    public TaskController(TaskRepository taskRepository, TaskService taskService) {
        this.taskRepository = taskRepository;
        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    public String tasks(Model model) {
        List<TaskResponseDTO> allTask = taskService.getAllTask();
        model.addAttribute("tasks",allTask);
        return "task/list-tasks";
    }

    @DeleteMapping("/deletetask")
    public String deletetask(){

        return "redirect/tasks";
    }
}