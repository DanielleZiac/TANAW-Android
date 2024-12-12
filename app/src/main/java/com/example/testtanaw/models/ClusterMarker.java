package com.example.testtanaw.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class ClusterMarker implements ClusterItem {

    private String title = null;
    private String snippet = null;
    private String userSdgId;
    private String userId;
    private String sdgNumber;
    private String caption;
    private Object createdDate;
    private String sdgPhotoUrl;
    private String photoChallenge;
    private LatLng position;
    private String avatarUrl;

    // Constructor
    public ClusterMarker(String userSdgId, String userId, String sdgNumber,
                         String caption, String sdgPhotoUrl,
                         String photoChallenge, double lat, double lng, String avatarUrl) {
        this.userSdgId = userSdgId;
        this.userId = userId;
        this.sdgNumber = sdgNumber;
        this.caption = caption;
        this.sdgPhotoUrl = sdgPhotoUrl;
        this.photoChallenge = photoChallenge;
        this.position = new LatLng(lat, lng);
        this.avatarUrl = avatarUrl;
    }

    @NonNull
    @Override
    public LatLng getPosition() {
        return position;
    }

    @Nullable
    @Override
    public String getTitle() {
        return caption;
    }

    @Nullable
    @Override
    public String getSnippet() {
        return photoChallenge;
    }

    @Nullable
    @Override
    public Float getZIndex() {
        return 0f;
    }

    // Getter methods for all fields
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

    public Object getCreatedDate() {
        return createdDate;
    }

    public String getSdgPhotoUrl() {
        return sdgPhotoUrl;
    }

    public String getPhotoChallenge() {
        return photoChallenge;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
}
