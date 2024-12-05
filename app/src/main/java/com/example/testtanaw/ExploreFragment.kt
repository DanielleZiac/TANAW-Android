package com.example.testtanaw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater

class ExploreFragment : Fragment() {

    private lateinit var unImagesAdapter: UNImagesAdapter
    private lateinit var sdgAdapter: ExploreSDGAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Set up the shared element transition
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView for United Nations images
        val recyclerView: RecyclerView = view.findViewById(R.id.un_images_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        unImagesAdapter = UNImagesAdapter(this)  // Pass the fragment to the adapter
        recyclerView.adapter = unImagesAdapter

        // Set up SDG RecyclerView
        val sdgRecyclerView: RecyclerView = view.findViewById(R.id.sdg_recycler_view)
        sdgRecyclerView.layoutManager = LinearLayoutManager(context)

        val sdgGoals = listOf(
            SDG("Goal 1: No Poverty", "End poverty in all its forms everywhere.", R.drawable.sdglink_1),
            SDG("Goal 2: Zero Hunger", "End hunger and achieve food security.", R.drawable.sdglink_2),
            SDG("Goal 3: Good Health and Well-Being", "Ensure healthy lives for all.", R.drawable.sdglink_3),
            SDG("Goal 4: Quality Education", "Ensure inclusive and equitable quality education.", R.drawable.sdglink_4),
            SDG("Goal 5: Gender Equality", "Achieve gender equality and empower all women and girls.", R.drawable.sdglink_5),
            SDG("Goal 6: Clean Water and Sanitation", "Ensure availability and sustainable management of water.", R.drawable.sdglink_6),
            SDG("Goal 7: Affordable and Clean Energy", "Ensure access to affordable and clean energy.", R.drawable.sdglink_7),
            SDG("Goal 8: Decent Work and Economic Growth", "Promote inclusive and sustainable economic growth.", R.drawable.sdglink_8),
            SDG("Goal 9: Industry, Innovation, and Infrastructure", "Build resilient infrastructure.", R.drawable.sdglink_9),
            SDG("Goal 10: Reduced Inequalities", "Reduce inequalities within and among countries.", R.drawable.sdglink_10),
            SDG("Goal 11: Sustainable Cities and Communities", "Make cities and human settlements inclusive.", R.drawable.sdglink_11),
            SDG("Goal 12: Responsible Consumption and Production", "Ensure sustainable consumption and production patterns.", R.drawable.sdglink_12),
            SDG("Goal 13: Climate Action", "Take urgent action to combat climate change.", R.drawable.sdglink_13),
            SDG("Goal 14: Life Below Water", "Conserve and sustainably use the oceans.", R.drawable.sdglink_14),
            SDG("Goal 15: Life on Land", "Protect, restore, and promote sustainable use of ecosystems.", R.drawable.sdglink_15),
            SDG("Goal 16: Peace, Justice, and Strong Institutions", "Promote peaceful societies for sustainable development.", R.drawable.sdglink_16),
            SDG("Goal 17: Partnerships for the Goals", "Strengthen the means of implementation and revitalize the global partnership for sustainable development.", R.drawable.sdglink_17)
        )

        sdgAdapter = ExploreSDGAdapter(sdgGoals)
        sdgRecyclerView.adapter = sdgAdapter
    }

    // Function to show the dialog when an image is clicked
    fun showImageDialog(imageResource: Int, articleText: String) {
        val dialogFragment = ImageArticleDialogFragment.newInstance(imageResource, articleText)
        dialogFragment.show(childFragmentManager, "ImageArticleDialog")
    }
}
