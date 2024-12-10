package com.example.testtanaw.models;

public class User {
    private String id;
    private String email;
    private String srCode;
    private String firstName;
    private String lastName;
    private String institutionId;

    public User() {}

    public User(String id, String email, String srCode, String firstName,
                String lastName, String institutionId) {
        this.id = id;
        this.email = email;
        this.srCode = srCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.institutionId = institutionId;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSrCode() { return srCode; }
    public void setSrCode(String srCode) { this.srCode = srCode; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getInstitutionId() { return institutionId; }
    public void setInstitutionId(String institutionId) { this.institutionId = institutionId; }
    public static String getUserAvatarPath(String id) {
        return String.format(Constants.BUCKET_USERS_AVATAR_PATH_FORMAT, id);
    }
}
