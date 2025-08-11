package org.linkedin.taskmanager.repository;

import org.junit.jupiter.api.Test;
import org.linkedin.taskmanager.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class TaskRepositoryTest {

    @Autowired
    public TaskRepository taskRepository;

    @Test
    void TestSaveTask(){
        //arrange    given
        Task task = new Task("TestTask", "To do");
        //act   when
        Task savedTask = taskRepository.save(task);
        //assert   then
        assertNotNull(savedTask);
        assertEquals("TestTask", task.getTitle());
    }

    @Test
    void testDeleteTask(){
        //arrange
        Task task = new Task("Task to delete", "Done");
        taskRepository.save(task);
        //act
        taskRepository.delete(task);
        Optional<Task> deletedTask = taskRepository.findById(task.getId());
        //assert
        assertFalse(deletedTask.isPresent());
    }

    @Test
    void TestFindTaskById(){
        //arrange  given
        Task task = new Task("TestTask", "To do");
        taskRepository.save(task);
        //act   when
        Optional<Task> optionalTask = taskRepository.findById(task.getId());
        //assert   then
        assertFalse(optionalTask.isEmpty());
        assertEquals(task.getId(), optionalTask.get().getId());
    }

    @Test
    void testUpdateTaskStatus(){
        //arrange  given
        Task task = new Task("TestTask", "To do");
        taskRepository.save(task);
        //act   when
        task.setStatus("Done");
        taskRepository.save(task);
        Optional<Task> optionalTask = taskRepository.findById(task.getId());
        //assert   then
        assertFalse(optionalTask.isEmpty());
        assertEquals("Done", optionalTask.get().getStatus());
    }


    @Test
    void testFindAllTasks() {
        // arrange
        Task task1 = new Task("Task 1", "To do");
        taskRepository.save(task1);
        Task task2 = new Task("Task 2", "Done");
        taskRepository.save(task2);
        // act
        List<Task> tasks = taskRepository.findAll();
        // assert
        assertEquals(2, tasks.size());

    }
}
