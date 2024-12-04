package com.example.testtanaw.models

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class ClusterMarker(
    private var position: LatLng,
    private var title: String,
    private var snippet: String,
    private var zindex: Float,
    private var iconPicture: Int
) : ClusterItem {
    //    private lateinit var user: User

    override fun getPosition(): LatLng {
        return position
    }

    fun setPosition(postition: LatLng) {
        this.position = position
    }

    override fun getTitle(): String {
        return title
    }

    fun setTitle(title: String) {
        this.title = title
    }

    override fun getSnippet(): String? {
        return snippet
    }

    fun setSnippet(snippet: String) {
        this.snippet = snippet
    }

    override fun getZIndex(): Float? {
        return zindex
    }

    fun setZIndex(zindex: Float) {
        this.zindex = zindex
    }

    fun getIconPicture(): Int {
        return iconPicture
    }

    fun setIconPicture(iconPicture: Int) {
        this.iconPicture = iconPicture
    }
}