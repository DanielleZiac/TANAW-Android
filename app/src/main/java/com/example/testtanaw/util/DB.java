package com.example.testtanaw.util;

import android.content.Context;
import android.util.Log;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DB {
//    private final FirebaseAuth mAuth;
    private final FirebaseFirestore db;

    public DB() {
        FirebaseManager manager = FirebaseManager.getInstance();
//        this.mAuth = manager.getAuth();
        this.db = manager.getDb();
    }
    private static final String defaultAvatarUrl = "https://srxhcymqociarjinmkpp.supabase.co/storage/v1/object/public/avatars/default/d8598e52-34b0-4f96-b7a7-e0ff3df7b2cb";

    public static class Institution {
        private final String institution;
        private final String institutionId;
        private final String emailExtension;

        public Institution(String institution, String institutionId, String emailExtension) {
            this.institution = institution;
            this.institutionId = institutionId;
            this.emailExtension = emailExtension;
        }

        public String getInstitution() { return institution; }
        public String getInstitutionId() { return institutionId; }
        public String getEmailExtension() { return emailExtension; }
    }

    public static class UserData {
        private final String userId;
        private final String email;
        private final String srCode;
        private final String firstName;
        private final String lastName;
        private final HashMap<String, String> institutions;
        private final HashMap<String, String> departments;
        private final HashMap<String, String> avatars;

        public UserData(String userId, String email, String srCode, String firstName,
                        String lastName, HashMap<String, String> institutions,
                        HashMap<String, String> departments, HashMap<String, String> avatars) {
            this.userId = userId;
            this.email = email;
            this.srCode = srCode;
            this.firstName = firstName;
            this.lastName = lastName;
            this.institutions = institutions;
            this.departments = departments;
            this.avatars = avatars;
        }

        // Getters
        public String getUserId() { return userId; }
        public String getEmail() { return email; }
        public String getSrCode() { return srCode; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public HashMap<String, String> getInstitutions() { return institutions; }
        public HashMap<String, String> getDepartments() { return departments; }
        public HashMap<String, String> getAvatars() { return avatars; }
    }

    private CompletableFuture<String> getDefaultDepartment(String userId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Implementation using Java Supabase client
                return null; // Placeholder
            } catch (Exception e) {
                Log.d("xxxxxx", "ERROR GET DEFAULT DEPT: " + e.getMessage());
                return null;
            }
        });
    }

    public CompletableFuture<String> getUserId() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Implementation using Java Supabase client
                return null; // Placeholder
            } catch (Exception e) {
                Log.d("xxxxxx", "error getUser " + e.getMessage());
                return null;
            }
        });
    }

    public CompletableFuture<List<Institution>> getInstitutions(Context context, Spinner institutionSpinner) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Implementation using Java Supabase client
                return null; // Placeholder
            } catch (Exception e) {
                Toast.makeText(context, "Error fetching data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("xxxxxx", e.getMessage());
                return null;
            }
        });
    }
}
