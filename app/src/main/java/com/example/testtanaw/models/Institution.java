package com.example.testtanaw.models;

public class Institution {
    private String id;
    private String institutionId;
    private String institution;
    private String campus;
    private String emailExtension;
    private int logoResId;

    public Institution() {

    }

    public Institution(String id, String institutionId, String institution, String campus, String emailExtension, int logoResId) {
        this.id = id;
        this.institutionId = institutionId;
        this.institution = institution;
        this.campus = campus;
        this.emailExtension = emailExtension;
        this.logoResId = logoResId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInstitutionId() {
        return institutionId;
    }

    public String getInstitution() {
        return institution;
    }

    public String getCampus() {
        return campus;
    }

    public String getEmailExtension() {
        return emailExtension;
    }

    public int getLogoResId() {
        return logoResId;
    }
}
