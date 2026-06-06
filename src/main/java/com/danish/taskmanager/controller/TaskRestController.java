package com.danish.taskmanager.controller;

import com.danish.taskmanager.dto.TaskFilter;
import com.danish.taskmanager.dto.TaskRequestDTO;
import com.danish.taskmanager.dto.TaskResponseDTO;
import com.danish.taskmanager.service.TaskService;
import com.danish.taskmanager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@Tag(name = "Tasks", description = "Task endpoints")
public class TaskRestController {

    private final TaskService taskService;

    public TaskRestController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @Operation(summary = "Get all tasks")
    public ResponseEntity<Page<TaskResponseDTO>> getAll(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        TaskFilter filter = new TaskFilter();
        filter.setStatus(status);
        filter.setPriority(priority);
        return ResponseEntity.ok(
                taskService.getFilteredTasks(filter,
                        PageRequest.of(page, size, Sort.by("createdAt").descending()))
        );
    }

    @PostMapping
    @Operation(summary = "Create a task")
    public ResponseEntity<Void> create(@Valid @RequestBody TaskRequestDTO dto) {
        taskService.save(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}