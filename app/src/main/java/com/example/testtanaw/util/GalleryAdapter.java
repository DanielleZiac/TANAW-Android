package com.example.testtanaw.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.testtanaw.R;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

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
        Context context = holder.itemView.getContext();
        String imageUrl = images.get(position);
        Glide.with(context).load(imageUrl).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }
}