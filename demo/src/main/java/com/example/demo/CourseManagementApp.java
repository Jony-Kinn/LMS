package com.example.demo;

import com.example.demo.controllers.LoginController;
import com.example.demo.models.UserManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CourseManagementApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Initialize user manager and controllers
        UserManager userManager = new UserManager();
        LoginController loginController = new LoginController(userManager);

        // Create the main scene with login view
        Scene scene = new Scene(loginController.createLoginView(), 800, 600);
        
        // Set up the stage
        primaryStage.setTitle("Course Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
} 