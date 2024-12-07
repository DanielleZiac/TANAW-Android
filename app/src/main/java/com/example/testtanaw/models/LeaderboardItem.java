package com.example.testtanaw.models;

public class LeaderboardItem {
    private final String userName;
    private final int score;
    private final int logo;

    public LeaderboardItem(String userName, int score, int logo) {
        this.userName = userName;
        this.score = score;
        this.logo = logo;
    }

    public String getUserName() {
        return userName;
    }

    public int getScore() {
        return score;
    }

    public int getLogo() {
        return logo;
    }
}
