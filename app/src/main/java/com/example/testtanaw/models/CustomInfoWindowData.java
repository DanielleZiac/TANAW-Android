package com.example.testtanaw.models;

import com.google.android.gms.maps.model.LatLng;

public class CustomInfoWindowData {
    private final String title;
    private final String snippet;
    private final String userSdgId;
    private final String userId;
    private final String sdgNumber;
    private final String url;
    private final String caption;
    private final String createdDate;
    private final String institutionId;
    private final String phototChall;
    private final String institution;
    private final String campus;
    private final String institutionLogo;
    private final LatLng position;
    private final String avatarUrl;

    public CustomInfoWindowData(String title, String snippet, String userSdgId, String userId,
                                String sdgNumber, String url, String caption, String createdDate,
                                String institutionId, String phototChall, String institution,
                                String campus, String institutionLogo, LatLng position, String avatarUrl) {
        this.title = title;
        this.snippet = snippet;
        this.userSdgId = userSdgId;
        this.userId = userId;
        this.sdgNumber = sdgNumber;
        this.url = url;
        this.caption = caption;
        this.createdDate = createdDate;
        this.institutionId = institutionId;
        this.phototChall = phototChall;
        this.institution = institution;
        this.campus = campus;
        this.institutionLogo = institutionLogo;
        this.position = position;
        this.avatarUrl = avatarUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getSnippet() {
        return snippet;
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

    public String getPhototChall() {
        return phototChall;
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

    public LatLng getPosition() {
        return position;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
}
