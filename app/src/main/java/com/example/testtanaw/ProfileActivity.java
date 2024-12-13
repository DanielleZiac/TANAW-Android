package com.example.testtanaw;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.Button;

import androidx.appcompat.widget.Toolbar;

import com.example.testtanaw.models.Institution;
import com.google.android.material.imageview.ShapeableImageView;

import com.example.testtanaw.models.Avatar;
import com.example.testtanaw.models.Constants;
import com.example.testtanaw.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ProfileActivity extends BaseActivity {
    private static final String TAG = "ProfileActivity";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private ExecutorService executorService;
    private TextView nameTextView;

    private EditText editTextFirstName;
    private EditText editTextLastName;
    private Button updateFirstAndLastNameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Firebase Auth instance
        mAuth = FirebaseAuth.getInstance();

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance(Constants.BUCKET);

        executorService = Executors.newSingleThreadExecutor();

        Intent intent = getIntent();
        String userUid = intent.getStringExtra("USER_UID");
        Log.d(TAG, "USER_UID: " + userUid);

        ShapeableImageView avatarImageView = findViewById(R.id.roundedImageView);
        TextView srCodeTextView = findViewById(R.id.srCode);
        nameTextView = findViewById(R.id.name);
        TextView institutionTextView = findViewById(R.id.institution);
        TextView collegeTextView = findViewById(R.id.college);
        TextView currentEmailTextView = findViewById(R.id.email);

        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        updateFirstAndLastNameButton = findViewById(R.id.updateFirstAndLastNameButton);

        if (userUid != null) {
            StorageReference storageRef = storage.getReference();
            String path = Avatar.getUserAvatarPath(userUid);
            StorageReference avatarImageRef = storageRef.child(path);

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
                                Toast.makeText(ProfileActivity.this, "Failed to load avatar: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                            });

                            srCodeTextView.setText(userData.getSrCode());
                            nameTextView.setText(String.format("%s %s", userData.getFirstName(), userData.getLastName()));

                            institutionTextView.setText(institution.getInstitution());
                            collegeTextView.setText(String.format("%s, %s", avatar.getBg().toUpperCase(), institution.getCampus()));
                            currentEmailTextView.setText(userData.getEmail());

                            editTextFirstName.setText(userData.getFirstName());
                            editTextLastName.setText(userData.getLastName());
                        }

                        // Set up the OnClickListener for the "Edit Avatar" button
                        Button editAvatarButton = findViewById(R.id.editAvatar);
                        editAvatarButton.setOnClickListener(v -> {
                            Intent intent = new Intent(ProfileActivity.this, AvatarActivity.class);
                            startActivity(intent);
                        });

                        // Set an onClick listener for the button
                        updateFirstAndLastNameButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                saveFirstAndLastName(userUid);
                            }
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

    public void saveFirstAndLastName(String userId) {
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a map of fields to update
        Map<String, Object> updates = new HashMap<>();
        updates.put("firstName", firstName);
        updates.put("lastName", lastName);

        // Update the "users" collection in Firestore
        db.collection(Constants.DB_USERS)
                .document(userId)
                .update(updates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ProfileActivity.this, "Names updated successfully", Toast.LENGTH_SHORT).show();

                    nameTextView.setText(String.format("%s %s", firstName, lastName));
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ProfileActivity.this, "Error updating names: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
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

    // TODO: For future implementation
    public void onResetPassword(View v) {
        Toast.makeText(ProfileActivity.this, "Reset Password: for presentation only", Toast.LENGTH_SHORT).show();
    }

    public void onSignOut(View v) {
        // Sign out the user
        mAuth.signOut();

        // Show a toast to confirm sign-out
        Toast.makeText(ProfileActivity.this, "Signed out successfully", Toast.LENGTH_SHORT).show();

        // Redirect to LoginActivity
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        // Finish the current activity to prevent going back
        finish();
    }
}

