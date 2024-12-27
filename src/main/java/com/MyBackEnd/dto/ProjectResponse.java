package com.MyBackEnd.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ProjectResponse {
    private Integer id;
    private String title;
    private String description;
    private int color;
    private Integer creatorId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String status;
    private List<ProjectMemberResponse> members;

    // Constructor
    public ProjectResponse(Integer id, String title, String description, int color, Integer creatorId,
                           LocalDateTime startDate, LocalDateTime endDate, LocalDateTime createdAt,
                           LocalDateTime updatedAt, String status, List<ProjectMemberResponse> members) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.color = color;
        this.creatorId = creatorId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
        this.members = members;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ProjectMemberResponse> getMembers() {
        return members;
    }

    public void setMembers(List<ProjectMemberResponse> members) {
        this.members = members;
    }
}

