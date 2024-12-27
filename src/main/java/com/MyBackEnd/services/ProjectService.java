package com.MyBackEnd.services;

import com.MyBackEnd.dto.ProjectMemberResponse;
import com.MyBackEnd.dto.ProjectResponse;
import com.MyBackEnd.models.Project;
import com.MyBackEnd.models.ProjectRolesEnum;
import com.MyBackEnd.models.User;
import com.MyBackEnd.models.UserProjectRole;
import com.MyBackEnd.repository.ProjectRepository;
import com.MyBackEnd.repository.UserProjectRoleRepository;
import com.MyBackEnd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserProjectRoleRepository userProjectRoleRepository;
    private final UserRepository userRepository;


    @Autowired
    public ProjectService(ProjectRepository projectRepository, UserProjectRoleRepository userProjectRoleRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userProjectRoleRepository = userProjectRoleRepository;
        this.userRepository = userRepository;
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
        return projectRepository.findProjectByIdAndUserId(projectId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Project not found or user not authorized"));
    }

    // Create a new project
    public Project createProject(Project project, int userId) {
        if (project == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project cannot be null");
        }

        // Set creation timestamp
        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(LocalDateTime.now());
        Project savedProject = projectRepository.save(project);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        UserProjectRole userProjectRole = new UserProjectRole();
        userProjectRole.setRole(ProjectRolesEnum.Owner);
        userProjectRole.setProject(project);
        userProjectRole.setUser(user);
        userProjectRoleRepository.save(userProjectRole);

        try {
            return savedProject;
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

        Project existingProject = projectRepository.findProjectByIdAndUserId(projectId, userId)
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
    public ProjectResponse toProjectResponse(Project project) {
        List<ProjectMemberResponse> members = project.getMembers()
                .stream()
                .map(member -> new ProjectMemberResponse(
                        member.getUser().getUserId(),
                        member.getUser().getFirstName(),
                        member.getUser().getLastName(),
                        member.getUser().getEmail(), // Assuming User has a username field
                        member.getRole().toString() // Assuming UserProjectRole has a getRole() method
                ))
                .collect(Collectors.toList());

        return new ProjectResponse(
                project.getId(),
                project.getTitle(),
                project.getDescription(),
                project.getColor(),
                project.getCreatorId(),
                project.getStartDate(),
                project.getEndDate(),
                project.getCreatedAt(),
                project.getUpdatedAt(),
                project.getStatus() != null ? project.getStatus().name() : null,
                members
        );
    }

    // Get all projects for a user
    public List<ProjectResponse> getAllProjectsByUser(Integer userId) {
        // Fetch UserProjectRole entries for the given user
        List<UserProjectRole> userRoles = userProjectRoleRepository.findByUserId(userId);

        // Extract projects from user roles
        List<Project> projects = userRoles.stream()
                .map(UserProjectRole::getProject)
                .collect(Collectors.toList());

        // Convert each project to ProjectResponse
        return projects.stream()
                .map(this::toProjectResponse)
                .collect(Collectors.toList());
    }
    
}