package com.example.testtanaw.models

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem


class ClusterMarker(
    title: String? = null,
    snippet: String? = null,
    userSdgId: String? = null,
    userId: String,
    sdgNumber: String? = null,
    url: String? = null,
    caption: String? = null,
    createdDate: String? = null,
    institutionId: String? = null,
    phototChall: String? = null,
    institution: String? = null,
    campus: String? = null,
    institutionLogo: String? = null,
    lat: Double,
    long: Double,
    avatarUrl: String
) : ClusterItem {
    private  val title: String?
    private val snippet: String?
    private val userSdgId: String?
    private val userId: String
    private val sdgNumber: String?
    private val url: String?
    private val caption: String?
    private val createdDate: String?
    private val institutionId: String?
    private val phototChall: String?
    private val institution: String?
    private val campus: String?
    private val institutionLogo: String?
    private val position: LatLng
    private val avatarUrl: String

    init {
        this.title = title
        this.snippet = snippet
        this.userSdgId = userSdgId
        this.userId = userId
        this.sdgNumber = sdgNumber
        this.url = url
        this.caption = caption
        this.createdDate = createdDate
        this.institutionId = institutionId
        this.phototChall = phototChall
        this.institution = institution
        this.campus = campus
        this.institutionLogo = institutionLogo
        this.position = LatLng(lat, long)
        this.avatarUrl = avatarUrl
    }

    override fun getPosition(): LatLng {
        return position
    }

    override fun getTitle(): String? {
        return caption
    }

    override fun getSnippet(): String? {
        return phototChall
    }

    override fun getZIndex(): Float {
        return 0f
    }

    fun getUserSdgId(): String? {
        return userSdgId
    }

    fun getUserId(): String {
        return userId
    }

    fun getSdgNumber(): String? {
        return sdgNumber
    }

    fun getUrl(): String? {
        return url
    }

    fun getCaption(): String? {
        return caption
    }

    fun getCreatedDate(): String? {
        return createdDate
    }

    fun getInstitutionId(): String? {
        return institutionId
    }

    fun getPhototChall(): String? {
        return phototChall
    }

    fun getInstitution(): String? {
        return institution
    }

    fun getCampus(): String? {
        return campus
    }

    fun getInstitutionLogo(): String? {
        return institutionLogo
    }

    fun getAvatarUrl(): String {
        return avatarUrl
    }
}