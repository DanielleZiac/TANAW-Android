package com.example.tanaw.models

import android.graphics.Bitmap
import com.example.tanaw.R
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class ClusterMarker(
    private var position: LatLng,
    private var title: String,
    private var snippet: String,
    private var zindex: Float,
) : ClusterItem {

    override fun getPosition(): LatLng {
        return position
    }

    fun setPosition(postition: LatLng) {
        this.position = position
    }

    override fun getTitle(): String {
        return title
    }
    val avatar: Int = R.drawable.avatar
    fun getIconPicture(): Int {
        return avatar// Replace with actual default icon resource ID
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
}