package com.example.testtanaw.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class ClusterMarker implements ClusterItem {
    private final String title;
    private final String snippet;
    private final String userSdgId;
    private final String userId;
    private final String sdgNumber;
    private final String url;
    private final String caption;
    private final String createdDate;
    private final String institutionId;
    private final String photoChall;
    private final String institution;
    private final String campus;
    private final String institutionLogo;
    private final LatLng position;
    private final String avatarUrl;

    public ClusterMarker(String title, String snippet, String userSdgId, String userId,
                         String sdgNumber, String url, String caption, String createdDate,
                         String institutionId, String phototChall, String institution,
                         String campus, String institutionLogo, double lat, double lng,
                         String avatarUrl) {
        this.title = title;
        this.snippet = snippet;
        this.userSdgId = userSdgId;
        this.userId = userId;
        this.sdgNumber = sdgNumber;
        this.url = url;
        this.caption = caption;
        this.createdDate = createdDate;
        this.institutionId = institutionId;
        this.photoChall = phototChall;
        this.institution = institution;
        this.campus = campus;
        this.institutionLogo = institutionLogo;
        this.position = new LatLng(lat, lng);
        this.avatarUrl = avatarUrl;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    @Override
    public String getTitle() {
        return caption;
    }

    @Override
    public String getSnippet() {
        return photoChall;
    }

    @Override
    public float getZIndex() {
        return 0f;
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

    public String getUrl() {
        return url;
    }

    public String getCaption() {
        return caption;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getInstitutionId() {
        return institutionId;
    }

    public String getPhotoChall() {
        return photoChall;
    }

    public String getInstitution() {
        return institution;
    }

    public String getCampus() {
        return campus;
    }

    public String getInstitutionLogo() {
        return institutionLogo;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
}