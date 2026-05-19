package com.danish.taskmanager.controller;

import com.danish.taskmanager.config.SecurityConfig;
import com.danish.taskmanager.dto.TaskRequestDTO;
import com.danish.taskmanager.dto.TaskResponseDTO;
import com.danish.taskmanager.dto.UserResponseDTO;
import com.danish.taskmanager.entity.User;
import com.danish.taskmanager.service.TaskService;
import com.danish.taskmanager.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
class TaskControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TaskService taskService;   // fake TaskService

    @MockBean
    UserService userService;   // fake UserService

    // ─────────────────────────────────────────────
    // TEST 1 — GET /tasks/all-tasks
    // ─────────────────────────────────────────────
    @Test
    void shouldLoadAllTasksPage() throws Exception {

        // ARRANGE — fake data service would return
        TaskResponseDTO dto = new TaskResponseDTO();
        dto.setTitle("Fix Bug");

        when(taskService.getAllTask()).thenReturn(List.of(dto));

        // ACT + ASSERT
        mockMvc.perform(get("/tasks/all-tasks"))
                .andExpect(status().isOk())                        // HTTP 200
                .andExpect(view().name("task/list-tasks"))         // correct view returned
                .andExpect(model().attributeExists("tasks"))       // model has "tasks"
                .andExpect(model().attribute("tasks", List.of(dto))); // correct data
    }


}