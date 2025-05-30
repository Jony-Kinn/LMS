package com.example.demo.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class LecturerDashboardController {
    @FXML
    private VBox mainContent;
    @FXML
    private TabPane tabPane;
    @FXML
    private Label welcomeLabel;
    
    private String lecturerName;
    
    public void initialize() {
        setupTabs();
        styleDashboard();
    }
    
    public void setLecturerName(String name) {
        this.lecturerName = name;
        welcomeLabel.setText("Welcome, Prof. " + lecturerName);
    }
    
    private void styleDashboard() {
        mainContent.getStyleClass().add("lecturer-dashboard");
        mainContent.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        
        // Style the welcome section
        VBox welcomeBox = new VBox(10);
        welcomeBox.getStyleClass().add("lecturer-welcome");
        welcomeBox.setPadding(new Insets(30));
        welcomeBox.setAlignment(Pos.CENTER_LEFT);

        Label welcomeLabel = new Label("Welcome, Prof. " + lecturerName);
        welcomeLabel.getStyleClass().add("lecturer-welcome-text");

        Label facultyLabel = new Label("Faculty of " + getFacultyName());
        facultyLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.9); -fx-font-size: 16px;");

        welcomeBox.getChildren().addAll(welcomeLabel, facultyLabel);

        // Style the stats section
        HBox statsBox = new HBox(20);
        statsBox.getStyleClass().add("lecturer-stats");
        statsBox.setAlignment(Pos.CENTER);

        VBox coursesStats = createStatCard("Active Courses", "3");
        VBox studentsStats = createStatCard("Total Students", "75");
        VBox assessmentsStats = createStatCard("Pending Assessments", "5");

        statsBox.getChildren().addAll(coursesStats, studentsStats, assessmentsStats);

        // Add welcome and stats sections to main content
        mainContent.getChildren().addAll(welcomeBox, statsBox);
    }

    private VBox createStatCard(String label, String value) {
        VBox card = new VBox(10);
        card.getStyleClass().add("lecturer-stat-card");
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));

        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("lecturer-stat-value");

        Label labelText = new Label(label);
        labelText.getStyleClass().add("lecturer-stat-label");

        card.getChildren().addAll(valueLabel, labelText);
        return card;
    }

    private String getFacultyName() {
        // This should be replaced with actual faculty name from user data
        return "Engineering";
    }

    private void setupTabs() {
        tabPane.getStyleClass().add("dashboard-tab-pane");
        
        // My Courses Tab
        Tab coursesTab = new Tab("My Courses");
        coursesTab.setClosable(false);
        VBox coursesContent = createCoursesContent();
        coursesTab.setContent(coursesContent);
        styleTab(coursesTab, "📚");
        
        // Assignments Tab
        Tab assignmentsTab = new Tab("Assignments");
        assignmentsTab.setClosable(false);
        VBox assignmentsContent = createAssignmentsContent();
        assignmentsTab.setContent(assignmentsContent);
        styleTab(assignmentsTab, "📝");
        
        // Students Tab
        Tab studentsTab = new Tab("Students");
        studentsTab.setClosable(false);
        VBox studentsContent = createStudentsContent();
        studentsTab.setContent(studentsContent);
        styleTab(studentsTab, "👥");
        
        tabPane.getTabs().addAll(coursesTab, assignmentsTab, studentsTab);
    }

    private void styleTab(Tab tab, String icon) {
        Label label = new Label(icon + " " + tab.getText());
        label.getStyleClass().add("tab-label");
        tab.setGraphic(label);
    }

    private VBox createCoursesContent() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.getStyleClass().add("dashboard-content");
        
        Button addCourseBtn = new Button("Add New Course");
        addCourseBtn.getStyleClass().add("action-button");
        
        // Sample courses
        ObservableList<String> courses = FXCollections.observableArrayList(
            "CS101 - Introduction to Programming (30 students)",
            "CS202 - Data Structures (25 students)",
            "CS303 - Database Systems (20 students)"
        );
        
        VBox coursesList = new VBox(10);
        for (String course : courses) {
            HBox courseBox = createCourseBox(course);
            coursesList.getChildren().add(courseBox);
        }
        
        content.getChildren().addAll(addCourseBtn, coursesList);
        return content;
    }
    
    private HBox createCourseBox(String courseName) {
        HBox courseBox = new HBox(15);
        courseBox.getStyleClass().add("course-card");
        courseBox.setPadding(new Insets(15));
        courseBox.setAlignment(Pos.CENTER_LEFT);
        
        Label nameLabel = new Label(courseName);
        nameLabel.getStyleClass().add("course-title");
        
        Button manageBtn = new Button("Manage");
        manageBtn.getStyleClass().add("action-button");
        
        Button announcementBtn = new Button("Make Announcement");
        announcementBtn.getStyleClass().add("secondary-button");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        courseBox.getChildren().addAll(nameLabel, spacer, announcementBtn, manageBtn);
        return courseBox;
    }
    
    private VBox createAssignmentsContent() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.getStyleClass().add("dashboard-content");
        
        Button addAssignmentBtn = new Button("Create New Assignment");
        addAssignmentBtn.getStyleClass().add("action-button");
        
        // Sample assignments
        String[][] assignments = {
            {"Programming Assignment 1", "CS101", "Due: 2024-03-20", "15/30 submitted"},
            {"Data Structures Project", "CS202", "Due: 2024-03-22", "20/25 submitted"},
            {"Database Design Task", "CS303", "Due: 2024-03-25", "10/20 submitted"}
        };
        
        VBox assignmentsList = new VBox(10);
        for (String[] assignment : assignments) {
            HBox assignmentBox = createAssignmentBox(assignment[0], assignment[1], assignment[2], assignment[3]);
            assignmentsList.getChildren().add(assignmentBox);
        }
        
        content.getChildren().addAll(addAssignmentBtn, assignmentsList);
        return content;
    }
    
    private HBox createAssignmentBox(String title, String course, String dueDate, String submissions) {
        HBox box = new HBox(15);
        box.getStyleClass().add("assessment-card");
        box.setPadding(new Insets(15));
        box.setAlignment(Pos.CENTER_LEFT);
        
        VBox details = new VBox(5);
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("assessment-title");
        Label courseLabel = new Label(course);
        courseLabel.getStyleClass().add("course-info");
        details.getChildren().addAll(titleLabel, courseLabel);
        
        Label dueDateLabel = new Label(dueDate);
        dueDateLabel.getStyleClass().add("assessment-due-date");
        
        Label submissionsLabel = new Label(submissions);
        submissionsLabel.getStyleClass().add("submissions-label");
        
        Button gradeBtn = new Button("Grade");
        gradeBtn.getStyleClass().add("action-button");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        box.getChildren().addAll(details, spacer, dueDateLabel, submissionsLabel, gradeBtn);
        return box;
    }
    
    private VBox createStudentsContent() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.getStyleClass().add("dashboard-content");
        
        // Filter/Search section
        HBox filterBox = new HBox(10);
        filterBox.setAlignment(Pos.CENTER_LEFT);
        filterBox.getStyleClass().add("search-box");
        
        TextField searchField = new TextField();
        searchField.setPromptText("Search students...");
        searchField.getStyleClass().add("search-field");
        
        ComboBox<String> courseFilter = new ComboBox<>();
        courseFilter.getItems().addAll("All Courses", "CS101", "CS202", "CS303");
        courseFilter.setValue("All Courses");
        courseFilter.getStyleClass().add("search-field");
        
        filterBox.getChildren().addAll(searchField, courseFilter);
        
        // Sample students
        String[][] students = {
            {"John Doe", "CS101, CS202", "Good Standing"},
            {"Jane Smith", "CS101, CS303", "Excellent"},
            {"Bob Wilson", "CS202", "At Risk"}
        };
        
        VBox studentsList = new VBox(10);
        for (String[] student : students) {
            HBox studentBox = createStudentBox(student[0], student[1], student[2]);
            studentsList.getChildren().add(studentBox);
        }
        
        content.getChildren().addAll(filterBox, studentsList);
        return content;
    }
    
    private HBox createStudentBox(String name, String courses, String status) {
        HBox box = new HBox(15);
        box.getStyleClass().add("student-box");
        box.setPadding(new Insets(15));
        box.setAlignment(Pos.CENTER_LEFT);
        
        VBox details = new VBox(5);
        Label nameLabel = new Label(name);
        nameLabel.getStyleClass().add("student-name");
        Label coursesLabel = new Label("Enrolled in: " + courses);
        coursesLabel.getStyleClass().add("student-courses");
        details.getChildren().addAll(nameLabel, coursesLabel);
        
        Label statusLabel = new Label(status);
        statusLabel.getStyleClass().add("status-label");
        if (status.equals("At Risk")) {
            statusLabel.getStyleClass().add("status-risk");
        } else if (status.equals("Excellent")) {
            statusLabel.getStyleClass().add("status-excellent");
        }
        
        Button viewProfileBtn = new Button("View Profile");
        viewProfileBtn.getStyleClass().add("action-button");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        box.getChildren().addAll(details, spacer, statusLabel, viewProfileBtn);
        return box;
    }
} 