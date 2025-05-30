package com.example.demo.controllers;

import com.example.demo.models.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.collections.FXCollections;

public class LoginController {
    private UserManager userManager;
    private CourseManagementController courseController;
    private VBox mainContent;
    private Label messageLabel;

    public LoginController(UserManager userManager) {
        this.userManager = userManager;
        this.courseController = new CourseManagementController(userManager);
    }

    public VBox createLoginView() {
        mainContent = new VBox(15);
        mainContent.setPadding(new Insets(20));
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setMaxWidth(400);

        // Title
        Label titleLabel = new Label("Course Management System");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Message label for errors/success
        messageLabel = new Label();
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(350);

        // Tab Pane for Login and Registration
        TabPane tabPane = new TabPane();
        
        // Login Tab
        Tab loginTab = new Tab("Login");
        loginTab.setContent(createLoginForm());
        loginTab.setClosable(false);
        
        // Registration Tab
        Tab registerTab = new Tab("Register as Lecturer");
        registerTab.setContent(createRegistrationForm());
        registerTab.setClosable(false);
        
        tabPane.getTabs().addAll(loginTab, registerTab);

        mainContent.getChildren().addAll(titleLabel, messageLabel, tabPane);
        return mainContent;
    }

    private GridPane createLoginForm() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20));

        // Username field
        Label userLabel = new Label("Username:");
        TextField userField = new TextField();
        userField.setPromptText("Enter username");

        // Password field
        Label passLabel = new Label("Password:");
        PasswordField passField = new PasswordField();
        passField.setPromptText("Enter password");

        // Login button
        Button loginButton = new Button("Login");
        loginButton.setDefaultButton(true);
        loginButton.setMaxWidth(Double.MAX_VALUE);

        // Add to grid
        grid.add(userLabel, 0, 0);
        grid.add(userField, 1, 0);
        grid.add(passLabel, 0, 1);
        grid.add(passField, 1, 1);
        grid.add(loginButton, 0, 2, 2, 1);

        // Login button action
        loginButton.setOnAction(e -> handleLogin(userField.getText(), passField.getText()));

        return grid;
    }

    private GridPane createRegistrationForm() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20));

        // Username field
        Label userLabel = new Label("Username:");
        TextField userField = new TextField();
        userField.setPromptText("Choose username");

        // Password field
        Label passLabel = new Label("Password:");
        PasswordField passField = new PasswordField();
        passField.setPromptText("Choose password");

        // Full Name field
        Label nameLabel = new Label("Full Name:");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter your full name");

        // Faculty ComboBox
        Label facultyLabel = new Label("Faculty:");
        ComboBox<Faculty> facultyComboBox = new ComboBox<>();
        facultyComboBox.setItems(FXCollections.observableArrayList(Faculty.values()));
        facultyComboBox.setPromptText("Select your faculty");
        facultyComboBox.setMaxWidth(Double.MAX_VALUE);

        // Register button
        Button registerButton = new Button("Register");
        registerButton.setMaxWidth(Double.MAX_VALUE);

        // Add to grid
        grid.add(userLabel, 0, 0);
        grid.add(userField, 1, 0);
        grid.add(passLabel, 0, 1);
        grid.add(passField, 1, 1);
        grid.add(nameLabel, 0, 2);
        grid.add(nameField, 1, 2);
        grid.add(facultyLabel, 0, 3);
        grid.add(facultyComboBox, 1, 3);
        grid.add(registerButton, 0, 4, 2, 1);

        // Register button action
        registerButton.setOnAction(e -> handleRegistration(
            userField.getText(),
            passField.getText(),
            nameField.getText(),
            facultyComboBox.getValue()
        ));

        return grid;
    }

    private void handleLogin(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Please enter both username and password", true);
            return;
        }

        if (userManager.loginUser(username, password, "LECTURER")) {
            showMessage("Login successful!", false);
            
            if (userManager.isLecturer()) {
                // Show the course management view for lecturers
                mainContent.getChildren().clear();
                mainContent.getChildren().add(courseController.createCourseManagementView());
            } else {
                showMessage("Student view not implemented yet", true);
            }
        } else {
            showMessage("Invalid username or password", true);
        }
    }

    private void handleRegistration(String username, String password, String fullName, Faculty faculty) {
        if (username.isEmpty() || password.isEmpty() || fullName.isEmpty() || faculty == null) {
            showMessage("Please fill in all fields", true);
            return;
        }

        if (userManager.registerLecturer(username, password, fullName, faculty)) {
            showMessage("Registration successful! You can now login.", false);
        } else {
            showMessage("Username already exists. Please choose another.", true);
        }
    }

    private void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        messageLabel.setTextFill(isError ? Color.RED : Color.GREEN);
    }
} 