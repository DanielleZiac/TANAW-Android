package com.example.testtanaw.fragments

import com.example.testtanaw.models.SDGItem
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testtanaw.R
import com.example.testtanaw.models.GridSpacingItemDecoration
import com.example.testtanaw.util.SDGAdapter

class SdgHomeFragment : Fragment() {

    private var lastToastTime: Long = 0 // Track the last toast time

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sdg_home, container, false)

        val sdgList = listOf(
            SDGItem("No Poverty", R.drawable.sdg_1),
            SDGItem("Goal 2", R.drawable.sdg_2),
            SDGItem("Goal 3", R.drawable.sdg_3),
            SDGItem("Goal 4", R.drawable.sdg_4),
            SDGItem("Goal 5", R.drawable.sdg_5),
            SDGItem("Goal 6", R.drawable.sdg_6),
            SDGItem("Goal 7", R.drawable.sdg_7),
            SDGItem("Goal 8", R.drawable.sdg_8),
            SDGItem("Goal 9", R.drawable.sdg_9),
            SDGItem("Goal 10", R.drawable.sdg_10),
            SDGItem("Goal 11", R.drawable.sdg_11),
            SDGItem("Goal 12", R.drawable.sdg_12),
            SDGItem("Goal 13", R.drawable.sdg_13),
            SDGItem("Goal 14", R.drawable.sdg_14),
            SDGItem("Goal 15", R.drawable.sdg_15),
            SDGItem("Goal 16", R.drawable.sdg_16),
            SDGItem("Goal 17", R.drawable.sdg_17)
            // Add more SDGItem objects here...
        )

        // Initialize the adapter with a click listener
        val adapter = SDGAdapter(sdgList) { sdgItem ->
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastToastTime > 2000) { // Show Toast if 2 seconds have passed
                Toast.makeText(context, "Clicked SDG: ${sdgItem.title}", Toast.LENGTH_SHORT).show()
                lastToastTime = currentTime
            }

            // Uncomment the following to use Snackbar instead of Toast:
            // Snackbar.make(view, "Clicked SDG: ${sdgItem.title}", Snackbar.LENGTH_SHORT).show()
        }

        // Use GridLayoutManager with custom span size
//        val layoutManager = object : GridLayoutManager(context, 3) {
//            override fun getSpanSizeLookup(): GridLayoutManager.SpanSizeLookup {
//                return object : GridLayoutManager.SpanSizeLookup() {
//                    override fun getSpanSize(position: Int): Int {
//                        // Customize span size logic
//                        return when (position) {
//                            16 -> 0  // Skip the last item, SDG 16
//                            else -> 1  // Normal items take 1 span
//                        }
//                    }
//                }
//            }
//        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.sdgRecyclerView)

        recyclerView.layoutManager = GridLayoutManager(context, 3) // 3 items per row
        recyclerView.adapter = adapter

        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.item_spacing) // Define this dimension in `res/values/dimens.xml`
//        val includeEdge = true // Include spacing at the edges of the grid
        recyclerView.addItemDecoration(GridSpacingItemDecoration(3, spacingInPixels, true))

        return view

    }
}
