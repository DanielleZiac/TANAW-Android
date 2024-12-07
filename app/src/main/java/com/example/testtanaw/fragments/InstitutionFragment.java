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
import com.example.testtanaw.fragments.GalleryFragment;
import com.example.testtanaw.models.Institution;
import com.example.testtanaw.models.InstitutionAdapter2;
import java.util.Arrays;
import java.util.List;

public class InstitutionFragment extends Fragment {

    private RecyclerView institutionRecyclerView;
    private InstitutionAdapter2 institutionAdapter2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                            @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_institution, container, false);

        // Initialize RecyclerView for Institutions
        institutionRecyclerView = rootView.findViewById(R.id.institutionRecyclerView);
        institutionRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        institutionRecyclerView.setHasFixedSize(true);

        // Example data for Institutions
        List<Institution> institutionList = Arrays.asList(
            new Institution("Batangas State University", "Alangilan", R.drawable.bsulogo),
            new Institution("Ateneo de Manila University", "Manila", R.drawable.admulogo),
            new Institution("De La Salle University", "Dasmariñas", R.drawable.dlsulogo)
        );

        // Set the Adapter for Institutions
        institutionAdapter2 = new InstitutionAdapter2(institutionList);
        institutionRecyclerView.setAdapter(institutionAdapter2);

        // Set up Button Click Listener
        Button galleryButton = rootView.findViewById(R.id.galleryButton);
        galleryButton.setOnClickListener(v -> {
            // Example userId; replace this with actual value
            String userId = "12345";

            GalleryFragment galleryFragment = GalleryFragment.newInstance(userId);

            requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, galleryFragment)
                .addToBackStack(null) // Optional: Add to back stack to allow back navigation
                .commit();
        });

        // Return the inflated view
        return rootView;
    }
}
