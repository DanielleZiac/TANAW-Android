package com.example.testtanaw.models;

public class Message {
    private final int id;
    private final String subject;
    private final String preview;
    private final String timestamp;
    private final boolean isRead;

    public Message(int id, String subject, String preview, String timestamp, boolean isRead) {
        this.id = id;
        this.subject = subject;
        this.preview = preview;
        this.timestamp = timestamp;
        this.isRead = isRead;
    }

    public int getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public String getPreview() {
        return preview;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public boolean isRead() {
        return isRead;
    }
}
