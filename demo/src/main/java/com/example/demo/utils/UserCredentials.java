package com.example.demo.utils;

import com.example.demo.models.Faculty;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserCredentials {
    private static Map<String, UserInfo> users = new HashMap<>();
    private static final String DATA_FILE = "user_credentials.dat";

    private static class UserInfo implements Serializable {
        private static final long serialVersionUID = 1L;
        String password;
        String role;
        String fullName;
        Faculty faculty;

        UserInfo(String password, String role, String fullName, Faculty faculty) {
            this.password = password;
            this.role = role;
            this.fullName = fullName;
            this.faculty = faculty;
        }

        boolean checkPassword(String password) {
            return this.password.equals(password);
        }
    }

    static {
        loadCredentials();
    }

    private static void loadCredentials() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                @SuppressWarnings("unchecked")
                Map<String, UserInfo> loadedUsers = (Map<String, UserInfo>) ois.readObject();
                users = loadedUsers;
                System.out.println("Loaded " + users.size() + " users from file");
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading credentials: " + e.getMessage());
                users = new HashMap<>();
            }
        }
    }

    private static void saveCredentials() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(users);
            System.out.println("Saved " + users.size() + " users to file");
        } catch (IOException e) {
            System.err.println("Error saving credentials: " + e.getMessage());
        }
    }

    public static boolean registerStudent(String username, String password, String fullName) {
        if (username == null || password == null || fullName == null || 
            username.trim().isEmpty() || password.trim().isEmpty() || fullName.trim().isEmpty()) {
            return false;
        }
        
        if (users.containsKey(username.trim())) {
            return false; // Username already exists
        }
        
        users.put(username.trim(), new UserInfo(password, "STUDENT", fullName.trim(), null));
        saveCredentials(); // Save after registration
        return true;
    }

    public static boolean registerLecturer(String username, String password, String fullName, Faculty faculty) {
        if (username == null || password == null || fullName == null || faculty == null || 
            username.trim().isEmpty() || password.trim().isEmpty() || fullName.trim().isEmpty()) {
            return false;
        }
        
        if (users.containsKey(username.trim())) {
            return false; // Username already exists
        }
        
        users.put(username.trim(), new UserInfo(password, "LECTURER", fullName.trim(), faculty));
        saveCredentials(); // Save after registration
        return true;
    }

    public static boolean validateStudent(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        
        UserInfo user = users.get(username.trim());
        return user != null && user.checkPassword(password) && user.role.equals("STUDENT");
    }

    public static boolean validateLecturer(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        
        UserInfo user = users.get(username.trim());
        return user != null && user.checkPassword(password) && user.role.equals("LECTURER");
    }

    public static String getFullName(String username) {
        if (username == null) return "";
        UserInfo user = users.get(username.trim());
        return user != null ? user.fullName : "";
    }

    public static Faculty getFaculty(String username) {
        if (username == null) return null;
        UserInfo user = users.get(username.trim());
        return user != null ? user.faculty : null;
    }

    public static String getRole(String username) {
        if (username == null) return "";
        UserInfo user = users.get(username.trim());
        return user != null ? user.role : "";
    }

    // Method to get credentials info for display
    public static String getCredentialsInfo() {
        StringBuilder info = new StringBuilder();
        info.append("Registered Users:\n\n");
        info.append("Students:\n");
        users.forEach((username, userInfo) -> {
            if (userInfo.role.equals("STUDENT")) {
                info.append(String.format("Username: %s, Full Name: %s\n", 
                    username, userInfo.fullName));
            }
        });
        info.append("\nLecturers:\n");
        users.forEach((username, userInfo) -> {
            if (userInfo.role.equals("LECTURER")) {
                info.append(String.format("Username: %s, Full Name: %s, Faculty: %s\n", 
                    username, userInfo.fullName, 
                    userInfo.faculty != null ? userInfo.faculty.getDisplayName() : "N/A"));
            }
        });
        return info.toString();
    }

    // Method to clear all credentials (useful for testing)
    public static void clearCredentials() {
        users.clear();
        File file = new File(DATA_FILE);
        if (file.exists()) {
            file.delete();
        }
    }
} 