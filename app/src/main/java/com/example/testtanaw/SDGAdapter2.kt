package com.example.testtanaw

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class SDGAdapter2(private val sdgImages: List<String>) :
    RecyclerView.Adapter<SDGAdapter2.SDGViewHolder>() {

    inner class SDGViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.sdgImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SDGViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sdg_circle, parent, false)
        return SDGViewHolder(view)
    }

    override fun onBindViewHolder(holder: SDGViewHolder, position: Int) {
        val resourceId = holder.itemView.context.resources.getIdentifier(
            sdgImages[position], "drawable", holder.itemView.context.packageName
        )
        holder.imageView.setImageResource(resourceId)
    }

    override fun getItemCount() = sdgImages.size
}
