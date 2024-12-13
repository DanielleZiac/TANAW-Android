package com.example.testtanaw;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowMetrics;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.camera.core.resolutionselector.ResolutionSelector;
import androidx.camera.core.resolutionselector.AspectRatioStrategy;
import androidx.camera.core.Preview;
import com.example.testtanaw.models.Constants;
import com.example.testtanaw.models.UserSdgPhoto;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import androidx.camera.lifecycle.ProcessCameraProvider;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.UUID;

public class CameraActivity extends AppCompatActivity {

    private static final String TAG = "CameraActivity";

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private FirebaseUser authUser;


    private ImageCapture imageCapture;
    private ProcessCameraProvider cameraProvider;
    private Camera camera;
    private CameraSelector cameraSelector;
    private int lensFacing = CameraSelector.LENS_FACING_BACK;
    private byte[] curImageData;


    private int sdgNumber;
    private String photoChallenge;
    private Double latitude;
    private Double longitude;

    private String path;
    private UserSdgPhoto newSdgPhoto;
    private byte[] imageData;

    private ImageButton fabCamera;
    private ImageButton fabFlipCamera;
    private EditText caption;
    private ImageButton upload;
    private ImageButton retake;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);


        // Initialize Firebase instances
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance(Constants.BUCKET);

        authUser = mAuth.getCurrentUser();


        if (authUser != null) {
            // get intent
            sdgNumber = getIntent().getIntExtra("SDGNUMBER", -1);
            photoChallenge = getIntent().getStringExtra("PHOTOCHALLENGE");
            latitude = getIntent().getDoubleExtra("LAT", 0.0);
            longitude = getIntent().getDoubleExtra("LONG", 0.0);

            // set views
            fabCamera = findViewById(R.id.fab_camera);
            fabFlipCamera = findViewById(R.id.fab_flip_camera);
            caption = findViewById(R.id.caption);
            upload = findViewById(R.id.upload);
            retake = findViewById(R.id.retake);

            setInitialState();

            fabCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    takePhoto();
                }
            });

            upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadPhoto();
                }
            });

            retake.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    retakePhoto();
                }
            });

            fabFlipCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flipCamera();
                }
            });

            // check cam permission
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestCameraPermission();
                startCamera();
            } else {
                startCamera();
            }
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }


    private void setInitialState() {
        fabCamera.setVisibility(View.VISIBLE);
        fabFlipCamera.setVisibility(View.VISIBLE);
        caption.setVisibility(View.GONE);
        upload.setVisibility(View.GONE);
        retake.setVisibility(View.GONE);
    }

    private void bindCameraUserCases() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        int width;
        int height;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = getWindowManager().getCurrentWindowMetrics();

            // Get the bounds of the display
            Rect bounds = windowMetrics.getBounds();
            width = bounds.width();
            height = bounds.height();

            Log.d("tag", "Width: " + width + ", Height: " + height);
        } else {
            // For older APIs, use the deprecated method
                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            width = displayMetrics.widthPixels;
            height = displayMetrics.heightPixels;

            Log.d("tag", "Width: " + width + ", Height: " + height);
        }

        int screenAspectRatio = aspectRatio(width, height);

        // Determine the rotation
        int rotation = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) ?
                getWindowManager().getDefaultDisplay().getRotation() :
                getWindowManager().getDefaultDisplay().getRotation();

        ResolutionSelector resolutionSelector = new ResolutionSelector.Builder()
                .setAspectRatioStrategy(new AspectRatioStrategy(screenAspectRatio, AspectRatioStrategy.FALLBACK_RULE_AUTO))
                .build();

        Preview preview = new Preview.Builder()
                .setResolutionSelector(resolutionSelector)
                .setTargetRotation(rotation)
                .build();
        preview.setSurfaceProvider(((PreviewView) findViewById(R.id.cameraView)).getSurfaceProvider());

        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .setResolutionSelector(resolutionSelector)
                .setTargetRotation(rotation)
                .build();

        cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build();

        try {
            cameraProvider.unbindAll();
            camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
        } catch (Exception e) {
            Log.d("tag", "Error bindcamera: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void flipCamera() {
        Toast.makeText(this, "Camera Flipped!", Toast.LENGTH_SHORT).show();
        lensFacing = (lensFacing == CameraSelector.LENS_FACING_FRONT) ? CameraSelector.LENS_FACING_BACK : CameraSelector.LENS_FACING_FRONT;
        bindCameraUserCases();
    }

    private void takePhoto() {
        Toast.makeText(this, "Photo Captured!", Toast.LENGTH_SHORT).show();
        fabCamera.setVisibility(View.GONE);
        fabFlipCamera.setVisibility(View.GONE);
        caption.setVisibility(View.VISIBLE);
        upload.setVisibility(View.VISIBLE);
        retake.setVisibility(View.VISIBLE);

        // Implement photo capture logic here
        File imageFile = null;
        try {
            imageFile = File.createTempFile("temp", ".jpg");
        } catch (IOException e) {
            Log.d(TAG, "ERROR TAKE PHOTO FILE TEMP: " + e.getMessage());
            throw new RuntimeException(e);
        }

        ImageCapture.OutputFileOptions outputOption = new ImageCapture.OutputFileOptions.Builder(imageFile).build();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        imageCapture.takePicture(
                outputOption,
                ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {

                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        try {
                            Uri savedUri = outputFileResults.getSavedUri();
                            if (savedUri != null) {
                                File file = new File(savedUri.getPath());
                                try (InputStream inputStream = new FileInputStream(file)) {
                                    byte[] buffer = new byte[1024];
                                    int bytesRead;
                                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                                        byteArrayOutputStream.write(buffer, 0, bytesRead);
                                    }
                                }

                                imageData = byteArrayOutputStream.toByteArray();

                                // Convert the byte array to Bitmap
                                Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

                                // Rotate if needed (assuming you determine the required angle)
                                int angle = 90;
                                bitmap = rotateImage(bitmap, angle);

                                // Crop the Bitmap to a 1x1 (square) aspect ratio
                                bitmap = cropToSquare(bitmap);

                                // Get the ImageView from the layout
                                ImageView capturedImageView = findViewById(R.id.imgView);

                                // Set the Bitmap to the ImageView to display the captured image
                                capturedImageView.setImageBitmap(bitmap);
                            }

                        } catch (Exception e) {
                            Log.d(TAG, "ERROR TAKE PHOTO IMAGE SAVE: " + e.getMessage());
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {

                    }
                });
    }

    private Bitmap rotateImage(Bitmap source, int angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private Bitmap cropToSquare(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int newDimension = Math.min(width, height);  // Take the smallest side for square

        // Calculate the center of the bitmap
        int x = (width - newDimension) / 2;
        int y = (height - newDimension) / 2;

        // Crop the image to a square
        Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, x, y, newDimension, newDimension);

        return croppedBitmap;
    }

    private void uploadUsersSdgPhoto(UserSdgPhoto userSdgPhoto, String path) {
        if (authUser == null) {
            return;
        }

        StorageReference storageRef = storage.getReference();
        StorageReference userSdgPhotoRef = storageRef.child(path);
        Toast.makeText(CameraActivity.this, path, Toast.LENGTH_SHORT).show();

        assert imageData != null;
        UploadTask uploadTask = userSdgPhotoRef.putBytes(imageData);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(CameraActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "User sdg image upload failed.", exception);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(CameraActivity.this, "User Sdg image uploaded.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "User Sdg uploaded.");
            }
        });
    }

    private void saveUsersSdgPhotoDataToFirestore(UserSdgPhoto userSdgPhoto, String uuid) {
        // Save data to Firestore under the "users_sdg_photos" collection
        db.collection(Constants.DB_USERS_SDG_PHOTOS)
                .document(userSdgPhoto.getUserSdgId())
                .set(userSdgPhoto)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(CameraActivity.this, "User Sdg Photo data saved.", Toast.LENGTH_SHORT).show();

//                         redirect to MainActivity
                        Intent intent = new Intent(CameraActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(CameraActivity.this, task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadPhoto() {
        String uuid = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();

        String captionText = caption.getText().toString();

        if (authUser != null) {
            path = UserSdgPhoto.getUserSdgPhotoPath(String.valueOf(sdgNumber), uuid);
            newSdgPhoto = new UserSdgPhoto(uuid, authUser.getUid(), String.valueOf(sdgNumber), captionText, now, path, photoChallenge, latitude, longitude);
        } else {
            Toast.makeText(this, "No user is currently logged in.", Toast.LENGTH_SHORT).show();
        }


        if (!captionText.isEmpty()) {
            // Your upload logic here
            Toast.makeText(this, "Uploading photo with caption: " + captionText, Toast.LENGTH_SHORT).show();
            uploadUsersSdgPhoto(newSdgPhoto, path);
            saveUsersSdgPhotoDataToFirestore(newSdgPhoto, uuid);
        } else {
            Toast.makeText(this, "Add caption", Toast.LENGTH_SHORT).show();
        }
    }

    private void retakePhoto() {
        Toast.makeText(this, "Retaking Photo!", Toast.LENGTH_SHORT).show();

        // clear img
        ImageView capturedImageView = findViewById(R.id.imgView);
        capturedImageView.setImageBitmap(null);

        setInitialState();
    }

    // Define the permission request launcher
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean isGranted) {
                    if (isGranted) {
                        Log.d(TAG, "Camera opened");
                        startCamera();
                        // Do capture
                    } else {
                        Log.d(TAG, "Permission denied");
                    }
                }
            });

    // Request the camera permission
    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)) {
            // Explain to the user why we need this permission, then launch the permission request
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA);
        } else {
            // Just launch the permission request
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA);
        }
    }

    private void startCamera() {
        // Get the ProcessCameraProvider instance
        ListenableFuture<ProcessCameraProvider> listendableFuture = ProcessCameraProvider.getInstance((this));

        listendableFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    // Get the cameraProvider instance
                    cameraProvider = ProcessCameraProvider.getInstance(getApplicationContext()).get();

                    // Bind the camera use cases
                    bindCameraUserCases();
                } catch (Exception e) {
                    // Log the error if the camera setup fails
                    Log.e(TAG, "Camera Error: " + e.getMessage());
                }
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private int aspectRatio(int width, int height) {
        double previewRatio = Math.max(height, width) / (double) Math.min(height, width);
        if (Math.abs(previewRatio - (4.0 / 3.0)) <= Math.abs(previewRatio - (16.0 / 9.0))) {
            return AspectRatio.RATIO_4_3;
        } else {
            return AspectRatio.RATIO_16_9;
        }
    }


}