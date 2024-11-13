package com.MyBackEnd.controllers;

import com.MyBackEnd.models.Project;
import com.MyBackEnd.models.User;
import com.MyBackEnd.services.ProjectService;
import com.MyBackEnd.services.auth.MyCustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private static final Logger log = LoggerFactory.getLogger(ProjectController.class);

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


        log.info(String.valueOf(userId));
        log.debug(String.valueOf(userId));

        Project project = projectService.getProjectByIdAndUser(projectId, userId);
        return ResponseEntity.ok(project);
    }

    // Create a new project
    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project, Authentication authentication) {
        if (authentication == null) {
            log.error("Authentication object is null - user is not authenticated");
            return ResponseEntity.status(401).build(); // Return 401 Unauthorized
        }

        try {
            int userId = getUserIdFromAuthentication(authentication);
            project.setCreatorId(userId);
            Project createdProject = projectService.createProject(project,userId);
            return ResponseEntity.ok(createdProject);
        } catch (Exception e) {
            log.error("Error creating project: ", e);
            return ResponseEntity.status(500).build();
        }
    }

    // Update an existing project
    @PutMapping("/{projectId}")
    public ResponseEntity<Project> updateProject(@PathVariable Integer projectId,
                                                 @RequestBody Project projectDetails,
                                                 Authentication authentication) {
        // Extract the userId from the authentication object (authenticated user)
        int userId = getUserIdFromAuthentication(authentication);

        // Call the service to update the project
        Project updatedProject = projectService.updateProject(projectId, userId, projectDetails);

        // Return the updated project
        return ResponseEntity.ok(updatedProject);
    }


    // Delete a project
    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Integer projectId,Authentication authentication ) {
        // Extract the userId from the authentication object (authenticated user)
        int userId = getUserIdFromAuthentication(authentication);
        //we don't need user id here
        projectService.deleteProject(projectId,userId);
        return ResponseEntity.noContent().build();
    }

    // Helper method to extract user ID from Authentication object
    private int getUserIdFromAuthentication(Authentication authentication) {
        // Cast to your custom UserDetails class
        MyCustomUserDetails userDetails = (MyCustomUserDetails) authentication.getPrincipal();

        // Access the User entity through MyCustomUserDetails
        User user = userDetails.getUser(); // Assuming MyCustomUserDetails has a getUser() method

        return user.getUser_id(); // Assuming User has a getUser_id() method
    }
}
