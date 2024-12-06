package com.example.testtanaw.util

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.maps.android.clustering.ClusterManager
import android.location.Location
import android.util.Log
import android.widget.Toast
import com.example.testtanaw.R
import com.example.testtanaw.models.ClusterMarker


class Maps : AppCompatActivity() {
    private val FINE_PERMISSION_CODE: Int = 1
    lateinit var curLocation: Location
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var mClusterManager: ClusterManager<ClusterMarker>? = null
    private var mClusterManagerRenderer: ClusterManagerRenderer? = null
    private val mClusterMarkers = ArrayList<ClusterMarker>()
    private lateinit var mGoogleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_maps)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation();

//        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragemnt) as SupportMapFragment
//        mapFragment.getMapAsync(this)


    }

    private fun getLastLocation() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            // Request permissions if they haven't been granted yet
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                FINE_PERMISSION_CODE
            )
            return
        }

        // Now that permissions are granted, get the last known location
        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener { location: Location? ->
            if (location != null) {
                curLocation = location

                Log.d(
                    "tag",
                    curLocation.longitude.toString() + " " + curLocation.latitude.toString()
                )

            } else {
                Log.d("tag", "NO LOCATION")
            }
        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}