package com.MyBackEnd.services;

import com.MyBackEnd.models.*;
import com.MyBackEnd.repository.ProjectRepository;
import com.MyBackEnd.repository.TaskAssignmentRepository;
import com.MyBackEnd.repository.TaskRepository;
import com.MyBackEnd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskAssignmentRepository taskAssignmentRepository;
    @Autowired
    public TaskService(ProjectRepository projectRepository, TaskRepository taskRepository, UserRepository userRepository, TaskAssignmentRepository taskAssignmentRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskAssignmentRepository = taskAssignmentRepository;
    }

    public List<Task> getAllTasksForProject(Integer projectId) {
        if (projectId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project ID cannot be null");
        }
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        return taskRepository.findByProject(project);
    }
    public Task createTaskByProjectId(Task task,Integer projectId,int userId){
        if (projectId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project Id cannot be null");
        }
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));


        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        task.setProject(project);
        Task createdTask = taskRepository.save(task);
        
        project.addTask(createdTask);

        TaskAssignment taskAssignment = new TaskAssignment();
        taskAssignment.setRole(TaskRolesEnum.Owner);
        taskAssignment.setTask(task);
        taskAssignment.setUser(user);
        taskAssignmentRepository.save(taskAssignment);


        try {
            return createdTask;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error creating contributor: " + e.getMessage());
        }
    }
    public Task updateTask(Integer taskId, Task taskDetails) {
        if (taskId == null || taskDetails == null ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Task ID and Task details cannot be null");
        }

        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Task not found"));

        // Update fields only if they are non-null
        if (taskDetails.getProject() != null) {
            existingTask.setProject(taskDetails.getProject());
        }
        if (taskDetails.getName() != null) {
            existingTask.setName(taskDetails.getName());
        }
        if (taskDetails.getDescription() != null) {
            existingTask.setDescription(taskDetails.getDescription());
        }
        if (taskDetails.getStatus() != null) {
            existingTask.setStatus(taskDetails.getStatus());
        }
        if (taskDetails.getPriority() != null) {
            existingTask.setPriority(taskDetails.getPriority());
        }
        if (taskDetails.getDueDate() != null) {
            existingTask.setDueDate(taskDetails.getDueDate());
        }

        // Update the updatedAt timestamp
        existingTask.setUpdatedAt(LocalDateTime.now());

        try {
            return taskRepository.save(existingTask);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error updating Task: " + e.getMessage());
        }
    }
    public Task deleteTask(Integer taskId) {
        if (taskId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Task ID cannot be null");
        }

        // Check if project exists
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
        existingTask.getProject().deleteTask(existingTask);


        try {
            taskRepository.deleteById(taskId);
            return existingTask;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error deleting Task: " + e.getMessage());
        }
    }

}
