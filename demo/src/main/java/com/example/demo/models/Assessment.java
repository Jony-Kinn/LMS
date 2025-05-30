package com.example.demo.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.DoubleProperty;

public class Assessment {
    private StringProperty id;
    private StringProperty name;
    private StringProperty type;
    private DoubleProperty weight;
    private DoubleProperty maxScore;
    private StringProperty courseId;

    public Assessment(String id, String name, String type, double weight, double maxScore) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.type = new SimpleStringProperty(type);
        this.weight = new SimpleDoubleProperty(weight);
        this.maxScore = new SimpleDoubleProperty(maxScore);
        this.courseId = new SimpleStringProperty("");
    }

    // Property getters for JavaFX binding
    public StringProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public StringProperty typeProperty() { return type; }
    public DoubleProperty weightProperty() { return weight; }
    public DoubleProperty maxScoreProperty() { return maxScore; }
    public StringProperty courseProperty() { return courseId; }

    // Regular getters and setters
    public String getId() { return id.get(); }
    public void setId(String id) { this.id.set(id); }

    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }

    public String getType() { return type.get(); }
    public void setType(String type) { this.type.set(type); }

    public double getWeight() { return weight.get(); }
    public void setWeight(double weight) { this.weight.set(weight); }

    public double getMaxScore() { return maxScore.get(); }
    public void setMaxScore(double maxScore) { this.maxScore.set(maxScore); }

    public String getCourseId() { return courseId.get(); }
    public void setCourseId(String courseId) { this.courseId.set(courseId); }

    @Override
    public String toString() {
        return String.format("%s - %s", id.get(), name.get());
    }
} 