package com.example.demo.models;

public enum Faculty {
    ENGINEERING("Engineering"),
    SCIENCE("Science"),
    BUSINESS("Business"),
    ARTS("Arts"),
    MEDICINE("Medicine");

    private final String displayName;

    Faculty(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
} 