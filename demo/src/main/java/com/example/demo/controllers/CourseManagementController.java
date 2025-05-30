package com.example.demo.controllers;

import com.example.demo.models.Assessment;
import com.example.demo.models.AssessmentResult;
import com.example.demo.models.Course;
import com.example.demo.models.Student;
import com.example.demo.models.UserManager;
import com.example.demo.models.Faculty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.time.LocalDateTime;
import java.util.Optional;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.control.Tooltip;

public class CourseManagementController {
    private VBox mainContent;
    private ObservableList<Course> allCourses;
    private ObservableList<Course> facultyCourses;
    private ObservableList<String> announcements;
    private TabPane contentTabPane;
    private UserManager userManager;

    public CourseManagementController(UserManager userManager) {
        this.userManager = userManager;
        this.allCourses = FXCollections.observableArrayList();
        this.facultyCourses = FXCollections.observableArrayList();
        this.announcements = FXCollections.observableArrayList();
        
        // Debug print
        System.out.println("Constructor - UserManager faculty: " + 
            (userManager != null ? userManager.getCurrentUserFaculty() : "null"));
        
        initializeSampleCourses();
        updateFacultyCourses();
    }

    private void initializeSampleCourses() {
        System.out.println("Initializing sample courses...");
        
        // Computer Science & Engineering Courses
        addCourse(new Course("CS101", "Introduction to Computer Science", 
            "Basic programming concepts, algorithms, and problem-solving techniques", Faculty.ENGINEERING));
        addCourse(new Course("CS201", "Data Structures and Algorithms", 
            "Advanced data structures, algorithm analysis, and complexity theory", Faculty.ENGINEERING));
        addCourse(new Course("CS301", "Software Engineering", 
            "Software development lifecycle, design patterns, and best practices", Faculty.ENGINEERING));
        addCourse(new Course("CS401", "Database Systems", 
            "Database design, SQL, normalization, and transaction management", Faculty.ENGINEERING));
        addCourse(new Course("CS501", "Web Development", 
            "HTML, CSS, JavaScript, and modern web frameworks", Faculty.ENGINEERING));
        addCourse(new Course("CS601", "Mobile App Development", 
            "iOS and Android development, mobile UI/UX design", Faculty.ENGINEERING));
        addCourse(new Course("CS701", "Cloud Computing", 
            "Cloud platforms, virtualization, and distributed systems", Faculty.ENGINEERING));
        addCourse(new Course("CS801", "Artificial Intelligence", 
            "Machine learning, neural networks, and AI applications", Faculty.ENGINEERING));
        addCourse(new Course("CS901", "Cybersecurity", 
            "Network security, cryptography, and ethical hacking", Faculty.ENGINEERING));
            
        // Science Courses
        addCourse(new Course("SCI101", "Physics I", 
            "Classical mechanics, thermodynamics, and wave phenomena", Faculty.SCIENCE));
        addCourse(new Course("SCI201", "Chemistry I", 
            "Atomic structure, chemical bonding, and reactions", Faculty.SCIENCE));
        addCourse(new Course("SCI301", "Biology I", 
            "Cell biology, genetics, and evolution", Faculty.SCIENCE));
        addCourse(new Course("SCI401", "Mathematics I", 
            "Calculus, linear algebra, and differential equations", Faculty.SCIENCE));
            
        // Business Courses
        addCourse(new Course("BUS101", "Business Management", 
            "Principles of management, organizational behavior, and leadership", Faculty.BUSINESS));
        addCourse(new Course("BUS201", "Marketing", 
            "Marketing strategies, consumer behavior, and market research", Faculty.BUSINESS));
        addCourse(new Course("BUS301", "Finance", 
            "Financial analysis, investment, and corporate finance", Faculty.BUSINESS));
        addCourse(new Course("BUS401", "Entrepreneurship", 
            "Business planning, startup management, and innovation", Faculty.BUSINESS));
            
        // Arts Courses
        addCourse(new Course("ART101", "Digital Design", 
            "Graphic design, UI/UX, and digital media", Faculty.ARTS));
        addCourse(new Course("ART201", "Visual Arts", 
            "Drawing, painting, and visual composition", Faculty.ARTS));
        addCourse(new Course("ART301", "Music Theory", 
            "Musical notation, harmony, and composition", Faculty.ARTS));
        addCourse(new Course("ART401", "Digital Media", 
            "Video production, animation, and multimedia", Faculty.ARTS));
            
        System.out.println("Initialized " + allCourses.size() + " courses");
        System.out.println("Current faculty courses: " + facultyCourses.size());
    }

    private void addCourse(Course course) {
        allCourses.add(course);
        Faculty userFaculty = userManager.getCurrentUserFaculty();
        System.out.println("Adding course: " + course.getTitle() + 
                         " (Faculty: " + course.getFaculty() + 
                         "), User Faculty: " + userFaculty);
                         
        if (userFaculty != null && course.getFaculty() == userFaculty) {
            facultyCourses.add(course);
            System.out.println("Added to faculty courses: " + course.getTitle());
        }
    }

    private void updateFacultyCourses() {
        Faculty userFaculty = userManager.getCurrentUserFaculty();
        System.out.println("Updating faculty courses. User Faculty: " + userFaculty);
        
        if (userFaculty != null) {
            facultyCourses.clear();
            for (Course course : allCourses) {
                if (course.getFaculty() == userFaculty) {
                    facultyCourses.add(course);
                    System.out.println("Added to faculty courses: " + course.getTitle());
                }
            }
        }
        System.out.println("Faculty courses after update: " + facultyCourses.size());
    }

    public VBox createCourseManagementView() {
        VBox root = new VBox(20);
        root.getStyleClass().add("course-management");
        root.setPadding(new Insets(20));

        // Add notification badge
        Label notificationBadge = new Label("New");
        notificationBadge.getStyleClass().add("notification-badge");
        notificationBadge.setVisible(false);

        // Course List with Progress
        VBox courseList = new VBox(15);
        courseList.getStyleClass().add("course-list");

        for (Course course : getCourses()) {
            VBox courseCard = createCourseCard(course);
            // Add fade-in animation
            courseCard.setOpacity(0);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(500), courseCard);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
            
            courseList.getChildren().add(courseCard);
        }

        // Add ScrollPane with improved styling
        ScrollPane scrollPane = new ScrollPane(courseList);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.getStyleClass().add("course-scroll-pane");
        scrollPane.setEffect(new DropShadow(10, Color.rgb(0, 0, 0, 0.2)));

        // Announcements Forum Tab
        TitledPane announcementsForumPane = new TitledPane();
        announcementsForumPane.setText("Announcements Forum");
        VBox forumContent = new VBox(10);
        forumContent.setPadding(new Insets(10));
        Label forumTitle = new Label("All Announcements");
        forumTitle.getStyleClass().add("section-title");
        ListView<String> forumList = new ListView<>(com.example.demo.models.UserManager.globalAnnouncements);
        forumList.setPrefHeight(200);
        forumList.getStyleClass().add("dashboard-table");
        forumContent.getChildren().addAll(forumTitle, forumList);
        announcementsForumPane.setContent(forumContent);

        root.getChildren().addAll(notificationBadge, scrollPane, announcementsForumPane);
        return root;
    }

    private VBox createCourseCard(Course course) {
        VBox card = new VBox(15);
        card.getStyleClass().add("course-card");
        card.setPadding(new Insets(20));

        // Title
        Label titleLabel = new Label(course.getTitle());
        titleLabel.getStyleClass().add("course-title");

        // Description
        Label descLabel = new Label(course.getDescription());
        descLabel.getStyleClass().add("course-desc");
        descLabel.setWrapText(true);

        // Progress indicator
        ProgressBar progressBar = new ProgressBar(course.getProgress());
        progressBar.getStyleClass().add("course-progress");
        progressBar.setPrefWidth(200);

        // Student count
        HBox studentCount = new HBox(5);
        Label studentIcon = new Label("👥");
        Label studentLabel = new Label(course.getStudentCount() + " students");
        studentCount.getChildren().addAll(studentIcon, studentLabel);

        // Action buttons
        HBox actionBox = new HBox(10);
        Button editBtn = new Button("Edit");
        editBtn.getStyleClass().add("action-button");
        Button deleteBtn = new Button("Delete");
        deleteBtn.getStyleClass().add("action-button");
        actionBox.getChildren().addAll(editBtn, deleteBtn);

        // Add all to card
        card.getChildren().addAll(titleLabel, descLabel, progressBar, studentCount, actionBox);

        // Hover effect
        card.setOnMouseEntered(e -> {
            card.setEffect(new DropShadow(20, Color.rgb(0, 0, 0, 0.3)));
            card.setTranslateY(-2);
        });
        card.setOnMouseExited(e -> {
            card.setEffect(new DropShadow(10, Color.rgb(0, 0, 0, 0.2)));
            card.setTranslateY(0);
        });

        return card;
    }

    private void styleTab(Tab tab, String icon) {
        Label label = new Label(icon + " " + tab.getText());
        label.setStyle("-fx-font-size: 14px;");
        tab.setGraphic(label);
    }

    public VBox createCourseManagementTab() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        content.getStyleClass().add("section-card");

        // Course Management Section
        TitledPane courseManagementPane = new TitledPane();
        courseManagementPane.setText("Course Management");
        courseManagementPane.setStyle("-fx-font-size: 16px;");
        
        VBox courseManagementContent = new VBox(15);
        courseManagementContent.setPadding(new Insets(20));
        courseManagementContent.setStyle("-fx-background-color: white;");
        
        // Create Course Button with icon
        Button createCourseBtn = new Button("Create New Course");
        createCourseBtn.getStyleClass().add("action-button-alt");
        createCourseBtn.setOnAction(e -> showCreateCourseDialog());

        // Course Table with enhanced styling
        TableView<Course> courseTable = new TableView<>();
        courseTable.setItems(facultyCourses);
        courseTable.setPrefHeight(400);
        courseTable.getStyleClass().add("table-view");

        TableColumn<Course, String> idCol = new TableColumn<>("Course ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(100);

        TableColumn<Course, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setPrefWidth(250);

        TableColumn<Course, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        descCol.setPrefWidth(350);

        TableColumn<Course, Faculty> facultyCol = new TableColumn<>("Faculty");
        facultyCol.setCellValueFactory(new PropertyValueFactory<>("faculty"));
        facultyCol.setPrefWidth(150);

        courseTable.getColumns().addAll(idCol, titleCol, descCol, facultyCol);

        // Style the table columns
        String columnStyle = "-fx-alignment: CENTER-LEFT; -fx-padding: 5;";
        idCol.setStyle(columnStyle);
        titleCol.setStyle(columnStyle);
        descCol.setStyle(columnStyle);
        facultyCol.setStyle(columnStyle);

        // Action Buttons with icons and styling
        HBox actionButtons = new HBox(10);
        actionButtons.setAlignment(Pos.CENTER_LEFT);

        Button editCourseBtn = createStyledButton("Edit Course", "#3498db");
        Button deleteCourseBtn = createStyledButton("Delete Course", "#e74c3c");
        Button viewStudentsBtn = createStyledButton("View Students", "#9b59b6");

        editCourseBtn.getStyleClass().add("action-button");
        deleteCourseBtn.getStyleClass().add("action-button-alt");
        viewStudentsBtn.getStyleClass().add("action-button");

        editCourseBtn.setTooltip(new Tooltip("Edit selected course"));
        deleteCourseBtn.setTooltip(new Tooltip("Delete selected course"));
        viewStudentsBtn.setTooltip(new Tooltip("View students in selected course"));

        editCourseBtn.setOnAction(e -> {
            Course selected = courseTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showEditCourseDialog(selected);
                courseTable.refresh();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Course updated successfully!");
                alert.showAndWait();
            }
        });

        deleteCourseBtn.setOnAction(e -> {
            Course selected = courseTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + selected.getTitle() + "?", ButtonType.YES, ButtonType.NO);
                alert.setHeaderText("Are you sure?");
                alert.showAndWait().ifPresent(type -> {
                    if (type == ButtonType.YES) {
                        facultyCourses.remove(selected);
                        allCourses.remove(selected);
                        courseTable.refresh();
                        Alert done = new Alert(Alert.AlertType.INFORMATION, "Course deleted successfully!");
                        done.showAndWait();
                    }
                });
            }
        });

        viewStudentsBtn.setOnAction(e -> {
            Course selected = courseTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showStudentList(selected);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a course to view students.");
                alert.showAndWait();
            }
        });

        actionButtons.getChildren().addAll(editCourseBtn, deleteCourseBtn, viewStudentsBtn);

        // Announcements Section
        TitledPane announcementsPane = new TitledPane();
        announcementsPane.setText("Course Announcements");
        announcementsPane.setStyle("-fx-font-size: 16px;");

        VBox announcementsContent = new VBox(10);
        announcementsContent.setPadding(new Insets(15));
        announcementsContent.setStyle("-fx-background-color: white;");

        TextArea announcementText = new TextArea();
        announcementText.setPromptText("Type your announcement here...");
        announcementText.setPrefRowCount(3);
        announcementText.setStyle("""
            -fx-background-color: #f8f9fa;
            -fx-border-color: #dee2e6;
            -fx-border-radius: 5;
            -fx-background-radius: 5;
            -fx-font-size: 14px;
            """);

        Button sendAnnouncementBtn = new Button("Send Announcement");
        sendAnnouncementBtn.getStyleClass().add("action-button");
        sendAnnouncementBtn.setOnAction(e -> {
            String text = announcementText.getText().trim();
            if (!text.isEmpty()) {
                String announcement = LocalDateTime.now() + ": " + text;
                announcements.add(0, announcement);
                com.example.demo.models.UserManager.globalAnnouncements.add(0, announcement);
                announcementText.clear();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Announcement sent!");
                alert.showAndWait();
            }
        });

        ListView<String> announcementsList = new ListView<>(announcements);
        announcementsList.setPrefHeight(200);
        announcementsList.setStyle("""
            -fx-background-color: white;
            -fx-border-color: #dee2e6;
            -fx-border-radius: 5;
            -fx-background-radius: 5;
            """);

        announcementsContent.getChildren().setAll(announcementText, sendAnnouncementBtn, announcementsList);
        announcementsPane.setContent(announcementsContent);

        courseManagementContent.getChildren().addAll(createCourseBtn, courseTable, actionButtons);
        courseManagementPane.setContent(courseManagementContent);

        content.getChildren().addAll(courseManagementPane, announcementsPane);
        return content;
    }

    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle(String.format("""
            -fx-background-color: %s;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-padding: 8 15;
            -fx-background-radius: 5;
            -fx-cursor: hand;
            """, color));
        return button;
    }

    private void styleAlert(Alert alert) {
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("""
            -fx-background-color: white;
            -fx-padding: 20;
            """);
        
        dialogPane.getButtonTypes().stream()
            .map(dialogPane::lookupButton)
            .forEach(button -> button.setStyle("""
                -fx-background-color: #3498db;
                -fx-text-fill: white;
                -fx-font-size: 14px;
                -fx-padding: 8 15;
                -fx-background-radius: 5;
                """));
    }

    public VBox createContentManagementTab() {
        VBox content = new VBox(25);
        content.setPadding(new Insets(25));
        content.getStyleClass().add("section-card");
        content.setStyle("""
            -fx-background-color: linear-gradient(to bottom right, #f8f9fa, #e9ecef);
            -fx-background-radius: 10;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);
            -fx-border-color: #e0e0e0;
            -fx-border-width: 1;
            -fx-border-radius: 10;
        """);

        // Decorative Background Elements
        StackPane backgroundPane = new StackPane();
        backgroundPane.setStyle("""
            -fx-background-color: transparent;
            -fx-padding: 20;
        """);
        
        // Add decorative emojis in corners
        Label topLeftEmoji = new Label("📚");
        topLeftEmoji.setStyle("-fx-font-size: 40px; -fx-opacity: 0.1;");
        StackPane.setAlignment(topLeftEmoji, Pos.TOP_LEFT);
        
        Label bottomRightEmoji = new Label("📝");
        bottomRightEmoji.setStyle("-fx-font-size: 40px; -fx-opacity: 0.1;");
        StackPane.setAlignment(bottomRightEmoji, Pos.BOTTOM_RIGHT);
        
        backgroundPane.getChildren().addAll(topLeftEmoji, bottomRightEmoji);

        // Section Title with Icon
        HBox titleBox = new HBox(10);
        titleBox.setAlignment(Pos.CENTER_LEFT);
        Label sectionTitle = new Label("Content Management");
        sectionTitle.getStyleClass().add("section-title");
        Label iconLabel = new Label("📚");
        iconLabel.setStyle("-fx-font-size: 24px;");
        titleBox.getChildren().addAll(iconLabel, sectionTitle);

        // Content Upload Section with enhanced styling
        TitledPane uploadPane = new TitledPane();
        uploadPane.setText("Upload Content");
        uploadPane.setStyle("""
            -fx-font-size: 16px;
            -fx-text-fill: #2c3e50;
            -fx-background-color: white;
            -fx-background-radius: 5;
            -fx-border-radius: 5;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 1);
            -fx-border-color: #e0e0e0;
            -fx-border-width: 1;
        """);
        
        VBox uploadContent = new VBox(20);
        uploadContent.setPadding(new Insets(20));
        uploadContent.setStyle("-fx-background-color: white; -fx-background-radius: 5;");

        HBox uploadButtons = new HBox(15);
        uploadButtons.setAlignment(Pos.CENTER_LEFT);
        
        final ListView<String> contentList = new ListView<>();
        final ListView<String> discussionsList = new ListView<>();
        final Button uploadVideoBtn = new Button("📹 Upload Video");
        final Button uploadPdfBtn = new Button("📄 Upload PDF");
        final Button createTopicBtn = new Button("💬 Create New Topic");
        final TextArea newTopicText = new TextArea();

        uploadVideoBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Upload Video");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.avi", "*.mkv"));
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                contentList.getItems().add("Video: " + file.getName());
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Video uploaded successfully!");
                alert.showAndWait();
            }
        });
        uploadPdfBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Upload PDF");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                contentList.getItems().add("PDF: " + file.getName());
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "PDF uploaded successfully!");
                alert.showAndWait();
            }
        });
        createTopicBtn.setOnAction(e -> {
            String topic = newTopicText.getText().trim();
            if (!topic.isEmpty()) {
                discussionsList.getItems().add(0, topic);
                newTopicText.clear();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Discussion topic created!");
                alert.showAndWait();
            }
        });

        uploadButtons.getChildren().addAll(uploadVideoBtn, uploadPdfBtn);

        Label contentListLabel = new Label("Uploaded Content");
        contentListLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        contentList.setPrefHeight(250);
        contentList.getStyleClass().add("table-view");
        contentList.setStyle("""
            -fx-background-color: white;
            -fx-border-color: #e0e0e0;
            -fx-border-radius: 5;
            -fx-background-radius: 5;
            -fx-padding: 5;
        """);

        uploadContent.getChildren().addAll(uploadButtons, contentListLabel, contentList);
        uploadPane.setContent(uploadContent);

        // Discussion Forum Section
        TitledPane forumPane = new TitledPane();
        forumPane.setText("Discussion Forum");
        forumPane.setStyle("""
            -fx-font-size: 16px;
            -fx-text-fill: #2c3e50;
            -fx-background-color: white;
            -fx-background-radius: 5;
            -fx-border-radius: 5;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 1);
        """);
        
        VBox forumContent = new VBox(20);
        forumContent.setPadding(new Insets(20));
        forumContent.setStyle("-fx-background-color: white; -fx-background-radius: 5;");

        Label discussionsLabel = new Label("Recent Discussions");
        discussionsLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        forumContent.getChildren().addAll(newTopicText, createTopicBtn, discussionsLabel, discussionsList);
        forumPane.setContent(forumContent);

        content.getChildren().addAll(uploadPane, forumPane);
        return content;
    }

    public VBox createAssessmentsTab() {
        VBox content = new VBox(25);
        content.setPadding(new Insets(25));
        content.getStyleClass().add("section-card");
        content.setStyle("""
            -fx-background-color: linear-gradient(to bottom right, #f8f9fa, #e9ecef);
            -fx-background-radius: 10;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);
            -fx-border-color: #e0e0e0;
            -fx-border-width: 1;
            -fx-border-radius: 10;
        """);

        // Decorative Background Elements
        StackPane backgroundPane = new StackPane();
        backgroundPane.setStyle("""
            -fx-background-color: transparent;
            -fx-padding: 20;
        """);
        
        // Add decorative emojis in corners
        Label topLeftEmoji = new Label("📋");
        topLeftEmoji.setStyle("-fx-font-size: 40px; -fx-opacity: 0.1;");
        StackPane.setAlignment(topLeftEmoji, Pos.TOP_LEFT);
        
        Label bottomRightEmoji = new Label("✏️");
        bottomRightEmoji.setStyle("-fx-font-size: 40px; -fx-opacity: 0.1;");
        StackPane.setAlignment(bottomRightEmoji, Pos.BOTTOM_RIGHT);
        
        backgroundPane.getChildren().addAll(topLeftEmoji, bottomRightEmoji);

        // Section Title with Icon
        HBox titleBox = new HBox(10);
        titleBox.setAlignment(Pos.CENTER_LEFT);
        Label sectionTitle = new Label("Assessments");
        sectionTitle.getStyleClass().add("section-title");
        Label iconLabel = new Label("📋");
        iconLabel.setStyle("-fx-font-size: 24px;");
        titleBox.getChildren().addAll(iconLabel, sectionTitle);

        // Assessment Creation Section
        TitledPane createAssessmentPane = new TitledPane();
        createAssessmentPane.setText("Create Assessment");
        createAssessmentPane.setStyle("""
            -fx-font-size: 16px;
            -fx-text-fill: #2c3e50;
            -fx-background-color: white;
            -fx-background-radius: 5;
            -fx-border-radius: 5;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 1);
        """);
        
        VBox createAssessmentContent = new VBox(20);
        createAssessmentContent.setPadding(new Insets(20));
        createAssessmentContent.setStyle("-fx-background-color: white; -fx-background-radius: 5;");

        HBox assessmentButtons = new HBox(15);
        assessmentButtons.setAlignment(Pos.CENTER_LEFT);

        final TableView<Assessment> assessmentTable = new TableView<>();
        final Button createMCQBtn = new Button("✏️ Create Multiple Choice Quiz");
        final Button createShortAnswerBtn = new Button("📝 Create Short Answer Test");
        final Button uploadAssessmentBtn = new Button("📤 Upload Assessment File");

        createMCQBtn.setOnAction(e -> {
            assessmentTable.getItems().add(new Assessment("MCQ" + (assessmentTable.getItems().size() + 1), "Sample MCQ Quiz", "MCQ", 0.2, 100));
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Multiple choice quiz created!");
            alert.showAndWait();
        });
        createShortAnswerBtn.setOnAction(e -> {
            assessmentTable.getItems().add(new Assessment("SA" + (assessmentTable.getItems().size() + 1), "Sample Short Answer Test", "Short Answer", 0.2, 100));
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Short answer test created!");
            alert.showAndWait();
        });
        uploadAssessmentBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Upload Assessment");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Assessment Files", "*.pdf", "*.docx", "*.txt"));
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                assessmentTable.getItems().add(new Assessment("UPL" + (assessmentTable.getItems().size() + 1), file.getName(), "Uploaded", 0.1, 100));
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Assessment uploaded!");
                alert.showAndWait();
            }
        });

        createMCQBtn.getStyleClass().add("action-button");
        createShortAnswerBtn.getStyleClass().add("action-button-alt");
        uploadAssessmentBtn.getStyleClass().add("action-button");

        createMCQBtn.setTooltip(new Tooltip("Create a multiple choice quiz"));
        createShortAnswerBtn.setTooltip(new Tooltip("Create a short answer test"));
        uploadAssessmentBtn.setTooltip(new Tooltip("Upload an assessment file"));

        createMCQBtn.setStyle("-fx-font-size: 14px; -fx-padding: 10 20;");
        createShortAnswerBtn.setStyle("-fx-font-size: 14px; -fx-padding: 10 20;");
        uploadAssessmentBtn.setStyle("-fx-font-size: 14px; -fx-padding: 10 20;");

        assessmentButtons.getChildren().addAll(createMCQBtn, createShortAnswerBtn, uploadAssessmentBtn);
        createAssessmentContent.getChildren().add(assessmentButtons);
        createAssessmentPane.setContent(createAssessmentContent);

        // Grading Section
        TitledPane gradingPane = new TitledPane();
        gradingPane.setText("Grading");
        gradingPane.setStyle("""
            -fx-font-size: 16px;
            -fx-text-fill: #2c3e50;
            -fx-background-color: white;
            -fx-background-radius: 5;
            -fx-border-radius: 5;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 1);
        """);
        
        VBox gradingContent = new VBox(20);
        gradingContent.setPadding(new Insets(20));
        gradingContent.setStyle("-fx-background-color: white; -fx-background-radius: 5;");

        Button gradeBtn = new Button("📊 Grade Selected Assessment");
        gradeBtn.getStyleClass().add("action-button");
        gradeBtn.setStyle("-fx-font-size: 14px; -fx-padding: 10 20;");

        gradeBtn.setTooltip(new Tooltip("Grade the selected assessment"));

        gradingContent.getChildren().addAll(assessmentTable, gradeBtn);
        gradingPane.setContent(gradingContent);

        content.getChildren().addAll(createAssessmentPane, gradingPane);
        return content;
    }

    public VBox createReportsTab() {
        VBox content = new VBox(25);
        content.setPadding(new Insets(25));
        content.getStyleClass().add("section-card");
        content.setStyle("""
            -fx-background-color: linear-gradient(to bottom right, #f8f9fa, #e9ecef);
            -fx-background-radius: 10;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);
            -fx-border-color: #e0e0e0;
            -fx-border-width: 1;
            -fx-border-radius: 10;
        """);

        // Decorative Background Elements
        StackPane backgroundPane = new StackPane();
        backgroundPane.setStyle("""
            -fx-background-color: transparent;
            -fx-padding: 20;
        """);
        
        // Add decorative emojis in corners
        Label topLeftEmoji = new Label("📊");
        topLeftEmoji.setStyle("-fx-font-size: 40px; -fx-opacity: 0.1;");
        StackPane.setAlignment(topLeftEmoji, Pos.TOP_LEFT);
        
        Label bottomRightEmoji = new Label("📈");
        bottomRightEmoji.setStyle("-fx-font-size: 40px; -fx-opacity: 0.1;");
        StackPane.setAlignment(bottomRightEmoji, Pos.BOTTOM_RIGHT);
        
        backgroundPane.getChildren().addAll(topLeftEmoji, bottomRightEmoji);

        // Section Title with Icon
        HBox titleBox = new HBox(10);
        titleBox.setAlignment(Pos.CENTER_LEFT);
        Label sectionTitle = new Label("Reports & Analytics");
        sectionTitle.getStyleClass().add("section-title");
        Label iconLabel = new Label("📊");
        iconLabel.setStyle("-fx-font-size: 24px;");
        titleBox.getChildren().addAll(iconLabel, sectionTitle);

        // Reports Generation Section
        TitledPane reportsPane = new TitledPane();
        reportsPane.setText("Reports");
        reportsPane.setStyle("""
            -fx-font-size: 16px;
            -fx-text-fill: #2c3e50;
            -fx-background-color: white;
            -fx-background-radius: 5;
            -fx-border-radius: 5;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 1);
        """);
        
        VBox reportsContent = new VBox(20);
        reportsContent.setPadding(new Insets(20));
        reportsContent.setStyle("-fx-background-color: white; -fx-background-radius: 5;");

        VBox reportButtons = new VBox(15);
        reportButtons.setAlignment(Pos.CENTER_LEFT);

        final Button generateTranscriptBtn = new Button("📄 Generate Transcripts");
        final Button classPerformanceBtn = new Button("📈 Class Performance Report");
        final Button certificationsBtn = new Button("🏆 Manage Certifications");

        generateTranscriptBtn.setOnAction(e -> {
            generateTranscripts();
        });
        classPerformanceBtn.setOnAction(e -> {
            showClassPerformance();
        });
        certificationsBtn.setOnAction(e -> {
            manageCertifications();
        });

        generateTranscriptBtn.getStyleClass().add("action-button");
        classPerformanceBtn.getStyleClass().add("action-button-alt");
        certificationsBtn.getStyleClass().add("action-button");

        generateTranscriptBtn.setTooltip(new Tooltip("Generate transcripts for students"));
        classPerformanceBtn.setTooltip(new Tooltip("View class performance report"));
        certificationsBtn.setTooltip(new Tooltip("Manage course certifications"));

        generateTranscriptBtn.setStyle("-fx-font-size: 14px; -fx-padding: 10 20;");
        classPerformanceBtn.setStyle("-fx-font-size: 14px; -fx-padding: 10 20;");
        certificationsBtn.setStyle("-fx-font-size: 14px; -fx-padding: 10 20;");

        reportButtons.getChildren().addAll(generateTranscriptBtn, classPerformanceBtn, certificationsBtn);
        reportsContent.getChildren().add(reportButtons);
        reportsPane.setContent(reportsContent);

        // Analytics Section
        TitledPane analyticsPane = new TitledPane();
        analyticsPane.setText("Analytics");
        analyticsPane.setStyle("""
            -fx-font-size: 16px;
            -fx-text-fill: #2c3e50;
            -fx-background-color: white;
            -fx-background-radius: 5;
            -fx-border-radius: 5;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 1);
        """);
        
        VBox analyticsContent = new VBox(20);
        analyticsContent.setPadding(new Insets(20));
        analyticsContent.setStyle("-fx-background-color: white; -fx-background-radius: 5;");

        // Pie Chart: Assessment Results Distribution
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
        pieChart.setPrefHeight(300);

        // Bar Chart: Course Enrollment
        javafx.scene.chart.CategoryAxis xAxis = new javafx.scene.chart.CategoryAxis();
        javafx.scene.chart.NumberAxis yAxis = new javafx.scene.chart.NumberAxis();
        javafx.scene.chart.BarChart<String, Number> barChart = new javafx.scene.chart.BarChart<>(xAxis, yAxis);
        barChart.setTitle("Course Enrollment");
        xAxis.setLabel("Course");
        yAxis.setLabel("Students");
        javafx.scene.chart.XYChart.Series<String, Number> series = new javafx.scene.chart.XYChart.Series<>();
        series.setName("Enrollment");
        series.getData().add(new javafx.scene.chart.XYChart.Data<>("CS101", 30));
        series.getData().add(new javafx.scene.chart.XYChart.Data<>("CS201", 25));
        series.getData().add(new javafx.scene.chart.XYChart.Data<>("CS301", 18));
        series.getData().add(new javafx.scene.chart.XYChart.Data<>("BUS101", 22));
        series.getData().add(new javafx.scene.chart.XYChart.Data<>("SCI101", 15));
        barChart.getData().add(series);
        barChart.setPrefHeight(300);

        analyticsContent.getChildren().addAll(pieChart, barChart);
        analyticsPane.setContent(analyticsContent);

        content.getChildren().addAll(reportsPane, analyticsPane);
        return content;
    }

    public VBox createProfileTab() {
        VBox profileContent = new VBox(25);
        profileContent.setPadding(new Insets(25));
        profileContent.getStyleClass().add("section-card");
        profileContent.setStyle("""
            -fx-background-color: linear-gradient(to bottom right, #f8f9fa, #e9ecef);
            -fx-background-radius: 10;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);
            -fx-border-color: #e0e0e0;
            -fx-border-width: 1;
            -fx-border-radius: 10;
        """);

        // Decorative Background Elements
        StackPane backgroundPane = new StackPane();
        backgroundPane.setStyle("""
            -fx-background-color: transparent;
            -fx-padding: 20;
        """);
        
        // Add decorative emojis in corners
        Label topLeftEmoji = new Label("👤");
        topLeftEmoji.setStyle("-fx-font-size: 40px; -fx-opacity: 0.1;");
        StackPane.setAlignment(topLeftEmoji, Pos.TOP_LEFT);
        
        Label bottomRightEmoji = new Label("🎓");
        bottomRightEmoji.setStyle("-fx-font-size: 40px; -fx-opacity: 0.1;");
        StackPane.setAlignment(bottomRightEmoji, Pos.BOTTOM_RIGHT);
        
        backgroundPane.getChildren().addAll(topLeftEmoji, bottomRightEmoji);

        // Profile Card with enhanced styling
        VBox profileCard = new VBox(25);
        profileCard.getStyleClass().add("profile-card");
        profileCard.setPadding(new Insets(30));
        profileCard.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 10;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);
            -fx-border-color: #e0e0e0;
            -fx-border-width: 1;
            -fx-border-radius: 10;
        """);

        // Profile Picture with enhanced styling
        StackPane pictureStack = new StackPane();
        Circle profilePicture = new Circle(60);
        profilePicture.setFill(Color.web("#3498db"));
        profilePicture.setStyle("""
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);
            -fx-stroke: white;
            -fx-stroke-width: 3;
        """);

        // Add decorative elements around profile picture
        Circle outerCircle = new Circle(65);
        outerCircle.setFill(Color.TRANSPARENT);
        outerCircle.setStroke(Color.web("#3498db"));
        outerCircle.setStrokeWidth(2);
        outerCircle.setStyle("-fx-opacity: 0.5;");

        Label initialsLabel = new Label(getInitials(userManager.getCurrentUserFullName()));
        initialsLabel.getStyleClass().add("profile-initials");
        initialsLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: white; -fx-font-weight: bold;");

        pictureStack.getChildren().addAll(outerCircle, profilePicture, initialsLabel);

        // Profile Details
        VBox detailsBox = new VBox(15);
        detailsBox.getStyleClass().add("profile-details");
        detailsBox.setStyle("-fx-padding: 20; -fx-background-color: #f8f9fa; -fx-background-radius: 5;");

        Label nameLabel = new Label("Full Name");
        nameLabel.getStyleClass().add("profile-label");
        nameLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");
        Label nameValue = new Label(userManager.getCurrentUserFullName());
        nameValue.getStyleClass().add("profile-value");
        nameValue.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label usernameLabel = new Label("Username");
        usernameLabel.getStyleClass().add("profile-label");
        usernameLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");
        Label usernameValue = new Label(userManager.getCurrentUsername());
        usernameValue.getStyleClass().add("profile-value");
        usernameValue.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label facultyLabel = new Label("Faculty");
        facultyLabel.getStyleClass().add("profile-label");
        facultyLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");
        Label facultyValue = new Label(userManager.getCurrentUserFaculty().getDisplayName());
        facultyValue.getStyleClass().add("profile-value");
        facultyValue.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label roleLabel = new Label("Role");
        roleLabel.getStyleClass().add("profile-label");
        roleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");
        Label roleValue = new Label("Lecturer");
        roleValue.getStyleClass().add("profile-value");
        roleValue.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        detailsBox.getChildren().addAll(
            nameLabel, nameValue,
            usernameLabel, usernameValue,
            facultyLabel, facultyValue,
            roleLabel, roleValue
        );

        // Stats Section with enhanced styling
        HBox statsBox = new HBox(30);
        statsBox.getStyleClass().add("profile-stats");
        statsBox.setAlignment(Pos.CENTER);
        statsBox.setStyle("""
            -fx-padding: 20;
            -fx-background-color: #f8f9fa;
            -fx-background-radius: 5;
            -fx-border-color: #e0e0e0;
            -fx-border-width: 1;
            -fx-border-radius: 5;
        """);

        // Add decorative elements between stats
        for (int i = 0; i < 2; i++) {
            Line separator = new Line();
            separator.setStyle("-fx-stroke: #e0e0e0; -fx-stroke-width: 1;");
            separator.setStartY(0);
            separator.setEndY(50);
            statsBox.getChildren().add(separator);
        }

        statsBox.getChildren().addAll(
            createStatItem("Courses", String.valueOf(facultyCourses.size())),
            createStatItem("Students", "150+"),
            createStatItem("Years Experience", "5+")
        );

        // Edit Profile Button
        Button editProfileBtn = new Button("✏️ Edit Profile");
        editProfileBtn.getStyleClass().add("edit-profile-button");
        editProfileBtn.setStyle("""
            -fx-font-size: 14px;
            -fx-padding: 10 20;
            -fx-background-color: #3498db;
            -fx-text-fill: white;
            -fx-background-radius: 5;
            -fx-cursor: hand;
        """);

        editProfileBtn.setTooltip(new Tooltip("Edit your profile information"));

        profileCard.getChildren().addAll(pictureStack, detailsBox, statsBox, editProfileBtn);
        profileContent.getChildren().add(profileCard);

        return profileContent;
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

    private VBox createStatItem(String label, String value) {
        VBox statBox = new VBox(5);
        statBox.setAlignment(Pos.CENTER);
        statBox.getStyleClass().add("profile-stat-item");
        statBox.setStyle("""
            -fx-padding: 15;
            -fx-background-color: white;
            -fx-background-radius: 5;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 1);
            -fx-border-color: #e0e0e0;
            -fx-border-width: 1;
            -fx-border-radius: 5;
        """);

        // Add decorative icon based on stat type
        String emoji = switch (label) {
            case "Courses" -> "📚";
            case "Students" -> "👥";
            case "Years Experience" -> "⭐";
            default -> "📊";
        };

        Label iconLabel = new Label(emoji);
        iconLabel.setStyle("-fx-font-size: 24px;");

        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("profile-stat-value");
        valueLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label descLabel = new Label(label);
        descLabel.getStyleClass().add("profile-stat-label");
        descLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");

        statBox.getChildren().addAll(iconLabel, valueLabel, descLabel);
        return statBox;
    }

    private void showCreateCourseDialog() {
        Dialog<Course> dialog = new Dialog<>();
        dialog.setTitle("Create New Course");
        dialog.setHeaderText("Enter Course Details");

        // Create the form fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField courseId = new TextField();
        TextField courseTitle = new TextField();
        TextArea courseDesc = new TextArea();
        courseDesc.setPrefRowCount(3);

        grid.add(new Label("Course ID:"), 0, 0);
        grid.add(courseId, 1, 0);
        grid.add(new Label("Title:"), 0, 1);
        grid.add(courseTitle, 1, 1);
        grid.add(new Label("Description:"), 0, 2);
        grid.add(courseDesc, 1, 2);

        dialog.getDialogPane().setContent(grid);

        ButtonType createButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == createButtonType) {
                Course newCourse = new Course(
                    courseId.getText(),
                    courseTitle.getText(),
                    courseDesc.getText(),
                    userManager.getCurrentUserFaculty()
                );
                addCourse(newCourse);
                return newCourse;
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showFileUploadDialog(String type) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload " + type);
        
        // Set extension filters based on type
        if (type.equals("Video")) {
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.avi", "*.mkv")
            );
        } else if (type.equals("PDF")) {
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
            );
        }
        
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            // Handle file upload
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Upload Successful");
            alert.setHeaderText(null);
            alert.setContentText(type + " file uploaded successfully: " + file.getName());
            alert.showAndWait();
        }
    }

    private void showCreateMCQDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Create Multiple Choice Quiz");
        dialog.setHeaderText("Create a new MCQ assessment");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField quizName = new TextField();
        quizName.setPromptText("Quiz Name");
        
        TextArea questionArea = new TextArea();
        questionArea.setPromptText("Enter your question here");
        questionArea.setPrefRowCount(3);
        
        grid.add(new Label("Quiz Name:"), 0, 0);
        grid.add(quizName, 1, 0);
        grid.add(new Label("Question:"), 0, 1);
        grid.add(questionArea, 1, 1);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        dialog.showAndWait();
    }

    private void showCreateShortAnswerDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Create Short Answer Test");
        dialog.setHeaderText("Create a new short answer assessment");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField testName = new TextField();
        testName.setPromptText("Test Name");
        
        TextArea questionArea = new TextArea();
        questionArea.setPromptText("Enter your question here");
        questionArea.setPrefRowCount(3);
        
        grid.add(new Label("Test Name:"), 0, 0);
        grid.add(testName, 1, 0);
        grid.add(new Label("Question:"), 0, 1);
        grid.add(questionArea, 1, 1);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        dialog.showAndWait();
    }

    private void showEditCourseDialog(Course course) {
        Dialog<Course> dialog = new Dialog<>();
        dialog.setTitle("Edit Course");
        dialog.setHeaderText("Edit Course Details");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField courseId = new TextField(course.getId());
        TextField courseTitle = new TextField(course.getTitle());
        TextArea courseDesc = new TextArea(course.getDescription());
        courseDesc.setPrefRowCount(3);

        grid.add(new Label("Course ID:"), 0, 0);
        grid.add(courseId, 1, 0);
        grid.add(new Label("Title:"), 0, 1);
        grid.add(courseTitle, 1, 1);
        grid.add(new Label("Description:"), 0, 2);
        grid.add(courseDesc, 1, 2);

        dialog.getDialogPane().setContent(grid);

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                course.setId(courseId.getText());
                course.setTitle(courseTitle.getText());
                course.setDescription(courseDesc.getText());
                return course;
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showStudentList(Course course) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Students in " + course.getTitle());
        dialog.setHeaderText("Enrolled Students");

        VBox content = new VBox(10);
        content.setPadding(new Insets(20));

        TableView<Student> studentTable = new TableView<>(FXCollections.observableArrayList(course.getEnrolledStudents()));
        
        TableColumn<Student, String> idCol = new TableColumn<>("Student ID");
        idCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("id"));
        
        TableColumn<Student, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("name"));
        
        TableColumn<Student, Double> gradeCol = new TableColumn<>("Overall Grade");
        gradeCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("overallGrade"));

        studentTable.getColumns().addAll(idCol, nameCol, gradeCol);
        content.getChildren().add(studentTable);

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        dialog.showAndWait();
    }

    private void generateTranscripts() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Generate Transcript");
        dialog.setHeaderText("Enter Student Transcript Details");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nameField = new TextField();
        nameField.setPromptText("Full Name");
        TextField idField = new TextField();
        idField.setPromptText("Student ID");
        TextField facultyField = new TextField();
        facultyField.setPromptText("Faculty");
        TextArea gradesArea = new TextArea();
        gradesArea.setPromptText("Enter grades as: CourseCode, CourseTitle, Grade (one per line)");
        gradesArea.setPrefRowCount(6);

        grid.add(new Label("Full Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Student ID:"), 0, 1);
        grid.add(idField, 1, 1);
        grid.add(new Label("Faculty:"), 0, 2);
        grid.add(facultyField, 1, 2);
        grid.add(new Label("Grades List:"), 0, 3);
        grid.add(gradesArea, 1, 3);

        dialog.getDialogPane().setContent(grid);
        ButtonType generateBtn = new ButtonType("Generate", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(generateBtn, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == generateBtn) {
                // Build transcript
                StringBuilder sb = new StringBuilder();
                sb.append("UNIVERSITY NAME\n");
                sb.append("Official Transcript\n\n");
                sb.append("Student Name: ").append(nameField.getText()).append("\n");
                sb.append("Student ID: ").append(idField.getText()).append("\n");
                sb.append("Faculty: ").append(facultyField.getText()).append("\n\n");
                sb.append("Course Code\tCourse Title\t\tGrade\n");
                sb.append("---------------------------------------------\n");
                String[] lines = gradesArea.getText().split("\n");
                for (String line : lines) {
                    String[] parts = line.split(",");
                    if (parts.length == 3) {
                        sb.append(String.format("%-10s %-30s %-5s\n", parts[0].trim(), parts[1].trim(), parts[2].trim()));
                    }
                }
                sb.append("\nDate Issued: ").append(java.time.LocalDate.now());

                TextArea transcriptArea = new TextArea(sb.toString());
                transcriptArea.setEditable(false);
                transcriptArea.setStyle("-fx-font-family: 'Consolas', monospace; -fx-font-size: 14px;");

                Dialog<Void> resultDialog = new Dialog<>();
                resultDialog.setTitle("Generated Transcript");
                resultDialog.getDialogPane().setContent(transcriptArea);
                resultDialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
                resultDialog.showAndWait();
            }
            return null;
        });
        dialog.showAndWait();
    }

    private void showClassPerformance() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Class Performance Report");
        dialog.setHeaderText("Enter Class Performance Data");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField avgGradeField = new TextField();
        avgGradeField.setPromptText("Average Grade");
        TextField highestField = new TextField();
        highestField.setPromptText("Highest Score");
        TextField lowestField = new TextField();
        lowestField.setPromptText("Lowest Score");
        TextField medianField = new TextField();
        medianField.setPromptText("Median Score");
        TextField numStudentsField = new TextField();
        numStudentsField.setPromptText("Number of Students");
        TextArea gradeDistArea = new TextArea();
        gradeDistArea.setPromptText("Grade Distribution (e.g. A: 10\nB: 12\nC: 5\nD: 2\nF: 1)");
        gradeDistArea.setPrefRowCount(5);

        grid.add(new Label("Average Grade:"), 0, 0);
        grid.add(avgGradeField, 1, 0);
        grid.add(new Label("Highest Score:"), 0, 1);
        grid.add(highestField, 1, 1);
        grid.add(new Label("Lowest Score:"), 0, 2);
        grid.add(lowestField, 1, 2);
        grid.add(new Label("Median Score:"), 0, 3);
        grid.add(medianField, 1, 3);
        grid.add(new Label("Number of Students:"), 0, 4);
        grid.add(numStudentsField, 1, 4);
        grid.add(new Label("Grade Distribution:"), 0, 5);
        grid.add(gradeDistArea, 1, 5);

        dialog.getDialogPane().setContent(grid);
        ButtonType generateBtn = new ButtonType("Generate", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(generateBtn, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == generateBtn) {
                StringBuilder sb = new StringBuilder();
                sb.append("CLASS PERFORMANCE REPORT\n\n");
                sb.append("Average Grade: ").append(avgGradeField.getText()).append("\n");
                sb.append("Highest Score: ").append(highestField.getText()).append("\n");
                sb.append("Lowest Score: ").append(lowestField.getText()).append("\n");
                sb.append("Median Score: ").append(medianField.getText()).append("\n");
                sb.append("Number of Students: ").append(numStudentsField.getText()).append("\n\n");
                sb.append("Grade Distribution:\n");
                sb.append(gradeDistArea.getText()).append("\n");

                TextArea reportArea = new TextArea(sb.toString());
                reportArea.setEditable(false);
                reportArea.setStyle("-fx-font-family: 'Consolas', monospace; -fx-font-size: 14px;");

                Dialog<Void> resultDialog = new Dialog<>();
                resultDialog.setTitle("Generated Class Performance Report");
                resultDialog.getDialogPane().setContent(reportArea);
                resultDialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
                resultDialog.showAndWait();
            }
            return null;
        });
        dialog.showAndWait();
    }

    private void manageCertifications() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Manage Certifications");
        dialog.setHeaderText("Course Certifications");
        
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        
        Button generateCertBtn = new Button("Generate New Certificate");
        ListView<String> certList = new ListView<>();
        certList.getItems().addAll(
            "Certificate of Completion - Java Programming",
            "Certificate of Excellence - Database Design",
            "Certificate of Achievement - Web Development"
        );
        
        content.getChildren().addAll(generateCertBtn, certList);
        
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        
        dialog.showAndWait();
    }

    public List<Course> getCourses() {
        return facultyCourses;
    }

    public Tab createCoursesTab() {
        Tab coursesTab = new Tab("Courses");
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        content.getStyleClass().add("dashboard-content");

        // Title and Add Course Button
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label titleLabel = new Label("My Courses");
        titleLabel.getStyleClass().add("section-title");
        Button addCourseBtn = new Button("+ Add Course");
        addCourseBtn.getStyleClass().add("action-button");
        header.getChildren().addAll(titleLabel, addCourseBtn);

        // Courses Table
        final TableView<Course> courseTable = new TableView<>(facultyCourses);
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

        // Actions column
        TableColumn<Course, Void> actionCol = new TableColumn<>("Actions");
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            {
                editBtn.getStyleClass().add("action-button");
                deleteBtn.getStyleClass().add("action-button-alt");
                editBtn.setTooltip(new Tooltip("Edit this course"));
                deleteBtn.setTooltip(new Tooltip("Delete this course"));
                editBtn.setOnAction(e -> {
                    Course course = getTableView().getItems().get(getIndex());
                    showEditCourseDialog(course);
                    courseTable.refresh();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Course updated successfully!");
                    alert.showAndWait();
                });
                deleteBtn.setOnAction(e -> {
                    Course course = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + course.getTitle() + "?", ButtonType.YES, ButtonType.NO);
                    alert.setHeaderText("Are you sure?");
                    alert.showAndWait().ifPresent(type -> {
                        if (type == ButtonType.YES) {
                            facultyCourses.remove(course);
                            allCourses.remove(course);
                            courseTable.refresh();
                            Alert done = new Alert(Alert.AlertType.INFORMATION, "Course deleted successfully!");
                            done.showAndWait();
                        }
                    });
                });
            }
            private final HBox pane = new HBox(5, editBtn, deleteBtn);
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
        courseTable.getColumns().addAll(idCol, titleCol, descCol, facultyCol, actionCol);

        addCourseBtn.setTooltip(new Tooltip("Add a new course"));
        addCourseBtn.setOnAction(e -> {
            showCreateCourseDialog();
            courseTable.refresh();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Course added successfully!");
            alert.showAndWait();
        });

        content.getChildren().addAll(header, courseTable);
        coursesTab.setContent(content);
        return coursesTab;
    }
} 