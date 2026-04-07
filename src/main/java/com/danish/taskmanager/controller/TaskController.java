package com.danish.taskmanager.controller;

import com.danish.taskmanager.dto.TaskResponseDTO;
import com.danish.taskmanager.repository.TaskRepository;
import com.danish.taskmanager.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
        model.addAttribute("tasks", allTask);
        return "task/list-tasks";
    }

    @GetMapping("/tasks/add")
    public String showAddForm(Model model) {
        model.addAttribute("task", new TaskResponseDTO());
        return "task/task-form";
    }

    @DeleteMapping("/tasks/delete/{id}")
    public String deletetask(@PathVariable int id) {
        taskService.deleteTask(id);

        return "redirect:/tasks";
    }
}