package com.example.testtanaw;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.testtanaw.models.UserParcelable;
import com.example.testtanaw.util.DB;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LoginActivity extends AppCompatActivity {
    private final DB db = new DB();
    private EditText srCodeInput;
    private EditText passwordInput;
    private Spinner institutionSpinner;
    private List<DB.Institution> institutions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        srCodeInput = findViewById(R.id.sr_code_input);
        passwordInput = findViewById(R.id.password_input);
        institutionSpinner = findViewById(R.id.institution_spinner);

        CompletableFuture.supplyAsync(() -> {
            try {
                return db.getInstitutions(this, institutionSpinner).get();
            } catch (Exception e) {
                Log.e("LoginActivity", "Error getting institutions", e);
                return null;
            }
        }).thenAccept(result -> {
            if (result != null) {
                institutions = result;
            }
        });
    }

    public void onLoginClicked(View view) {
        String srCode = srCodeInput.getText().toString();
        String password = passwordInput.getText().toString();
        String selectedInstitution = institutions.get(institutionSpinner.getSelectedItemPosition()).getInstitution();
        String emailExtension = institutions.get(institutionSpinner.getSelectedItemPosition()).getEmailExtension();

        if (institutionSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select a valid institution.", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("LoginActivity", selectedInstitution + ", " + emailExtension);

        if (!srCode.isEmpty() && !password.isEmpty()) {
            String email = srCode + emailExtension;
            Log.d("LoginActivity", "email: " + email);

            CompletableFuture.supplyAsync(() -> {
                try {
                    return db.login(email, password, this).get();
                } catch (Exception e) {
                    Log.e("LoginActivity", "Error during login", e);
                    return null;
                }
            }).thenAccept(userId -> {
                if (userId != null) {
                    CompletableFuture.supplyAsync(() -> {
                        try {
                            return db.getUserDataByUserId(userId).get();
                        } catch (Exception e) {
                            Log.e("LoginActivity", "Error getting user data", e);
                            return null;
                        }
                    }).thenAccept(userDbData -> {
                        if (userDbData != null) {
                            if (userDbData.getAvatars().get("avatar_url").isEmpty() ||
                                userDbData.getDepartments().get("department").isEmpty()) {
                                // Handle avatar creation redirect
                            } else {
                                UserParcelable userData = new UserParcelable(
                                    userDbData.getUserId(),
                                    userDbData.getEmail(),
                                    userDbData.getSrCode(),
                                    userDbData.getFirstName(),
                                    userDbData.getLastName(),
                                    userDbData.getInstitutions().get("institution"),
                                    userDbData.getInstitutions().get("institution_logo"),
                                    userDbData.getInstitutions().get("campus"),
                                    userDbData.getDepartments().get("department"),
                                    userDbData.getAvatars().get("avatar_url")
                                );

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("userData", userData);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            });
        } else {
            Toast.makeText(this, "Please fill in both fields.", Toast.LENGTH_SHORT).show();
        }
    }

    public void openSignUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}
