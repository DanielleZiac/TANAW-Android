// SDGDetailActivity.kt
package com.example.testtanaw

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class SdgMapActivity : AppCompatActivity(), OnMapReadyCallback {
    private val FINE_PERMISSION_CODE = 1
    private lateinit var mGoogleMap: GoogleMap

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
        val sdgNumber = intent.getIntExtra("SDG_NUMBER", 0)
        toolbarTitle.text = "SDG Number: $sdgNumber" // Set SDG title in the toolbar

        // Retrieve the SDG title passed from the previous activity
        val sdgTitle = intent.getStringExtra("SDG_TITLE")

        // Set the SDG title to a TextView
        val titleTextView = findViewById<TextView>(R.id.sdgTitleTextView)
        titleTextView.text = sdgTitle

        // Initialize the map
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
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
}
