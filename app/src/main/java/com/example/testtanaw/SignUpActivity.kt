package com.example.testtanaw;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.testtanaw.util.DB;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SignUpActivity extends AppCompatActivity {
    private EditText firstNameInput;
    private EditText lastNameInput;
    private EditText srCodeInput;
    private EditText passwordInput;
    private EditText confirmPasswordInput;
    private Spinner institutionSpinner;
    private List<DB.Institution> institutions;
    private final DB db = new DB();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initializeViews();
        setupInstitutionsSpinner();
    }

    private void initializeViews() {
        srCodeInput = findViewById(R.id.sr_code_input);
        firstNameInput = findViewById(R.id.first_name_input);
        lastNameInput = findViewById(R.id.last_name_input);
        institutionSpinner = findViewById(R.id.institution_spinner);
        passwordInput = findViewById(R.id.password_input);
        confirmPasswordInput = findViewById(R.id.confirm_password_input);
    }

    private void setupInstitutionsSpinner() {
        CompletableFuture.supplyAsync(() -> {
            try {
                return db.getInstitutions(this, institutionSpinner).get();
            } catch (Exception e) {
                Log.e("SignUpActivity", "Error getting institutions", e);
                return null;
            }
        }).thenAccept(result -> {
            if (result != null) {
                institutions = result;
                institutionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedInstitution = parent.getItemAtPosition(position).toString();
                        Toast.makeText(SignUpActivity.this, 
                            "Selected: " + selectedInstitution, 
                            Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Handle no selection case if needed
                    }
                });
            }
        });
    }

    public void onSignUpClicked(View view) {
        String firstName = firstNameInput.getText().toString();
        String lastName = lastNameInput.getText().toString();
        String srCode = srCodeInput.getText().toString();
        String password = passwordInput.getText().toString();
        String confirmPassword = confirmPasswordInput.getText().toString();
        
        if (institutions == null || institutionSpinner.getSelectedItemPosition() < 0) {
            Toast.makeText(this, "Please select an institution.", Toast.LENGTH_SHORT).show();
            return;
        }

        String selectedInstitution = institutions.get(institutionSpinner.getSelectedItemPosition()).getInstitution();
        String emailExtension = institutions.get(institutionSpinner.getSelectedItemPosition()).getEmailExtension();
        String institutionId = institutions.get(institutionSpinner.getSelectedItemPosition()).getInstitutionId();

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("xxxxxx", selectedInstitution + ", " + emailExtension);

        if (!firstName.isEmpty() && !lastName.isEmpty() && !srCode.isEmpty() 
            && !password.isEmpty() && !emailExtension.isEmpty()) {
            
            CompletableFuture.supplyAsync(() -> {
                try {
                    return db.signup(firstName, lastName, srCode, password, emailExtension, institutionId).get();
                } catch (Exception e) {
                    Log.e("SignUpActivity", "Error during signup", e);
                    return null;
                }
            }).thenAccept(user -> runOnUiThread(() -> {
                if (user != null) {
                    Log.d("xxxxxx", user.toString());
                    Toast.makeText(SignUpActivity.this, "check your email", Toast.LENGTH_SHORT).show();
                    clearInputs();
                } else {
                    Log.d("xxxxxx", "Failed to sign up user. Response is null.");
                }
            }));
        } else {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearInputs() {
        firstNameInput.setText("");
        lastNameInput.setText("");
        srCodeInput.setText("");
        passwordInput.setText("");
        confirmPasswordInput.setText("");
        institutionSpinner.setSelection(0);
    }

    public void openLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
