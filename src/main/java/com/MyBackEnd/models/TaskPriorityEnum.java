package com.MyBackEnd.models;

public enum TaskPriorityEnum {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High"),;

    private final String displayName;

    TaskPriorityEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
