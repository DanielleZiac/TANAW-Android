package com.example.testtanaw;

import static com.example.testtanaw.models.Constants.DB_USERS_SDG_PHOTOS;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.example.testtanaw.data.PhotoChallenges;
import com.example.testtanaw.models.Avatar;
import com.example.testtanaw.models.ClusterMarker;
import com.example.testtanaw.models.Constants;
import com.example.testtanaw.util.ClusterManagerRenderer;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.maps.android.clustering.ClusterManager;
import android.content.SharedPreferences;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class SdgMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "SdgMapActivity";

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private ExecutorService executorService;
    private int sdgNumber;

    private TextView photoChallengeText;

    Location curLocation;

    private GoogleMap mGoogleMap;
    private final int FINE_PERMISSION_CODE = 1;
    FusedLocationProviderClient fusedLocationProviderClient;


    private ClusterManager<ClusterMarker> mClusterManager;
    private ClusterManagerRenderer mClusterManagerRenderer;
    private final ArrayList<ClusterMarker> mClusterMarkers = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdg_map);


        // Initialize Auth, Firestore, and Storage
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance(Constants.BUCKET);
        FirebaseUser authUser = mAuth.getCurrentUser();

        // Retrieve data passed from the adapter
        String sdgTitle = getIntent().getStringExtra("SDG_TITLE");
        sdgNumber = getIntent().getIntExtra("SDG_NUMBER", -1);

        // Setup Toolbar
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
        // Set the SDG title dynamically
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("SDG: " + sdgNumber);

        // set photo chall
        photoChallengeText = findViewById(R.id.photoChallengeText);
        FloatingActionButton fabArrow = findViewById(R.id.fab_arrow);
        FloatingActionButton fabPlus = findViewById(R.id.fab_plus);

        // Display the first challenge for the SDG
        changePhotoChallenge();

        // Handle arrow button click to change challenges
        fabArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePhotoChallenge();
            }
        });

        // Handle the Plus FAB click
        fabPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SdgMapActivity.this, "Plus Button Clicked!", Toast.LENGTH_SHORT).show();

                // Create an Intent to open CameraActivity
                Intent intent = new Intent(SdgMapActivity.this, CameraActivity.class);
                intent.putExtra("PHOTOCHALLENGE", photoChallengeText.getText().toString());
                intent.putExtra("SDGNUMBER", sdgNumber);
                intent.putExtra("LAT", curLocation.getLatitude());
                intent.putExtra("LONG", curLocation.getLongitude());

                // Start the activity
                startActivity(intent);
            }
        });


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
    }

    private void changePhotoChallenge() {
        // Get the list of challenges for the current SDG number
        List<String> challenges = PhotoChallenges.PHOTO_CHALLENGES.get(sdgNumber);

        if (challenges != null && !challenges.isEmpty()) {
            // Get the current challenge index and increment it to show the next challenge
            int currentIndex = getCurrentChallengeIndex();
            int nextIndex = (currentIndex + 1) % challenges.size();  // Loop back to the first challenge if it's the last one
            String nextChallenge = challenges.get(nextIndex);

            // Update the TextView to display the next challenge
            photoChallengeText.setText(nextChallenge);

            // Store the index of the current challenge (so you can navigate through them)
            saveCurrentChallengeIndex(nextIndex);
        } else {
            // Handle case if no challenges are found for the current SDG
            photoChallengeText.setText("No challenges available for this SDG.");
        }
    }

    private int getCurrentChallengeIndex() {
        SharedPreferences sharedPreferences = getSharedPreferences("ChallengePrefs", MODE_PRIVATE);
        return sharedPreferences.getInt("currentChallengeIndex", 0);  // Default to 0 if not set
    }


    private void saveCurrentChallengeIndex(int index) {
        // Save the current index to SharedPreferences or another persistent storage
        SharedPreferences sharedPreferences = getSharedPreferences("ChallengePrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("currentChallengeIndex", index);
        editor.apply();  // Apply changes asynchronously
    }


    private void fetchAndAddSdgPhotos() {
        try {
            Log.d(TAG, String.valueOf(sdgNumber));
            db.collection(DB_USERS_SDG_PHOTOS).whereEqualTo("sdgNumber", String.valueOf(sdgNumber)).get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String userId = document.getString("userId");
                        String avatarPath = Avatar.getUserAvatarPath(userId);


                        StorageReference avatarRef = storage.getReference().child(avatarPath);
                        StorageReference sdgImageRef = storage.getReference().child(document.getString("sdgPhotoUrl"));

                        avatarRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            sdgImageRef.getDownloadUrl().addOnSuccessListener(sdgUri -> {
                                Log.d(TAG, document.getString("userSdgId"));
                                Log.d(TAG, userId);
                                Log.d(TAG, document.getString("sdgNumber"));
                                Log.d(TAG, document.getString("caption"));
                                Log.d(TAG, sdgUri.toString());
                                Log.d(TAG, document.getString("photoChallenge"));
                                Log.d(TAG, document.getDouble("latitude").toString());
                                Log.d(TAG, document.getDouble("longitude").toString());
                                Log.d(TAG, uri.toString());

                                ClusterMarker clusterMarker = new ClusterMarker(
                                        document.getString("userSdgId"),
                                        userId,
                                        document.getString("sdgNumber"),
                                        document.getString("caption"),
                                        sdgUri.toString(),
                                        document.getString("photoChallenge"),
                                        document.getDouble("latitude"),
                                        document.getDouble("longitude"),
                                        uri.toString()
                                );

                                mClusterManager.addItem(clusterMarker);
                                mClusterMarkers.add(clusterMarker);
                                mClusterManager.cluster();
                            }).addOnFailureListener(e -> Log.e(TAG, "Failed to fetch sdgPhoto for userId: " + userId, e));
                        }).addOnFailureListener(e -> Log.e(TAG, "Failed to fetch avatar for userId: " + userId, e));
                    }
                }
            });
        } catch(Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }


    private void addMarkers() {
        if (mGoogleMap != null) {

            if (mClusterManager == null) {
                mClusterManager = new ClusterManager<>(getApplicationContext(), mGoogleMap);
            }

            if (mClusterManagerRenderer == null) {
                mClusterManagerRenderer = new ClusterManagerRenderer(this,
                        getApplicationContext(), mGoogleMap, mClusterManager, mClusterMarkers
                );
                mClusterManager.setRenderer(mClusterManagerRenderer);
            }

            fetchAndAddSdgPhotos();

            float zoomLevel = 12.0f;
            mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
            mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(curLocation.getLatitude(), curLocation.getLongitude()), zoomLevel
            ));
        }
    }

    private void getLastLocation() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    curLocation = location;

                    // Find the SupportMapFragment and request map initialization
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
                    if (mapFragment != null) {
                        mapFragment.getMapAsync(SdgMapActivity.this); // Get map asynchronously
                    } else {
                        // Handle error if SupportMapFragment is not found
                        Log.e(TAG, "SupportMapFragment is not found.");
                    }
                }
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mGoogleMap = googleMap;
        addMarkers();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // This will navigate back to the previous activity
            onBackPressed(); // Optionally, you can customize the behavior if needed
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
