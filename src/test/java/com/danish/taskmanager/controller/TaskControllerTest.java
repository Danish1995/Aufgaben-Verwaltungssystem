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

    // TEST 2 — GET /tasks/add (show empty form)

    @Test
    void shouldLoadAddTaskForm() throws Exception {

        mockMvc.perform(get("/tasks/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("task/task-form"))   // correct form view
                .andExpect(model().attributeExists("task"));        // empty dto added to model
    }

    // TEST 3 — POST /tasks/save (valid data)

    @Test
    void shouldSaveTask_andRedirect_whenValidInput() throws Exception {

        mockMvc.perform(post("/tasks/save")
                        .param("title", "Fix Bug")         // simulates form fields
                        .param("status", "PENDING")
                        .param("priority", "HIGH")
                        .param("assignedUserId", "1"))
                .andExpect(status().is3xxRedirection())            // redirect happened
                .andExpect(redirectedUrl("/tasks/all-tasks"));     // redirected to correct URL

        verify(taskService, times(1)).save(any());             // save was called
    }

    // TEST 4 — POST /tasks/save (INVALID data → validation)

    @Test
    void shouldReturnForm_whenValidationFails() throws Exception {

        mockMvc.perform(post("/tasks/save")
                        .param("title", ""))               // blank title = validation error
                .andExpect(status().isOk())
                .andExpect(view().name("task/task-form")); // stays on form, no redirect

        verify(taskService, never()).save(any());      // save was NEVER called
    }

    // TEST 5 — GET /tasks/edit/{id}

    @Test
    void shouldLoadEditForm_withTaskData() throws Exception {

        // ARRANGE
        TaskRequestDTO existingTask = new TaskRequestDTO();
        existingTask.setId(1L);
        existingTask.setTitle("Old Title");

        UserResponseDTO user = new UserResponseDTO();

        user.setId(1L);

        when(taskService.taskUpdateValue(1L)).thenReturn(existingTask);
        when(userService.findAll()).thenReturn(List.of(user));

        // ACT + ASSERT
        mockMvc.perform(get("/tasks/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("task/task-form"))
                .andExpect(model().attribute("task", existingTask)) // correct task loaded
                .andExpect(model().attributeExists("users"));       // users list in model
    }


    // TEST 6 — DELETE /tasks/delete/{id}

    @Test
    void shouldDeleteTask_andRedirect() throws Exception {

        mockMvc.perform(delete("/tasks/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks/all-tasks"));

        verify(taskService, times(1)).deleteTask(1L); // deleteTask called with correct ID
    }

}