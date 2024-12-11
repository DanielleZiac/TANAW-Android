package com.example.testtanaw;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class CameraActivity extends AppCompatActivity {
    private ImageButton fabCamera;
    private ImageButton fabFlipCamera;
    private EditText caption;
    private ImageButton upload;
    private ImageButton retake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        fabCamera = findViewById(R.id.fab_camera);
        fabFlipCamera = findViewById(R.id.fab_flip_camera);
        caption = findViewById(R.id.caption);
        upload = findViewById(R.id.upload);
        retake = findViewById(R.id.retake);

        // Initial state
        setInitialState();

        // Camera FAB click listener
        fabCamera.setOnClickListener(v -> takePhoto());

        // Upload FAB click listener
        upload.setOnClickListener(v -> uploadPhoto());

        // Retake FAB click listener
        retake.setOnClickListener(v -> retakePhoto());

        // Flip camera FAB click listener
        fabFlipCamera.setOnClickListener(v -> flipCamera());

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
    }

    private void setInitialState() {
        fabCamera.setVisibility(View.VISIBLE);
        fabFlipCamera.setVisibility(View.VISIBLE);
        caption.setVisibility(View.GONE);
        upload.setVisibility(View.GONE);
        retake.setVisibility(View.GONE);
    }

    private void takePhoto() {
        // Logic to freeze or capture the photo
        Toast.makeText(this, "Photo Captured!", Toast.LENGTH_SHORT).show();

        // Toggle visibility
        fabCamera.setVisibility(View.GONE);
        fabFlipCamera.setVisibility(View.GONE);
        caption.setVisibility(View.VISIBLE);
        upload.setVisibility(View.VISIBLE);
        retake.setVisibility(View.VISIBLE);
    }

    private void uploadPhoto() {
        // Logic to upload photo and caption
        String captionText = caption.getText().toString();
        Toast.makeText(this, "Uploading photo with caption: " + captionText, Toast.LENGTH_SHORT).show();

        // Navigate back to the previous activity
        finish();
    }

    private void retakePhoto() {
        // Logic to reset the camera preview
        Toast.makeText(this, "Retaking Photo!", Toast.LENGTH_SHORT).show();

        // Revert to the initial state
        setInitialState();
    }

    private void flipCamera() {
        // Logic to flip the camera
        Toast.makeText(this, "Camera Flipped!", Toast.LENGTH_SHORT).show();
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