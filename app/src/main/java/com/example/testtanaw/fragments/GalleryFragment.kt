package com.example.testtanaw.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.testtanaw.util.GalleryAdapter;
import com.example.testtanaw.util.SDGAdapter2;
import com.example.testtanaw.databinding.FragmentGalleryBinding;
import com.example.testtanaw.util.CRUD;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private SDGAdapter2 sdgAdapter2;
    private GalleryAdapter galleryAdapter;
    private boolean isUploadsTab = true; // Tracks the active tab (uploads or events)
    private String userId;

    // This is where the 'userId' is passed to the fragment.
    public static GalleryFragment newInstance(String userId) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle args = new Bundle();
        args.putString("userId", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                            @Nullable Bundle savedInstanceState) {
        binding = FragmentGalleryBinding.inflate(inflater, container, false);

        // Retrieve the userId from arguments
        if (getArguments() != null) {
            userId = getArguments().getString("userId");
        }

        // SDG Data
        List<String> sdgImages = IntStream.rangeClosed(1, 17)
                .mapToObj(i -> "sdg_" + i)
                .collect(Collectors.toList());

        // Horizontal RecyclerView for SDGs
        sdgAdapter2 = new SDGAdapter2(sdgImages);
        binding.sdgRecyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.sdgRecyclerView.setAdapter(sdgAdapter2);

        // Grid RecyclerView for Gallery
        setupGalleryRecyclerView();

        // Tab Switching
        binding.uploadsTab.setOnClickListener(v -> {
            isUploadsTab = true;
            setupGalleryRecyclerView();
        });
        
        binding.eventsTab.setOnClickListener(v -> {
            isUploadsTab = false;
            setupGalleryRecyclerView();
        });

        return binding.getRoot();
    }

    private void setupGalleryRecyclerView() {
        List<String> images = IntStream.rangeClosed(isUploadsTab ? 1 : 7, isUploadsTab ? 6 : 12)
                .mapToObj(i -> "sdglink_" + i)
                .collect(Collectors.toList());

        CRUD crud = new CRUD();
        crud.getPhotoByUserId(userId).thenAccept(photoDetails -> {
            if (photoDetails != null) {
                List<String> photoList = new ArrayList<>();
                photoDetails.forEach(res -> {
                    Log.d("xxxxxx", res.getUrl());
                    photoList.add(res.getUrl());
                });

                requireActivity().runOnUiThread(() -> {
                    galleryAdapter = new GalleryAdapter(photoList);
                    binding.galleryRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3));
                    binding.galleryRecyclerView.setAdapter(galleryAdapter);

                    // Update tab styles
                    binding.uploadsTab.setAlpha(isUploadsTab ? 1.0f : 0.5f);
                    binding.eventsTab.setAlpha(isUploadsTab ? 0.5f : 1.0f);
                });
            }
        });
    }
}
