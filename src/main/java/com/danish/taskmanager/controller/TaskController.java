package com.danish.taskmanager.controller;

import com.danish.taskmanager.dto.TaskRequestDTO;
import com.danish.taskmanager.dto.TaskResponseDTO;
import com.danish.taskmanager.repository.TaskRepository;
import com.danish.taskmanager.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    TaskRepository taskRepository;
    TaskService taskService;

    public TaskController(TaskRepository taskRepository, TaskService taskService) {
        this.taskRepository = taskRepository;
        this.taskService = taskService;
    }

    @GetMapping("/all-tasks")
    public String tasks(Model model) {
        List<TaskResponseDTO> allTask = taskService.getAllTask();
        model.addAttribute("tasks", allTask);
        return "task/list-tasks";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("task", new TaskResponseDTO());
        return "task/task-form";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteTask(@PathVariable int id) {
        taskService.deleteTask(id);

        return "redirect:/tasks/all-tasks";
    }

    @PostMapping("/save")
    public String saveTask(TaskRequestDTO dto) {
        taskService.save(dto);

        return "redirect:/tasks/all-tasks";
    }
}