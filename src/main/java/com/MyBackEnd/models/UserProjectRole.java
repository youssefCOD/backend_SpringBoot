package com.MyBackEnd.models;

import jakarta.persistence.*;

@Entity
@Table(name="user_project_roles")
public class UserProjectRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="user_id",nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "project_id",nullable = false)
    private Project project;

    @Enumerated(EnumType.STRING)
    @Column(name="role",nullable = false)
    private ProjectRoles role;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public ProjectRoles getRole() {
        return role;
    }

    public void setRole(ProjectRoles role) {
        this.role = role;
    }
}
