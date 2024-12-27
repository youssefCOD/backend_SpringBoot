package com.MyBackEnd.dto;

public class ProjectMemberResponse {
    private Integer userId;
    private String firstName;
    private String lastName;
    private String email;
    private String role;

    // Constructor
    public ProjectMemberResponse(Integer userId, String firstName, String lastName, String email, String role) {
        this.userId = userId;
        this.firstName=firstName;
        this.lastName=lastName;
        this.email = email;
        this.role = role;
    }

    // Getters and Setters
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
