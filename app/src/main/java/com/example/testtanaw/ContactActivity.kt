package com.example.testtanaw

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ContactActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        // Set the Toolbar as the ActionBar
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.contact_toolbar)
        setSupportActionBar(toolbar)

        // Disable the default action bar title
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Optional: Handle any actions if needed, e.g., opening the links, etc.
    }
}
