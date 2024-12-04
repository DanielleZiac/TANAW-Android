package com.example.tanaw

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tanaw.models.ClusterMarker
import com.example.tanaw.util.ClusterManagerRenderer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.clustering.ClusterManager


class Maps : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
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

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Request permissions if they haven't been granted yet
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), FINE_PERMISSION_CODE)
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

                val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragemnt) as SupportMapFragment
                mapFragment.getMapAsync(this)
            } else {
                Log.d("tag", "NO LOCATION")
            }
        }

    }

    private fun addMapMarkers() {
        if (mGoogleMap != null) {

            if (mClusterManager == null) {
                mClusterManager = ClusterManager<ClusterMarker>(applicationContext, mGoogleMap)
            }

            if (mClusterManagerRenderer == null) {
                mClusterManagerRenderer = ClusterManagerRenderer(applicationContext, mGoogleMap, mClusterManager, mClusterMarkers)
                mClusterManager!!.setRenderer(mClusterManagerRenderer)
            }

            val coordinates = listOf(
                LatLng(14.6042, 120.9822), // Manila
                LatLng(10.3157, 123.8854), // Cebu
                LatLng(7.1907, 125.4553),  // Davao
                LatLng(9.7263, 123.8507),  // Tagbilaran
                LatLng(16.4023, 120.5960)  // Baguio
            )

            // Loop through the coordinates and create a ClusterMarker for each
            coordinates.forEach { coordinate ->
                val newClusterMarker: ClusterMarker = ClusterMarker(
                    coordinate,
                    "name",
                    "caption",
                    1.0F
                )
                mClusterManager!!.addItem(newClusterMarker);
                mClusterMarkers.add(newClusterMarker);
            }
            mClusterManager!!.cluster()

            val zoomLevel = 12.0f
            mGoogleMap.uiSettings.isZoomControlsEnabled = true
            mGoogleMap.uiSettings.isZoomGesturesEnabled = true
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(curLocation.latitude, curLocation.longitude), zoomLevel));
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        addMapMarkers()
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

    override fun onInfoWindowClick(p0: Marker) {
        TODO("Not yet implemented")
    }
}