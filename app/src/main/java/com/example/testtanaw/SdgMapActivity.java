package com.example.testtanaw;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import com.example.testtanaw.models.PhotoChallenges;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class SdgMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private final int FINE_PERMISSION_CODE = 1;
    private GoogleMap mGoogle;
    Location curLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private TextView photoChallengeText;

//    private final List<String> challenges = PhotoChallenges.PHOTO_CHALLENGES; // Import your photo challenges

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdg_map);

        // Retrieve data passed from the adapter
        String sdgTitle = getIntent().getStringExtra("SDG_TITLE");
        int sdgNumber = getIntent().getIntExtra("sdgNumber", -1);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set the SDG title dynamically
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("SDG: " + sdgNumber);

        // set photo chall
        photoChallengeText = findViewById(R.id.photoChallengeText);
        FloatingActionButton fabArrow = findViewById(R.id.fab_arrow);
        FloatingActionButton fabPlus = findViewById(R.id.fab_plus);

        // Display the first challenge for the SDG
        updatePhotoChallenge();

        // Handle arrow button click to change challenges
        fabArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                changePhotoChallenge();
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

                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragemnt);
                    mapFragment.getMapAsync(SdgMapActivity.this);
                }
            }
        });
    }

        // Add your logic here to handle the SDG details
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mGoogle = googleMap;

        Log.d("SdgMapActivity", "Current Location: Latitude = " + curLocation.getLatitude() + ", Longitude = " + curLocation.getLongitude());

        LatLng myLoc = new LatLng(curLocation.getLatitude(), curLocation.getLongitude());
        Log.d("SdgMapActivity", "Adding marker at: " + myLoc.toString());
        mGoogle.addMarker(new MarkerOptions().position(myLoc).title("You're here!").snippet("mwehehe"));
        mGoogle.animateCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 15));
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

    private void updatePhotoChallenge() {
//        String sdgChallenges = challenges[sdgNumber] ?: emptyList()
    }

//    private fun updatePhotoChallenge() {
//        val sdgChallenges = challenges[sdgNumber] ?: emptyList()
//        if (sdgChallenges.isNotEmpty()) {
//            photoChallengeText.text = sdgChallenges[currentChallengeIndex]
//        } else {
//            photoChallengeText.text = "No challenges available for this SDG."
//        }
//    }

    private void changePhotoChallenge() {

    }

//    private fun changePhotoChallenge() {
//        val sdgChallenges = challenges[sdgNumber] ?: emptyList()
//        if (sdgChallenges.isNotEmpty()) {
//            currentChallengeIndex = (currentChallengeIndex + 1) % sdgChallenges.size
//            updatePhotoChallenge()
//        }
//    }
}