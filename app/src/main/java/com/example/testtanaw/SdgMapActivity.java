package com.example.testtanaw;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.testtanaw.data.PhotoChallenges;
import com.example.testtanaw.models.ClusterMarker;
import com.example.testtanaw.util.CRUD;
import com.example.testtanaw.util.ClusterManagerRenderer;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.maps.android.clustering.ClusterManager;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SdgMapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    private final CRUD crud = new CRUD();
    private Location curLocation;
    private List<CRUD.SdgPhoto> sdgPhotoList = new ArrayList<>();
    private ClusterManager<ClusterMarker> mClusterManager;
    private ClusterManagerRenderer mClusterManagerRenderer;
    private final ArrayList<ClusterMarker> mClusterMarkers = new ArrayList<>();
    private GoogleMap mGoogleMap;
    private TextView photoChallengeText;
    private final List<String> challenges = PhotoChallenges.PHOTO_CHALLENGES;
    private int currentChallengeIndex = 0;
    private int sdgNumber = 1;

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
        sdgNumber = getIntent().getIntExtra("sdgNumber", 0);
        toolbarTitle.setText("SDG: " + sdgNumber);

        String institutionId = getIntent().getStringExtra("institutionId");

        Log.d("xxxxxx", "sdgNumber: " + sdgNumber);
        Log.d("xxxxxx", "institutionID: " + institutionId);

        CompletableFuture.runAsync(() -> {
            sdgPhotoList = crud.getSdgPhoto(sdgNumber, "today", null);
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        photoChallengeText = findViewById(R.id.photoChallengeText);
        FloatingActionButton fabArrow = findViewById(R.id.fab_arrow);
        FloatingActionButton fabPlus = findViewById(R.id.fab_plus);
        FloatingActionButton fabFilter = findViewById(R.id.fab_filter);

        updatePhotoChallenge();

        fabArrow.setOnClickListener(v -> changePhotoChallenge());

        fabPlus.setOnClickListener(v -> {
            Toast.makeText(this, "Plus Button Clicked!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, CameraActivity.class);
            startActivity(intent);
        });

        fabFilter.setOnClickListener(this::showPopupMenu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addMapMarkers() {
        Log.d("xxxxxxx", "PHOTO COUNT: " + sdgPhotoList.size());
        mGoogleMap.clear();

        if (mClusterManager == null) {
            mClusterManager = new ClusterManager<>(getApplicationContext(), mGoogleMap);
        }
        if (mClusterManagerRenderer == null) {
            mClusterManagerRenderer = new ClusterManagerRenderer(
                getApplicationContext(), mGoogleMap, mClusterManager, mClusterMarkers);
            mClusterManager.setRenderer(mClusterManagerRenderer);
        }

        try {
            if (!sdgPhotoList.isEmpty()) {
                Log.d("tag", "sdgPhotoList: " + sdgPhotoList);
                for (CRUD.SdgPhoto sdgPhoto : sdgPhotoList) {
                    Log.d("tag", sdgPhoto.toString());

                    ClusterMarker newClusterMarker = new ClusterMarker(
                        sdgPhoto.getUserSdgId(),
                        sdgPhoto.getUserId(),
                        sdgPhoto.getSdgNumber(),
                        sdgPhoto.getUrl(),
                        sdgPhoto.getCaption(),
                        sdgPhoto.getCreatedDate(),
                        sdgPhoto.getInstitutionId(),
                        sdgPhoto.getPhototChall(),
                        sdgPhoto.getInstitution(),
                        sdgPhoto.getCampus(),
                        sdgPhoto.getInstitutionLogo(),
                        sdgPhoto.getLat(),
                        sdgPhoto.getLng(),
                        sdgPhoto.getAvatarUrl()
                    );

                    mClusterManager.addItem(newClusterMarker);
                    mClusterMarkers.add(newClusterMarker);
                }
            }
        } catch (Exception e) {
            Log.d("xxxxxx", e.getMessage());
        }

        mClusterManager.cluster();

        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        addMapMarkers();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        // Implementation needed
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
            if (menuItem.getItemId() == R.id.filter_all) {
                CompletableFuture.runAsync(() -> {
                    sdgPhotoList = crud.getSdgPhoto(sdgNumber, "all", null);
                }).thenRun(() -> runOnUiThread(() -> {
                    addMapMarkers();
                    Toast.makeText(SdgMapActivity.this, "All selected", Toast.LENGTH_SHORT).show();
                }));
                return true;
            } else if (menuItem.getItemId() == R.id.filter_today) {
                CompletableFuture.runAsync(() -> {
                    sdgPhotoList = crud.getSdgPhoto(sdgNumber, "today", null);
                }).thenRun(() -> runOnUiThread(() -> {
                    addMapMarkers();
                    Toast.makeText(SdgMapActivity.this, "Today selected", Toast.LENGTH_SHORT).show();
                }));
                return true;
            } else if (menuItem.getItemId() == R.id.filter_yesterday) {
                CompletableFuture.runAsync(() -> {
                    sdgPhotoList = crud.getSdgPhoto(sdgNumber, "yesterday", null);
                }).thenRun(() -> runOnUiThread(() -> {
                    addMapMarkers();
                    Toast.makeText(SdgMapActivity.this, "Yesterday selected", Toast.LENGTH_SHORT).show();
                }));
                return true;
            } else if (menuItem.getItemId() == R.id.filter_last_week) {
                CompletableFuture.runAsync(() -> {
                    sdgPhotoList = crud.getSdgPhoto(sdgNumber, "last week", null);
                }).thenRun(() -> runOnUiThread(() -> {
                    addMapMarkers();
                    Toast.makeText(SdgMapActivity.this, "Last Week selected", Toast.LENGTH_SHORT).show();
                }));
                return true;
            } else if (menuItem.getItemId() == R.id.filter_last_month) {
                CompletableFuture.runAsync(() -> {
                    sdgPhotoList = crud.getSdgPhoto(sdgNumber, "last month", null);
                }).thenRun(() -> runOnUiThread(() -> {
                    addMapMarkers();
                    Toast.makeText(SdgMapActivity.this, "Last Month selected", Toast.LENGTH_SHORT).show();
                }));
                return true;
            }
            return false;
        });
        popupMenu.show();
    }
}
