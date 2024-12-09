package com.example.testtanaw.util;

import com.example.testtanaw.R;

public class CRUD {
    private static final String defaultAvatarUrl = "https://srxhcymqociarjinmkpp.supabase.co/storage/v1/object/public/avatars/default/d8598e52-34b0-4f96-b7a7-e0ff3df7b2cb";

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
    }

    // Additional methods would go here, converted from Kotlin
    // These would need to be adapted to use Java's async patterns
    // rather than Kotlin coroutines
}
