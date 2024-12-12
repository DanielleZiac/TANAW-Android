package com.example.testtanaw.models;

import java.time.LocalDateTime;
import java.util.Date;

public class UserSdgPhoto {
    private String userSdgId;
    private String userId;
    private String sdgNumber;
    private String caption;
    private LocalDateTime createdAt;
    private String sdgPhotoUrl;
    private String photoChallenge;
    private Double latitude;
    private Double longitude;

    // No-argument constructor
    public UserSdgPhoto() {
    }

    // Parameterized constructor (optional)
    public UserSdgPhoto(String userSdgId, String userId, String sdgNumber, String caption, LocalDateTime createdAt, String sdgPhotoUrl, String photoChallenge, Double latitude, Double longitude) {
        this.userSdgId = userSdgId;
        this.userId = userId;
        this.sdgNumber = sdgNumber;
        this.caption = caption;
        this.createdAt = createdAt;
        this.sdgPhotoUrl = sdgPhotoUrl;
        this.photoChallenge = photoChallenge;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getUserSdgId() {
        return userSdgId;
    }

    public String getUserId() {
        return userId;
    }

    public String getSdgNumber() {
        return sdgNumber;
    }

    public String getCaption() {
        return caption;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getSdgPhotoUrl() {
        return sdgPhotoUrl;
    }

    public String getPhotoChallenge() {
        return photoChallenge;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }



    public static String getUserSdgPhotoPath(String sdgNumber, String filename) {
        return String.format(Constants.BUCKET_USERS_SDG_PHOTO_PATH_FORMAT, sdgNumber, filename);
    }
}
