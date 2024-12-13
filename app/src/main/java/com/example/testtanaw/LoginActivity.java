package com.example.testtanaw;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testtanaw.adapters.InstitutionSpinnerAdapter;
import com.example.testtanaw.models.Avatar;
import com.example.testtanaw.models.Constants;
import com.example.testtanaw.models.Institution;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private FirebaseAuth mAuth;
    private static final int LIMIT = 50;
    private EditText srCodeInput;
    private EditText passwordInput;
    private Spinner institutionSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase instances
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance(Constants.BUCKET);

        // Initialize views
        srCodeInput = findViewById(R.id.sr_code_input);
        passwordInput = findViewById(R.id.password_input);

        final List<Institution> institutionList = new ArrayList<>();
        institutionSpinner = findViewById(R.id.institution_spinner);

        // Get institutions from Firestore
        db.collection(Constants.DB_INSTITUTIONS)
                .orderBy("institution", Query.Direction.ASCENDING)
                .limit(LIMIT)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Institution institution = document.toObject(Institution.class);
                                institution.setId(document.getId());
                                institutionList.add(institution);
                            }

                            InstitutionSpinnerAdapter adapter = new InstitutionSpinnerAdapter(
                                    LoginActivity.this,
                                    institutionList
                            );
                            institutionSpinner.setAdapter(adapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    // Login button click handler
    public void onLoginClicked(View view) {
        String srCode = srCodeInput.getText().toString();
        String password = passwordInput.getText().toString();
        Institution selectedInstitution = (Institution) institutionSpinner.getSelectedItem();

        if (selectedInstitution == null) {
            Toast.makeText(this, "Please select an institution", Toast.LENGTH_SHORT).show();
            return;
        }

        if (srCode.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String email = srCode + "@" + selectedInstitution.getEmailExtension();

        // Attempt Firebase Authentication
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, get user data and redirect
                        FirebaseUser authUser = mAuth.getCurrentUser();
                        if (authUser != null) {
                            fetchUserDataAndRedirect(authUser.getUid());
                        }
                    } else {
                        // If sign in fails, display a message to the user
                        Toast.makeText(LoginActivity.this, "Authentication failed: " + 
                                task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Open SignUp Activity when the "Sign Up" text is clicked
    public void openSignUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }


    private void fetchUserDataAndRedirect(String userId) {
        db.collection(Constants.DB_USERS).document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        // Check if the user exists
                        if (task.getResult().exists()) {
                            db.collection(Constants.DB_AVATARS).document(userId)
                                    .get()
                                    .addOnCompleteListener(avatarTask -> {
                                        if (avatarTask.isSuccessful() && avatarTask.getResult() != null) {
                                            // Check if the avatar exists
                                            if (avatarTask.getResult().exists()) {
                                                // Avatar exists, proceed to the main activity
                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                // Avatar doesn't exist, upload a default one
                                                String path = Avatar.getUserAvatarPath(userId);
                                                Avatar newAvatar = new Avatar(userId, path, "cics", null, null, "boy", "shirt", null, "none");
                                                uploadDefaultAvatar(userId, path);
                                                saveAvatarDataToFirestore(newAvatar);
                                            }
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Failed to fetch user avatar",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(LoginActivity.this, "User does not exist",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Failed to fetch user data",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private byte[] drawableToBitmap() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.avatar);

        // Convert the Bitmap to a byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    private void uploadDefaultAvatar(String userId, String path) {
        StorageReference storageRef = storage.getReference();
        StorageReference avatarImageRef = storageRef.child(path);

        byte[] data = drawableToBitmap();

        UploadTask uploadTask = avatarImageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e(TAG, "Avatar image upload failed.", exception);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "Avatar image uploaded.");
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
                        Toast.makeText(LoginActivity.this, "Avatar data saved.", Toast.LENGTH_SHORT).show();

                        // redirect to AvatarActivity
                        Intent intent = new Intent(LoginActivity.this, AvatarActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Failed to save avatar data: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

