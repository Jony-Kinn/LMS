package com.example.demo.controllers;

import com.example.demo.models.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.layout.StackPane;
import java.io.File;
import java.time.LocalDateTime;
import java.util.Optional;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.scene.effect.DropShadow;

public class StudentDashboardController {
    @FXML
    private VBox mainContent;
    @FXML
    private TabPane tabPane;
    @FXML
    private Label welcomeLabel;
    
    private UserManager userManager;
    private ObservableList<Course> availableCourses;
    private ObservableList<Course> enrolledCourses;
    private ObservableList<Assessment> pendingAssessments;
    private ObservableList<AssessmentResult> assessmentResults;
    
    private Stage primaryStage;
    private Scene homeScene;
    
    public StudentDashboardController(UserManager userManager) {
        this.userManager = userManager;
        this.availableCourses = FXCollections.observableArrayList();
        this.enrolledCourses = FXCollections.observableArrayList();
        this.pendingAssessments = FXCollections.observableArrayList();
        this.assessmentResults = FXCollections.observableArrayList();
        initializeSampleData();
    }
    
    private void initializeSampleData() {
        // Add sample available courses
        availableCourses.addAll(
            new Course("CS101", "Introduction to Programming", "Basic programming concepts", Faculty.ENGINEERING),
            new Course("CS201", "Data Structures", "Advanced data structures", Faculty.ENGINEERING),
            new Course("MATH101", "Calculus I", "Introduction to calculus", Faculty.SCIENCE),
            new Course("PHY101", "Physics I", "Basic physics concepts", Faculty.SCIENCE),
            new Course("CHEM101", "General Chemistry", "Fundamentals of chemistry", Faculty.SCIENCE),
            new Course("BIO101", "Biology I", "Introduction to biology", Faculty.SCIENCE),
            new Course("ENG101", "English Composition", "Academic writing and composition", Faculty.ARTS),
            new Course("HIST101", "World History", "Survey of world history", Faculty.ARTS),
            new Course("BUS101", "Principles of Management", "Introduction to management", Faculty.BUSINESS),
            new Course("FIN101", "Financial Accounting", "Basics of accounting", Faculty.BUSINESS),
            new Course("CS301", "Software Engineering", "Software development lifecycle", Faculty.ENGINEERING),
            new Course("CS401", "Artificial Intelligence", "Intro to AI concepts", Faculty.ENGINEERING),
            new Course("STAT201", "Statistics", "Probability and statistics", Faculty.SCIENCE),
            new Course("PSY101", "Introduction to Psychology", "Fundamentals of psychology", Faculty.ARTS),
            new Course("LAW101", "Introduction to Law", "Basics of law and legal systems", Faculty.ARTS),
            new Course("MED101", "Medical Science", "Introduction to medical science", Faculty.SCIENCE)
        );

        // Add sample enrolled courses
        enrolledCourses.addAll(
            new Course("CS101", "Introduction to Programming", "Basic programming concepts", Faculty.ENGINEERING),
            new Course("MATH101", "Calculus I", "Introduction to calculus", Faculty.SCIENCE),
            new Course("ENG101", "English Composition", "Academic writing and composition", Faculty.ARTS)
        );

        // Add sample pending assessments
        pendingAssessments.addAll(
            new Assessment("A1", "Assignment 1", "DOCUMENT", 0.2, 100),
            new Assessment("A2", "Project Video", "VIDEO", 0.3, 100),
            new Assessment("A3", "Essay Submission", "DOCUMENT", 0.2, 100)
        );
    }
    
    public VBox createStudentDashboard() {
        mainContent = new VBox(20);
        mainContent.setPadding(new Insets(40));
        mainContent.getStyleClass().add("student-dashboard");
        mainContent.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        // Welcome section
        VBox welcomeBox = new VBox(10);
        welcomeBox.getStyleClass().add("student-welcome");
        welcomeBox.setPadding(new Insets(30));
        welcomeBox.setAlignment(Pos.CENTER_LEFT);

        String fullName = userManager.getCurrentUserFullName();
        Label welcomeLabel = new Label("Welcome, " + fullName);
        welcomeLabel.getStyleClass().add("student-welcome-text");

        Label studentIdLabel = new Label("Student ID: " + userManager.getCurrentUsername());
        studentIdLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.9); -fx-font-size: 16px;");

        // Add logout button
        Button logoutButton = new Button("Logout");
        logoutButton.getStyleClass().add("logout-button");
        logoutButton.setOnAction(e -> handleLogout());

        HBox welcomeHeader = new HBox(10);
        welcomeHeader.setAlignment(Pos.CENTER_LEFT);
        welcomeHeader.getChildren().addAll(welcomeLabel, studentIdLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox welcomeRow = new HBox(10);
        welcomeRow.setAlignment(Pos.CENTER_LEFT);
        welcomeRow.getChildren().addAll(welcomeHeader, spacer, logoutButton);

        welcomeBox.getChildren().add(welcomeRow);

        // Stats section
        HBox statsBox = new HBox(20);
        statsBox.getStyleClass().add("student-stats");
        statsBox.setAlignment(Pos.CENTER);

        VBox enrolledStats = createStatCard("Enrolled Courses", String.valueOf(enrolledCourses.size()));
        VBox pendingStats = createStatCard("Pending Assessments", String.valueOf(pendingAssessments.size()));
        VBox completedStats = createStatCard("Completed Assessments", String.valueOf(assessmentResults.size()));

        statsBox.getChildren().addAll(enrolledStats, pendingStats, completedStats);

        // Create TabPane for different sections
        TabPane mainTabPane = new TabPane();
        mainTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        mainTabPane.getStyleClass().add("dashboard-tab-pane");

        // Available Courses Tab
        Tab availableCoursesTab = new Tab("Available Courses");
        availableCoursesTab.setContent(createAvailableCoursesTab());
        styleTab(availableCoursesTab, "📚");

        // My Courses Tab
        Tab myCoursesTab = new Tab("My Courses");
        myCoursesTab.setContent(createMyCoursesTab());
        styleTab(myCoursesTab, "🎓");

        // Assessments Tab
        Tab assessmentsTab = new Tab("Assessments");
        assessmentsTab.setContent(createAssessmentsTab());
        styleTab(assessmentsTab, "📝");

        // Grades Tab
        Tab gradesTab = new Tab("Grades & Transcripts");
        gradesTab.setContent(createGradesTab());
        styleTab(gradesTab, "📊");

        // Reports & Analytics Tab
        Tab reportsTab = new Tab("Reports & Analytics");
        reportsTab.setContent(createReportsAndAnalyticsTab());
        styleTab(reportsTab, "📈");

        // Profile Tab
        Tab profileTab = new Tab("Profile");
        profileTab.setContent(createProfileTab());
        styleTab(profileTab, "👤");

        mainTabPane.getTabs().addAll(
            availableCoursesTab, myCoursesTab, assessmentsTab, gradesTab, reportsTab, profileTab
        );

        mainContent.getChildren().addAll(welcomeBox, statsBox, mainTabPane);
        return mainContent;
    }

    private VBox createStatCard(String label, String value) {
        VBox card = new VBox(10);
        card.getStyleClass().add("student-stat-card");
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));

        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("student-stat-value");

        Label labelText = new Label(label);
        labelText.getStyleClass().add("student-stat-label");

        card.getChildren().addAll(valueLabel, labelText);
        return card;
    }

    private void styleTab(Tab tab, String icon) {
        Label label = new Label(icon + " " + tab.getText());
        label.getStyleClass().add("tab-label");
        tab.setGraphic(label);
    }

    private VBox createAvailableCoursesTab() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        content.getStyleClass().add("dashboard-content");

        Label titleLabel = new Label("Available Courses");
        titleLabel.getStyleClass().add("section-title");

        TableView<Course> courseTable = new TableView<>(availableCourses);
        courseTable.setPrefHeight(400);
        courseTable.getStyleClass().add("dashboard-table");

        TableColumn<Course, String> idCol = new TableColumn<>("Course ID");
        idCol.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        idCol.setPrefWidth(120);

        TableColumn<Course, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        titleCol.setPrefWidth(250);

        TableColumn<Course, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        descCol.setPrefWidth(300);

        TableColumn<Course, Faculty> facultyCol = new TableColumn<>("Faculty");
        facultyCol.setCellValueFactory(cellData -> cellData.getValue().facultyProperty());
        facultyCol.setPrefWidth(150);

        courseTable.getColumns().addAll(idCol, titleCol, descCol, facultyCol);

        Button enrollButton = new Button("Enroll in Selected Course");
        enrollButton.getStyleClass().add("action-button");
        enrollButton.setOnAction(e -> {
            Course selected = courseTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                enrollInCourse(selected);
            }
        });

        content.getChildren().addAll(titleLabel, courseTable, enrollButton);
        return content;
    }

    private VBox createMyCoursesTab() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        content.getStyleClass().add("dashboard-content");

        Label titleLabel = new Label("My Enrolled Courses");
        titleLabel.getStyleClass().add("section-title");

        TableView<Course> courseTable = new TableView<>(enrolledCourses);
        courseTable.setPrefHeight(400);
        courseTable.getStyleClass().add("dashboard-table");

        TableColumn<Course, String> idCol = new TableColumn<>("Course ID");
        idCol.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        idCol.setPrefWidth(120);

        TableColumn<Course, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        titleCol.setPrefWidth(250);

        TableColumn<Course, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        descCol.setPrefWidth(300);

        TableColumn<Course, Faculty> facultyCol = new TableColumn<>("Faculty");
        facultyCol.setCellValueFactory(cellData -> cellData.getValue().facultyProperty());
        facultyCol.setPrefWidth(150);

        courseTable.getColumns().addAll(idCol, titleCol, descCol, facultyCol);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        Button viewContentButton = new Button("View Course Content");
        viewContentButton.getStyleClass().add("action-button");
        viewContentButton.setOnAction(e -> {
            Course selected = courseTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showCourseContent(selected);
            }
        });

        Button uploadButton = new Button("Upload Material");
        uploadButton.getStyleClass().add("secondary-button");
        uploadButton.setOnAction(e -> {
            Course selected = courseTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showUploadDialog(selected);
            } else {
                showMessage("Please select a course first", true);
            }
        });

        buttonBox.getChildren().addAll(viewContentButton, uploadButton);
        content.getChildren().addAll(titleLabel, courseTable, buttonBox);
        return content;
    }

    private VBox createAssessmentsTab() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        content.getStyleClass().add("dashboard-content");

        Label titleLabel = new Label("Pending Assessments");
        titleLabel.getStyleClass().add("section-title");

        TableView<Assessment> assessmentTable = new TableView<>(pendingAssessments);
        assessmentTable.setPrefHeight(400);
        assessmentTable.getStyleClass().add("dashboard-table");

        TableColumn<Assessment, String> nameCol = new TableColumn<>("Assessment");
        nameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        TableColumn<Assessment, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(cellData -> cellData.getValue().typeProperty());

        TableColumn<Assessment, Double> weightCol = new TableColumn<>("Weight");
        weightCol.setCellValueFactory(cellData -> cellData.getValue().weightProperty().asObject());

        assessmentTable.getColumns().addAll(nameCol, typeCol, weightCol);

        Button submitButton = new Button("Submit Assessment");
        submitButton.getStyleClass().add("action-button");
        submitButton.setOnAction(e -> {
            Assessment selected = assessmentTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showSubmissionDialog(selected);
            }
        });

        content.getChildren().addAll(titleLabel, assessmentTable, submitButton);
        return content;
    }

    private VBox createGradesTab() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        content.getStyleClass().add("dashboard-content");

        Label titleLabel = new Label("Grades & Transcripts");
        titleLabel.getStyleClass().add("section-title");

        TabPane gradesTabPane = new TabPane();
        gradesTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Transcript Tab
        Tab transcriptTab = new Tab("View Transcript");
        VBox transcriptContent = new VBox(15);
        transcriptContent.setPadding(new Insets(10));

        TableView<AssessmentResult> gradesTable = new TableView<>(assessmentResults);
        gradesTable.setPrefHeight(300);
        gradesTable.getStyleClass().add("dashboard-table");

        TableColumn<AssessmentResult, String> courseCol = new TableColumn<>("Course");
        courseCol.setCellValueFactory(cellData -> cellData.getValue().getAssessment().courseProperty());

        TableColumn<AssessmentResult, String> assessmentCol = new TableColumn<>("Assessment");
        assessmentCol.setCellValueFactory(cellData -> cellData.getValue().getAssessment().nameProperty());

        TableColumn<AssessmentResult, Double> scoreCol = new TableColumn<>("Score");
        scoreCol.setCellValueFactory(cellData -> cellData.getValue().scoreProperty().asObject());

        TableColumn<AssessmentResult, String> fileCol = new TableColumn<>("Submitted File");
        fileCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
            cellData.getValue().getSubmissionFile() != null ? cellData.getValue().getSubmissionFile() : "-"
        ));

        gradesTable.getColumns().addAll(courseCol, assessmentCol, scoreCol, fileCol);

        transcriptContent.getChildren().addAll(gradesTable);
        transcriptTab.setContent(transcriptContent);

        // Report Tab
        Tab reportTab = new Tab("View Report");
        VBox reportContent = new VBox(15);
        reportContent.setPadding(new Insets(10));

        // Transcript Generation Button
        Button generateTranscriptBtn = new Button("Generate Transcript");
        generateTranscriptBtn.getStyleClass().add("action-button");
        generateTranscriptBtn.setOnAction(e -> generateTranscript());

        // Analytics Section (Pie Chart)
        javafx.scene.chart.PieChart pieChart = new javafx.scene.chart.PieChart();
        pieChart.setTitle("Assessment Results Distribution");
        pieChart.getData().addAll(
            new javafx.scene.chart.PieChart.Data("A (90-100)", 5),
            new javafx.scene.chart.PieChart.Data("B (80-89)", 8),
            new javafx.scene.chart.PieChart.Data("C (70-79)", 4),
            new javafx.scene.chart.PieChart.Data("D (60-69)", 2),
            new javafx.scene.chart.PieChart.Data("F (<60)", 1)
        );
        pieChart.setLabelsVisible(true);
        pieChart.setLegendVisible(true);
        pieChart.setPrefHeight(350);

        reportContent.getChildren().addAll(generateTranscriptBtn, pieChart);
        reportTab.setContent(reportContent);

        gradesTabPane.getTabs().addAll(transcriptTab, reportTab);

        content.getChildren().addAll(titleLabel, gradesTabPane);
        return content;
    }

    private VBox createProfileTab() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        content.getStyleClass().add("dashboard-content");

        // Profile Picture
        Circle profilePicture = new Circle(50);
        profilePicture.setFill(Color.web("#3498db"));
        Label initialsLabel = new Label(getInitials(userManager.getCurrentUserFullName()));
        initialsLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px;");
        StackPane pictureStack = new StackPane(profilePicture, initialsLabel);

        // Profile Information
        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(10);
        infoGrid.setVgap(10);
        infoGrid.setPadding(new Insets(20));
        infoGrid.getStyleClass().add("profile-info");

        infoGrid.add(new Label("Full Name:"), 0, 0);
        infoGrid.add(new Label(userManager.getCurrentUserFullName()), 1, 0);
        
        infoGrid.add(new Label("Student ID:"), 0, 1);
        infoGrid.add(new Label(userManager.getCurrentUsername()), 1, 1);

        // Edit Profile Button
        Button editButton = new Button("Edit Profile");
        editButton.getStyleClass().add("action-button");
        editButton.setOnAction(e -> showEditProfileDialog());

        content.getChildren().addAll(pictureStack, infoGrid, editButton);
        return content;
    }

    private void enrollInCourse(Course course) {
        if (!enrolledCourses.contains(course)) {
            enrolledCourses.add(course);
            availableCourses.remove(course);
            showMessage("Successfully enrolled in " + course.getTitle(), false);
        } else {
            showMessage("Already enrolled in this course", true);
        }
    }

    private void showCourseContent(Course course) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Course Content: " + course.getTitle());
        dialog.setHeaderText("Course Materials and Announcements");

        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        // Course Materials
        TitledPane materialsPane = new TitledPane("Course Materials", new VBox(10));
        ListView<String> materialsList = new ListView<>();
        // Show all real materials for this course
        for (CourseMaterial mat : course.getMaterials()) {
            materialsList.getItems().add(mat.getTitle() + " (" + mat.getType() + ")");
        }
        materialsPane.setContent(materialsList);

        // Announcements
        TitledPane announcementsPane = new TitledPane("Announcements", new VBox(10));
        ListView<String> announcementsList = new ListView<>();
        for (String ann : course.getAnnouncements()) {
            announcementsList.getItems().add(ann);
        }
        announcementsPane.setContent(announcementsList);

        content.getChildren().addAll(materialsPane, announcementsPane);
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    private void showSubmissionDialog(Assessment assessment) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Submit Assessment: " + assessment.getName());
        
        // Set extension filters based on assessment type
        if (assessment.getType().equals("DOCUMENT")) {
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Documents", "*.pdf", "*.doc", "*.docx")
            );
        } else if (assessment.getType().equals("VIDEO")) {
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Videos", "*.mp4", "*.avi", "*.mkv")
            );
        }
        
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            // Store the submission in assessmentResults
            AssessmentResult result = new AssessmentResult(assessment, 0.0); // 0.0 as placeholder score
            result.setSubmissionFile(file.getAbsolutePath());
            result.setSubmissionDate(java.time.LocalDateTime.now().toString());
            assessmentResults.add(result);
            showMessage("Assessment submitted successfully!", false);
            pendingAssessments.remove(assessment);
        }
    }

    private void generateTranscript() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Transcript");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );
        
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            showMessage("Transcript generated successfully!", false);
        }
    }

    private void showEditProfileDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Profile");
        dialog.setHeaderText("Update Your Information");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField fullNameField = new TextField(userManager.getCurrentUserFullName());
        PasswordField currentPasswordField = new PasswordField();
        PasswordField newPasswordField = new PasswordField();

        grid.add(new Label("Full Name:"), 0, 0);
        grid.add(fullNameField, 1, 0);
        grid.add(new Label("Current Password:"), 0, 1);
        grid.add(currentPasswordField, 1, 1);
        grid.add(new Label("New Password:"), 0, 2);
        grid.add(newPasswordField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Update profile information
            showMessage("Profile updated successfully!", false);
        }
    }

    private String getInitials(String fullName) {
        if (fullName == null || fullName.isEmpty()) return "";
        String[] names = fullName.split(" ");
        StringBuilder initials = new StringBuilder();
        for (String name : names) {
            if (!name.isEmpty()) {
                initials.append(name.charAt(0));
            }
        }
        return initials.toString().toUpperCase();
    }

    private void showMessage(String message, boolean isError) {
        Alert alert = new Alert(isError ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        alert.setTitle(isError ? "Error" : "Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().setStyle("""
            -fx-background-color: white;
            -fx-font-family: 'Segoe UI', Arial, sans-serif;
            -fx-background-radius: 15;
            """);
        alert.showAndWait();
    }

    private void showUploadDialog(Course course) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Upload Material");
        dialog.setHeaderText("Upload Material for " + course.getTitle());
        dialog.getDialogPane().setStyle("""
            -fx-background-color: white;
            -fx-font-family: 'Segoe UI', Arial, sans-serif;
            -fx-background-radius: 15;
            """);

        VBox content = new VBox(25);
        content.setPadding(new Insets(25));

        // Material type selection
        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("PDF", "VIDEO");
        typeComboBox.setPromptText("Select material type");
        typeComboBox.setMaxWidth(Double.MAX_VALUE);
        typeComboBox.setStyle("""
            -fx-font-size: 14px;
            -fx-padding: 10;
            -fx-background-radius: 8;
            -fx-border-color: #e0e0e0;
            -fx-border-radius: 8;
            """);

        // Material title
        TextField titleField = new TextField();
        titleField.setPromptText("Enter material title");
        titleField.setMaxWidth(Double.MAX_VALUE);
        titleField.setStyle("""
            -fx-font-size: 14px;
            -fx-padding: 10;
            -fx-background-radius: 8;
            -fx-border-color: #e0e0e0;
            -fx-border-radius: 8;
            """);

        // Upload button
        Button uploadButton = new Button("Choose File");
        uploadButton.setStyle("""
            -fx-background-color: linear-gradient(to right, #2ecc71, #27ae60);
            -fx-text-fill: white;
            -fx-font-size: 15px;
            -fx-font-weight: bold;
            -fx-padding: 15 30;
            -fx-background-radius: 8;
            -fx-cursor: hand;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 3);
            """);

        uploadButton.setOnAction(e -> {
            if (typeComboBox.getValue() == null) {
                showMessage("Please select material type", true);
                return;
            }
            if (titleField.getText().trim().isEmpty()) {
                showMessage("Please enter material title", true);
                return;
            }

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Upload " + typeComboBox.getValue());
            
            if (typeComboBox.getValue().equals("PDF")) {
                fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
                );
            } else if (typeComboBox.getValue().equals("VIDEO")) {
                fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.avi", "*.mkv")
                );
            }
            
            File file = fileChooser.showOpenDialog(dialog.getDialogPane().getScene().getWindow());
            if (file != null) {
                CourseMaterial material = new CourseMaterial(
                    titleField.getText().trim(),
                    typeComboBox.getValue(),
                    file.getAbsolutePath()
                );
                course.addMaterial(material);
                showMessage("Material uploaded successfully!", false);
                dialog.close();
            }
        });

        Label typeLabel = new Label("Material Type:");
        typeLabel.setStyle("""
            -fx-font-weight: bold;
            -fx-font-size: 15px;
            -fx-text-fill: #1a237e;
            """);
        Label titleLabel = new Label("Material Title:");
        titleLabel.setStyle("""
            -fx-font-weight: bold;
            -fx-font-size: 15px;
            -fx-text-fill: #1a237e;
            """);

        content.getChildren().addAll(
            typeLabel,
            typeComboBox,
            titleLabel,
            titleField,
            uploadButton
        );

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        dialog.showAndWait();
    }

    private VBox createReportsAndAnalyticsTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.getStyleClass().add("tab-content");

        // Reports Section
        TitledPane reportsPane = new TitledPane();
        reportsPane.setText("Reports");
        reportsPane.setExpanded(true);

        VBox reportsContent = new VBox(10);
        Button generateTranscriptBtn = new Button("Generate Transcript");
        generateTranscriptBtn.getStyleClass().add("add-button");
        generateTranscriptBtn.setOnAction(e -> generateTranscript());
        // Add more report buttons as needed
        reportsContent.getChildren().addAll(generateTranscriptBtn);
        reportsPane.setContent(reportsContent);

        // Analytics Section
        TitledPane analyticsPane = new TitledPane();
        analyticsPane.setText("Analytics");
        analyticsPane.setExpanded(true);

        VBox analyticsContent = new VBox(10);
        javafx.scene.chart.PieChart pieChart = new javafx.scene.chart.PieChart();
        pieChart.setTitle("Assessment Results Distribution");
        pieChart.getData().addAll(
            new javafx.scene.chart.PieChart.Data("A (90-100)", 5),
            new javafx.scene.chart.PieChart.Data("B (80-89)", 8),
            new javafx.scene.chart.PieChart.Data("C (70-79)", 4),
            new javafx.scene.chart.PieChart.Data("D (60-69)", 2),
            new javafx.scene.chart.PieChart.Data("F (<60)", 1)
        );
        pieChart.setLabelsVisible(true);
        pieChart.setLegendVisible(true);
        pieChart.setPrefHeight(350);
        analyticsContent.getChildren().add(pieChart);
        analyticsPane.setContent(analyticsContent);

        content.getChildren().addAll(reportsPane, analyticsPane);
        return content;
    }

    public void setPrimaryStageAndHomeScene(Stage stage, Scene homeScene) {
        this.primaryStage = stage;
        this.homeScene = homeScene;
    }

    private void handleLogout() {
        if (primaryStage != null && homeScene != null) {
            primaryStage.setScene(homeScene);
        } else {
            try {
                Stage stage = (Stage) mainContent.getScene().getWindow();
                Parent homeRoot = FXMLLoader.load(getClass().getResource("/com/example/demo/hello-view.fxml"));
                Scene fallbackScene = new Scene(homeRoot);
                stage.setScene(fallbackScene);
            } catch (IOException e) {
                e.printStackTrace();
                showMessage("Failed to load home page.", true);
            }
        }
    }

    private VBox createNotificationPanel() {
        VBox notificationPanel = new VBox(10);
        notificationPanel.getStyleClass().add("notification-panel");
        notificationPanel.setPadding(new Insets(15));

        // Notification header
        HBox headerBox = new HBox(10);
        Label titleLabel = new Label("Notifications");
        titleLabel.getStyleClass().add("notification-title");
        headerBox.getChildren().add(titleLabel);

        // Notification list
        ListView<String> notificationList = new ListView<>(com.example.demo.models.UserManager.globalAnnouncements);
        notificationList.setPrefHeight(200);
        notificationList.getStyleClass().add("notification-list");

        notificationPanel.getChildren().addAll(headerBox, notificationList);
        return notificationPanel;
    }
} 