package com.MyBackEnd.services;

import com.MyBackEnd.models.Task;
import com.MyBackEnd.models.TaskAssignment;
import com.MyBackEnd.repository.TaskAssignmentRepository;
import com.MyBackEnd.repository.TaskRepository;
import com.MyBackEnd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TaskAssignmentService {
    private final TaskAssignmentRepository taskAssignmentRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    @Autowired
    public TaskAssignmentService(TaskAssignmentRepository taskAssignmentRepository, UserRepository userRepository, TaskRepository taskRepository) {
        this.taskAssignmentRepository = taskAssignmentRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }
    public List<TaskAssignment> getAllContributorsForTask(Integer taskId) {
        if (taskId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task ID cannot be null");
        }
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        return taskAssignmentRepository.findByTask(task);
    }






}
