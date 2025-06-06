package com.example.demo.models;

public class User {
    private String username;
    private String password;
    private String fullName;

    public User(String username, String password, String fullName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    protected void setPassword(String password) {
        this.password = password;
    }

    protected void setFullName(String fullName) {
        this.fullName = fullName;
    }
} 