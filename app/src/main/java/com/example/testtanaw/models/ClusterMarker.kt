package com.example.testtanaw.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class ClusterMarker implements ClusterItem {
    private final String title = null;
    private final String snippet = null;
    private final String userSdgId = null;
    private final String userId;
    private final String sdgNumber = null;
    private final String url = null;
    private final String caption = null;
    private final String createdDate = null;
    private final String institutionId = null;
    private final String phototChall = null;
    private final String institution = null;
    private final String campus = null;
    private final String institutionLogo = null;
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
        this.phototChall = phototChall;
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
        return phototChall;
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

    public String getAvatarUrl() {
        return avatarUrl;
    }
}
