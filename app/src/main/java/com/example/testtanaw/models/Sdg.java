package com.example.testtanaw.models;

public class Sdg {
    private final int imageResId;
    private final String title;
    private final String description; // Added description to match your original structure

    // Constructor including description
    public Sdg(int imageResId, String title, String description) {
        this.imageResId = imageResId;
        this.title = title;
        this.description = description;
    }

    // Getter for image resource ID
    public int getImageResId() {
        return imageResId;
    }

    // Getter for title
    public String getTitle() {
        return title;
    }

    // Getter for description
    public String getDescription() {
        return description;
    }
}
