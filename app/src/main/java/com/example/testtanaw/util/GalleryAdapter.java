package com.example.testtanaw.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import com.example.testtanaw.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {
    private static final String TAG = "GalleryAdapter";


    private final List<String> images;

    public GalleryAdapter(List<String> images) {
        this.images = images;
    }

    public static class GalleryViewHolder extends RecyclerView.ViewHolder {
        public final ImageView imageView;

        public GalleryViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.galleryImageView);
        }
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery_image, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
        Picasso.get()
                .load(images.get(position))
                .fit()
                .centerCrop()
                .rotate(90)
                .placeholder(R.drawable.loading)
                .error(R.drawable.baseline_error_outline_24)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }
}