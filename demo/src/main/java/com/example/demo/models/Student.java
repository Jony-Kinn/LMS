package com.example.demo.models;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private double overallGrade;
    private List<AssessmentResult> results;

    public Student(String username, String password, String fullName) {
        super(username, password, fullName);
        this.overallGrade = 0.0;
        this.results = new ArrayList<>();
    }

    public void addResult(AssessmentResult result) {
        results.add(result);
        calculateOverallGrade();
    }

    private void calculateOverallGrade() {
        if (results.isEmpty()) {
            overallGrade = 0.0;
            return;
        }
        double total = 0;
        double totalWeight = 0;
        for (AssessmentResult result : results) {
            total += result.getScore() * result.getAssessment().getWeight();
            totalWeight += result.getAssessment().getWeight();
        }
        overallGrade = total / totalWeight;
    }

    public double getOverallGrade() { 
        return overallGrade; 
    }
    
    public List<AssessmentResult> getResults() { 
        return results; 
    }

    @Override
    public String toString() {
        return String.format("%s - %s (Grade: %.2f)", getUsername(), getFullName(), overallGrade);
    }
} 