package com.example.testtanaw.models;

import com.google.android.gms.maps.model.LatLng;

public class CustomInfoWindowData {

    private String title;
    private String snippet;
    private String userSdgId;
    private String userId;
    private String sdgNumber;
    private String caption;
    private String createdDate;
    private String sdgPhotoUrl;
    private String photoChallenge;
    private LatLng position;
    private String avatarUrl;

    // Constructor
    public CustomInfoWindowData(String userSdgId, String userId, String sdgNumber, String caption,
                                String sdgPhotoUrl, String photoChallenge,
                                LatLng position, String avatarUrl) {
        this.title = null;  // Can be set if needed
        this.snippet = null;  // Can be set if needed
        this.userSdgId = userSdgId;
        this.userId = userId;
        this.sdgNumber = sdgNumber;
        this.caption = caption;
        this.sdgPhotoUrl = sdgPhotoUrl;
        this.photoChallenge = photoChallenge;
        this.position = position;
        this.avatarUrl = avatarUrl;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getUserSdgId() {
        return userSdgId;
    }

    public void setUserSdgId(String userSdgId) {
        this.userSdgId = userSdgId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSdgNumber() {
        return sdgNumber;
    }

    public void setSdgNumber(String sdgNumber) {
        this.sdgNumber = sdgNumber;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getSdgPhotoUrl() {
        return sdgPhotoUrl;
    }

    public void setSdgPhotoUrl(String sdgPhotoUrl) {
        this.sdgPhotoUrl = sdgPhotoUrl;
    }

    public String getPhotoChallenge() {
        return photoChallenge;
    }

    public void setPhotoChallenge(String photoChallenge) {
        this.photoChallenge = photoChallenge;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
