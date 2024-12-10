package com.example.testtanaw;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.Button;

import androidx.appcompat.widget.Toolbar;

import com.example.testtanaw.models.Institution;
import com.google.android.material.imageview.ShapeableImageView;

import com.example.testtanaw.models.Avatar;
import com.example.testtanaw.models.Constants;
import com.example.testtanaw.models.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ProfileActivity extends BaseActivity {
    private static final String TAG = "ProfileActivity";
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance(Constants.BUCKET);
        executorService = Executors.newSingleThreadExecutor();

        Intent intent = getIntent();
        String userUid = intent.getStringExtra("USER_UID");
        Log.d(TAG, "USER_UID: " + userUid);

        if (userUid != null) {
            getUserAndInstitution(userUid, new FirestoreCallback() {
                @Override
                public void onSuccess(User userData, Institution institution, Avatar avatar) {
                    runOnUiThread(() -> {
                        if (userData != null) {
                            Toolbar toolbar = findViewById(R.id.toolbar);
                            setSupportActionBar(toolbar);

                            // Set the toolbar title to be centered
                            if (getSupportActionBar() != null) {
                                getSupportActionBar().setDisplayShowTitleEnabled(false); // Disable default title
                            }

                            // Enable the back button in the toolbar
                            if (getSupportActionBar() != null) {
                                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                            }

                            ShapeableImageView avatarImageView = findViewById(R.id.roundedImageView);
                            TextView srCodeTextView = findViewById(R.id.srCode);
                            TextView nameTextView = findViewById(R.id.name);
                            TextView institutionTextView = findViewById(R.id.institution);
                            TextView collegeTextView = findViewById(R.id.college);
                            TextView currentEmailTextView = findViewById(R.id.currentEmail);

                            StorageReference storageRef = storage.getReference();
                            String path = Avatar.getUserAvatarPath(userUid);
                            StorageReference avatarImageRef = storageRef.child(path);

                            // Get the download URL
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
                                Toast.makeText(ProfileActivity.this, "Failed to load avatar: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                            });

                            srCodeTextView.setText(userData.getSrCode());
                            nameTextView.setText(String.format("%s %s", userData.getFirstName(), userData.getLastName()));

                            institutionTextView.setText(institution.getInstitution());
                            collegeTextView.setText(String.format("%s, %s", avatar.getBg().toUpperCase(), institution.getCampus()));
                            currentEmailTextView.setText(userData.getEmail());
                        }

                        // Set up the OnClickListener for the "Edit Avatar" button
                        Button editAvatarButton = findViewById(R.id.editAvatar);
                        editAvatarButton.setOnClickListener(v -> {
                            Intent intent = new Intent(ProfileActivity.this, AvatarActivity.class);
                            startActivity(intent);
                        });
                    });
                }

                public void onError(Exception exception) {
                    Log.e(TAG, "Failed to fetch user and institution data: " + exception.getMessage());
                }
            });
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void getUserAndInstitution(String userId, ProfileActivity.FirestoreCallback callback) {
        // Fetch user by userId
        db.collection(Constants.DB_USERS)
                .document(userId)
                .get()
                .addOnSuccessListener(executorService, userSnapshot -> {
                    if (userSnapshot.exists()) {
                        // Extract user data
                        User user = userSnapshot.toObject(User.class);

                        if (user != null && user.getInstitutionId() != null) {
                            // Fetch institution using user.institutionId
                            db.collection(Constants.DB_INSTITUTIONS)
                                    .document(user.getInstitutionId())
                                    .get()
                                    .addOnSuccessListener(executorService, institutionSnapshot -> {
                                        if (institutionSnapshot.exists()) {
                                            Institution institution = institutionSnapshot.toObject(Institution.class);

                                            if (institution != null) {
                                                // Fetch avatar using userId
                                                db.collection(Constants.DB_AVATARS)
                                                        .document(userId)
                                                        .get()
                                                        .addOnSuccessListener(executorService, avatarSnapshot -> {
                                                            if (avatarSnapshot.exists()) {
                                                                Avatar avatar = avatarSnapshot.toObject(Avatar.class);
                                                                callback.onSuccess(user, institution, avatar);
                                                            } else {
                                                                callback.onError(new Exception("Avatar not found"));
                                                            }
                                                        })
                                                        .addOnFailureListener(callback::onError);
                                            } else {
                                                callback.onError(new Exception("Avatar ID not found in user data"));
                                            }
                                        } else {
                                            callback.onError(new Exception("Institution not found."));
                                        }
                                    })
                                    .addOnFailureListener(callback::onError);
                        } else {
                            callback.onError(new Exception("Institution ID not found in user data."));
                        }
                    } else {
                        callback.onError(new Exception("User not found."));
                    }
                })
                .addOnFailureListener(callback::onError);
    }

    // Callback interface for single document on multiple collections
    public interface FirestoreCallback {
        void onSuccess(User user, Institution institution, Avatar avatar);

        void onError(Exception e);
    }

    // Handle back button click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

