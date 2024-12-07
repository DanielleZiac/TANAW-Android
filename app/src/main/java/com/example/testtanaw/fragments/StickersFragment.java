package com.example.testtanaw.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testtanaw.AvatarActivity
import com.example.testtanaw.R
import com.example.testtanaw.models.Sdg
import com.example.testtanaw.util.StickerAdapter

class StickersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_stickers, container, false)

        // Initialize the first RecyclerView (Unlocked)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val sdgList = generateSdgList()
        val adapter = StickerAdapter(sdgList)
        recyclerView.adapter = adapter

        // Adjust height of the first RecyclerView
        adjustRecyclerViewHeight(recyclerView, sdgList.size)

        // Initialize the second RecyclerView (Locked)
        val recyclerView2: RecyclerView = view.findViewById(R.id.recyclerView2)
        recyclerView2.layoutManager = LinearLayoutManager(requireContext())

        val placeholderList = ArrayList<Sdg>() // Placeholder variable
        placeholderList.addAll(sdgList) // Copy data from sdgList
        val adapter2 = StickerAdapter(placeholderList)
        recyclerView2.adapter = adapter2

        // Adjust height of the second RecyclerView
        adjustRecyclerViewHeight(recyclerView2, placeholderList.size)

        // Set up the OnClickListener for the "Edit Avatar" button
        val editAvatarButton = view.findViewById<Button>(R.id.editAvatar)
        editAvatarButton.setOnClickListener {
            // Navigate to AvatarActivity
            val intent = Intent(requireContext(), AvatarActivity::class.java)
            startActivity(intent)
        }
        return view
    }

    private fun generateSdgList(): List<Sdg> {
        val sdgTitles = listOf(
            "No Poverty", "Zero Hunger", "Good Health and Well-Being",
            "Quality Education", "Gender Equality", "Clean Water and Sanitation",
            "Affordable and Clean Energy", "Decent Work and Economic Growth",
            "Industry, Innovation and Infrastructure", "Reduced Inequalities",
            "Sustainable Cities and Communities", "Responsible Consumption and Production",
            "Climate Action", "Life Below Water", "Life on Land",
            "Peace, Justice and Strong Institutions", "Partnerships for the Goals"
        )

        return (1..17).map { index ->
            val imageResId = resources.getIdentifier("background_sdg$index", "drawable", requireContext().packageName)
            Sdg(imageResId, "SDG $index: ${sdgTitles[index - 1]}")
        }
    }

    private fun adjustRecyclerViewHeight(recyclerView: RecyclerView, itemCount: Int) {
        recyclerView.post {
            val itemHeight = resources.getDimensionPixelSize(R.dimen.item_height) // Define item height in dimens.xml
            val totalHeight = itemHeight * itemCount
            val layoutParams = recyclerView.layoutParams
            layoutParams.height = totalHeight
            recyclerView.layoutParams = layoutParams
        }
    }
}
