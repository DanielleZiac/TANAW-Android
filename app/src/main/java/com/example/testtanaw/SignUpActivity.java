package com.example.testtanaw;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testtanaw.models.Avatar;
import com.example.testtanaw.models.Constants;
import com.example.testtanaw.models.Institution;
import com.example.testtanaw.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import com.example.testtanaw.adapters.InstitutionSpinnerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";

    private FirebaseAuth mAuth;

    private FirebaseFirestore db;
    //    private Query mQuery;
    private static final int LIMIT = 50;

    private EditText firstNameInput;
    private EditText lastNameInput;
    private EditText srCodeInput;
    private EditText passwordInput;
    private EditText confirmPasswordInput;
    private Spinner institutionSpinner;
    private CheckBox checkBoxTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Firebase Auth instance
        mAuth = FirebaseAuth.getInstance();

        // Firebase Firestore instance
        db = FirebaseFirestore.getInstance();

        final List<Institution> institutionList = new ArrayList<>();
        institutionSpinner = findViewById(R.id.institution_spinner);

        // Get institutions from Firestore
        db.collection("institutions")
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
                                    SignUpActivity.this,
                                    institutionList
                            );
                            institutionSpinner.setAdapter(adapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        // Initialize views
        srCodeInput = findViewById(R.id.sr_code_input);
        firstNameInput = findViewById(R.id.first_name_input);
        lastNameInput = findViewById(R.id.last_name_input);
        passwordInput = findViewById(R.id.password_input);
        confirmPasswordInput = findViewById(R.id.confirm_password_input);
        checkBoxTerms = findViewById(R.id.checkBoxTerms);

        // Handle spinner item selection
        institutionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Institution selectedInstitution = (Institution) parent.getItemAtPosition(position);
                Toast.makeText(
                        SignUpActivity.this,
                        "Selected: " + selectedInstitution.getInstitution(),
                        Toast.LENGTH_SHORT
                ).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no selection case if needed
            }
        });
    }

    // Sign-Up button click handler
    public void onSignUpClicked(View view) {
        if (!checkBoxTerms.isChecked()) {
            Toast.makeText(this, "Please accept the Terms and Conditions to proceed", Toast.LENGTH_SHORT).show();
            return;
        }
        String firstName = firstNameInput.getText().toString();
        String lastName = lastNameInput.getText().toString();
        String srCode = srCodeInput.getText().toString();
        String password = passwordInput.getText().toString();
        String confirmPassword = confirmPasswordInput.getText().toString();
        Institution selectedInstitution = (Institution) institutionSpinner.getSelectedItem();
        if (selectedInstitution == null) {
            Toast.makeText(this, "Please select an institution", Toast.LENGTH_SHORT).show();
            return;
        }
        String institutionId = selectedInstitution.getId();
        String emailExtension = selectedInstitution.getEmailExtension();

        // Validate inputs
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            return;
        }

        String email = srCode + "@" + emailExtension;

        if (isValidEmail(email) && isValidPassword(password)) {
            signUpUser(email, password, firstName, lastName, srCode, institutionId);
        }
    }

    // Open Login Activity when the "Sign In" text is clicked
    public void openLogin(View view) {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private boolean isValidEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isValidPassword(String password) {
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void signUpUser(String email, String password, String firstName, String lastName, String srCode, String institutionId) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign-up successful, get the signed-in user's UID
                        FirebaseUser authUser = mAuth.getCurrentUser();
                        if (authUser != null) {
                            User newUser = new User();
                            newUser.setId(authUser.getUid());
                            newUser.setFirstName(firstName);
                            newUser.setEmail(email);
                            newUser.setLastName(lastName);
                            newUser.setSrCode(srCode);
                            newUser.setInstitutionId(institutionId);
                            saveUserDataToFirestore(newUser);
                        }

                        // TODO add default user avatar





                        // redirect to LoginActivity
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);

                    } else {
                        // If sign-up fails, display a message to the user
                        Toast.makeText(SignUpActivity.this, "Sign-up failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserDataToFirestore(User user) {
        // Save data to Firestore under the "users" collection
        db.collection(Constants.DB_USERS)
                .document(user.getId())
                .set(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this, "User data saved successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Failed to save user data: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

