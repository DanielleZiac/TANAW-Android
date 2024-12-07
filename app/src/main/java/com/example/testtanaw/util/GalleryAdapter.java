package com.example.testtanaw.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testtanaw.R;
import com.squareup.picasso.Picasso;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {
    private final List<String> images;

    public GalleryAdapter(List<String> images) {
        this.images = images;
    }

    public static class GalleryViewHolder extends RecyclerView.ViewHolder {
        public final ImageView imageView;

        public GalleryViewHolder(@NonNull View view) {
            super(view);
            imageView = view.findViewById(R.id.galleryImageView);
        }
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gallery_image, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
        Picasso.get()
                .load(images.get(position))
                .fit()
                .centerCrop()
                .placeholder(R.drawable.baseline_cached_24)
                .error(R.drawable.baseline_error_outline_24)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }
}
