package org.linkedin.taskmanager.service;

import org.linkedin.taskmanager.exception.TaskNotFoundException;
import org.linkedin.taskmanager.model.Task;
import org.linkedin.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task updateTaskStatus(long id, String status) {
        Task task = getTaskById(id);
        task.setStatus(status);
        return taskRepository.save(task);
    }

    public Task getTaskById(long id) {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task not found, id: "+id));
    }
}
