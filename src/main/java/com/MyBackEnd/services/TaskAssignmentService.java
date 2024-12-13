package com.MyBackEnd.services;

import com.MyBackEnd.models.*;
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
    public TaskAssignmentService(TaskAssignmentRepository taskAssignmentRepository, UserRepository userRepository,
            TaskRepository taskRepository) {
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

    public TaskAssignment createContributorById(Integer taskId, int userId) {
        if (taskId == null || userId == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task Id or User Id cannot be null");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        if (taskAssignmentRepository.findByUserAndTask(user, task) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user is already a contributor in this task");
        }

        TaskAssignment taskAssignment = new TaskAssignment();
        taskAssignment.setTask(task);
        taskAssignment.setUser(user);
        taskAssignment.setRole(TaskRolesEnum.Contributor);

        try {
            return taskAssignmentRepository.save(taskAssignment);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error creating contributor: " + e.getMessage());
        }
    }

    public TaskAssignment createContributorByEmail(Integer taskId, String email) {
        if (taskId == null || email == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "task Id or Email cannot be null");
        }
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        if (taskAssignmentRepository.findByUserAndTask(user, task) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user is already a contributor in this task");
        }

        TaskAssignment taskAssignment = new TaskAssignment();
        taskAssignment.setTask(task);
        taskAssignment.setUser(user);
        taskAssignment.setRole(TaskRolesEnum.Contributor);

        try {
            return taskAssignmentRepository.save(taskAssignment);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error creating contributor: " + e.getMessage());
        }
    }

    public TaskAssignment deleteContributorById(Integer taskId, int userId) {
        if (taskId == null || userId == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task Id or User Id cannot be null");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        TaskAssignment taskAssignment = taskAssignmentRepository.findByUserAndTask(user, task);
        if (taskAssignment == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not have a role in this task");
        }

        // Prevent deletion of the owner
        if (taskAssignment.getRole().equals(TaskRolesEnum.Owner)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot delete the Owner");
        }

        try {
            taskAssignmentRepository.delete(taskAssignment);
            return taskAssignment;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error deleting contributor: " + e.getMessage());
        }
    }

    public TaskAssignment deleteContributorByEmail(Integer taskId, String email) {
        if (taskId == null || email == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "task Id or Email cannot be null");
        }
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        TaskAssignment taskAssignment = taskAssignmentRepository.findByUserAndTask(user, task);
        if (taskAssignment == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not have a role in this task");
        }

        // Prevent deletion of the owner
        if (taskAssignment.getRole().equals(TaskRolesEnum.Owner)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot delete the Owner");
        }

        try {
            taskAssignmentRepository.delete(taskAssignment);
            return taskAssignment;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error deleting contributor: " + e.getMessage());
        }
    }

}
