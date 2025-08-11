package org.linkedin.taskmanager.repository;

import org.junit.jupiter.api.Test;
import org.linkedin.taskmanager.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class TaskRepositoryTest {

    @Autowired
    public TaskRepository taskRepository;

    @Test
    void TestSaveTask(){
        //arrange    given
        Task task = new Task();
        task.setTitle("TestTask");
        task.setStatus("To do");

        //act   when

        Task savedTask = taskRepository.save(task);

        //assert   then
        assertNotNull(savedTask);
        assertEquals("TestTask", task.getTitle());

    }

    @Test
    void testDeleteTask(){
        //arrange
        Task task = new Task();
        task.setTitle("Task to delete");
        task.setStatus("Done");
        taskRepository.save(task);
        //act
        taskRepository.delete(task);
        Optional<Task> deletedTask = taskRepository.findById(task.getId());
        //assert
        assertFalse(deletedTask.isPresent());
    }
}
