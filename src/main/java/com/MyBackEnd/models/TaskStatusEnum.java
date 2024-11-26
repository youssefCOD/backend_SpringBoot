package com.MyBackEnd.models;

public enum TaskStatusEnum {
    TO_DO("To Do"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    BLOCKED("Blocked"),
    ON_HOLD("On Hold");

    private final String displayName;

    TaskStatusEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
