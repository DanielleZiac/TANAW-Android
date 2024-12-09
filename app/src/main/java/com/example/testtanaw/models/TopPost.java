package com.example.testtanaw.models;

public class TopPost {
    private final int imageResId;
    private final String caption;

    public TopPost(int imageResId, String caption) {
        this.imageResId = imageResId;
        this.caption = caption;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getCaption() {
        return caption;
    }
}
