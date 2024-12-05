package com.example.testtanaw.models

import com.google.android.gms.maps.model.LatLng

data class CustomInfoWindowData(
    val title: String? = null,
    val snippet: String? = null,
    val userSdgId: String?,
    val userId: String,
    val sdgNumber: String?,
    val url: String?,
    val caption: String?,
    val createdDate: String?,
    val institutionId: String?,
    val phototChall: String?,
    val institution: String?,
    val campus: String?,
    val institutionLogo: String?,
    val position: LatLng,
    val avatarUrl: String
);