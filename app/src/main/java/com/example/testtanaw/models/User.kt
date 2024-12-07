package com.example.testtanaw.models;

import android.os.Parcel;
import android.os.Parcelable;

public class UserParcelable implements Parcelable {
    private String userId;
    private String email;
    private String srCode;
    private String firstName;
    private String lastName;
    private String institution;
    private String institutionLogo;
    private String campus;
    private String department;
    private String avatarUrl;

    public UserParcelable(String userId, String email, String srCode, String firstName,
                         String lastName, String institution, String institutionLogo,
                         String campus, String department, String avatarUrl) {
        this.userId = userId;
        this.email = email;
        this.srCode = srCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.institution = institution;
        this.institutionLogo = institutionLogo;
        this.campus = campus;
        this.department = department;
        this.avatarUrl = avatarUrl;
    }

    protected UserParcelable(Parcel in) {
        userId = in.readString();
        email = in.readString();
        srCode = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        institution = in.readString();
        institutionLogo = in.readString();
        campus = in.readString();
        department = in.readString();
        avatarUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(email);
        dest.writeString(srCode);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(institution);
        dest.writeString(institutionLogo);
        dest.writeString(campus);
        dest.writeString(department);
        dest.writeString(avatarUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserParcelable> CREATOR = new Creator<UserParcelable>() {
        @Override
        public UserParcelable createFromParcel(Parcel in) {
            return new UserParcelable(in);
        }

        @Override
        public UserParcelable[] newArray(int size) {
            return new UserParcelable[size];
        }
    };

    // Getters and setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getSrCode() { return srCode; }
    public void setSrCode(String srCode) { this.srCode = srCode; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getInstitution() { return institution; }
    public void setInstitution(String institution) { this.institution = institution; }
    
    public String getInstitutionLogo() { return institutionLogo; }
    public void setInstitutionLogo(String institutionLogo) { this.institutionLogo = institutionLogo; }
    
    public String getCampus() { return campus; }
    public void setCampus(String campus) { this.campus = campus; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
}

