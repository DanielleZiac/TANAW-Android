package com.example.testtanaw.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testtanaw.R
import com.example.testtanaw.fragments.ExploreFragment

class UNImagesAdapter(
    private val fragment: ExploreFragment
) : RecyclerView.Adapter<UNImagesAdapter.ViewHolder>() {

    private val unImages = listOf(
        R.drawable.un1,  // Replace with actual image resource ids
        R.drawable.un2,
        R.drawable.un3
    )

    private val overlayTexts = listOf(
        "The United Nations General Assembly calls for urgent measures to end the humanitarian crisis in Gaza amidst ongoing conflict.",  // Sample message 1
        "Message 2",  // Sample message 2
        "Message 3"   // Sample message 3
    )

    private val articles = listOf(
        "On December 4, 2024, the United Nations General Assembly resumed its emergency special session, addressing the dire situation in Gaza. Delegates stressed the urgency of halting the ongoing violence and implementing measures to alleviate the suffering of civilians. The humanitarian crisis in Gaza has reached alarming levels, with many civilians facing critical shortages of basic necessities such as food, medicine, and shelter. The international community, including the UN, is calling for immediate interventions to ensure the protection of civilians and the provision of essential aid. Delegates also discussed the importance of a long-term solution that can bring lasting peace to the region and address the root causes of the conflict.",  // Sample article for the first image
        "On December 3, 2024, the UN General Assembly adopted three key resolutions aimed at advancing peace in the Middle East, with a focus on the two-state solution for Israel and Palestine. The resolutions call for a ceasefire in Gaza and greater humanitarian access to the region. The General Assembly has emphasized that a two-state solution remains the only viable path to lasting peace between Israelis and Palestinians. In addition, the resolutions advocate for the right of Palestinian refugees to return to their homes and for the international community to provide necessary assistance to both Israel and Palestine in achieving a sustainable peace.",  // Sample article for the second image
        "As the war between Russia and Ukraine continues into its third year, the UN has drawn attention to its devastating effects on children. During a Security Council meeting on December 4, 2024, a senior UN official shared troubling reports regarding the systematic abduction and adoption of Ukrainian children. The war has disrupted countless young lives, with many children subjected to violence, displacement, and separation from their families. The UN has called for international efforts to address these violations, highlighting the need for humanitarian assistance and the protection of children's rights in conflict zones."   // Sample article for the third image
    )

    private var itemClickListener: ((View, Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (View, Int) -> Unit) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_un_image, parent, false)
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
        val overlayTextView: TextView =
            view.findViewById(R.id.overlay_text)  // Reference to TextView
    }
}
