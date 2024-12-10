package com.example.testtanaw;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testtanaw.models.Avatar;
import com.example.testtanaw.models.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class AvatarActivity extends AppCompatActivity {

    private static final String TAG = "AvatarActivity";

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private FirebaseUser authUser;
    private Avatar currentAvatar;

    private ImageView avatarBackground;
    private ImageView avatarGender;
    private ImageView avatarEyes;
    private ImageView avatarMouth;
    private ImageView avatarGlasses;
    private ImageView avatarShirt;

    private String bg;
    private String gender = Constants.GENDER_BOY;
    private String eyewear = null;
    private String shirtStyle = Constants.SHIRT_STYLE_POLO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar);

        // Initialize Firebase instances
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance(Constants.BUCKET);

        authUser = mAuth.getCurrentUser();

        if (authUser != null) {
            fetchSingleAvatarData(authUser.getUid(), new FirestoreCallback() {
                @Override
                public void onCallback(Avatar userAvatar) {
                    currentAvatar = userAvatar;
                    initializeViews();

                    setupGlassesSwitch();
                    setupShirtSwitch();
                    setupGenderButtons();
                    setupCollegeButtons();
                }
            });
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchSingleAvatarData(String userId, FirestoreCallback callback) {
        db.collection(Constants.DB_AVATARS).whereEqualTo("userId", userId).limit(1) // Limit the query to 1 document
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        // Get the first (and only) matching document
                        QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                        Avatar userAvatar = document.toObject(Avatar.class);
                        callback.onCallback(userAvatar); // Pass the document data to the callback
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
        void onCallback(Avatar userAvatar);
    }

    private void initializeViews() {
        avatarBackground = findViewById(R.id.avatarBackground);
        avatarGender = findViewById(R.id.avatarGender);
        avatarEyes = findViewById(R.id.avatarEyes);
        avatarMouth = findViewById(R.id.avatarMouth);
        avatarGlasses = findViewById(R.id.avatarGlasses);
        avatarShirt = findViewById(R.id.avatarShirt);

        // Set constant components
        avatarEyes.setImageResource(R.drawable.eyes_opened);
        avatarMouth.setImageResource(R.drawable.mouth_closed);
    }

    public void saveAvatar(View view) {
        if (authUser != null) {
            Log.d(TAG, "user: " + authUser.getEmail());
            Log.d(TAG, eyewear + ", " + shirtStyle + ", " + gender + ", " + bg);

            if (bg != null) {
                // save
                String path = Avatar.getUserAvatarPath(authUser.getUid());
                Avatar newAvatar = new Avatar(authUser.getUid(), path, bg, null, null, gender, shirtStyle, null, eyewear);
                uploadAvatarImage(newAvatar, path);
                saveAvatarDataToFirestore(newAvatar);
            } else {
                Toast.makeText(this, "You must select a College.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No user is currently logged in.", Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadAvatarImage(Avatar avatar, String path) {
        if (authUser == null) {
            Log.e(TAG, "uploadAvatarImage: User is not authenticated");
            return;
        }

        byte[] imgData = combineImg(getResources(), avatar);

        StorageReference storageRef = storage.getReference();
        StorageReference avatarImageRef = storageRef.child(path);

        assert imgData != null;
        UploadTask uploadTask = avatarImageRef.putBytes(imgData);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Use the correct context for Toast
                Toast.makeText(getApplicationContext(), "Failed to upload avatar.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Upload failed", exception);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String uploadedPath = taskSnapshot.getStorage().getPath();
                Log.d(TAG, "Uploaded to: " + uploadedPath);
            }
        });
    }

    private void saveAvatarDataToFirestore(Avatar avatar) {
        // Save data to Firestore under the "avatars" collection
        db.collection(Constants.DB_AVATARS)
                .document(avatar.getUserId())
                .set(avatar)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(AvatarActivity.this, "Avatar saved successfully", Toast.LENGTH_SHORT).show();

                        // redirect to MainActivity
                        Intent intent = new Intent(AvatarActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(AvatarActivity.this, "Failed to save avatar data: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupGlassesSwitch() {
        Switch glassesSwitch = findViewById(R.id.glasses);

        if (currentAvatar != null) {
            glassesSwitch.setChecked(currentAvatar.getEyewear() == Constants.EYEWEAR_GLASSES);
        }

        glassesSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            eyewear = isChecked ? Constants.EYEWEAR_GLASSES : null;
            avatarGlasses.setImageResource(isChecked ? R.drawable.glasses : 0);
        });
    }

    private void setupShirtSwitch() {
        Switch shirtSwitch = findViewById(R.id.shirt);

        if (currentAvatar != null) {
            shirtSwitch.setChecked(currentAvatar.getShirtStyle() == Constants.SHIRT_STYLE_POLO);
        }

        shirtSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            shirtStyle = isChecked ? Constants.SHIRT_STYLE_POLO : Constants.SHIRT_STYLE_SHIRT;
            avatarShirt.setImageResource(isChecked ? R.drawable.polo : R.drawable.shirt);
        });
    }

    private void setupGenderButtons() {
        if (currentAvatar != null) {
            if (currentAvatar.getGender() == Constants.GENDER_BOY) {
                avatarGender.setImageResource(R.drawable.boy);
            } else {
                avatarGender.setImageResource(R.drawable.girl);
            }
        }

        Button boyButton = findViewById(R.id.boy);
        Button girlButton = findViewById(R.id.girl);

        boyButton.setOnClickListener(v -> {
            gender = Constants.GENDER_BOY;
            avatarGender.setImageResource(R.drawable.boy);
        });

        girlButton.setOnClickListener(v -> {
            gender = Constants.GENDER_GIRL;
            avatarGender.setImageResource(R.drawable.girl);
        });
    }

    private void setupCollegeButtons() {
        if (currentAvatar != null) {
            if (currentAvatar.getBg() == Constants.DEPT_COE) {
                avatarBackground.setImageResource(R.drawable.bg_coe);
            } else if (currentAvatar.getBg() == Constants.DEPT_CICS) {
                avatarBackground.setImageResource(R.drawable.bg_cics);
            } else if (currentAvatar.getBg() == Constants.DEPT_CAFAD) {
                avatarBackground.setImageResource(R.drawable.bg_cafad);
            } else if (currentAvatar.getBg() == Constants.DEPT_CET) {
                avatarBackground.setImageResource(R.drawable.bg_cet);
            }
        }

        Button coeButton = findViewById(R.id.coe);
        Button cicsButton = findViewById(R.id.cics);
        Button cafadButton = findViewById(R.id.cafad);
        Button cetButton = findViewById(R.id.cet);

        coeButton.setOnClickListener(v -> {
            bg = Constants.DEPT_COE;
            avatarBackground.setImageResource(R.drawable.bg_coe);
        });

        cicsButton.setOnClickListener(v -> {
            bg = Constants.DEPT_CICS;
            avatarBackground.setImageResource(R.drawable.bg_cics);
        });

        cafadButton.setOnClickListener(v -> {
            bg = Constants.DEPT_CAFAD;
            avatarBackground.setImageResource(R.drawable.bg_cafad);
        });

        cetButton.setOnClickListener(v -> {
            bg = Constants.DEPT_CET;
            avatarBackground.setImageResource(R.drawable.bg_cet);
        });
    }

    private static byte[] combineImg(Resources resources, Avatar avatar) {
        // Load the PNG images as Bitmaps
        Bitmap bgBitmap = null;
        Bitmap sexBitmap = null;
        Bitmap shirtStyleBitmap = null;
        Bitmap eyewearBitmap = null;

        Bitmap eyeBitmap = BitmapFactory.decodeResource(resources, R.drawable.eyes_opened);
        Bitmap smileBitmap = BitmapFactory.decodeResource(resources, R.drawable.mouth_closed);

        if (Objects.equals(avatar.getEyewear(), Constants.EYEWEAR_GLASSES)) {
            eyewearBitmap = BitmapFactory.decodeResource(resources, R.drawable.glasses);
        }

        switch (avatar.getShirtStyle()) {
            case Constants.SHIRT_STYLE_SHIRT:
                shirtStyleBitmap = BitmapFactory.decodeResource(resources, R.drawable.shirt);
                break;
            case Constants.SHIRT_STYLE_POLO:
                shirtStyleBitmap = BitmapFactory.decodeResource(resources, R.drawable.polo);
                break;
        }

        switch (avatar.getGender()) {
            case Constants.GENDER_BOY:
                sexBitmap = BitmapFactory.decodeResource(resources, R.drawable.boy);
                break;
            case Constants.GENDER_GIRL:
                sexBitmap = BitmapFactory.decodeResource(resources, R.drawable.girl);
                break;
        }

        switch (avatar.getBg()) {
            case Constants.DEPT_CICS:
                bgBitmap = BitmapFactory.decodeResource(resources, R.drawable.bg_cics);
                break;
            case Constants.DEPT_COE:
                bgBitmap = BitmapFactory.decodeResource(resources, R.drawable.bg_coe);
                break;
            case Constants.DEPT_CET:
                bgBitmap = BitmapFactory.decodeResource(resources, R.drawable.bg_cet);
                break;
            case Constants.DEPT_CAFAD:
                bgBitmap = BitmapFactory.decodeResource(resources, R.drawable.bg_cafad);
                break;
        }

        // Ensure that the base image is not null before proceeding
        if (bgBitmap == null) {
            return null;
        }

        // Create a blank bitmap with the size of the base image
        Bitmap overlayedBitmap = Bitmap.createBitmap(bgBitmap.getWidth(), bgBitmap.getHeight(), Bitmap.Config.ARGB_8888);

        // Create a Canvas to draw on the overlayedBitmap
        Canvas canvas = new Canvas(overlayedBitmap);

        // Draw the base image
        canvas.drawBitmap(bgBitmap, 0f, 0f, null);

        // Draw the overlay images on top of the base image
        if (sexBitmap != null) {
            canvas.drawBitmap(sexBitmap, 0f, 0f, null);
        }
        if (shirtStyleBitmap != null) {
            canvas.drawBitmap(shirtStyleBitmap, 0f, 0f, null);
        }
        if (avatar.getEyewear() != null && eyewearBitmap != null) {
            canvas.drawBitmap(eyewearBitmap, 0f, 0f, null);
        }
        canvas.drawBitmap(eyeBitmap, 0f, 0f, null);
        canvas.drawBitmap(smileBitmap, 0f, 0f, null);

        // Convert the bitmap to ByteArray
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        overlayedBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}

