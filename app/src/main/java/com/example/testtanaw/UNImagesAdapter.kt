package com.example.testtanaw

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testtanaw.R

class UNImagesAdapter(
    private val fragment: ExploreFragment
) : RecyclerView.Adapter<UNImagesAdapter.ViewHolder>() {

    private val unImages = listOf(
        R.drawable.sdglink_1,  // Replace with actual image resource ids
        R.drawable.sdglink_2,
        R.drawable.sdglink_3
    )

    private val overlayTexts = listOf(
        "Message 1",  // Sample message 1
        "Message 2",  // Sample message 2
        "Message 3"   // Sample message 3
    )

    private val articles = listOf(
        "Article about Image 1",  // Sample article for the first image
        "Article about Image 2",  // Sample article for the second image
        "Article about Image 3"   // Sample article for the third image
    )

    private var itemClickListener: ((View, Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (View, Int) -> Unit) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_un_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageView.setImageResource(unImages[position])
        holder.overlayTextView.text = overlayTexts[position]  // Set the overlay text
        holder.itemView.setOnClickListener {
            // Trigger the modal when an image is clicked
            fragment.showImageDialog(unImages[position], articles[position])
        }
    }

    override fun getItemCount(): Int {
        return unImages.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.un_image)
        val overlayTextView: TextView = view.findViewById(R.id.overlay_text)  // Reference to TextView
    }
}
