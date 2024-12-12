package com.example.testtanaw.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testtanaw.R;
import com.example.testtanaw.util.GalleryAdapter;
import com.example.testtanaw.util.SDGAdapter2;
import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    private RecyclerView sdgRecyclerView;
    private RecyclerView galleryRecyclerView;
    private TextView uploadsTab;
    private TextView eventsTab;
    private SDGAdapter2 sdgAdapter2;
    private GalleryAdapter galleryAdapter;
    private boolean isUploadsTab = true; // Tracks the active tab (uploads or events)

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        // Initialize views
        sdgRecyclerView = view.findViewById(R.id.sdgRecyclerView);
        galleryRecyclerView = view.findViewById(R.id.galleryRecyclerView);
        uploadsTab = view.findViewById(R.id.uploadsTab);
        eventsTab = view.findViewById(R.id.eventsTab);

        // SDG Data
        List<String> sdgImages = new ArrayList<>();
        for (int i = 1; i <= 17; i++) {
            sdgImages.add("sdg_" + i); // Image names for SDGs
        }

        // Horizontal RecyclerView for SDGs
        sdgAdapter2 = new SDGAdapter2(sdgImages);
        sdgRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        sdgRecyclerView.setAdapter(sdgAdapter2);

        // Setup gallery RecyclerView
        setupGalleryRecyclerView();

        // Tab switching
        uploadsTab.setOnClickListener(v -> {
            isUploadsTab = true;
            setupGalleryRecyclerView();
        });

        eventsTab.setOnClickListener(v -> {
            isUploadsTab = false;
            setupGalleryRecyclerView();
        });

        return view;
    }

    private void setupGalleryRecyclerView() {
        // Sample images for uploads and events tabs
        List<String> images = new ArrayList<>();
        if (isUploadsTab) {
            for (int i = 1; i <= 6; i++) {
                images.add("sdglink_" + i);
            }
        } else {
            for (int i = 7; i <= 12; i++) {
                images.add("sdglink_" + i);
            }
        }

        // Set up GalleryAdapter with sample images
        galleryAdapter = new GalleryAdapter(images);
        galleryRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        galleryRecyclerView.setAdapter(galleryAdapter);

        // Update tab styles
        uploadsTab.setAlpha(isUploadsTab ? 1.0f : 0.5f);
        eventsTab.setAlpha(isUploadsTab ? 0.5f : 1.0f);
    }
}
