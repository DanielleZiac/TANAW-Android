package com.example.testtanaw;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.testtanaw.fragments.FeedbackModalFragment;
import com.example.testtanaw.fragments.ExploreFragment;
import com.example.testtanaw.fragments.GalleryFragment;
import com.example.testtanaw.fragments.HomeFragment;
import com.example.testtanaw.fragments.InboxFragment;
import com.example.testtanaw.fragments.StickersFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.example.testtanaw.models.Avatar;
import com.example.testtanaw.models.Constants;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private FirebaseAuth mAuth;
    private FirebaseStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Apply edge-to-edge insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize toolbar and navigation drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Disable toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);

        // Access the header view
        View headerView = navigationView.getHeaderView(0);

        // Access the views inside the nav_header layout
        TextView userTextView = headerView.findViewById(R.id.username);
        ShapeableImageView avatarImageView = headerView.findViewById(R.id.avatar);

        // Hamburger menu click listener to open the navigation drawer
        ImageButton hamburgerMenu = findViewById(R.id.hamburgerMenu);
        hamburgerMenu.setOnClickListener(v -> drawerLayout.openDrawer(navigationView));

        // Set up navigation menu item clicks
        setupNavigationMenu();

        // Initialize BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomAppBar);

        // Set ExploreFragment as the default fragment
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());  // Load ExploreFragment initially
            bottomNavigationView.setSelectedItemId(R.id.nav_home); // Set nav_explore as selected
        }


        // Firebase Auth and Storage instance
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance(Constants.BUCKET);
        FirebaseUser authUser = mAuth.getCurrentUser();

        ImageButton profileIcon = findViewById(R.id.profileIcon);
        ShapeableImageView roundedImageView = findViewById(R.id.roundedImageView);


        // Set up listener for bottom navigation item selection
        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            if (menuItem.getItemId() == R.id.nav_explore) {
                loadFragment(new ExploreFragment());
                return true;  // Return true to indicate selection is handled
            } else if (menuItem.getItemId() == R.id.nav_home) {  // Add Home navigation
                loadFragment(new HomeFragment());
                return true;
            }   else if (menuItem.getItemId() == R.id.nav_inbox) {  // Add Home navigation
                loadFragment(new InboxFragment());
                return true;
            }   else if (menuItem.getItemId() == R.id.nav_gallery) {  // Add Home navigation
                loadFragment(new GalleryFragment(authUser.getUid()));
                return true;
            }   else if (menuItem.getItemId() == R.id.nav_stickers) {  // Add Home navigation
                assert authUser != null;
                loadFragment(new StickersFragment(authUser.getUid()));
                return true;
            }
            return false; // Return false if the item is not handled
        });


        if (authUser != null) {
            userTextView.setText(authUser.getDisplayName());

            // Show the rounded image
            roundedImageView.setVisibility(View.VISIBLE);

            // Hide the default icon
            profileIcon.setVisibility(View.GONE);

            StorageReference storageRef = storage.getReference();
            String path = Avatar.getUserAvatarPath(authUser.getUid());
            StorageReference avatarImageRef = storageRef.child(path);

            // Get the avatar image URL
            avatarImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                // Load image using Picasso
                Picasso.get()
                        .load(uri.toString())
                        .fit()
                        .centerCrop()
                        .placeholder(R.drawable.app_logo)
                        .error(R.drawable.baseline_error_outline_24)
                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                        .into(roundedImageView);

                Picasso.get()
                        .load(uri.toString())
                        .fit()
                        .centerCrop()
                        .placeholder(R.drawable.app_logo)
                        .error(R.drawable.baseline_error_outline_24)
                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                        .into(avatarImageView);

                roundedImageView.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    intent.putExtra("USER_UID", authUser.getUid());
                    startActivity(intent);
                });
            }).addOnFailureListener(exception -> {
                // Handle any errors
                Toast.makeText(MainActivity.this, "Failed to load avatar: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            // Show the default icon
            profileIcon.setVisibility(View.VISIBLE);

            // Hide the rounded image
            roundedImageView.setVisibility(View.GONE);

            profileIcon.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            });
        }
    }

    private void setupNavigationMenu() {
        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_settings) {
                Intent settingsIntent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(settingsIntent);
            } else if (itemId == R.id.nav_about) {
                Intent aboutIntent = new Intent(MainActivity.this, AboutUsActivity.class);
                startActivity(aboutIntent);
            } else if (itemId == R.id.nav_contact) {
                Intent contactIntent = new Intent(MainActivity.this, ContactActivity.class);
                startActivity(contactIntent);
            } else if (itemId == R.id.nav_feedback) {
                FeedbackModalFragment feedbackDialog = new FeedbackModalFragment();
                feedbackDialog.show(getSupportFragmentManager(), "FeedbackDialog");
            } else if (itemId == R.id.nav_help) {
                // Handle Help menu
            } else if (itemId == R.id.nav_terms) {
                Intent termsIntent = new Intent(MainActivity.this, TermsActivity.class);
                startActivity(termsIntent);
            } else if (itemId == R.id.nav_logout) {
                // Handle Logout
            } else {
                return false;
            }

            drawerLayout.closeDrawer(navigationView); // Close the drawer after selection
            return true;
        });
    }

    // Method to load fragment
    private void loadFragment(androidx.fragment.app.Fragment fragment) {
        androidx.fragment.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);  // Optional: allows back navigation
        transaction.commit();
    }
}
