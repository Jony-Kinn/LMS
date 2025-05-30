package com.example.demo.models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;

public class AssessmentResult {
    private ObjectProperty<Assessment> assessment;
    private DoubleProperty score;
    private String submissionDate;
    private String submissionFile;

    public AssessmentResult(Assessment assessment, double score) {
        this.assessment = new SimpleObjectProperty<>(assessment);
        this.score = new SimpleDoubleProperty(score);
        this.submissionDate = "";
        this.submissionFile = "";
    }

    // Property getters for JavaFX binding
    public ObjectProperty<Assessment> assessmentProperty() { return assessment; }
    public DoubleProperty scoreProperty() { return score; }

    // Regular getters and setters
    public Assessment getAssessment() { return assessment.get(); }
    public void setAssessment(Assessment assessment) { this.assessment.set(assessment); }

    public double getScore() { return score.get(); }
    public void setScore(double score) { this.score.set(score); }

    public String getSubmissionDate() { return submissionDate; }
    public void setSubmissionDate(String submissionDate) { this.submissionDate = submissionDate; }

    public String getSubmissionFile() { return submissionFile; }
    public void setSubmissionFile(String submissionFile) { this.submissionFile = submissionFile; }

    @Override
    public String toString() {
        return String.format("%s - Score: %.1f", assessment.get().getName(), score.get());
    }
} 