package com.example.testtanaw;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        // Find and set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Disable default title and set the toolbar title to be centered
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false); // Disable default title
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // Enable back button
        }

        // Find the TextView for the toolbar title
        TextView titleTextView = toolbar.findViewById(R.id.toolbar_title);
        // You can set the title text dynamically if needed, e.g., titleTextView.setText("About Us");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle back button click
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
