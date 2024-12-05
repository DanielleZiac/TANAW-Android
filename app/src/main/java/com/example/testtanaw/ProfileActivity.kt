package com.example.testtanaw

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.appcompat.widget.Toolbar
import com.example.testtanaw.models.UserParcelable
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userData: UserParcelable? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("userData", UserParcelable::class.java)
        } else {
            @Suppress("DEPRECATION") // Suppress warning for deprecated method
            intent.getParcelableExtra("userData")
        }

        if (userData != null) {
            Log.d("xxxxxx", "$userData")

            val toolbar: Toolbar = findViewById(R.id.toolbar)
            setSupportActionBar(toolbar)

            // Set the toolbar title to be centered
            supportActionBar?.setDisplayShowTitleEnabled(false) // Disable default title
            val titleTextView = toolbar.findViewById<TextView>(R.id.toolbar_title)

            // Enable the back button in the toolbar
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.title = "Profile Settings" // Optional: Set a custom title


            val avatarImageView = findViewById<ShapeableImageView>(R.id.roundedImageView)
            val srCodeTextView = findViewById<TextView>(R.id.srCode)
            val nameTextView = findViewById<TextView>(R.id.name)
            val institutionTextView = findViewById<TextView>(R.id.institution)
            val collegeTextView = findViewById<TextView>(R.id.college)
            val currentEmailTextView = findViewById<TextView>(R.id.currentEmail)

            Picasso
                .get()
                .load(userData.avatarUrl)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.baseline_cached_24)
                .error(R.drawable.baseline_error_outline_24)
                .into(avatarImageView)
            srCodeTextView.text = userData.srCode
            nameTextView.text = "${userData.firstName} ${userData.lastName}"
            institutionTextView.text = userData.institution
            collegeTextView.text = userData.department
            currentEmailTextView.text = userData.email
        }

        // Set up the OnClickListener for the "Edit Avatar" button
        val editAvatarButton = findViewById<Button>(R.id.editAvatar)
        editAvatarButton.setOnClickListener {
            // Navigate to AvatarActivity
            val intent = Intent(this, AvatarActivity::class.java)
            startActivity(intent)
        }
    }

    // Handle back button click
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}