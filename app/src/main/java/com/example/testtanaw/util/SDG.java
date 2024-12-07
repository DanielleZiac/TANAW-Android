package com.example.testtanaw.util;

public class SDG {
    private final String title;
    private final String description;
    private final int imageRes;

    public SDG(String title, String description, int imageRes) {
        this.title = title;
        this.description = description;
        this.imageRes = imageRes;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getImageRes() {
        return imageRes;
    }
}
