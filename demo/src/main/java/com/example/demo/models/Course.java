package com.example.demo.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.ObjectProperty;

public class Course {
    private StringProperty id;
    private StringProperty title;
    private StringProperty description;
    private ObjectProperty<Faculty> faculty;
    private double progress;
    private int studentCount;
    private List<Student> enrolledStudents;
    private List<Assessment> assessments;
    private List<CourseMaterial> materials;
    private List<String> announcements;

    public Course(String id, String title, String description, Faculty faculty) {
        this.id = new SimpleStringProperty(id);
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
        this.faculty = new SimpleObjectProperty<>(faculty);
        this.progress = 0.0;
        this.studentCount = 0;
        this.enrolledStudents = new ArrayList<>();
        this.assessments = new ArrayList<>();
        this.materials = new ArrayList<>();
        this.announcements = new ArrayList<>();
        initializeDefaultAssessments();
        initializeSampleMaterials();
    }

    private void initializeDefaultAssessments() {
        // Add default assessments for the course
        Assessment midterm = new Assessment("MID", "Midterm Exam", "EXAM", 0.3, 100);
        Assessment finalExam = new Assessment("FINAL", "Final Exam", "EXAM", 0.4, 100);
        Assessment project = new Assessment("PROJ", "Course Project", "PROJECT", 0.2, 100);
        Assessment quiz = new Assessment("QUIZ", "Quizzes", "QUIZ", 0.1, 100);
        
        assessments.addAll(Arrays.asList(midterm, finalExam, project, quiz));
    }

    private void initializeSampleMaterials() {
        materials.add(new CourseMaterial("Lecture 1: Introduction", "PDF", "lecture1.pdf"));
        materials.add(new CourseMaterial("Lecture 2: Basic Concepts", "VIDEO", "lecture2.mp4"));
        materials.add(new CourseMaterial("Assignment 1", "PDF", "assignment1.pdf"));
        materials.add(new CourseMaterial("Course Syllabus", "PDF", "syllabus.pdf"));
    }

    public void addAnnouncement(String announcement) {
        announcements.add(0, announcement); // Add to the beginning of the list
    }

    public void addMaterial(CourseMaterial material) {
        materials.add(material);
    }

    // Getters and setters
    public String getId() { return id.get(); }
    public void setId(String id) { this.id.set(id); }

    public String getTitle() { return title.get(); }
    public void setTitle(String title) { this.title.set(title); }

    public String getDescription() { return description.get(); }
    public void setDescription(String description) { this.description.set(description); }

    public Faculty getFaculty() { return faculty.get(); }
    public void setFaculty(Faculty faculty) { this.faculty.set(faculty); }

    public double getProgress() { return progress; }
    public void setProgress(double progress) { this.progress = progress; }

    public int getStudentCount() { return studentCount; }
    public void setStudentCount(int studentCount) { this.studentCount = studentCount; }

    public List<Student> getEnrolledStudents() { return enrolledStudents; }
    public void setEnrolledStudents(List<Student> enrolledStudents) { this.enrolledStudents = enrolledStudents; }

    public List<Assessment> getAssessments() { return assessments; }
    public void addAssessment(Assessment assessment) { assessments.add(assessment); }

    public List<CourseMaterial> getMaterials() { return materials; }
    public void setMaterials(List<CourseMaterial> materials) { this.materials = materials; }

    public List<String> getAnnouncements() { return announcements; }
    public void setAnnouncements(List<String> announcements) { this.announcements = announcements; }

    // Property getters for JavaFX binding
    public StringProperty idProperty() { return id; }
    public StringProperty titleProperty() { return title; }
    public StringProperty descriptionProperty() { return description; }
    public ObjectProperty<Faculty> facultyProperty() { return faculty; }

    // Helper methods
    public void enrollStudent(Student student) {
        if (!enrolledStudents.contains(student)) {
            enrolledStudents.add(student);
            studentCount = enrolledStudents.size();
        }
    }

    public void unenrollStudent(Student student) {
        enrolledStudents.remove(student);
        studentCount = enrolledStudents.size();
    }

    public void updateProgress(double newProgress) {
        this.progress = Math.min(1.0, Math.max(0.0, newProgress));
    }

    @Override
    public String toString() {
        return String.format("%s - %s", id.get(), title.get());
    }
} 