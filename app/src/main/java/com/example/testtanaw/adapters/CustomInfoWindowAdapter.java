package com.example.testtanaw.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.testtanaw.R;
import com.example.testtanaw.models.CustomInfoWindowData;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final Context context;

    public CustomInfoWindowAdapter(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);

        // Initialize the like button here if needed
        ImageView likeButton = view.findViewById(R.id.like_button);

        // Handle the heart icon toggle click event
        final boolean[] isLiked = {false};  // Array to make it effectively final
        likeButton.setOnClickListener(v -> {
            isLiked[0] = !isLiked[0];  // Toggle the state
            if (isLiked[0]) {
                likeButton.setImageResource(R.drawable.ic_heart_filled);  // Filled heart
            } else {
                likeButton.setImageResource(R.drawable.ic_heart_outline);  // Heart outline
            }
        });

        return view;
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
        RoundedImageView sdgContentImageView = view.findViewById(R.id.sdgContentImageView);
        TextView sdgCaptionTextView = view.findViewById(R.id.sdgCaptionTextView);
        TextView sdgPhotoChallTextView = view.findViewById(R.id.sdgPhotoChallTextView);
        ImageView likeButton = view.findViewById(R.id.like_button);

        CustomInfoWindowData data = (CustomInfoWindowData) marker.getTag();
        // Load image using Picasso
        Picasso.get()
                .load(data.getSdgPhotoUrl())
                .resize(1000, 1000)
                .centerInside()
                .placeholder(R.drawable.loading)
                .into(sdgContentImageView);


        // Set the caption and photo challenge text
        sdgCaptionTextView.setText(data.getCaption());
        sdgPhotoChallTextView.setText(data.getPhotoChallenge());

        // Handle the like button toggle logic
        final boolean[] isLiked = {false};  // Array to make it effectively final
        likeButton.setOnClickListener(v -> {
            isLiked[0] = !isLiked[0];  // Toggle the state
            if (isLiked[0]) {
                likeButton.setImageResource(R.drawable.ic_heart_filled);  // Filled heart
            } else {
                likeButton.setImageResource(R.drawable.ic_heart_outline);  // Heart outline
            }
        });

        return view;
    }
}
