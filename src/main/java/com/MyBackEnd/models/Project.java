package com.MyBackEnd.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.List;



@Entity
@Table(name = "projects")
public class Project {

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserProjectRole> userProjectRoles;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;  // changed from name to title to match earlier example

    private String description;

    @Column(name = "color")
    private int color = 0xFF0087FF;

    @Column(name = "creator_id", nullable = false)
    private Integer creatorId;  // changed from creator_id to follow Java naming conventions

    @Column(name = "start_date")
    private LocalDateTime startDate;  // changed from start_date to follow Java naming conventions

    @Column(name = "end_date")
    private LocalDateTime endDate;    // changed from end_date to follow Java naming conventions

    @Column(name = "created_at")
    private LocalDateTime createdAt;  // changed from created_at to follow Java naming conventions

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;  // changed from updated_at to follow Java naming conventions

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProjectStatus status;


    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getColor() {
        return this.color;
    }
    public void setColor(int color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Update getter/setter to use enum
    public ProjectStatus getStatus() {
        return status;
    }

    public Set<UserProjectRole> getUserProjectRoles() {
        return userProjectRoles;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }
    public void addTask(Task task) {
        this.tasks.add(task);
        task.setProject(this);
    }
}