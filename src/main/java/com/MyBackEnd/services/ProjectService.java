package com.MyBackEnd.services;

import com.MyBackEnd.models.Project;
import com.MyBackEnd.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    // Find all projects by user
    public List<Project> getAllProjectsForUser(Integer userId) {
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID cannot be null");
        }
        return projectRepository.findByCreatorId(userId);
    }

    // Find a project by project id and user id
    public Project getProjectByIdAndUser(Integer projectId, Integer userId) {
        if (projectId == null || userId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project ID and User ID cannot be null");
        }
        return projectRepository.findByIdAndCreatorId(projectId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Project not found or user not authorized"));
    }

    // Create a new project
    public Project createProject(Project project) {
        if (project == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project cannot be null");
        }

        // Set creation timestamp
        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(LocalDateTime.now());

        try {
            return projectRepository.save(project);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error creating project: " + e.getMessage());
        }
    }

    // Update a project
    public Project updateProject(Integer projectId, Integer userId, Project projectDetails) {
        if (projectId == null || projectDetails == null || userId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Project ID, User ID and Project details cannot be null");
        }

        Project existingProject = projectRepository.findByIdAndCreatorId(projectId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Project not found or user not authorized"));

        // Update the fields
        existingProject.setTitle(projectDetails.getTitle());
        existingProject.setDescription(projectDetails.getDescription());
        existingProject.setStartDate(projectDetails.getStartDate());
        existingProject.setEndDate(projectDetails.getEndDate());
        existingProject.setUpdatedAt(LocalDateTime.now());

        try {
            return projectRepository.save(existingProject);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error updating project: " + e.getMessage());
        }
    }

    // Delete a project
    public void deleteProject(Integer projectId, Integer userId) {
        if (projectId == null || userId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Project ID and User ID cannot be null");
        }

        // Check if project exists and belongs to user
        if (!projectRepository.existsByIdAndCreatorId(projectId, userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Project not found or user not authorized");
        }

        try {
            projectRepository.deleteByIdAndCreatorId(projectId, userId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error deleting project: " + e.getMessage());
        }
    }
}