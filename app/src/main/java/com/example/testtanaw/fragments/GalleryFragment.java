package com.example.testtanaw.fragments;

import static com.example.testtanaw.models.Constants.DB_USERS_SDG_PHOTOS;

import android.os.Bundle;
import android.util.Log;
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
import com.example.testtanaw.models.Constants;
import com.example.testtanaw.util.GalleryAdapter;
import com.example.testtanaw.util.SDGAdapter2;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {
    private static final String TAG = "GalleryFragment";

    private RecyclerView sdgRecyclerView;
    private RecyclerView galleryRecyclerView;
    private TextView uploadsTab;
    private TextView eventsTab;
    private SDGAdapter2 sdgAdapter2;
    private GalleryAdapter galleryAdapter;
    private boolean isUploadsTab = true; // Tracks the active tab (uploads or events)

    private final String userId;
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    public GalleryFragment(String userId) {
        this.userId = userId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Initialize Firebase instances
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance(Constants.BUCKET);

        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        // Initialize views
        sdgRecyclerView = view.findViewById(R.id.sdgRecyclerView);
        galleryRecyclerView = view.findViewById(R.id.galleryRecyclerView);
        uploadsTab = view.findViewById(R.id.uploadsTab);
        eventsTab = view.findViewById(R.id.eventsTab);

        // SDG Data (Example)
        List<String> sdgImages = new ArrayList<>();
        for (int i = 1; i <= 17; i++) {
            sdgImages.add("sdg_" + i); // Image names for SDGs
        }

        // Generate SDG list and update UI
        getUserSdgsById((sdgPhotoUrls) -> {
            // Once SDG photo URLs are fetched, update the Gallery RecyclerView
            setupGalleryRecyclerView(sdgPhotoUrls);

            // Tab switching
            uploadsTab.setOnClickListener(v -> {
                isUploadsTab = true;
                setupGalleryRecyclerView(sdgPhotoUrls);  // Refresh with empty for now
            });

            eventsTab.setOnClickListener(v -> {
                isUploadsTab = false;
                setupGalleryRecyclerView(sdgPhotoUrls);  // Refresh with empty for now
            });
        });

        // Horizontal RecyclerView for SDGs
        sdgAdapter2 = new SDGAdapter2(sdgImages);
        sdgRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        sdgRecyclerView.setAdapter(sdgAdapter2);



        return view;
    }

    private void setupGalleryRecyclerView(List<String> sdgPhotoUrls) {
        // Set up GalleryAdapter with the fetched SDG photo URLs
        galleryAdapter = new GalleryAdapter(sdgPhotoUrls);
        galleryRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        galleryRecyclerView.setAdapter(galleryAdapter);

        // Update tab styles
        uploadsTab.setAlpha(isUploadsTab ? 1.0f : 0.5f);
        eventsTab.setAlpha(isUploadsTab ? 0.5f : 1.0f);
    }

    private void getUserSdgsById(SdgsUrlListCallback callback) {
        List<String> sdgPhotoUrls = new ArrayList<>();
        final int[] totalDocuments = {0}; // Track number of documents
        final int[] processedDocuments = {0}; // Track processed documents

        db.collection(DB_USERS_SDG_PHOTOS)
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        totalDocuments[0] = task.getResult().size();  // Total documents to process

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String sdgPhotoUrl = document.getString("sdgPhotoUrl");
                            try {
                                if (sdgPhotoUrl != null) {
                                    StorageReference storageRef = storage.getReference();
                                    StorageReference userSdgPhotoRef = storageRef.child(sdgPhotoUrl);

                                    userSdgPhotoRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                        Log.d(TAG, uri.toString());
                                        sdgPhotoUrls.add(uri.toString());

                                        // Increment processedDocuments counter after adding a URL
                                        if (++processedDocuments[0] == totalDocuments[0]) {
                                            // All documents processed, return the list via callback
                                            callback.onResult(sdgPhotoUrls);
                                        }
                                    }).addOnFailureListener(e -> {
                                        // Handle failure to fetch download URL
                                        Log.e(TAG, "Error fetching download URL: ", e);

                                        // Increment processedDocuments counter even on failure
                                        if (++processedDocuments[0] == totalDocuments[0]) {
                                            callback.onResult(sdgPhotoUrls);  // Return the result
                                        }
                                    });
                                } else {
                                    // Increment counter if there's no valid URL
                                    if (++processedDocuments[0] == totalDocuments[0]) {
                                        callback.onResult(sdgPhotoUrls);  // Return the result
                                    }
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Error: " + sdgPhotoUrl, e);

                                // Increment processedDocuments counter even on exception
                                if (++processedDocuments[0] == totalDocuments[0]) {
                                    callback.onResult(sdgPhotoUrls);  // Return the result
                                }
                            }
                        }
                    } else {
                        Log.e(TAG, "Error fetching data: ", task.getException());
                        callback.onResult(new ArrayList<>());  // Return an empty list on failure
                    }
                });
    }

    public interface SdgsUrlListCallback {
        void onResult(List<String> sdgPhotoUrls);
    }
}
