package com.example.tanaw

import com.google.android.gms.maps.model.LatLng
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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

@Serializable
data class SdgPhoto (
    @SerialName("user_sdg_id") val userSdgId: String,
    @SerialName("user_id") val userId: String,
    @SerialName("sdg_number") val sdgNumber: String,
    @SerialName("url") val url: String,
    @SerialName("caption") val caption: String,
    @SerialName("created_date") val createdDate: String,
    @SerialName("institution_id") val institutionId: String,
    @SerialName("photo_challenge") val phototChall: String,
    @SerialName("institution") val institution: String,
    @SerialName("campus") val campus: String,
    @SerialName("institution_logo") val institutionLogo: String,
    @SerialName("lat") val lat: Double,
    @SerialName("long") val long: Double,
    @SerialName("avatar_url") val avatarUrl: String,
)