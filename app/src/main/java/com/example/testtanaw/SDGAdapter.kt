package com.example.testtanaw

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testtanaw.models.SDGItem

class SDGAdapter(
    private val sdgList: List<SDGItem>,
    private val onItemClick: (SDGItem) -> Unit // Click listener
) : RecyclerView.Adapter<SDGAdapter.SDGViewHolder>() {

    // ViewHolder to represent a single SDG item
    class SDGViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sdgIcon: ImageView = view.findViewById(R.id.sdgIcon)
        val sdgTitle: TextView = view.findViewById(R.id.sdgTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SDGViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sdg, parent, false)
        return SDGViewHolder(view)
    }

    override fun onBindViewHolder(holder: SDGViewHolder, position: Int) {
        val sdg = sdgList[position]
        holder.sdgIcon.setImageResource(sdg.iconResId) // Set image resource
        holder.sdgTitle.text = sdg.title // Set title text

        // Apply alignment logic based on the index
//        val alignmentClass: Int
        val marginTop: Int
        val marginBottom: Int
        val marginLeft: Int
        val marginRight: Int

        when (position % 6) {
            1, 4 -> {
                // Justify-center
//                alignmentClass = ViewGroup.LayoutParams.MATCH_PARENT
                marginTop = 0
                marginBottom = 0
                marginLeft = 0
                marginRight = 0
            }
            2, 3 -> {
                // Justify-end
//                alignmentClass = ViewGroup.LayoutParams.MATCH_PARENT
                marginTop = 50
                marginBottom = -50
                marginLeft = 0
                marginRight = 0
            }
            else -> {
                // Justify-start
//                alignmentClass = ViewGroup.LayoutParams.MATCH_PARENT
                marginTop = -50
                marginBottom = 50
                marginLeft = 0
                marginRight = 0
            }
        }

        // Dynamically set layout parameters
//        val params = holder.itemView.layoutParams
//        params.width = alignmentClass
//        holder.itemView.layoutParams = params
//
//        // Set additional margins and padding
//        holder.itemView.setPadding(paddingLeft, marginTop, paddingRight, marginBottom)

        // Apply dynamic margins
        val params = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
        params.setMargins(marginLeft, marginTop, marginRight, marginBottom)
        holder.itemView.layoutParams = params

        // Apply dynamic padding
        holder.itemView.setPadding(16, 8, 16, 8)

        // Show title on focus and hide when unfocused
        holder.itemView.setOnFocusChangeListener { _, hasFocus ->
            holder.sdgTitle.visibility = if (hasFocus) View.VISIBLE else View.GONE
        }

        // Show title on card click
        holder.itemView.setOnClickListener {
            holder.sdgTitle.visibility = View.VISIBLE  // Show title
            onItemClick(sdg)  // Handle click event
        }

        // Optionally hide the title when the card is unclicked or after a delay
        holder.itemView.setOnLongClickListener {
            holder.sdgTitle.visibility = View.GONE  // Hide title
            true  // Handle long click
        }
    }

    override fun getItemCount(): Int = sdgList.size
}
