package com.MyBackEnd.controllers;

import com.MyBackEnd.models.Task;
import com.MyBackEnd.services.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")

public class TaskController {
    private static final Logger log = LoggerFactory.getLogger(ProjectController.class);
    @Autowired
    private TaskService taskService;
    @GetMapping("/project/{project_id}")
    public ResponseEntity<List<Task>> getAllTasks(@PathVariable("project_id")Integer projectId) {
        List<Task> tasks = taskService.getAllTasksForProject(projectId);
        return ResponseEntity.ok(tasks);
    }
    @PostMapping("/project/{project_id}/user/{user_id}")
    public ResponseEntity<Task> createContributor(@RequestBody Task taskBody,@PathVariable("project_id")Integer projectId,@PathVariable("user_id")int userId){
        try {
            
            Task task = taskService.createTaskByProjectId(taskBody,projectId,userId);
            return ResponseEntity.ok(task);
        } catch (Exception e) {
            log.error("Error creating the task: ", e);
            return ResponseEntity.status(500).build();
        }
    }
    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateProject(@PathVariable Integer taskId,
                                                 @RequestBody Task taskDetails) {

        // Call the service to update the project
        Task updatedTask = taskService.updateTask(taskId, taskDetails);

        // Return the updated project
        return ResponseEntity.ok(updatedTask);
    }
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Task> deleteProject(@PathVariable Integer taskId ) {
        Task task =taskService.deleteTask(taskId);
        return ResponseEntity.ok(task);

    }
}
