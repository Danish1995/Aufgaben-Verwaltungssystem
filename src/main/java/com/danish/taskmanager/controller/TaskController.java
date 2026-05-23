    package com.danish.taskmanager.controller;

    import com.danish.taskmanager.dto.TaskRequestDTO;
    import com.danish.taskmanager.dto.TaskResponseDTO;
    import com.danish.taskmanager.service.TaskService;
    import com.danish.taskmanager.service.UserService;
    import jakarta.validation.Valid;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.validation.BindingResult;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @Controller
    @RequestMapping("/tasks")
    public class TaskController {


        TaskService taskService;

        UserService userService;


        public TaskController(TaskService taskService, UserService userService) {

            this.taskService = taskService;

            this.userService = userService;
        }

        @GetMapping("/all-tasks")
        public String tasks(Model model) {
            List<TaskResponseDTO> allTask = taskService.getAllTask();
            model.addAttribute("tasks", allTask);
            return "task/list-tasks";
        }

        @GetMapping("/add")
        public String showAddForm(Model model) {
            model.addAttribute("task", new TaskRequestDTO());
            return "task/task-form";
        }

        @DeleteMapping("/delete/{id}")
        public String deleteTask(@PathVariable Long id) {
            taskService.deleteTask(id);

            return "redirect:/tasks/all-tasks";
        }

        @PostMapping("/save")
        public String saveTask(@Valid @ModelAttribute("task") TaskRequestDTO dto, BindingResult result) {

            if (result.hasErrors()) {
                return "task/task-form";
            } else {
                taskService.save(dto);
                return "redirect:/tasks/all-tasks";
            }
        }

        // For opening an edit form from a link, use @GetMapping.
        @GetMapping("/edit/{taskID}")
        public String updateTask(@PathVariable("taskID") Long taskID, Model model) {

            TaskRequestDTO requestDTO = taskService.taskUpdateValue(taskID);
            model.addAttribute("task", requestDTO);
            model.addAttribute("users", userService.findAll());


            return "task/task-form";
        }
    }