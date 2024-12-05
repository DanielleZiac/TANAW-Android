// SDGDetailActivity.kt
package com.example.testtanaw

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.testtanaw.data.PhotoChallenges.PHOTO_CHALLENGES
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SdgMapActivity : AppCompatActivity(), OnMapReadyCallback {
    private val FINE_PERMISSION_CODE = 1
    private lateinit var mGoogleMap: GoogleMap
    private lateinit var photoChallengeText: TextView
    private val challenges = PHOTO_CHALLENGES // Import your photo challenges
    private var currentChallengeIndex = 0
    private var sdgNumber: Int = 1 // Default SDG

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sdg_map)

        // Setup Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Enable back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Handle back button click
        toolbar.setNavigationOnClickListener {
            onBackPressed()  // Go back when the back button is pressed
        }

        // Set the SDG title dynamically
        supportActionBar?.setDisplayShowTitleEnabled(false) // Disable default title
        val toolbarTitle: TextView = findViewById(R.id.toolbar_title)
//        val sdgNumber = intent.getIntExtra("SDG_NUMBER", 0)
        toolbarTitle.text = "SDG Number: $sdgNumber" // Set SDG title in the toolbar

        // Retrieve the SDG title passed from the previous activity
        val sdgTitle = intent.getStringExtra("SDG_TITLE")

        // Set the SDG title to a TextView
        val titleTextView = findViewById<TextView>(R.id.sdgTitleTextView)
        titleTextView.text = sdgTitle

        // Initialize the map
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        photoChallengeText = findViewById(R.id.photoChallengeText)
        val fabArrow: FloatingActionButton = findViewById(R.id.fab_arrow)
        val fabPlus: FloatingActionButton = findViewById(R.id.fab_plus)

        // Retrieve the SDG number from the intent
        sdgNumber = intent.getIntExtra("SDG_NUMBER", 1)

        // Display the first challenge for the SDG
        updatePhotoChallenge()

        // Handle arrow button click to change challenges
        fabArrow.setOnClickListener {
            changePhotoChallenge()
        }

        // Handle the Plus FAB click
        fabPlus.setOnClickListener {
            Toast.makeText(this, "Plus Button Clicked!", Toast.LENGTH_SHORT).show()
        }

        val fabFilter: FloatingActionButton = findViewById(R.id.fab_filter)

        fabFilter.setOnClickListener { view ->
            // Show PopupMenu
            showPopupMenu(view)
        }
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

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap

        // Check location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), FINE_PERMISSION_CODE)
            return
        }

        // Enable location and set a marker
        mGoogleMap.isMyLocationEnabled = true
        val sdgLocation = LatLng(14.5995, 120.9842) // Example coordinates
        mGoogleMap.addMarker(com.google.android.gms.maps.model.MarkerOptions().position(sdgLocation).title("SDG Location"))
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sdgLocation, 12f))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == FINE_PERMISSION_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onMapReady(mGoogleMap)
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updatePhotoChallenge() {
        val sdgChallenges = challenges[sdgNumber] ?: emptyList()
        if (sdgChallenges.isNotEmpty()) {
            photoChallengeText.text = sdgChallenges[currentChallengeIndex]
        } else {
            photoChallengeText.text = "No challenges available for this SDG."
        }
    }

    private fun changePhotoChallenge() {
        val sdgChallenges = challenges[sdgNumber] ?: emptyList()
        if (sdgChallenges.isNotEmpty()) {
            currentChallengeIndex = (currentChallengeIndex + 1) % sdgChallenges.size
            updatePhotoChallenge()
        }
    }

    private fun showPopupMenu(view: android.view.View) {
        val popupMenu = PopupMenu(this, view)
        val inflater: MenuInflater = popupMenu.menuInflater
        inflater.inflate(R.menu.filter_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.filter_today -> {
                    Toast.makeText(this, "Today selected", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.filter_yesterday -> {
                    Toast.makeText(this, "Yesterday selected", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.filter_last_week -> {
                    Toast.makeText(this, "Last Week selected", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.filter_last_month -> {
                    Toast.makeText(this, "Last Month selected", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }
}
