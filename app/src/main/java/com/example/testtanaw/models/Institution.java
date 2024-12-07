package com.example.testtanaw.models;

public class Institution {
    private final String institution;  // Name of the institution
    private final String campus;       // Description or full name
    private final int logoResId;

    public Institution(String institution, String campus, int logoResId) {
        this.institution = institution;
        this.campus = campus;
        this.logoResId = logoResId;
    }

    public String getInstitution() {
        return institution;
    }

    public String getCampus() {
        return campus;
    }

    public int getLogoResId() {
        return logoResId;
    }
}


