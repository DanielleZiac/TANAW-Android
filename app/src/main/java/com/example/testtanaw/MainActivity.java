package com.example.testtanaw;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.testtanaw.fragments.ExploreFragment;
import com.example.testtanaw.fragments.FeedbackModalFragment;
import com.example.testtanaw.fragments.GalleryFragment;
import com.example.testtanaw.fragments.HomeFragment;
import com.example.testtanaw.fragments.InboxFragment;
import com.example.testtanaw.fragments.StickersFragment;
import com.example.testtanaw.models.UserParcelable;
import com.example.testtanaw.util.CRUD;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private String userId;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location curLocation;
    private static final int FINE_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserParcelable userData = getIntent().getParcelableExtra("userData");
        
        if (userData != null) {
            userId = userData.getUserId();

            // Initialize location services
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            getLastLocation();

            // Initialize views
            drawerLayout = findViewById(R.id.drawer_layout);
            toolbar = findViewById(R.id.toolbar);
            navigationView = findViewById(R.id.navigationView);

            // Set the toolbar as the action bar
            setSupportActionBar(toolbar);

            // Disable the toolbar title display
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }

            // Handle hamburger icon click
            ImageButton hamburgerMenu = findViewById(R.id.hamburgerMenu);
            hamburgerMenu.setOnClickListener(v -> drawerLayout.openDrawer(navigationView));

            // Handle profile icon click
            ImageButton profileIcon = findViewById(R.id.profileIcon);
            profileIcon.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra("userData", userData);
                startActivity(intent);
            });

            // Handle navigation menu item clicks
            navigationView.setNavigationItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_settings) {
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    intent.putExtra("userData", userData);
                    startActivity(intent);
                } else if (itemId == R.id.nav_about) {
                    startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                } else if (itemId == R.id.nav_contact) {
                    startActivity(new Intent(MainActivity.this, ContactActivity.class));
                } else if (itemId == R.id.nav_feedback) {
                    FeedbackModalFragment feedbackDialog = new FeedbackModalFragment();
                    feedbackDialog.show(getSupportFragmentManager(), "FeedbackDialog");
                } else if (itemId == R.id.nav_terms) {
                    startActivity(new Intent(MainActivity.this, TermsActivity.class));
                }
                drawerLayout.closeDrawer(navigationView);
                return true;
            });

            // Bottom Navigation setup
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottomAppBar);

            // Set HomeFragment as default
            if (savedInstanceState == null) {
                loadFragment(new HomeFragment());
                bottomNavigationView.setSelectedItemId(R.id.nav_home);
            }

            // Bottom navigation listener
            bottomNavigationView.setOnItemSelectedListener(item -> {
                Fragment fragment = null;
                int itemId = item.getItemId();
                
                if (itemId == R.id.nav_home) {
                    fragment = new HomeFragment();
                } else if (itemId == R.id.nav_gallery) {
                    fragment = new GalleryFragment();
                } else if (itemId == R.id.nav_explore) {
                    fragment = new ExploreFragment();
                } else if (itemId == R.id.nav_inbox) {
                    fragment = new InboxFragment();
                } else if (itemId == R.id.nav_stickers) {
                    fragment = new StickersFragment();
                }
                
                if (fragment != null) {
                    loadFragment(fragment);
                }
                return true;
            });
        }
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                curLocation = location;
                CRUD crud = new CRUD();
                crud.saveUserLastLocation(userId, curLocation.getLatitude(), curLocation.getLongitude());
                Log.d("tag", curLocation.getLongitude() + " " + curLocation.getLatitude());
            } else {
                Log.d("tag", "NO LOCATION");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(navigationView);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
