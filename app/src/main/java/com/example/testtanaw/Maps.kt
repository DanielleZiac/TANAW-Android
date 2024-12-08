package com.example.testtanaw

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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
import android.location.Location
import android.util.Log
import android.widget.Toast
import com.example.testtanaw.models.ClusterMarker
import com.example.testtanaw.util.ClusterManagerRenderer


class Maps : AppCompatActivity(), OnMapReadyCallback {
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
                mClusterManagerRenderer = ClusterManagerRenderer(applicationContext, mGoogleMap, mClusterManager)
                mClusterManager!!.setRenderer(mClusterManagerRenderer)
            }

            val avatar: Int = R.drawable.avatar

            val newClusterMarker: ClusterMarker = ClusterMarker(
                LatLng(curLocation.latitude, curLocation.longitude),
                "name",
                "caption",
                1.0F,
                avatar
            )
            mClusterManager!!.addItem(newClusterMarker);
            mClusterMarkers.add(newClusterMarker);

            mClusterManager!!.cluster()
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(curLocation.latitude, curLocation.longitude)));
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
}