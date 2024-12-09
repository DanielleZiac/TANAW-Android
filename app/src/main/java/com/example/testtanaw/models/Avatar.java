package com.example.testtanaw.models;

public class Avatar {
    private String userId;
    private String avatarUrl;
    private String bg;
    private String eye;
    private String sdg;
    private String gender;
    private String shirtStyle;
    private String smile;
    private String eyewear;

    // No-argument constructor
    public Avatar() {
    }

    // Parameterized constructor (optional)
    public Avatar(String userId, String avatarUrl, String bg, String eye, String sdg, String gender, String shirtStyle, String smile, String eyewear) {
        this.userId = userId;
        this.avatarUrl = avatarUrl;
        this.bg = bg;
        this.eye = eye;
        this.sdg = sdg;
        this.gender = gender;
        this.shirtStyle = shirtStyle;
        this.smile = smile;
        this.eyewear = eyewear;
    }

    public String getUserId() {
        return userId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getEyewear() {
        return eyewear;
    }

    public String getShirtStyle() {
        return shirtStyle;
    }

    public String getGender() {
        return gender;
    }

    public String getBg() {
        return bg;
    }
}

