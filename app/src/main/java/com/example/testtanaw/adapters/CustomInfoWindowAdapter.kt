package com.example.testtanaw.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.testtanaw.R
import com.example.testtanaw.models.CustomInfoWindowData
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso

class CustomInfoWindowAdapter(private val context: Context): GoogleMap.InfoWindowAdapter {
    override fun getInfoContents(p0: Marker): View? {
        val view: View = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null)

        // Initialize the like button here if needed
        val likeButton: ImageView = view.findViewById(R.id.like_button)

        // Handle the heart icon toggle click event
        var isLiked = false  // Boolean to track the like status
        likeButton.setOnClickListener {
            isLiked = !isLiked  // Toggle the state
            if (isLiked) {
                likeButton.setImageResource(R.drawable.ic_heart_filled)  // Filled heart
            } else {
                likeButton.setImageResource(R.drawable.ic_heart_outline)  // Heart outline
            }
        }

        return view
    }

    override fun getInfoWindow(marker: Marker): View? {
        val view: View = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null)
        val sdgContentImageVIew: RoundedImageView = view.findViewById<RoundedImageView>(R.id.sdgContentImageView)
        val sdgCaptionTextView: TextView = view.findViewById<TextView>(R.id.sdgCaptionTextView)
        val sdgPhotoChallTextView: TextView = view.findViewById<TextView>(R.id.sdgPhotoChallTextView)
        val likeButton: ImageView = view.findViewById(R.id.like_button)

        val data = marker?.tag as CustomInfoWindowData

        // can add more
        Picasso.get().load(data.url).resize(1000, 1000).centerInside().placeholder(R.drawable.loading).into(sdgContentImageVIew)
        sdgCaptionTextView.text = data.caption
        sdgPhotoChallTextView.text = data.phototChall

        // Set the caption and photo challenge text
        sdgCaptionTextView.text = data.caption
        sdgPhotoChallTextView.text = data.phototChall

        // Handle the like button toggle logic (similar to getInfoContents)
        var isLiked = false  // Boolean to track the like status
        likeButton.setOnClickListener {
            isLiked = !isLiked  // Toggle the state
            if (isLiked) {
                likeButton.setImageResource(R.drawable.ic_heart_filled)  // Filled heart
            } else {
                likeButton.setImageResource(R.drawable.ic_heart_outline)  // Heart outline
            }
        }

        return view
    }
}