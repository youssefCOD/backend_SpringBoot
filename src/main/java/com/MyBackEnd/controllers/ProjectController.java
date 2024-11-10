package com.MyBackEnd.controllers;

import com.MyBackEnd.models.Project;
import com.MyBackEnd.models.User;
import com.MyBackEnd.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    // Get all projects the authenticated user has access to
    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects(Authentication authentication) {
        int userId = getUserIdFromAuthentication(authentication);
        List<Project> projects = projectService.getAllProjectsForUser(userId);
        return ResponseEntity.ok(projects);
    }

    // Get a specific project by ID
    @GetMapping("/{projectId}")
    public ResponseEntity<Project> getProjectById(@PathVariable int projectId, Authentication authentication) {
        int userId = getUserIdFromAuthentication(authentication);
        Project project = projectService.getProjectByIdAndUser(projectId, userId);
        return ResponseEntity.ok(project);
    }

    // Create a new project
    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project, Authentication authentication) {
        int userId = getUserIdFromAuthentication(authentication);
        project.setCreator_id(userId);  // Set creator to authenticated user
        Project createdProject = projectService.createProject(project);
        return ResponseEntity.ok(createdProject);
    }

    // Update an existing project
    @PutMapping("/{projectId}")
    public ResponseEntity<Project> updateProject(@PathVariable int projectId, @RequestBody Project projectDetails) {
        Project updatedProject = projectService.updateProject(projectId, projectDetails);
        return ResponseEntity.ok(updatedProject);
    }

    // Delete a project
    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable int projectId) {
//        we don't need user id here
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }

    // Helper method to extract user ID from Authentication object
    private int getUserIdFromAuthentication(Authentication authentication) {
        User user = (User) authentication.getPrincipal();  // Assuming User is your user entity class
        return user.getUser_id();  // Assuming user entity has a getId() method
    }
}
