package com.example.testtanaw

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testtanaw.models.TopPost

// Adapter for the top posts list
class TopPostsAdapter(private val topPosts: List<TopPost>) :
    RecyclerView.Adapter<TopPostsAdapter.TopPostViewHolder>() {

    // Inflates the item layout and returns the ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopPostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_top_post, parent, false)
        return TopPostViewHolder(view)
    }

    // Binds the data to the ViewHolder
    override fun onBindViewHolder(holder: TopPostViewHolder, position: Int) {
        val topPost = topPosts[position]
        holder.bind(topPost)
    }

    // Returns the size of the list
    override fun getItemCount(): Int {
        return topPosts.size
    }

    // Inner ViewHolder to hold the item view
    inner class TopPostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val postImage: ImageView = itemView.findViewById(R.id.postImage)
        private val postCaption: TextView = itemView.findViewById(R.id.postCaption)

        fun bind(topPost: TopPost) {
            postImage.setImageResource(topPost.imageResId)
            postCaption.text = topPost.caption
        }
    }
}
