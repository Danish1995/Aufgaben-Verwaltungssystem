package com.danish.taskmanager.service;

import com.danish.taskmanager.dto.TaskResponseDTO;
import com.danish.taskmanager.entity.Task;
import com.danish.taskmanager.mapper.TaskMapper;
import com.danish.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Tell JUnit to use Mockito
class TaskServiceTest {

    // Create fake/dummy repositories
    @Mock
    TaskRepository taskRepository;

    @Mock
    TaskMapper taskMapper;

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

        // When taskRepository.findAll() is called → return this fake list
        when(taskRepository.findAll()).thenReturn(List.of(task));

        // When taskMapper.toDTO(task) is called → return this fake dto
        when(taskMapper.toDTO(task)).thenReturn(dto);

        // Calling required testing functions
        List<TaskResponseDTO> result = taskService.getAllTask();

        assertEquals(1, result.size());
        assertEquals("Fix bug", result.get(0).getTitle());
    }
}