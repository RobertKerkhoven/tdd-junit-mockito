package org.linkedin.taskmanager.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.linkedin.taskmanager.exception.TaskNotFoundException;
import org.linkedin.taskmanager.model.Task;
import org.linkedin.taskmanager.repository.TaskRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//    }

    @Test
    void testGetAllTasks() {
        //arrange
        List<Task> tasks = Arrays.asList(
                new Task("Task 1", "To do"),
                new Task("Task 2","In progress")
        );
        when(taskRepository.findAll()).thenReturn(tasks);
        // act
        List<Task> retrievedTasks = taskService.getAllTasks();
        // assert
        assertNotNull(retrievedTasks);
        assertEquals(2, retrievedTasks.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testCreateTask() {
        //arrange
        Task task = new Task("Test task", "To do");
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        //act
        Task createdTask = taskService.createTask(task);
        //assert
        assertNotNull(createdTask);
        assertEquals("Test task",createdTask.getTitle());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void getTaskById() {
        //arrange
        Task task = new Task(1L,"Task 1", "To do");
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        //act
        Task retrievedTask = taskService.getTaskById(1L);
        //assert
        assertNotNull(retrievedTask);
        assertEquals("Task 1", retrievedTask.getTitle());
        assertEquals("To do", retrievedTask.getStatus());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTaskById_TaskNotFound() {
        // arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());
        // act & assert
        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(1L));
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateTaskStatus() {
        // arrange
        Task task = new Task(1L,"Existing task","To do");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // act
        Task updatedTask = taskService.updateTaskStatus(1L,"In progress");

        // assert
        assertNotNull(updatedTask);
        assertEquals("In progress", updatedTask.getStatus());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testDeleteTask() {
        //arrange
        Task existingTask = new Task(1L,"Existing task","To do");
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        //act
        taskService.deleteTask(1L);

        //assert
        verify(taskRepository).delete(existingTask);
        verify(taskRepository).findById(1L);

    }

    @Test
    void testDeleteTask_TaskNotFound() {
        //arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());
        // act & assert
        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(1L));
        verify(taskRepository, times(1)).findById(1L);
    }

}
