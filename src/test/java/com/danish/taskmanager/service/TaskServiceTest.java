package com.danish.taskmanager.service;

import com.danish.taskmanager.dto.TaskRequestDTO;
import com.danish.taskmanager.dto.TaskResponseDTO;
import com.danish.taskmanager.entity.Task;
import com.danish.taskmanager.entity.User;
import com.danish.taskmanager.mapper.TaskMapper;
import com.danish.taskmanager.repository.TaskRepository;
import com.danish.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Tell JUnit to use Mockito
class TaskServiceTest {

    // Create fake/dummy repositories
    @Mock
    TaskRepository taskRepository;

    @Mock
    TaskMapper taskMapper;

    @Mock
    UserRepository userRepository;

    //Create REAL TaskService, inject the mocks above into it
    @InjectMocks
    TaskService taskService;


    @Test
    void shouldReturnAllTasks() {

        // Set values( adding task and adding title to dto) for testing
        Task task = new Task();
        task.setTitle("Fix bug");

        TaskResponseDTO dto = new TaskResponseDTO();
        dto.setTitle("Fix bug");

        // When taskRepository.findAll() is called return this fake list
        when(taskRepository.findAll()).thenReturn(List.of(task));

        // When taskMapper.toDTO(task) is called return this fake dto
        when(taskMapper.toDTO(task)).thenReturn(dto);

        // Calling required testing functions
        List<TaskResponseDTO> result = taskService.getAllTask();

        assertEquals(1, result.size());
        assertEquals("Fix bug", result.get(0).getTitle());
    }


    @Test
    void shouldUpdateTask_whenIdIsProvided() {

        TaskRequestDTO dto = new TaskRequestDTO();
        dto.setId(5L);                        // NOT null = update
        dto.setTitle("Updated Title");
        dto.setDescription("Updated Desc");
        dto.setStatus("IN_PROGRESS");
        dto.setPriority("HIGH");
        dto.setAssignedUserId(1L);

        User user = new User();
        user.setId(1L);

        Task existingTask = new Task();      // task already in DB
        existingTask.setId(5L);
        existingTask.setTitle("Old Title");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(taskRepository.findById(5L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(existingTask)).thenReturn(existingTask);

        Task result = taskService.save(dto);

        assertEquals("Updated Title", result.getTitle());
        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    void shouldReturnEmptyList_whenNoTasksExist() {

        when(taskRepository.findAll()).thenReturn(List.of()); // empty list
        List<TaskResponseDTO> result = taskService.getAllTask();
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldCreateNewTask_whenIdIsNull() {

        TaskRequestDTO dto = new TaskRequestDTO();
        dto.setId(null);              // null = create new
        dto.setTitle("New Task");
        dto.setAssignedUserId(1L);

        User user = new User();
        user.setId(1L);

        Task entity = new Task();
        entity.setTitle("New Task");

        Task savedTask = new Task();
        savedTask.setId(10L);          // DB assigned ID after save
        savedTask.setTitle("New Task");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(taskMapper.toEntity(dto)).thenReturn(entity);
        when(taskRepository.save(entity)).thenReturn(savedTask);


        Task result = taskService.save(dto);


        assertNotNull(result);
        assertEquals(10, result.getId());
        assertEquals("New Task", result.getTitle());

        // Verify save was actually called once
        verify(taskRepository, times(1)).save(entity);
    }


    @Test
    void shouldThrowException_whenUpdatingNonExistentTask() {

        TaskRequestDTO dto = new TaskRequestDTO();
        dto.setId(99L);               // ID that doesn't exist
        dto.setAssignedUserId(1L);

        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(taskRepository.findById(99L)).thenReturn(Optional.empty()); // not found!

        assertThrows(RuntimeException.class, () -> taskService.save(dto));
    }
    @Test
    void shouldDeleteTask() {

        taskService.deleteTask(1L);

        // verify deleteById was called with correct ID
        verify(taskRepository, times(1)).deleteById(1L);
    }


}