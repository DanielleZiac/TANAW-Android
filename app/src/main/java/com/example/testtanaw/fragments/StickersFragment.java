package com.example.testtanaw.fragments;

import static com.example.testtanaw.models.Constants.DB_AVATARS;
import static com.example.testtanaw.models.Constants.DB_USERS_SDG_PHOTOS;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testtanaw.AvatarActivity;
import com.example.testtanaw.R;
import com.example.testtanaw.models.Constants;
import com.example.testtanaw.models.Sdg;
import com.example.testtanaw.util.StickerAdapter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;


public class StickersFragment extends Fragment {
    private static final String TAG = "StickersFragment";
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1001;

    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private ImageView avatarImageView;

    private final String userId;

    private View view;
    private int sdgSize;

    public StickersFragment(String userId) {
        this.userId = userId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Initialize Firebase instances
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance(Constants.BUCKET);

        view = inflater.inflate(R.layout.fragment_stickers, container, false);

        avatarImageView = view.findViewById(R.id.avatar);

        // Initialize the first RecyclerView (Unlocked)
        AtomicReference<RecyclerView> recyclerView = new AtomicReference<>(view.findViewById(R.id.recyclerView));
        recyclerView.get().setLayoutManager(new LinearLayoutManager(requireContext()));

        // Initialize the second RecyclerView (Locked)
        RecyclerView recyclerView2 = view.findViewById(R.id.recyclerView2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Initialize placeholder list for locked SDGs
        ArrayList<Sdg> placeholderList = new ArrayList<>();

        // Set up the "Edit Avatar" button
        Button editAvatarButton = view.findViewById(R.id.editAvatar);
        editAvatarButton.setOnClickListener(v -> {
            // Navigate to AvatarActivity
            Intent intent = new Intent(requireContext(), AvatarActivity.class);
            startActivity(intent);
        });

        // Generate SDG list and update UI
        generateSdgList((sdgList, lockedSdgList, avatarParts) -> {
            // Update RecyclerView with the SDG list (Unlocked)
            StickerAdapter adapter = new StickerAdapter(sdgList, avatarParts);
            recyclerView.get().setAdapter(adapter);

            // Adjust height of the first RecyclerView
            adjustRecyclerViewHeight(recyclerView.get(), sdgList.size());

            // Update the second RecyclerView with a placeholder list (Locked)
            StickerAdapter adapter2 = new StickerAdapter(lockedSdgList, avatarParts);
            recyclerView2.setAdapter(adapter2);
            adjustRecyclerViewHeight(recyclerView2, lockedSdgList.size());

            // Handle avatar parts
            Log.d(TAG, "Avatar Parts: " + avatarParts);
            sdgSize = sdgList.size();
        });

        Button claim = view.findViewById(R.id.claim);
        claim.setOnClickListener(v -> {
            Log.d(TAG, "Claim button clicked!");
            Toast.makeText(getContext(), "Claim button clicked!", Toast.LENGTH_SHORT).show();

            recyclerView.set(view.findViewById(R.id.recyclerView));
//            int position = 2; // The position of the item you want to access

            Log.d(TAG, String.valueOf(sdgSize));

            for (int position = 0; position < sdgSize; position++) {
                RecyclerView.ViewHolder holder = recyclerView.get().findViewHolderForAdapterPosition(position);

                if (holder != null) {
                    // Access the views inside the ViewHolder
                    ImageView imageView = ((StickerAdapter.SdgViewHolder) holder).imageView;
                    ImageView eyewear = ((StickerAdapter.SdgViewHolder) holder).eyewear;
                    ImageView gender = ((StickerAdapter.SdgViewHolder) holder).gender;
                    ImageView shirtStyle = ((StickerAdapter.SdgViewHolder) holder).shirtStyle;
                    ImageView eye = ((StickerAdapter.SdgViewHolder) holder).eye;
                    ImageView mouth = ((StickerAdapter.SdgViewHolder) holder).mouth;

                    // Combine the images or perform any other logic with the views
                    Bitmap combinedBitmap = combineImages(imageView, eyewear, gender, shirtStyle, eye, mouth);

//                // Do something with the combined image, e.g., save it
                    saveImage(combinedBitmap);
                }
            }
        });

        return view;
    }

    public void saveImage(Bitmap imageBitmap) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "image_" + System.currentTimeMillis() + ".png"); // Unique name
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png"); // MIME type
        values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES); // Save to Pictures folder

        // Get content resolver to insert the image into MediaStore
        ContentResolver contentResolver = getActivity().getContentResolver();
        Uri imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        if (imageUri != null) {
            try (OutputStream outputStream = contentResolver.openOutputStream(imageUri)) {
                if (outputStream != null) {
                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    Toast.makeText(getContext(), "Image saved successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Error opening output stream.", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error saving image.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Error creating image URI.", Toast.LENGTH_SHORT).show();
        }
    }

    public Bitmap combineImages(ImageView imageView, ImageView eyewear, ImageView gender,
                                ImageView shirtStyle, ImageView eye, ImageView mouth) {

        // Extract bitmaps from each ImageView
        Bitmap baseBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        // Create a Bitmap with the custom size (for example, three times the size of the base image)
        Bitmap combinedBitmap = Bitmap.createBitmap(baseBitmap.getWidth() * 3, baseBitmap.getHeight() * 3, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(combinedBitmap);

        // Draw the base image at the top-left corner
        canvas.drawBitmap(baseBitmap, 0, 0, null);

        // Draw the other images on top of the base image at the center position
        if (eyewear.getDrawable() != null) {
            Bitmap eyewearBitmap = ((BitmapDrawable) eyewear.getDrawable()).getBitmap();
            int eyewearCenterX = (baseBitmap.getWidth() - eyewearBitmap.getWidth()) / 2;
            int eyewearCenterY = (baseBitmap.getHeight() - eyewearBitmap.getHeight()) / 2;
            canvas.drawBitmap(eyewearBitmap, eyewearCenterX, eyewearCenterY, null);
        }

        if (gender.getDrawable() != null) {
            Bitmap genderBitmap = ((BitmapDrawable) gender.getDrawable()).getBitmap();
            int genderCenterX = (baseBitmap.getWidth() - genderBitmap.getWidth()) / 2;
            int genderCenterY = (baseBitmap.getHeight() - genderBitmap.getHeight()) / 2;
            canvas.drawBitmap(genderBitmap, genderCenterX, genderCenterY, null);
        }

        if (shirtStyle.getDrawable() != null) {
            Bitmap shirtStyleBitmap = ((BitmapDrawable) shirtStyle.getDrawable()).getBitmap();
            int shirtStyleCenterX = (baseBitmap.getWidth() - shirtStyleBitmap.getWidth()) / 2;
            int shirtStyleCenterY = (baseBitmap.getHeight() - shirtStyleBitmap.getHeight()) / 2;
            canvas.drawBitmap(shirtStyleBitmap, shirtStyleCenterX, shirtStyleCenterY, null);
        }

        if (eye.getDrawable() != null) {
            Bitmap eyeBitmap = ((BitmapDrawable) eye.getDrawable()).getBitmap();
            int eyeCenterX = (baseBitmap.getWidth() - eyeBitmap.getWidth()) / 2;
            int eyeCenterY = (baseBitmap.getHeight() - eyeBitmap.getHeight()) / 2;
            canvas.drawBitmap(eyeBitmap, eyeCenterX, eyeCenterY, null);
        }

        if (mouth.getDrawable() != null) {
            Bitmap mouthBitmap = ((BitmapDrawable) mouth.getDrawable()).getBitmap();
            int mouthCenterX = (baseBitmap.getWidth() - mouthBitmap.getWidth()) / 2;
            int mouthCenterY = (baseBitmap.getHeight() - mouthBitmap.getHeight()) / 2;
            canvas.drawBitmap(mouthBitmap, mouthCenterX, mouthCenterY, null);
        }

        return combinedBitmap;
    }

    public interface SdgsCallback {
        void onResult(Set<Integer> sdgNumbers);
    }

    public interface SdgsListCallback {
        void onResult(List<Sdg> sdgList, List<Sdg> lockedSdgList, List<String> avatarParts);
    }

    private void getUserSdgsById(SdgsCallback callback) {
        Set<Integer> sdgNumbers = new HashSet<>();

        db.collection(DB_USERS_SDG_PHOTOS)
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String sdgNumberString = document.getString("sdgNumber");
                            try {
                                if (sdgNumberString != null) {
                                    int sdgNumber = Integer.parseInt(sdgNumberString);
                                    sdgNumbers.add(sdgNumber);
                                }
                            } catch (NumberFormatException e) {
                                Log.e(TAG, "Invalid SDG number format: " + sdgNumberString, e);
                            }
                        }
                        // Return the results via callback
                        callback.onResult(sdgNumbers);
                    } else {
                        Log.e(TAG, "Error fetching data: ", task.getException());
                        // Return an empty set on failure
                        callback.onResult(new HashSet<>());
                    }
                });
    }

    private void generateSdgList(SdgsListCallback callback) {
        List<String> sdgTitles = Arrays.asList(
                "No Poverty", "Zero Hunger", "Good Health and Well-Being",
                "Quality Education", "Gender Equality", "Clean Water and Sanitation",
                "Affordable and Clean Energy", "Decent Work and Economic Growth",
                "Industry, Innovation and Infrastructure", "Reduced Inequalities",
                "Sustainable Cities and Communities", "Responsible Consumption and Production",
                "Climate Action", "Life Below Water", "Life on Land",
                "Peace, Justice and Strong Institutions", "Partnerships for the Goals"
        );

        getUserSdgsById(sdgNumbers -> {
            getUsersAvatar(avatarParts -> {
                Log.d(TAG, "Fetched SDG Numbers: " + sdgNumbers.size());

                List<Sdg> sdgList = new ArrayList<>();
                List<Sdg> lockedSdgList = new ArrayList<>();

                for (int i = 1; i <= 17; i++) {
                    int imageResId = getResources().getIdentifier("background_sdg" + i, "drawable",
                            requireContext().getPackageName());
                    Sdg sdg = new Sdg(imageResId, "SDG " + i + ": " + sdgTitles.get(i - 1), "This is SDG " + i);

                    if (sdgNumbers.contains(i)) {
                        sdgList.add(sdg);
                    } else {
                        lockedSdgList.add(sdg);
                    }
                }

                // Return both the unlocked and locked SDG lists via callback
                callback.onResult(sdgList, lockedSdgList, avatarParts);
            });
        });
    }

    public interface AvatarPartsCallback {
        void onResult(List<String> avatarParts);
    }

    // Fetch Avatar Parts
    private void getUsersAvatar(AvatarPartsCallback callback) {
//        List<String> avatarParts = new ArrayList<>();

        db.collection(DB_AVATARS)
                .document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    List<String> avatarParts = new ArrayList<>(); // Ensure this is defined

                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot document = task.getResult();

                        // Retrieve fields from the document
                        String bg = document.getString("bg");
                        String eyewear = document.getString("eyewear");
                        String gender = document.getString("gender");
                        String shirtStyle = document.getString("shirtStyle");
                        String avatarUrl = document.getString("avatarUrl");

                        // Fetch the image from Firebase Storage using the avatarUrl
                        if (avatarUrl != null) {
                            StorageReference storageRef = storage.getReference();
                            StorageReference avatarImageRef = storageRef.child(avatarUrl);

                            // Get the avatar image URL
                            avatarImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                // Load image using Picasso
                                Picasso.get()
                                        .load(uri.toString())
                                        .fit()
                                        .centerCrop()
                                        .placeholder(R.drawable.loading)
                                        .error(R.drawable.baseline_error_outline_24)
                                        .into(avatarImageView);
                            }).addOnFailureListener(exception -> {
                                // Handle any errors
                                Log.d(TAG, "Failed to load avatar: " + exception.getMessage());
                            });
                        }


//                        Log.d(TAG, document.getString("avatarUrl"));
//                        Log.d(TAG, eyewear);
//                        Log.d(TAG, gender);
//                        Log.d(TAG, shirtStyle);

                        // Add non-null parts to the list
                        if (bg != null) avatarParts.add(bg);
                        if (eyewear != null) avatarParts.add(eyewear);
                        if (gender != null) avatarParts.add(gender);
                        if (shirtStyle != null) avatarParts.add(shirtStyle);
                    } else {
                        Log.e(TAG, "Error fetching avatar parts: ", task.getException());
                    }

                    // Return the result via the callback
                    callback.onResult(avatarParts);
                });
    }


    private void adjustRecyclerViewHeight(RecyclerView recyclerView, int itemCount) {
        recyclerView.post(() -> {
            int itemHeight = getResources().getDimensionPixelSize(R.dimen.item_height);
            int totalHeight = itemHeight * itemCount;
            ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
            layoutParams.height = totalHeight;
            recyclerView.setLayoutParams(layoutParams);
        });
    }

}