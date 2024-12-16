package com.MyBackEnd.controllers.DTO;

import com.MyBackEnd.models.ProjectRolesEnum;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectWithContributors implements Serializable {
    private Integer id;
    private String name;
    private String description;
    private List<ContributorDTO> contributors;

    // Default constructor
    public ProjectWithContributors() {
    }

    // Constructor that transforms Project and UserProjectRole to simpler DTOs
    public ProjectWithContributors(com.MyBackEnd.models.Project project, List<com.MyBackEnd.models.UserProjectRole> projectRoles) {
        this.id = project.getId();
        this.name = project.getTitle();
        this.description = project.getDescription();

        // Transform UserProjectRole to a simpler DTO
        this.contributors = projectRoles.stream()
                .map(role -> new ContributorDTO(
                        role.getUser().getUserId(),
                        role.getUser().getEmail(),
                        role.getRole()
                ))
                .collect(Collectors.toList());
    }

    // Nested DTO for contributors to avoid complex serialization
    public static class ContributorDTO implements Serializable {
        private Integer userId;
        private String username;
        private String role;

        public ContributorDTO() {}

        public ContributorDTO(Integer userId, String username, String role) {
            this.userId = userId;
            this.username = username;
            this.role = role;
        }

        public ContributorDTO(int userId, String email, ProjectRolesEnum role) {
        }

        // Getters and setters
        public Integer getUserId() { return userId; }
        public void setUserId(Integer userId) { this.userId = userId; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }

    // Getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<ContributorDTO> getContributors() { return contributors; }
    public void setContributors(List<ContributorDTO> contributors) { this.contributors = contributors; }
}