package org.linkedin.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.linkedin.taskmanager.exception.TaskNotFoundException;
import org.linkedin.taskmanager.model.Task;
import org.linkedin.taskmanager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @MockBean
    private TaskService taskService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void testGetAllTasks() throws Exception {
        //arrange
        List<Task> tasks = Arrays.asList(new Task(1L, "Task 1", "To do"), new Task(2L, "Task 2", "In progress"));

        when(taskService.getAllTasks()).thenReturn(tasks);

        //act & assert
        mockMvc.perform(get("/tasks").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[1].title").value("Task 2"));
    }

    @Test
    void testCreateTask() throws Exception {
        //arrange
        Task task = new Task(1L, "Controller test task", "To do");

        when(taskService.createTask(any(Task.class))).thenReturn(task);

        //act & assert
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Controller test task"));
    }

    @Test
    void testCreateTask_InvalidTitle() throws Exception {
        //arrange
        Task task = new Task(1L, "", "To do");

        // act & assert
        mockMvc.perform(post("/tasks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetTaskById_TaskNotFound() throws Exception {
        //arrange
        when(taskService.getTaskById(1L)).thenThrow(new TaskNotFoundException("Task Not Found"));
        //act & assert
        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isNotFound());
        verify(taskService, times(1)).getTaskById(1L);
    }

    @Test
    void testGetTaskById() throws Exception {
        Task task = new Task(1L, "Task 1", "To do");

        when(taskService.getTaskById(1L)).thenReturn(task);

        mockMvc.perform(get("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Task 1"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testUpdateTask() throws Exception {
        Task updatedTask = new Task(1L, "Updated task", "In progress");
        when(taskService.updateTask(eq(1L), any(Task.class))).thenReturn(updatedTask);

        String taskJson = objectMapper.writeValueAsString(updatedTask);

        //act & assert
        mockMvc.perform(put("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated task"))
                .andExpect(jsonPath("$.id").value(1));

        verify(taskService).updateTask(eq(1L), any(Task.class));
    }

    @Test
    void testDeleteTask() throws Exception {
        //arrange
        doNothing().when(taskService).deleteTask(eq(1L));
        //act & assert
        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent());
        verify(taskService).deleteTask(eq(1L));
    }

}