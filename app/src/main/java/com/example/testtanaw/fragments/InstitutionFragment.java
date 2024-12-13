package com.example.testtanaw.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testtanaw.R;
import com.example.testtanaw.models.Institution;
import com.example.testtanaw.models.InstitutionAdapter;

import java.util.Arrays;
import java.util.List;

public class InstitutionFragment extends Fragment {

    private RecyclerView institutionRecyclerView;
    private InstitutionAdapter institutionAdapter;
    private Button galleryButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_institution, container, false);

        // Initialize RecyclerView
        institutionRecyclerView = rootView.findViewById(R.id.institutionRecyclerView);
        institutionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        institutionRecyclerView.setHasFixedSize(true);

        // Hardcoded list of institutions
        List<Institution> institutionList = Arrays.asList(
                new Institution("1", "BSU", "Batangas State University", "Alangilan", "bsu.edu", R.drawable.bsulogo),
                new Institution("2", "ADMU", "Ateneo de Manila University", "Makati", "admu.edu", R.drawable.admulogo),
                new Institution("3", "DLSU", "De La Salle University", "Dasmariñas", "dlsu.edu", R.drawable.dlsulogo)
        );

        // Set the Adapter with hardcoded data
        institutionAdapter = new InstitutionAdapter(institutionList);
        institutionRecyclerView.setAdapter(institutionAdapter);


        galleryButton = rootView.findViewById(R.id.galleryButton);

        galleryButton.setOnClickListener(v -> {
            // Begin the fragment transaction
            GalleryFragment galleryFragment = new GalleryFragment("asdasdsa");
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, galleryFragment) // Replace with the container ID
                    .addToBackStack(null)  // Optional: Adds this transaction to the back stack
                    .commit();
        });
        return rootView;
    }
}
