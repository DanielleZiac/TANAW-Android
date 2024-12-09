package com.example.testtanaw.models;

public class Sdg {
    private final int imageResId;
    private final String title;

    public Sdg(int imageResId, String title) {
        this.imageResId = imageResId;
        this.title = title;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getTitle() {
        return title;
    }
}
