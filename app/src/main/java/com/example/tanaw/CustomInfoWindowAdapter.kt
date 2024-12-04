package com.example.tanaw

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class CustomInfoWindowAdapter(private val context: Context): GoogleMap.InfoWindowAdapter {
    override fun getInfoContents(p0: Marker): View? {
        val view: View = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null)

        return view
    }

    override fun getInfoWindow(p0: Marker): View? {
        val view: View = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null)

        return view
    }
}