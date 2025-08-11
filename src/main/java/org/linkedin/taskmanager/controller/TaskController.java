package org.linkedin.taskmanager.controller;

import jakarta.validation.Valid;
import org.linkedin.taskmanager.model.Task;
import org.linkedin.taskmanager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @PostMapping
    public Task createTask(@Valid Task taskData) {
        return taskService.createTask(taskData);
    }
}
