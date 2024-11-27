package com.MyBackEnd.controllers;

import com.MyBackEnd.models.TaskAssignment;
import com.MyBackEnd.services.TaskAssignmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks/assignment")

public class TaskAssignmentController {
    private static final Logger log = LoggerFactory.getLogger(ProjectController.class);
    @Autowired
    private TaskAssignmentService taskAssignmentService;

    //    Get all the contributors
    @GetMapping("/task/{task_id}")
    public ResponseEntity<List<TaskAssignment>> getAllContributors(@PathVariable("task_id")Integer taskId) {
        List<TaskAssignment> contributors = taskAssignmentService.getAllContributorsForTask(taskId);
        return ResponseEntity.ok(contributors);
    }
    @PostMapping("/task/{task_id}/user/{user_id}")
    public ResponseEntity<TaskAssignment> createContributor(@PathVariable("task_id")Integer taskId, @PathVariable("user_id") int userId){
        try {
            TaskAssignment contributor = taskAssignmentService.createContributorById(taskId,userId);
            return ResponseEntity.ok(contributor);
        } catch (Exception e) {
            log.error("Error creating contributor: ", e);
            return ResponseEntity.status(500).build();
        }
    }
    @PostMapping("/task/{task_id}")
    public ResponseEntity<TaskAssignment> createContributor(@PathVariable("task_id")Integer taskId, @RequestParam("email")String email){
        try {
            TaskAssignment contributor = taskAssignmentService.createContributorByEmail(taskId,email);
            return ResponseEntity.ok(contributor);
        } catch (Exception e) {
            log.error("Error creating contributor: ", e);
            return ResponseEntity.status(500).build();
        }
    }
    @DeleteMapping("/task/{task_id}/user/{user_id}")
    public ResponseEntity<TaskAssignment> deleteContributor(@PathVariable("task_id")Integer taskId, @PathVariable("user_id") int userId){
        try {
            TaskAssignment contributor = taskAssignmentService.deleteContributorById(taskId,userId);
            return ResponseEntity.ok(contributor);
        } catch (Exception e) {
            log.error("Error creating contributor: ", e);
            return ResponseEntity.status(500).build();
        }
    }
    @DeleteMapping("/task/{task_id}")
    public ResponseEntity<TaskAssignment> deleteContributor(@PathVariable("task_id")Integer taskId, @RequestParam("email")String email){
        try {
            TaskAssignment contributor = taskAssignmentService.deleteContributorByEmail(taskId,email);
            return ResponseEntity.ok(contributor);
        } catch (Exception e) {
            log.error("Error creating contributor: ", e);
            return ResponseEntity.status(500).build();
        }
    }
}
