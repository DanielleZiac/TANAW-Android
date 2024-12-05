// SDGDetailActivity.kt
package com.example.testtanaw

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity

class SdgMapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sdg_map)

        // Setup Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Enable back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Handle back button click (optional)
        toolbar.setNavigationOnClickListener {
            onBackPressed()  // Go back when the back button is pressed
        }

        // Set the SDG title dynamically
        supportActionBar?.setDisplayShowTitleEnabled(false) // Disable default title
        val toolbarTitle: TextView = findViewById(R.id.toolbar_title)
        val sdgNumber = intent.getIntExtra("SDG_NUMBER", 0)
        toolbarTitle.text = "SDG Number: $sdgNumber" // Set SDG title in the toolbar

        // Retrieve the SDG title passed from the previous activity
        val sdgTitle = intent.getStringExtra("SDG_TITLE")

        // Set the SDG title to a TextView
        val titleTextView = findViewById<TextView>(R.id.sdgTitleTextView)
        titleTextView.text = sdgTitle
    }
    // Handle the back button press to navigate back
    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed() // Navigate back when back button is pressed
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
