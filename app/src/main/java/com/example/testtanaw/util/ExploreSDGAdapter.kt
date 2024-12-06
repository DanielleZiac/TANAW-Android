package com.example.testtanaw.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testtanaw.R

// SDG data class to hold information about each goal
data class SDG(val title: String, val description: String, val imageRes: Int)

class ExploreSDGAdapter(private val sdgGoals: List<SDG>) :
    RecyclerView.Adapter<ExploreSDGAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_sdg_goal, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sdg = sdgGoals[position]
        holder.titleTextView.text = sdg.title
        holder.descriptionTextView.text = sdg.description
        holder.imageView.setImageResource(sdg.imageRes)
    }

    override fun getItemCount(): Int {
        return sdgGoals.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.sdg_title)
        val descriptionTextView: TextView = view.findViewById(R.id.sdg_description)
        val imageView: ImageView = view.findViewById(R.id.sdg_image)
    }
}