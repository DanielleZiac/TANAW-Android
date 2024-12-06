package com.example.testtanaw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testtanaw.fragments.GalleryFragment
import com.example.testtanaw.models.Institution
import com.example.testtanaw.models.InstitutionAdapter2

class InstitutionFragment : Fragment() {

    private lateinit var institutionRecyclerView: RecyclerView
    private lateinit var institutionAdapter2: InstitutionAdapter2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_institution, container, false)

        // Initialize RecyclerView for Institutions
        institutionRecyclerView = rootView.findViewById(R.id.institutionRecyclerView)
        institutionRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        institutionRecyclerView.setHasFixedSize(true)

        // Example data for Institutions
        val institutionList = listOf(
            Institution("BSU", "Batangas State University", R.drawable.bsulogo),
            Institution("ADMU", "Ateneo de Manila University", R.drawable.admulogo),
            Institution("DLSU", "De La Salle University", R.drawable.dlsulogo)
        )

        // Set the Adapter for Institutions
        institutionAdapter2 = InstitutionAdapter2(institutionList)
        institutionRecyclerView.adapter = institutionAdapter2

        // Set up Button Click Listener
        val galleryButton: Button = rootView.findViewById(R.id.galleryButton)
        galleryButton.setOnClickListener {
            // Example userId; replace this with actual value
            val userId = "12345"

            val galleryFragment = GalleryFragment.newInstance(userId)

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, galleryFragment)
                .addToBackStack(null) // Optional: Add to back stack to allow back navigation
                .commit()
        }



        // Return the inflated view
        return rootView
    }
}