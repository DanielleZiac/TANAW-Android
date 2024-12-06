package com.example.testtanaw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testtanaw.models.Institution

class InstitutionFragment : Fragment() {

    private lateinit var institutionRecyclerView: RecyclerView
    private lateinit var institutionAdapter2: InstitutionAdapter2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_institution, container, false)

        // Initialize RecyclerView
        institutionRecyclerView = rootView.findViewById(R.id.institutionRecyclerView)
        institutionRecyclerView.layoutManager = LinearLayoutManager(context)
        institutionRecyclerView.setHasFixedSize(true)

        // Example data
        val institutionList = listOf(
            Institution("BSU", "Batangas State University", R.drawable.bsulogo),
            Institution("ADMU", "Ateneo de Manila University", R.drawable.admulogo),
            Institution("DLSU", "De La Salle University", R.drawable.dlsulogo)
        )

        // Set the Adapter
        institutionAdapter2 = InstitutionAdapter2(institutionList)
        institutionRecyclerView.adapter = institutionAdapter2

        return rootView
    }
}