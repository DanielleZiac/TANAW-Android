package com.example.testtanaw;

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
    private FirebaseUser currentUser;
    private Avatar currentAvatar;

    private ImageView avatarBackground;
    private ImageView avatarGender;
    private ImageView avatarEyes;
    private ImageView avatarMouth;
    private ImageView avatarGlasses;
    private ImageView avatarShirt;

    private String bg;
    private String gender = "boy";
    private String eyewear = null;
    private String shirtStyle = "polo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar);

        // Initialize Firebase instances
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance("gs://tanaw-android.firebasestorage.app");

        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            fetchSingleAvatarData(currentUser.getUid(), new FirestoreCallback() {
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
        db.collection("avatars").whereEqualTo("user_id", userId).limit(1) // Limit the query to 1 document
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
        if (currentUser != null) {
            Log.d(TAG, "user: " + currentUser.getEmail());
            Log.d(TAG, eyewear + ", " + shirtStyle + ", " + gender + ", " + bg);

            if (bg != null) {
                // save
                String path = "users/avatars/" + currentUser.getUid() + "/" + UUID.randomUUID() + ".png";
                Avatar newAvatar = new Avatar(currentUser.getUid(), path, bg, null, null, gender, shirtStyle, null, eyewear);
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
        if (currentUser == null) {
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
        db.collection("avatars")
                .document(avatar.getUserId())
                .set(avatar)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(AvatarActivity.this, "User data saved successfully", Toast.LENGTH_SHORT).show();

                        // TODO: To ProfileActivity
                    } else {
                        Toast.makeText(AvatarActivity.this, "Failed to save avatar data: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupGlassesSwitch() {
        Switch glassesSwitch = findViewById(R.id.glasses);

        if (currentAvatar != null) {
            glassesSwitch.setChecked(currentAvatar.getEyewear() == "glassses");
        }

        glassesSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            eyewear = isChecked ? "glasses" : null;
            avatarGlasses.setImageResource(isChecked ? R.drawable.glasses : 0);
        });
    }

    private void setupShirtSwitch() {
        Switch shirtSwitch = findViewById(R.id.shirt);

        if (currentAvatar != null) {
            shirtSwitch.setChecked(currentAvatar.getShirtStyle() == "polo");
        }

        shirtSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            shirtStyle = isChecked ? "polo" : "shirt";
            avatarShirt.setImageResource(isChecked ? R.drawable.polo : R.drawable.shirt);
        });
    }

    private void setupGenderButtons() {
        if (currentAvatar != null) {
            if (currentAvatar.getGender() == "boy") {
                avatarGender.setImageResource(R.drawable.boy);
            } else {
                avatarGender.setImageResource(R.drawable.girl);
            }
        }

        Button boyButton = findViewById(R.id.boy);
        Button girlButton = findViewById(R.id.girl);

        boyButton.setOnClickListener(v -> {
            gender = "boy";
            avatarGender.setImageResource(R.drawable.boy);
        });

        girlButton.setOnClickListener(v -> {
            gender = "girl";
            avatarGender.setImageResource(R.drawable.girl);
        });
    }

    private void setupCollegeButtons() {
        if (currentAvatar != null) {
            if (currentAvatar.getBg() == "coe") {
                avatarBackground.setImageResource(R.drawable.bg_coe);
            } else if (currentAvatar.getBg() == "cics") {
                avatarBackground.setImageResource(R.drawable.bg_cics);
            } else if (currentAvatar.getBg() == "cafad") {
                avatarBackground.setImageResource(R.drawable.bg_cafad);
            } else if (currentAvatar.getBg() == "cet") {
                avatarBackground.setImageResource(R.drawable.bg_cet);
            }
        }

        Button coeButton = findViewById(R.id.coe);
        Button cicsButton = findViewById(R.id.cics);
        Button cafadButton = findViewById(R.id.cafad);
        Button cetButton = findViewById(R.id.cet);

        coeButton.setOnClickListener(v -> {
            bg = "coe";
            avatarBackground.setImageResource(R.drawable.bg_coe);
        });

        cicsButton.setOnClickListener(v -> {
            bg = "cics";
            avatarBackground.setImageResource(R.drawable.bg_cics);
        });

        cafadButton.setOnClickListener(v -> {
            bg = "cafad";
            avatarBackground.setImageResource(R.drawable.bg_cafad);
        });

        cetButton.setOnClickListener(v -> {
            bg = "cet";
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

        if (Objects.equals(avatar.getEyewear(), "glasses")) {
            eyewearBitmap = BitmapFactory.decodeResource(resources, R.drawable.glasses);
        }

        switch (avatar.getShirtStyle()) {
            case "shirt":
                shirtStyleBitmap = BitmapFactory.decodeResource(resources, R.drawable.shirt);
                break;
            case "polo":
                shirtStyleBitmap = BitmapFactory.decodeResource(resources, R.drawable.polo);
                break;
        }

        switch (avatar.getGender()) {
            case "boy":
                sexBitmap = BitmapFactory.decodeResource(resources, R.drawable.boy);
                break;
            case "girl":
                sexBitmap = BitmapFactory.decodeResource(resources, R.drawable.girl);
                break;
        }

        switch (avatar.getBg()) {
            case "cics":
                bgBitmap = BitmapFactory.decodeResource(resources, R.drawable.bg_cics);
                break;
            case "coe":
                bgBitmap = BitmapFactory.decodeResource(resources, R.drawable.bg_coe);
                break;
            case "cet":
                bgBitmap = BitmapFactory.decodeResource(resources, R.drawable.bg_cet);
                break;
            case "cafad":
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

    private void setupConfirmButton(String userId) {
        if (userId != null) {
            // update
        } else {
            // save new avatar
        }

        Button confirmButton = findViewById(R.id.confirm_avatar);
        confirmButton.setOnClickListener(v -> {
            Log.d("xxxxxx", eyewear + ", " + shirtStyle + ", " + gender + ", " + bg);

//            crud.saveAvatar(getResources(), userId, eyewear, shirtStyle, sex, bg)
//                    .thenAccept(newUrl -> {
//                        Log.d("xxxxxx", String.valueOf(newUrl));
//                        Intent intent = new Intent(AvatarActivity.this, ProfileActivity.class);
//                        startActivity(intent);
//                        finish();
//                    });
        });
    }
}

