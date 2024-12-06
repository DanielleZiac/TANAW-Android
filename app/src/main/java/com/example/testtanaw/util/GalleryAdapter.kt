package com.example.testtanaw.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.testtanaw.R
import com.squareup.picasso.Picasso

class GalleryAdapter(private val images: List<String>) :
    RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    inner class GalleryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.galleryImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gallery_image, parent, false)
        return GalleryViewHolder(view)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
//        val resourceId = holder.itemView.context.resources.getIdentifier(
//            images[position], "drawable", holder.itemView.context.packageName
//        )
//        holder.imageView.setImageResource(resourceId)
        Picasso
            .get()
            .load(images[position])
            .fit()
            .centerCrop()
            .placeholder(R.drawable.baseline_cached_24)
            .error(R.drawable.baseline_error_outline_24)
            .into(holder.imageView)
    }

    override fun getItemCount() = images.size
}
