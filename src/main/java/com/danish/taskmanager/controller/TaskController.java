    package com.danish.taskmanager.controller;

    import com.danish.taskmanager.dto.TaskFilter;
    import com.danish.taskmanager.dto.TaskRequestDTO;
    import com.danish.taskmanager.dto.TaskResponseDTO;
    import com.danish.taskmanager.service.TaskService;
    import com.danish.taskmanager.service.UserService;
    import jakarta.validation.Valid;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.PageRequest;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.domain.Sort;
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

//        @GetMapping("/all-tasks")
//        public String tasks(Model model) {
//            List<TaskResponseDTO> allTask = taskService.getAllTask();
//            model.addAttribute("tasks", allTask);
//            return "task/list-tasks";
//        }

        @GetMapping("/all-tasks")
        public String tasks(
                @RequestParam(required = false) String status,
                @RequestParam(required = false) String priority,
                @RequestParam(required = false) Long assignedUserId,
                @RequestParam(required = false) String keyword,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "5") int size,
                Model model) {

            TaskFilter filter = new TaskFilter();
            filter.setStatus(status);
            filter.setPriority(priority);
            filter.setAssignedUserId(assignedUserId);
            filter.setKeyword(keyword);

            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<TaskResponseDTO> taskPage = taskService.getFilteredTasks(filter, pageable);

            model.addAttribute("tasks", taskPage.getContent());
            model.addAttribute("taskPage", taskPage);
            model.addAttribute("users", userService.findAll());

            // Pass filters back so form stays filled
            model.addAttribute("currentStatus", status);
            model.addAttribute("currentPriority", priority);
            model.addAttribute("currentUser", assignedUserId);
            model.addAttribute("currentKeyword", keyword);
            model.addAttribute("currentSize", size);

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