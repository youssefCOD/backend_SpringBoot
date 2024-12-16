package com.MyBackEnd.controllers;

import com.MyBackEnd.controllers.DTO.ProjectWithContributors;
import com.MyBackEnd.models.Project;
import com.MyBackEnd.models.User;
import com.MyBackEnd.models.UserProjectRole;
import com.MyBackEnd.services.ContributorsService;
import com.MyBackEnd.services.ProjectService;
import com.MyBackEnd.services.auth.MyCustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private static final Logger log = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ContributorsService contributorsService; //inject ContributorsService

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
            project.getMembers().forEach(user -> user.setProject(project));

            Project createdProject = projectService.createProject(project, userId);
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
    public ResponseEntity<Void> deleteProject(@PathVariable Integer projectId, Authentication authentication) {
        // Extract the userId from the authentication object (authenticated user)
        int userId = getUserIdFromAuthentication(authentication);
        //we don't need user id here
        projectService.deleteProject(projectId, userId);
        return ResponseEntity.noContent().build();
    }
    // TODO : fixe this route it's not working properly
    @GetMapping("/withContributors")
    public ResponseEntity<List<ProjectWithContributors>> getAllProjectsWithContributors(Authentication authentication) {
        try {
            int userId = getUserIdFromAuthentication(authentication);
            log.info("Fetching projects for userId: {}", userId);

            // Fetch projects for the user
            List<Project> projects = projectService.getAllProjectsForUser(userId);
            log.info("Number of projects found: {}", projects.size());

            // Optimize: Fetch contributors for all projects in one query
            List<Integer> projectIds = projects.stream().map(Project::getId).collect(Collectors.toList());
            List<UserProjectRole> allContributors = contributorsService.findContributorsForProjects(projectIds);

            // Map contributors to their projects
            Map<Integer, List<UserProjectRole>> contributorsMap = allContributors.stream()
                    .collect(Collectors.groupingBy(UserProjectRole::getId));

            // Transform projects with contributors
            List<ProjectWithContributors> projectsWithContributors = projects.stream()
                    .map(project -> new ProjectWithContributors(project, contributorsMap.getOrDefault(project.getId(), Collections.emptyList())))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(projectsWithContributors);
        } catch (Exception e) {
            log.error("Error in getAllProjectsWithContributors", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // Helper method to extract user ID from Authentication object
    private int getUserIdFromAuthentication(Authentication authentication) {
        // Cast to your custom UserDetails class
        MyCustomUserDetails userDetails = (MyCustomUserDetails) authentication.getPrincipal();

        // Access the User entity through MyCustomUserDetails
        //User user = userDetails.getUser(); // Assuming MyCustomUserDetails has a getUser() method

        return userDetails.getUser().getUserId(); // Assuming User has a getUser_id() method
    }

    // Helper method to map projects to a list of projects with contributors
    private List<ProjectWithContributors> getProjectsWithContributors(List<Project> projects) {
        List<ProjectWithContributors> projectsWithContributors = new ArrayList<>();

        for (Project project : projects) {
            List<UserProjectRole> contributors = contributorsService.getAllContributorsForProject(project.getId());
            ProjectWithContributors projectWithContributors = new ProjectWithContributors(project, contributors);
            projectsWithContributors.add(projectWithContributors);
        }

        return projectsWithContributors;
    }

}
