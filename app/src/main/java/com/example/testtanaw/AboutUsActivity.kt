package com.example.testtanaw

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class AboutUsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Set the toolbar title to be centered
        supportActionBar?.setDisplayShowTitleEnabled(false) // Disable default title
        val titleTextView = toolbar.findViewById<TextView>(R.id.toolbar_title)

        // Enable the back button in the toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Additional setup can go here if needed.
    }
}