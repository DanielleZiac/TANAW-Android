package com.example.testtanaw;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.Button;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.imageview.ShapeableImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.testtanaw.models.Avatar;
import com.example.testtanaw.models.Constants;
import com.example.testtanaw.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;


public class ProfileActivity extends BaseActivity {
    private static final String TAG = "ProfileActivity";
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            WindowInsetsCompat.Type.systemBars();
            v.setPadding(
                    insets.getSystemWindowInsetLeft(),
                    insets.getSystemWindowInsetTop(),
                    insets.getSystemWindowInsetRight(),
                    insets.getSystemWindowInsetBottom()
            );
            return insets;
        });

        Intent intent = getIntent();
        String userUid = intent.getStringExtra("USER_UID");
        Log.d(TAG, "USER_UID: " + userUid);

        if (userUid != null) {
            fetchSingleUser(userUid, new FirestoreCallback() {
                @Override
                public void onCallback(User userData) {
//                    if (userData != null) {
//                        Toolbar toolbar = findViewById(R.id.toolbar);
//                        setSupportActionBar(toolbar);
//
//                        // Set the toolbar title to be centered
//                        if (getSupportActionBar() != null) {
//                            getSupportActionBar().setDisplayShowTitleEnabled(false); // Disable default title
//                        }
//                        TextView titleTextView = toolbar.findViewById(R.id.toolbar_title);
//
//                        // Enable the back button in the toolbar
//                        if (getSupportActionBar() != null) {
//                            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//                        }
//
//                        ShapeableImageView avatarImageView = findViewById(R.id.roundedImageView);
//                        TextView srCodeTextView = findViewById(R.id.srCode);
//                        TextView nameTextView = findViewById(R.id.name);
//                        TextView institutionTextView = findViewById(R.id.institution);
//                        TextView collegeTextView = findViewById(R.id.college);
//                        TextView currentEmailTextView = findViewById(R.id.currentEmail);
//
//                        // TODO: load via Firebase
//                        // userData.getUserAvatarPath()
//
//                        Picasso.get()
//                                .load(User.getUserAvatarPath(userData.getId()))
//                                .fit()
//                                .centerCrop()
//                                .placeholder(R.drawable.loading)
//                                .error(R.drawable.baseline_error_outline_24)
//                                .into(avatarImageView);
//
//                        srCodeTextView.setText(userData.getSrCode());
//                        nameTextView.setText(userData.getFirstName() + " " + userData.getLastName());
//                        institutionTextView.setText(userData.getInstitution());
//                        collegeTextView.setText(userData.getDepartment());
//                        currentEmailTextView.setText(userData.getEmail());
//                    }

//                    // Set up the OnClickListener for the "Edit Avatar" button
//                    Button editAvatarButton = findViewById(R.id.editAvatar);
//                    editAvatarButton.setOnClickListener(v -> {
//                        Intent intent = new Intent(this, AvatarActivity.class);
////                        intent.putExtra("userData", userData);
//                        startActivity(intent);
//                    });
                }
            });
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchSingleUser(String userId, ProfileActivity.FirestoreCallback callback) {
        Log.d(TAG, "fetchSingleUser: " + userId);
        db.collection(Constants.DB_USERS).whereEqualTo("id", userId).limit(1) // Limit the query to 1 document
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        // Get the first (and only) matching document
                        QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                        User user = document.toObject(User.class);
                        callback.onCallback(user); // Pass the document data to the callback
                    } else {
                        if (task.getResult().isEmpty()) {
                            Log.d(TAG, "No document found for user_id: " + userId);
                        } else {
                            Log.e(TAG, "Failed to fetch user data: " + task.getException().getMessage());
                        }
                        callback.onCallback(null); // Return null if no document is found or on failure
                    }
                });
    }

    // Callback interface for a single document
    public interface FirestoreCallback {
        void onCallback(User user);
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

