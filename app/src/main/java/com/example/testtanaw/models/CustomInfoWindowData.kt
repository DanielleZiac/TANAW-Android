package com.example.testtanaw.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.gms.maps.model.LatLng;

public class CustomInfoWindowData {
    @NonNull private final String title;
    @NonNull private final String snippet;
    @NonNull private final String userSdgId;
    @NonNull private final String userId;
    @NonNull private final String sdgNumber;
    @NonNull private final String url;
    @NonNull private final String caption;
    @NonNull private final String createdDate;
    @NonNull private final String institutionId;
    @NonNull private final String phototChall;
    @NonNull private final String institution;
    @NonNull private final String campus;
    @NonNull private final String institutionLogo;
    @NonNull private final LatLng position;
    @Nullable private final String avatarUrl;

    public CustomInfoWindowData(@NonNull String title, @NonNull String snippet, 
                              @NonNull String userSdgId, @NonNull String userId,
                              @NonNull String sdgNumber, @NonNull String url, 
                              @NonNull String caption, @NonNull String createdDate,
                              @NonNull String institutionId, @NonNull String phototChall, 
                              @NonNull String institution, @NonNull String campus, 
                              @NonNull String institutionLogo, @NonNull LatLng position, 
                              @Nullable String avatarUrl) {
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

    @NonNull public String getTitle() { return title; }
    @NonNull public String getSnippet() { return snippet; }
    @NonNull public String getUserSdgId() { return userSdgId; }
    @NonNull public String getUserId() { return userId; }
    @NonNull public String getSdgNumber() { return sdgNumber; }
    @NonNull public String getUrl() { return url; }
    @NonNull public String getCaption() { return caption; }
    @NonNull public String getCreatedDate() { return createdDate; }
    @NonNull public String getInstitutionId() { return institutionId; }
    @NonNull public String getPhototChall() { return phototChall; }
    @NonNull public String getInstitution() { return institution; }
    @NonNull public String getCampus() { return campus; }
    @NonNull public String getInstitutionLogo() { return institutionLogo; }
    @NonNull public LatLng getPosition() { return position; }
    @Nullable public String getAvatarUrl() { return avatarUrl; }
}
