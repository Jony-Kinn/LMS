package com.example.demo.models;

public class Lecturer extends User {
    private Faculty faculty;

    public Lecturer(String username, String password, String fullName, Faculty faculty) {
        super(username, password, fullName);
        this.faculty = faculty;
    }

    public Faculty getFaculty() {
        return faculty;
    }
} 