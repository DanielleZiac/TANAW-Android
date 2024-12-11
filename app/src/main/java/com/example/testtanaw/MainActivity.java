package com.example.testtanaw;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.testtanaw.fragments.FeedbackModalFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private FirebaseAuth mAuth;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

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

        // Firebase Auth instance
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser authUser = mAuth.getCurrentUser();

        if (authUser != null) {
            Log.d(TAG, "User logged in -- email: " + authUser.getEmail() + " | id: " + authUser.getUid());
        } else {
            Log.d(TAG, "No user logged in");
        }

        // Initialize toolbar and navigation drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Disable toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);

        // Hamburger menu click listener to open the navigation drawer
        ImageButton hamburgerMenu = findViewById(R.id.hamburgerMenu);
        hamburgerMenu.setOnClickListener(v -> drawerLayout.openDrawer(navigationView));

        // Set up navigation menu item clicks
        setupNavigationMenu(authUser);

        // Set up profile icon click
        ImageButton profileIcon = findViewById(R.id.profileIcon);
        profileIcon.setOnClickListener(v -> {
            if (authUser != null) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra("USER_UID", authUser.getUid());
                startActivity(intent);
            } else {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    private void setupNavigationMenu(FirebaseUser authUser) {
        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_settings) {
                Intent settingsIntent = new Intent(MainActivity.this, ProfileActivity.class);
                if (authUser != null) {
                    settingsIntent.putExtra("USER_UID", authUser.getUid());
                }
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
                Log.d(TAG, "Help menu clicked");
            } else if (itemId == R.id.nav_terms) {
                Intent termsIntent = new Intent(MainActivity.this, TermsActivity.class);
                startActivity(termsIntent);
            } else if (itemId == R.id.nav_logout) {
                Log.d(TAG, "Logout menu clicked");
            } else {
                return false;
            }

            drawerLayout.closeDrawer(navigationView); // Close the drawer after selection
            return true;
        });
    }

}
