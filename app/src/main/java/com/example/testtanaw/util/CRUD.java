package com.example.testtanaw.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.SetOptions;
import com.example.testtanaw.R;

import java.io.ByteArrayOutputStream;

public class CRUD {
    private static final String defaultAvatarUrl = "https://srxhcymqociarjinmkpp.supabase.co/storage/v1/object/public/avatars/default/d8598e52-34b0-4f96-b7a7-e0ff3df7b2cb";
    private static final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public static class SdgPhoto {
        private String userSdgId;
        private String userId;
        private String sdgNumber;
        private String url;
        private String caption;
        private String createdDate;
        private String institutionId;
        private String phototChall;
        private String institution;
        private String campus;
        private String institutionLogo;
        private double lat;
        private double lng;
        private String avatarUrl;

        // Constructor
        public SdgPhoto(String userSdgId, String userId, String sdgNumber, String url,
                        String caption, String createdDate, String institutionId,
                        String phototChall, String institution, String campus,
                        String institutionLogo, double lat, double lng, String avatarUrl) {
            this.userSdgId = userSdgId;
            this.userId = userId;
            this.sdgNumber = sdgNumber;
            this.url = url;
            this.caption = caption;
            this.createdDate = createdDate;
            this.institutionId = institutionId;
            this.phototChall = phototChall;
            this.institution = institution;
            this.campus = campus;
            this.institutionLogo = institutionLogo;
            this.lat = lat;
            this.lng = lng;
            this.avatarUrl = avatarUrl;
        }

        // Getters
        public String getUserSdgId() { return userSdgId; }
        public String getUserId() { return userId; }
        public String getSdgNumber() { return sdgNumber; }
        public String getUrl() { return url; }
        public String getCaption() { return caption; }
        public String getCreatedDate() { return createdDate; }
        public String getInstitutionId() { return institutionId; }
        public String getPhototChall() { return phototChall; }
        public String getInstitution() { return institution; }
        public String getCampus() { return campus; }
        public String getInstitutionLogo() { return institutionLogo; }
        public double getLat() { return lat; }
        public double getLng() { return lng; }
        public String getAvatarUrl() { return avatarUrl; }

        // Firebase save method
        public void saveToFirebase() {
            DocumentReference docRef = firestore.collection("SdgPhotos").document(userSdgId);
            docRef.set(this, SetOptions.merge())
                    .addOnSuccessListener(aVoid -> Log.d("CRUD", "SdgPhoto successfully written!"))
                    .addOnFailureListener(e -> Log.w("CRUD", "Error writing SdgPhoto", e));
        }
    }

    public static class UserAvatarData {
        private String avatarId;
        private String userId;
        private String avatarUrl;
        private String bg;
        private String eye;
        private String sex;
        private String shirtStyle;
        private String smile;
        private String eyewear;

        // Constructor
        public UserAvatarData(String avatarId, String userId, String avatarUrl,
                              String bg, String eye, String sex, String shirtStyle,
                              String smile, String eyewear) {
            this.avatarId = avatarId;
            this.userId = userId;
            this.avatarUrl = avatarUrl;
            this.bg = bg;
            this.eye = eye;
            this.sex = sex;
            this.shirtStyle = shirtStyle;
            this.smile = smile;
            this.eyewear = eyewear;
        }

        // Getters
        public String getAvatarId() { return avatarId; }
        public String getUserId() { return userId; }
        public String getAvatarUrl() { return avatarUrl; }
        public String getBg() { return bg; }
        public String getEye() { return eye; }
        public String getSex() { return sex; }
        public String getShirtStyle() { return shirtStyle; }
        public String getSmile() { return smile; }
        public String getEyewear() { return eyewear; }

        // Firebase save method
        public void saveToFirebase() {
            DocumentReference docRef = firestore.collection("UserAvatars").document(avatarId);
            docRef.set(this, SetOptions.merge())
                    .addOnSuccessListener(aVoid -> Log.d("CRUD", "UserAvatarData successfully written!"))
                    .addOnFailureListener(e -> Log.w("CRUD", "Error writing UserAvatarData", e));
        }
    }

    public static class LeaderboardSchool {
        private String institutionId;
        private String institution;
        private String campus;
        private String departmentLogo;
        private String department;
        private int count;

        // Constructor
        public LeaderboardSchool(String institutionId, String institution,
                                 String campus, String departmentLogo,
                                 String department, int count) {
            this.institutionId = institutionId;
            this.institution = institution;
            this.campus = campus;
            this.departmentLogo = departmentLogo;
            this.department = department;
            this.count = count;
        }

        // Getters
        public String getInstitutionId() { return institutionId; }
        public String getInstitution() { return institution; }
        public String getCampus() { return campus; }
        public String getDepartmentLogo() { return departmentLogo; }
        public String getDepartment() { return department; }
        public int getCount() { return count; }

        // Firebase save method
        public void saveToFirebase() {
            DocumentReference docRef = firestore.collection("LeaderboardSchools").document(institutionId);
            docRef.set(this, SetOptions.merge())
                    .addOnSuccessListener(aVoid -> Log.d("CRUD", "LeaderboardSchool successfully written!"))
                    .addOnFailureListener(e -> Log.w("CRUD", "Error writing LeaderboardSchool", e));
        }
    }

    // Additional methods for image combination
    public static byte[] combineAvatars(Bitmap bgBitmap, Bitmap sexBitmap, Bitmap shirtStyleBitmap, Bitmap eyeBitmap, Bitmap smileBitmap, Bitmap eyewearBitmap) {
        if (bgBitmap == null || sexBitmap == null || shirtStyleBitmap == null || eyeBitmap == null || smileBitmap == null) {
            throw new IllegalArgumentException("Invalid background or image");
        }

        Bitmap combinedBitmap = Bitmap.createBitmap(bgBitmap.getWidth(), bgBitmap.getHeight(), bgBitmap.getConfig());
        Canvas canvas = new Canvas(combinedBitmap);
        canvas.drawBitmap(bgBitmap, 0, 0, null);
        canvas.drawBitmap(sexBitmap, 100, 80, null);
        canvas.drawBitmap(shirtStyleBitmap, 100, 80, null);
        canvas.drawBitmap(eyeBitmap, 120, 100, null);
        if (eyewearBitmap != null) {
            canvas.drawBitmap(eyewearBitmap, 130, 90, null);
        }
        canvas.drawBitmap(smileBitmap, 140, 120, null);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        combinedBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
