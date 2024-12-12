package com.example.testtanaw;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.example.testtanaw.data.PhotoChallenges;
import com.example.testtanaw.models.ClusterMarker;
import com.example.testtanaw.util.CRUD;
import com.example.testtanaw.util.ClusterManagerRenderer;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.maps.android.clustering.ClusterManager;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SdgMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private final int FINE_PERMISSION_CODE = 1;
    private GoogleMap mGoogleMap;
    Location curLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private TextView photoChallengeText;

    private int currentChallengeIndex = 0;
    private int sdgNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdg_map);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        // Retrieve data passed from the adapter
        String sdgTitle = getIntent().getStringExtra("SDG_TITLE");
        sdgNumber = getIntent().getIntExtra("sdgNumber", -1);

        toolbarTitle.setText("SDG: " + sdgNumber);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        photoChallengeText = findViewById(R.id.photoChallengeText);
        FloatingActionButton fabArrow = findViewById(R.id.fab_arrow);
        FloatingActionButton fabPlus = findViewById(R.id.fab_plus);
        FloatingActionButton fabFilter = findViewById(R.id.fab_filter);

        updatePhotoChallenge();

        fabArrow.setOnClickListener(v -> changePhotoChallenge());

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

        fabFilter.setOnClickListener(this::showPopupMenu);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        Log.d("SdgMapActivity", "Current Location: Latitude = " + curLocation.getLatitude() + ", Longitude = " + curLocation.getLongitude());

        LatLng myLoc = new LatLng(curLocation.getLatitude(), curLocation.getLongitude());
        Log.d("SdgMapActivity", "Adding marker at: " + myLoc.toString());
        mGoogleMap.addMarker(new MarkerOptions().position(myLoc).title("You're here!").snippet("mwehehe"));
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 15));
    }


    private void updatePhotoChallenge() {
        List<String> sdgChallenges = challenges.get(sdgNumber);
        if (sdgChallenges != null && !sdgChallenges.isEmpty()) {
            photoChallengeText.setText(sdgChallenges.get(currentChallengeIndex));
        } else {
            photoChallengeText.setText("No challenges available for this SDG.");
        }
    }

    private void changePhotoChallenge() {
        List<String> sdgChallenges = challenges.get(sdgNumber);
        if (sdgChallenges != null && !sdgChallenges.isEmpty()) {
            currentChallengeIndex = (currentChallengeIndex + 1) % sdgChallenges.size();
            updatePhotoChallenge();
        }
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.filter_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            return false;
        });
        popupMenu.show();
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

}