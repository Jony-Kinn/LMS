package com.example.demo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.effect.DropShadow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.example.demo.controllers.StudentDashboardController;
import com.example.demo.controllers.LecturerDashboardController;
import com.example.demo.controllers.CourseManagementController;
import com.example.demo.models.UserManager;
import com.example.demo.models.Faculty;
import com.example.demo.utils.UserCredentials;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.scene.control.Pagination;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Tooltip;

public class HelloApplication extends Application {
    private Stage primaryStage;
    private Scene homeScene;
    private StudentDashboardController studentDashboard;
    private LecturerDashboardController lecturerDashboard;
    private UserManager userManager;

    // Database connection parameters
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/lms_db";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "1234";

    // Course data
    private final String[] courses = {
        "Introduction to Computer Science",
        "Data Structures and Algorithms",
        "Database Systems",
        "Web Development",
        "Software Engineering",
        "Artificial Intelligence",
        "Computer Networks",
        "Operating Systems",
        "Mobile App Development",
        "Cloud Computing",
        "Cybersecurity",
        "Machine Learning"
    };

    private final String[] descriptions = {
        "Learn the fundamentals of programming and computer science",
        "Master essential data structures and algorithm design",
        "Explore database design, implementation, and management",
        "Build modern web applications using latest technologies",
        "Understand software development lifecycle and best practices",
        "Introduction to AI concepts and applications",
        "Study network protocols and communication systems",
        "Learn about operating system concepts and design",
        "Develop mobile applications for iOS and Android",
        "Understand cloud computing platforms and services",
        "Learn about security principles and practices",
        "Explore machine learning algorithms and applications"
    };

    private final String[] instructors = {
        "Dr. Silake",
        "Prof. Joshowa",
        "Dr. Tsepo",
        "Prof. Borotho",
        "Dr. Khalefo",
        "Prof. Moikaele",
        "Dr. Sanki",
        "Prof. More",
        "Dr. Tanki",
        "Prof. Larako",
        "Dr. Thomaki",
        "Prof. Lekhona"
    };

    @Override
    public void start(Stage stage) throws IOException {
        this.primaryStage = stage;
        this.userManager = new UserManager();
        try {
            // Connect to PostgreSQL database
            Connection conn = getPostgresConnection();
            if (conn != null) {
                System.out.println("Connected to PostgreSQL database!");
            } else {
                System.err.println("Failed to connect to PostgreSQL database.");
            }
            // Create root layout
            BorderPane root = new BorderPane();
            
            // Create the menu bar
            MenuBar menuBar = createMenuBar();
            VBox topContainer = new VBox();
            topContainer.getChildren().add(menuBar);
            
            // Create the top section with title and login buttons
            HBox topSection = createTopSection();
            topContainer.getChildren().add(topSection);
            root.setTop(topContainer);
            
            // Create the center section with course list
            VBox centerContent = createCenterContent();
            
            // Add credentials info at the bottom
            Label credentialsLabel = new Label(UserCredentials.getCredentialsInfo());
            credentialsLabel.getStyleClass().add("credentials-info");
            credentialsLabel.setStyle("-fx-font-family: monospace; -fx-padding: 20;");
            
            VBox mainContent = new VBox(20);
            mainContent.getChildren().addAll(centerContent, credentialsLabel);
            root.setCenter(mainContent);
            
            // Add DropShadow effect to mainContent
            DropShadow dropShadow = new DropShadow();
            dropShadow.setRadius(12);
            dropShadow.setOffsetY(6);
            dropShadow.setColor(Color.rgb(44, 62, 80, 0.15));
            mainContent.setEffect(dropShadow);

            // Add FadeTransition effect to mainContent
            FadeTransition fade = new FadeTransition(Duration.millis(800), mainContent);
            fade.setFromValue(0.0);
            fade.setToValue(1.0);
            fade.play();
            
            // Create scene
            homeScene = new Scene(root, 1200, 800);
            homeScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            
            // Configure stage
            stage.setTitle("Learning Management System");
            stage.setScene(homeScene);
            stage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(e -> Platform.exit());
        fileMenu.getItems().add(exitItem);
        menuBar.getMenus().add(fileMenu);
        return menuBar;
    }

    private HBox createTopSection() {
        HBox topSection = new HBox(20);
        topSection.getStyleClass().add("top-section");
        topSection.setPadding(new Insets(20));
        topSection.setAlignment(Pos.CENTER);
        
        // Title on the left
        Label titleLabel = new Label("Learning Management System");
        titleLabel.getStyleClass().add("title-label");
        HBox.setHgrow(titleLabel, Priority.ALWAYS);
        
        // Login buttons on the right
        Button studentLoginBtn = new Button("Student Login");
        studentLoginBtn.getStyleClass().add("login-button");
        studentLoginBtn.setOnAction(e -> showLoginDialog("Student Login"));
        
        Button lecturerLoginBtn = new Button("Lecturer Login");
        lecturerLoginBtn.getStyleClass().add("login-button");
        lecturerLoginBtn.setOnAction(e -> showLoginDialog("Lecturer Login"));
        
        topSection.getChildren().addAll(titleLabel, studentLoginBtn, lecturerLoginBtn);
        return topSection;
    }

    private VBox createCenterContent() {
        VBox centerContent = new VBox(30);
        centerContent.getStyleClass().add("center-content");
        centerContent.setPadding(new Insets(40));
        
        // Welcome Section
        VBox welcomeSection = new VBox(15);
        welcomeSection.setAlignment(Pos.CENTER);
        
        Label welcomeLabel = new Label("Welcome to Learning Management System");
        welcomeLabel.getStyleClass().add("welcome-label");
        
        Label subtitleLabel = new Label("Empowering Education Through Technology");
        subtitleLabel.getStyleClass().add("subtitle-label");
        
        welcomeSection.getChildren().addAll(welcomeLabel, subtitleLabel);
        
        // Create ScrollPane for courses
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("course-scroll-pane");
        
        // Use the new grid with pagination
        VBox courseGridWithPagination = createCourseGridWithPagination();
        scrollPane.setContent(courseGridWithPagination);
        
        // Stats Section
        HBox statsSection = createStatsSection();
        
        // Featured Section
        VBox featuredSection = createFeaturedSection();
        
        centerContent.getChildren().addAll(welcomeSection, statsSection, featuredSection, scrollPane);
        return centerContent;
    }

    private VBox createCourseGridWithPagination() {
        GridPane courseGrid = new GridPane();
        courseGrid.getStyleClass().add("course-grid");
        courseGrid.setHgap(20);
        courseGrid.setVgap(20);
        courseGrid.setAlignment(Pos.CENTER);

        Pagination pagination = new Pagination();
        pagination.getStyleClass().add("course-pagination");
        pagination.setPageCount((int) Math.ceil(courses.length / 9.0)); // 9 items per page
        pagination.setCurrentPageIndex(0);

        int itemsPerPage = 9;

        pagination.setPageFactory(pageIndex -> {
            courseGrid.getChildren().clear();
            int startIndex = pageIndex * itemsPerPage;
            int endIndex = Math.min(startIndex + itemsPerPage, courses.length);

            int col = 0;
            int row = 0;

            for (int i = startIndex; i < endIndex; i++) {
                VBox courseCard = createCourseCard(courses[i], descriptions[i], instructors[i]);
                courseGrid.add(courseCard, col, row);

                col++;
                if (col == 3) { // 3 columns
                    col = 0;
                    row++;
                }
            }

            return courseGrid;
        });

        VBox container = new VBox(20, courseGrid, pagination);
        container.setAlignment(Pos.CENTER);
        return container;
    }

    private VBox createCourseCard(String courseName, String description, String instructor) {
        VBox card = new VBox(10);
        card.getStyleClass().add("course-card");
        card.setPadding(new Insets(20));
        
        Label nameLabel = new Label(courseName);
        nameLabel.getStyleClass().add("course-name");
        nameLabel.setWrapText(true);
        
        Label descLabel = new Label(description);
        descLabel.getStyleClass().add("course-description");
        descLabel.setWrapText(true);
        
        Label instructorLabel = new Label("Instructor: " + instructor);
        instructorLabel.getStyleClass().add("course-instructor");
        
        HBox statusBox = new HBox(10);
        statusBox.setAlignment(Pos.CENTER_LEFT);
        
        Label statusLabel = new Label("Enrollment Open");
        statusLabel.getStyleClass().add("course-status");
        
        Button enrollButton = new Button("Enroll Now");
        enrollButton.getStyleClass().add("enroll-button");
        
        statusBox.getChildren().addAll(statusLabel, enrollButton);
        
        card.getChildren().addAll(nameLabel, descLabel, instructorLabel, statusBox);
        return card;
    }

    private HBox createStatsSection() {
        HBox statsSection = new HBox(30);
        statsSection.getStyleClass().add("stats-section");
        statsSection.setAlignment(Pos.CENTER);
        
        VBox[] stats = {
            createStatBox("1000+", "Active Students"),
            createStatBox("100+", "Expert Instructors"),
            createStatBox("500+", "Courses Available"),
            createStatBox("95%", "Success Rate")
        };
        
        statsSection.getChildren().addAll(stats);
        return statsSection;
    }

    private VBox createStatBox(String number, String label) {
        VBox statBox = new VBox(5);
        statBox.getStyleClass().add("stat-box");
        statBox.setAlignment(Pos.CENTER);
        
        Label numberLabel = new Label(number);
        numberLabel.getStyleClass().add("stat-number");
        
        Label textLabel = new Label(label);
        textLabel.getStyleClass().add("stat-label");
        
        statBox.getChildren().addAll(numberLabel, textLabel);
        return statBox;
    }

    private VBox createFeaturedSection() {
        VBox featuredSection = new VBox(15);
        featuredSection.getStyleClass().add("featured-section");
        
        Label featuredTitle = new Label("Featured Programs");
        featuredTitle.getStyleClass().add("featured-title");
        
        HBox featuredCards = new HBox(20);
        featuredCards.setAlignment(Pos.CENTER);
        
        VBox[] programs = {
            createFeaturedCard("Computer Science", "Bachelor's Program", "4 Years"),
            createFeaturedCard("Data Science", "Master's Program", "2 Years"),
            createFeaturedCard("Cyber Security", "Professional Certificate", "6 Months")
        };
        
        featuredCards.getChildren().addAll(programs);
        featuredSection.getChildren().addAll(featuredTitle, featuredCards);
        return featuredSection;
    }

    private VBox createFeaturedCard(String program, String level, String duration) {
        VBox card = new VBox(10);
        card.getStyleClass().add("featured-card");
        card.setPadding(new Insets(20));
        
        Label programLabel = new Label(program);
        programLabel.getStyleClass().add("program-name");
        
        Label levelLabel = new Label(level);
        levelLabel.getStyleClass().add("program-level");
        
        Label durationLabel = new Label("Duration: " + duration);
        durationLabel.getStyleClass().add("program-duration");
        
        Button learnMoreBtn = new Button("Learn More");
        learnMoreBtn.getStyleClass().add("learn-more-button");
        
        card.getChildren().addAll(programLabel, levelLabel, durationLabel, learnMoreBtn);
        return card;
    }

    private void showLoginDialog(String userType) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(userType);
        dialog.setHeaderText("Please login or register");
        dialog.initOwner(primaryStage);
        
        TabPane tabPane = new TabPane();
        
        // Login Tab
        Tab loginTab = new Tab("Login");
        loginTab.setContent(createLoginPanel(dialog, userType));
        loginTab.setClosable(false);
        
        // Register Tab
        Tab registerTab = new Tab("Register");
        registerTab.setContent(createRegistrationPanel(dialog, userType));
        registerTab.setClosable(false);
        
        tabPane.getTabs().addAll(loginTab, registerTab);
        dialog.getDialogPane().setContent(tabPane);
        
        // Add close button
        ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(closeButton);
        
        dialog.showAndWait();
    }

    private GridPane createLoginPanel(Dialog<ButtonType> dialog, String userType) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField username = new TextField();
        username.setPromptText("Username");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        
        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);
        
        // Add error label
        Label errorLabel = new Label();
        errorLabel.getStyleClass().add("error-label");
        errorLabel.setTextFill(Color.RED);
        grid.add(errorLabel, 0, 2, 2, 1);
        
        // Create login button
        Button loginButton = new Button("Login");
        loginButton.setDefaultButton(true);
        grid.add(loginButton, 1, 3);
        
        loginButton.setOnAction(e -> {
            String enteredUsername = username.getText().trim();
            String enteredPassword = password.getText();
            
            if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                errorLabel.setText("Please fill in all fields");
                return;
            }
            
            boolean isValid;
            if (userType.equals("Student Login")) {
                isValid = userManager.loginUser(enteredUsername, enteredPassword, "STUDENT");
                if (isValid) {
                    createStudentDashboard(enteredUsername);
                    dialog.close();
                }
            } else {
                isValid = userManager.loginUser(enteredUsername, enteredPassword, "LECTURER");
                if (isValid) {
                    createLecturerDashboard(enteredUsername);
                    dialog.close();
                }
            }
            
            if (!isValid) {
                errorLabel.setText("Invalid credentials! Please try again.");
            }
        });
        
        return grid;
    }

    private GridPane createRegistrationPanel(Dialog<ButtonType> dialog, String userType) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField username = new TextField();
        username.setPromptText("Choose username");
        
        PasswordField password = new PasswordField();
        password.setPromptText("Choose password");
        
        TextField fullName = new TextField();
        fullName.setPromptText("Enter your full name");
        
        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);
        grid.add(new Label("Full Name:"), 0, 2);
        grid.add(fullName, 1, 2);
        
        final ComboBox<Faculty> facultyComboBox;
        if (userType.equals("Lecturer Login")) {
            facultyComboBox = new ComboBox<>(FXCollections.observableArrayList(Faculty.values()));
            facultyComboBox.setPromptText("Select your faculty");
            grid.add(new Label("Faculty:"), 0, 3);
            grid.add(facultyComboBox, 1, 3);
        } else {
            facultyComboBox = null;
        }
        
        // Add error/success label
        Label messageLabel = new Label();
        messageLabel.getStyleClass().add("message-label");
        grid.add(messageLabel, 0, userType.equals("Lecturer Login") ? 4 : 3, 2, 1);
        
        // Create register button
        Button registerButton = new Button("Register");
        grid.add(registerButton, 1, userType.equals("Lecturer Login") ? 5 : 4);
        
        // Enable/disable register button based on field values
        registerButton.setDisable(true);
        
        // Validation listener
        Runnable validateFields = () -> {
            boolean fieldsEmpty = username.getText().trim().isEmpty() || 
                                password.getText().trim().isEmpty() || 
                                fullName.getText().trim().isEmpty();
            
            if (userType.equals("Lecturer Login")) {
                registerButton.setDisable(fieldsEmpty || facultyComboBox.getValue() == null);
            } else {
                registerButton.setDisable(fieldsEmpty);
            }
        };
        
        // Add listeners to all fields
        username.textProperty().addListener((obs, old, newVal) -> validateFields.run());
        password.textProperty().addListener((obs, old, newVal) -> validateFields.run());
        fullName.textProperty().addListener((obs, old, newVal) -> validateFields.run());
        if (facultyComboBox != null) {
            facultyComboBox.valueProperty().addListener((obs, old, newVal) -> validateFields.run());
        }
        
        registerButton.setOnAction(e -> {
            String enteredUsername = username.getText().trim();
            String enteredPassword = password.getText().trim();
            String enteredFullName = fullName.getText().trim();
            
            boolean success;
                    if (userType.equals("Student Login")) {
                success = userManager.registerStudent(enteredUsername, enteredPassword, enteredFullName);
                    } else {
                Faculty selectedFaculty = facultyComboBox.getValue();
                success = userManager.registerLecturer(enteredUsername, enteredPassword, enteredFullName, selectedFaculty);
                    }
            
            if (success) {
                messageLabel.setTextFill(Color.GREEN);
                messageLabel.setText("Registration successful! You can now login.");
                // Clear the fields
                username.clear();
                password.clear();
                fullName.clear();
                if (facultyComboBox != null) {
                    facultyComboBox.setValue(null);
                }
                // Switch to login tab
                TabPane tabPane = (TabPane) dialog.getDialogPane().getContent();
                tabPane.getSelectionModel().select(0);
            } else {
                messageLabel.setTextFill(Color.RED);
                messageLabel.setText("Username already exists. Please choose another.");
            }
        });
        
        return grid;
    }

    private void createStudentDashboard(String username) {
        try {
            // Use the real student dashboard controller
            StudentDashboardController controller = new StudentDashboardController(userManager);
            VBox dashboardRoot = controller.createStudentDashboard();

            Scene dashboardScene = new Scene(dashboardRoot, 1200, 800);
            dashboardScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

            // Pass primaryStage and homeScene to the controller for logout
            controller.setPrimaryStageAndHomeScene(primaryStage, homeScene);

            primaryStage.setScene(dashboardScene);
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error creating student dashboard", e.getMessage());
        }
    }

    private void createLecturerDashboard(String username) {
        try {
            CourseManagementController courseManager = new CourseManagementController(userManager);
            VBox dashboardRoot = new VBox(10);
            dashboardRoot.getStyleClass().add("lecturer-dashboard");
            dashboardRoot.getStyleClass().add("visible"); // For fade-in effect
            dashboardRoot.setPadding(new Insets(20));
            dashboardRoot.setStyle("-fx-background-color: linear-gradient(to bottom right, #f8f9fa, #e3eafc);");

            // Add DropShadow effect
            DropShadow dropShadow = new DropShadow();
            dropShadow.setRadius(16);
            dropShadow.setOffsetY(8);
            dropShadow.setColor(Color.rgb(44, 62, 80, 0.12));
            dashboardRoot.setEffect(dropShadow);

            // Add FadeTransition effect
            FadeTransition fade = new FadeTransition(Duration.millis(900), dashboardRoot);
            fade.setFromValue(0.0);
            fade.setToValue(1.0);
            fade.play();

            String fullName = userManager.getCurrentUserFullName();
            String initials = "?";
            if (fullName != null && !fullName.isEmpty()) {
                String[] names = fullName.split(" ");
                StringBuilder sb = new StringBuilder();
                for (String n : names) if (!n.isEmpty()) sb.append(n.charAt(0));
                initials = sb.toString().toUpperCase();
            }
            // Profile avatar
            Circle avatar = new Circle(22, Color.web("#3498db"));
            Label avatarLabel = new Label(initials);
            avatarLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");
            StackPane avatarStack = new StackPane(avatar, avatarLabel);
            avatarStack.setStyle("-fx-effect: dropshadow(gaussian, rgba(52,152,219,0.18), 8, 0, 0, 2);");

            Label welcomeLabel = new Label("Welcome, Prof. " + (fullName != null ? fullName : username));
            welcomeLabel.getStyleClass().add("welcome-label");
            welcomeLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

            // Add logout button with vibrant color and tooltip
            Button logoutBtn = new Button("Logout");
            logoutBtn.getStyleClass().add("logout-button");
            logoutBtn.setStyle("-fx-background-color: linear-gradient(to right, #e74c3c, #c0392b); -fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold; -fx-padding: 8 22; -fx-background-radius: 8; -fx-cursor: hand;");
            logoutBtn.setTooltip(new Tooltip("Logout and return to homepage"));
            logoutBtn.setOnAction(e -> {
                userManager.logout();
                primaryStage.setScene(homeScene);
            });

            HBox topBar = new HBox(18);
            topBar.setAlignment(Pos.CENTER_LEFT);
            topBar.getChildren().addAll(avatarStack, welcomeLabel, logoutBtn);
            topBar.setStyle("-fx-background-color: linear-gradient(to right, #2c3e50, #3498db); -fx-padding: 18 24 18 24; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(44,62,80,0.10), 8, 0, 0, 2);");

            // Create TabPane for different sections with icons and tooltips
            TabPane tabPane = new TabPane();
            tabPane.getStyleClass().add("dashboard-tabs");
            tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

            // Courses Tab (with Add Course button)
            Tab coursesTab = courseManager.createCoursesTab();
            coursesTab.setClosable(false);
            coursesTab.setGraphic(new Label("📚"));
            coursesTab.setTooltip(new Tooltip("View and manage your courses"));

            // Course Management Tab
            Tab courseManagementTab = new Tab("Course Management");
            courseManagementTab.setContent(courseManager.createCourseManagementView());
            courseManagementTab.setClosable(false);
            courseManagementTab.setGraphic(new Label("🛠️"));
            courseManagementTab.setTooltip(new Tooltip("Advanced course management"));

            // Content Management Tab
            Tab contentManagementTab = new Tab("Content Management");
            contentManagementTab.setContent(courseManager.createContentManagementTab());
            contentManagementTab.setClosable(false);
            contentManagementTab.setGraphic(new Label("📂"));
            contentManagementTab.setTooltip(new Tooltip("Upload and organize course content"));

            // Assessments Tab
            Tab assessmentsTab = new Tab("Assessments");
            assessmentsTab.setContent(courseManager.createAssessmentsTab());
            assessmentsTab.setClosable(false);
            assessmentsTab.setGraphic(new Label("📝"));
            assessmentsTab.setTooltip(new Tooltip("Create and grade assessments"));

            // Reports Tab with notification badge (simulate with red dot)
            Label reportsIcon = new Label("📈");
            StackPane reportsStack = new StackPane(reportsIcon);
            Circle badge = new Circle(5, Color.web("#e74c3c"));
            badge.setTranslateX(10); badge.setTranslateY(-10);
            reportsStack.getChildren().add(badge); // Always show for demo
            Tab reportsTab = new Tab("Reports & Analytics");
            reportsTab.setContent(courseManager.createReportsTab());
            reportsTab.setClosable(false);
            reportsTab.setGraphic(reportsStack);
            reportsTab.setTooltip(new Tooltip("View analytics and generate reports"));

            // Profile Tab with tooltip
            Tab profileTab = new Tab("Profile");
            profileTab.setContent(courseManager.createProfileTab());
            profileTab.setClosable(false);
            profileTab.setGraphic(new Label("👤"));
            profileTab.setTooltip(new Tooltip("View and edit your profile"));

            tabPane.getTabs().addAll(coursesTab, courseManagementTab, contentManagementTab, assessmentsTab, reportsTab, profileTab);

            dashboardRoot.getChildren().addAll(topBar, tabPane);

            Scene dashboardScene = new Scene(dashboardRoot, 1200, 800);
            dashboardScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

            primaryStage.setScene(dashboardScene);
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error creating lecturer dashboard", e.getMessage());
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        // Test database connection and create tables before launching the application
        if (!testDatabaseConnection()) {
            System.err.println("Warning: Could not connect to the database. Some features may not work.");
        } else {
            try {
                createDatabaseTables();
                System.out.println("Database tables created successfully!");
            } catch (SQLException e) {
                System.err.println("Error creating database tables: " + e.getMessage());
            }
        }
        launch();
    }

    /**
     * Get a connection to the PostgreSQL database
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getPostgresConnection() throws SQLException {
        try {
            // Register the PostgreSQL JDBC driver
            Class.forName("org.postgresql.Driver");
            
            // Create and return the connection
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC Driver not found.", e);
        }
    }

    /**
     * Test the database connection
     * @return true if connection is successful, false otherwise
     */
    public static boolean testDatabaseConnection() {
        try (Connection conn = getPostgresConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Create all necessary database tables if they don't exist
     * @throws SQLException if table creation fails
     */
    private static void createDatabaseTables() throws SQLException {
        try (Connection conn = getPostgresConnection()) {
            // Drop existing tables in reverse order to handle dependencies
            String[] tablesToDrop = {
                "messages",
                "activity_logs",
                "user_roles",
                "course_materials",
                "announcements",
                "assessment_results",
                "assessments",
                "enrollments",
                "courses",
                "lecturers",
                "students",
                "faculties"
            };

            for (String table : tablesToDrop) {
                try {
                    conn.createStatement().execute("DROP TABLE IF EXISTS " + table + " CASCADE");
                } catch (SQLException e) {
                    System.out.println("Error dropping table " + table + ": " + e.getMessage());
                }
            }

            // Create faculties table first (no dependencies)
            conn.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS faculties (
                    faculty_id SERIAL PRIMARY KEY,
                    name VARCHAR(100) UNIQUE NOT NULL,
                    description TEXT,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);

            // Create students table (no dependencies)
            conn.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS students (
                    student_id SERIAL PRIMARY KEY,
                    username VARCHAR(50) UNIQUE NOT NULL,
                    password VARCHAR(100) NOT NULL,
                    full_name VARCHAR(100) NOT NULL,
                    email VARCHAR(100),
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);

            // Create lecturers table (depends on faculties)
            conn.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS lecturers (
                    lecturer_id SERIAL PRIMARY KEY,
                    username VARCHAR(50) UNIQUE NOT NULL,
                    password VARCHAR(100) NOT NULL,
                    full_name VARCHAR(100) NOT NULL,
                    email VARCHAR(100),
                    faculty_id INTEGER REFERENCES faculties(faculty_id),
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);

            // Create courses table (depends on lecturers)
            conn.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS courses (
                    course_id SERIAL PRIMARY KEY,
                    course_name VARCHAR(100) NOT NULL,
                    description TEXT,
                    lecturer_id INTEGER REFERENCES lecturers(lecturer_id),
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);

            // Create enrollments table (depends on students and courses)
            conn.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS enrollments (
                    enrollment_id SERIAL PRIMARY KEY,
                    student_id INTEGER REFERENCES students(student_id),
                    course_id INTEGER REFERENCES courses(course_id),
                    enrollment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    status VARCHAR(20) DEFAULT 'active',
                    UNIQUE(student_id, course_id)
                )
            """);

            // Create assessments table (depends on courses)
            conn.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS assessments (
                    assessment_id SERIAL PRIMARY KEY,
                    course_id INTEGER REFERENCES courses(course_id),
                    title VARCHAR(100) NOT NULL,
                    description TEXT,
                    due_date TIMESTAMP,
                    total_marks INTEGER,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);

            // Create assessment_results table (depends on assessments and students)
            conn.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS assessment_results (
                    result_id SERIAL PRIMARY KEY,
                    assessment_id INTEGER,
                    student_id INTEGER,
                    marks_obtained INTEGER,
                    submission_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    feedback TEXT,
                    FOREIGN KEY (assessment_id) REFERENCES assessments(assessment_id),
                    FOREIGN KEY (student_id) REFERENCES students(student_id),
                    UNIQUE(assessment_id, student_id)
                )
            """);

            // Create announcements table (depends on courses)
            conn.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS announcements (
                    announcement_id SERIAL PRIMARY KEY,
                    course_id INTEGER REFERENCES courses(course_id),
                    title VARCHAR(100) NOT NULL,
                    content TEXT,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);

            // Create course_materials table (depends on courses)
            conn.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS course_materials (
                    material_id SERIAL PRIMARY KEY,
                    course_id INTEGER REFERENCES courses(course_id),
                    title VARCHAR(100) NOT NULL,
                    description TEXT,
                    file_path VARCHAR(255),
                    file_type VARCHAR(50),
                    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);

            // Create user_roles table (no dependencies)
            conn.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS user_roles (
                    role_id SERIAL PRIMARY KEY,
                    username VARCHAR(50) UNIQUE NOT NULL,
                    role VARCHAR(20) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);

            // Create activity_logs table (no dependencies)
            conn.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS activity_logs (
                    log_id SERIAL PRIMARY KEY,
                    user_id INTEGER,
                    activity_type VARCHAR(50) NOT NULL,
                    description TEXT,
                    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);

            // Create messages table (depends on students and lecturers)
            conn.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS messages (
                    message_id SERIAL PRIMARY KEY,
                    sender_id INTEGER,
                    receiver_id INTEGER,
                    content TEXT NOT NULL,
                    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    is_read BOOLEAN DEFAULT FALSE
                )
            """);

            // Insert default faculty data if not exists
            conn.createStatement().execute("""
                INSERT INTO faculties (name, description)
                VALUES 
                    ('Computer Science', 'Computer Science and Information Technology'),
                    ('Engineering', 'Engineering and Technology'),
                    ('Business', 'Business and Management'),
                    ('Arts', 'Arts and Humanities')
                ON CONFLICT (name) DO NOTHING
            """);

        } catch (SQLException e) {
            throw new SQLException("Error creating database tables: " + e.getMessage(), e);
        }
    }
}