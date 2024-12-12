package com.example.testtanaw.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class GalleryFragment extends Fragment {

    private RecyclerView sdgRecyclerView;
    private SDGAdapter2 sdgAdapter2;
    private RecyclerView galleryRecyclerView;
    private GalleryAdapter galleryAdapter;
    private TextView myUploadsTab, communityTab;
    private ImageView loadingImage;
    private List<String> images = new ArrayList<>();
    private FirebaseAuth auth;
    private FirebaseStorage storage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        // Initialize views
        sdgRecyclerView = view.findViewById(R.id.sdgRecyclerView);
        galleryRecyclerView = view.findViewById(R.id.galleryRecyclerView);
        myUploadsTab = view.findViewById(R.id.myUploadsTab);
        communityTab = view.findViewById(R.id.communityTab);
        loadingImage = view.findViewById(R.id.loadingImage); // Initialize progress bar

        // Set up gallery RecyclerView
        galleryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        galleryAdapter = new GalleryAdapter(images);
        galleryRecyclerView.setAdapter(galleryAdapter);

        // SDG Data
        List<String> sdgImages = new ArrayList<>();
        for (int i = 1; i <= 17; i++) {
            sdgImages.add("sdg_" + i); // Image names for SDGs
        }

        // Horizontal RecyclerView for SDGs
        sdgAdapter2 = new SDGAdapter2(sdgImages);
        sdgRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        sdgRecyclerView.setAdapter(sdgAdapter2);

        // Load My Uploads by default
        loadMyUploads();

        // Set default alpha values
        myUploadsTab.setAlpha(1.0f);
        communityTab.setAlpha(0.5f);

        // Set Tab Click Listeners
        myUploadsTab.setOnClickListener(v -> {
            loadMyUploads();
            myUploadsTab.setAlpha(1.0f);
            communityTab.setAlpha(0.5f);
        });

        communityTab.setOnClickListener(v -> {
            loadCommunityUploads();
            myUploadsTab.setAlpha(0.5f); // Set "My Uploads" tab alpha to 0.5
            communityTab.setAlpha(1.0f);    // Set "Others" tab alpha to 1.0
        });
        return view;
    }

    private void loadMyUploads() {
        loadingImage.setVisibility(View.VISIBLE); // Show the loading spinner
        String currentUser = auth.getCurrentUser().getUid();
        StorageReference userPhotosRef = storage.getReference().child("users_sdg_photos").child(currentUser);

        fetchPhotos(userPhotosRef);
    }

    private void loadCommunityUploads() {
        loadingImage.setVisibility(View.VISIBLE); // Show the loading spinner
        StorageReference allPhotosRef = storage.getReference().child("users_sdg_photos");

        fetchPhotos(allPhotosRef);
    }

    private void fetchPhotos(StorageReference reference) {
        images.clear();

        reference.listAll().addOnSuccessListener(listResult -> {
            if (listResult.getItems().isEmpty()) {
                Toast.makeText(getContext(), "No images available", Toast.LENGTH_SHORT).show();
            }
            for (StorageReference fileRef : listResult.getItems()) {
                fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    images.add(uri.toString());
                    galleryAdapter.notifyDataSetChanged();
                }).addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
                });
            }
            loadingImage.setVisibility(View.GONE); // Hide the loading spinner
        }).addOnFailureListener(e -> {
            loadingImage.setVisibility(View.GONE); // Hide the loading spinner
            Toast.makeText(getContext(), "Error fetching images", Toast.LENGTH_SHORT).show();
        });
    }
}
