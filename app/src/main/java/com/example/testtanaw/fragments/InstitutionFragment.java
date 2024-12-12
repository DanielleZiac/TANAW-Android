package com.example.testtanaw.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testtanaw.R;
import com.example.testtanaw.models.Institution;
import com.example.testtanaw.models.InstitutionAdapter;  // Make sure the adapter class is imported here

import java.util.Arrays;
import java.util.List;

public class InstitutionFragment extends Fragment {

    private RecyclerView institutionRecyclerView;
    private InstitutionAdapter institutionAdapter;
    private Button galleryButton;  // Declare the gallery button

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_institution, container, false);

        // Initialize RecyclerView
        institutionRecyclerView = rootView.findViewById(R.id.institutionRecyclerView);
        institutionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        institutionRecyclerView.setHasFixedSize(true);

        // Example data: Initialize the list of institutions with the correct data
        List<Institution> institutionList = Arrays.asList(
                new Institution("1", "BSU", "Batangas State University", "Main Campus", "bsu.edu", R.drawable.bsulogo),
                new Institution("2", "ADMU", "Ateneo de Manila University", "Quezon City", "admu.edu", R.drawable.admulogo),
                new Institution("3", "DLSU", "De La Salle University", "Makati", "dlsu.edu", R.drawable.dlsulogo)
        );

        // Set the Adapter with the institution list
        institutionAdapter = new InstitutionAdapter(institutionList);
        institutionRecyclerView.setAdapter(institutionAdapter);

        // Initialize the button
        galleryButton = rootView.findViewById(R.id.galleryButton);

        // Set the OnClickListener to navigate to the GalleryFragment
        galleryButton.setOnClickListener(v -> {
            // Begin the fragment transaction
            GalleryFragment galleryFragment = new GalleryFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, galleryFragment) // Replace with the container ID
                    .addToBackStack(null)  // Optional: Adds this transaction to the back stack
                    .commit();
        });

        return rootView;
    }
}
