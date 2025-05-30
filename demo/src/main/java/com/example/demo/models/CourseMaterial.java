package com.example.demo.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CourseMaterial {
    private StringProperty title;
    private StringProperty type;
    private StringProperty filePath;

    public CourseMaterial(String title, String type, String filePath) {
        this.title = new SimpleStringProperty(title);
        this.type = new SimpleStringProperty(type);
        this.filePath = new SimpleStringProperty(filePath);
    }

    // Property getters for JavaFX binding
    public StringProperty titleProperty() { return title; }
    public StringProperty typeProperty() { return type; }
    public StringProperty filePathProperty() { return filePath; }

    // Regular getters and setters
    public String getTitle() { return title.get(); }
    public void setTitle(String title) { this.title.set(title); }

    public String getType() { return type.get(); }
    public void setType(String type) { this.type.set(type); }

    public String getFilePath() { return filePath.get(); }
    public void setFilePath(String filePath) { this.filePath.set(filePath); }

    @Override
    public String toString() {
        return String.format("%s (%s)", title.get(), type.get());
    }
} 