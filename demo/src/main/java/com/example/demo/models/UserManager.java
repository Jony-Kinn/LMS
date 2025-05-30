package com.example.demo.models;

import java.util.HashMap;
import java.util.Map;
import com.example.demo.models.User;
import com.example.demo.models.Student;
import com.example.demo.models.Lecturer;
import com.example.demo.models.Faculty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserManager {
    private Map<String, User> users;
    private User currentUser;
    public static ObservableList<String> globalAnnouncements = FXCollections.observableArrayList();

    public UserManager() {
        this.users = new HashMap<>();
        // Add some sample users for testing
        registerLecturer("john.doe", "password", "John Doe", Faculty.ENGINEERING);
        registerLecturer("jane.smith", "password", "Jane Smith", Faculty.SCIENCE);
        registerStudent("student1", "password", "Alice Johnson");
    }

    public boolean login(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            return true;
        }
        return false;
    }

    public boolean loginUser(String username, String password, String userType) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            if ((userType.equals("STUDENT") && user instanceof Student) ||
                (userType.equals("LECTURER") && user instanceof Lecturer)) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }

    public boolean registerStudent(String username, String password, String fullName) {
        if (users.containsKey(username)) {
            return false;
        }
        users.put(username, new Student(username, password, fullName));
        return true;
    }

    public boolean registerLecturer(String username, String password, String fullName, Faculty faculty) {
        if (users.containsKey(username)) {
            return false;
        }
        users.put(username, new Lecturer(username, password, fullName, faculty));
        return true;
    }

    public void logout() {
        currentUser = null;
    }

    public String getCurrentUserFullName() {
        return currentUser != null ? currentUser.getFullName() : null;
    }

    public String getCurrentUsername() {
        return currentUser != null ? currentUser.getUsername() : null;
    }

    public Faculty getCurrentUserFaculty() {
        if (currentUser instanceof Lecturer) {
            return ((Lecturer) currentUser).getFaculty();
        }
        return null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean isLecturer() {
        return isLoggedIn() && currentUser instanceof Lecturer;
    }

    public boolean isStudent() {
        return isLoggedIn() && currentUser instanceof Student;
    }
} 