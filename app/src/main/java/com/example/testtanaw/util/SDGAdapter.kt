package com.example.testtanaw.util

import android.content.Intent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testtanaw.R
import com.example.testtanaw.SdgMapActivity
import com.example.testtanaw.models.SDGItem

class SDGAdapter(
    private val sdgList: List<SDGItem>,
    private val onItemClick: (SDGItem) -> Unit // Click listener
) : RecyclerView.Adapter<SDGAdapter.SDGViewHolder>() {

    // ViewHolder to represent a single SDG item
    class SDGViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sdgIcon: ImageView = view.findViewById(R.id.sdgIcon)
        val sdgNumber: TextView = view.findViewById(R.id.sdgNumber)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SDGViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sdg, parent, false)
        return SDGViewHolder(view)
    }

    override fun onBindViewHolder(holder: SDGViewHolder, position: Int) {
        val sdg = sdgList[position]

        holder.sdgIcon.setImageResource(sdg.iconResId) // Set image resource
        holder.sdgNumber.text = "SDG ${position + 1}" // Display SDG number (position + 1)

        // Apply alignment logic based on the index
        val marginTop: Int
        val marginBottom: Int
        val marginLeft: Int
        val marginRight: Int

        when (position % 6) {
            1, 4 -> {
                // Justify-center
                marginTop = 50
                marginBottom = -50
                marginLeft = 0
                marginRight = 0
            }

            2, 3 -> {
                // Justify-end
                marginTop = 100
                marginBottom = -100
                marginLeft = 0
                marginRight = 0
            }

            else -> {
                // Justify-start
                marginTop = 0
                marginBottom = 0
                marginLeft = 0
                marginRight = 0
            }
        }

        // Apply dynamic margins
        val params = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
        params.setMargins(marginLeft, marginTop, marginRight, marginBottom)
        holder.itemView.layoutParams = params

        // Apply dynamic padding
//        holder.itemView.setPadding(16, 8, 16, 8)

        // Initially hide the title
        holder.sdgNumber.visibility = View.GONE

        // Set the touch listener to detect press and release
        holder.itemView.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Show title when pressed
                    holder.sdgNumber.visibility = View.VISIBLE
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    // Hide title when released
                    holder.sdgNumber.visibility = View.GONE
                }
            }

            // Detect click event and call performClick for accessibility
            if (event.action == MotionEvent.ACTION_UP) {
                v.performClick() // Call performClick to ensure proper accessibility behavior
            }

            // Return true to indicate that the touch event has been handled
            true
        }

        // Set onClickListener for SDG image to navigate to the new activity
        holder.itemView.setOnClickListener {
            // Create an Intent to open the SDGDetailActivity
            val intent = Intent(holder.itemView.context, SdgMapActivity::class.java)

            // Pass the SDG title (or other relevant information) to the new activity
            intent.putExtra("SDG_TITLE", sdg.title)
            intent.putExtra("sdgNumber", position + 1)

            // Start the new activity
            holder.itemView.context.startActivity(intent)
        }

        // Optionally handle item click logic here
//        holder.itemView.setOnClickListener {
//            onItemClick(sdg)
//        }
    }

    override fun getItemCount(): Int = sdgList.size
}
