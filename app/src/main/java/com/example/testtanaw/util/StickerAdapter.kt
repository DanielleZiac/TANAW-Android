package com.example.testtanaw.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testtanaw.R
import com.example.testtanaw.models.Sdg

class StickerAdapter(private val sdgList: List<Sdg>) : RecyclerView.Adapter<StickerAdapter.SdgViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SdgViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sticker, parent, false)
        return SdgViewHolder(view)
    }

    override fun onBindViewHolder(holder: SdgViewHolder, position: Int) {
        val sdg = sdgList[position]
        holder.imageView.setImageResource(sdg.imageResId)
        holder.textView.text = sdg.title
    }

    override fun getItemCount() = sdgList.size

    class SdgViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val textView: TextView = itemView.findViewById(R.id.textStickers)
    }
}
