package com.example.testtanaw.models;

public class SDGItem {
    private final String title;
    private final int iconResId;

    public SDGItem(String title, int iconResId) {
        this.title = title;
        this.iconResId = iconResId;
    }

    public String getTitle() {
        return title;
    }

    public int getIconResId() {
        return iconResId;
    }
}
