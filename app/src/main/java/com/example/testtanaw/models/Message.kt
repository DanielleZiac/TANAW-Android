package com.example.testtanaw.models

data class Message(
    val id: Int,
    val subject: String,
    val preview: String,
    val timestamp: String,
    val isRead: Boolean
)
