// SDGDetailActivity.kt
package com.example.testtanaw

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.example.testtanaw.models.ClusterMarker
import com.example.testtanaw.util.CRUD
import com.example.testtanaw.util.ClusterManagerRenderer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SdgMapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    val crud = CRUD()
    private val FINE_PERMISSION_CODE: Int = 1
    lateinit var curLocation: Location
    var sdgPhotoList: List<CRUD.SdgPhoto> = emptyList()
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var mClusterManager: ClusterManager<ClusterMarker>? = null
    private var mClusterManagerRenderer: ClusterManagerRenderer? = null
    private val mClusterMarkers = ArrayList<ClusterMarker>()
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

        CoroutineScope(Dispatchers.Main).launch {
            sdgPhotoList = crud.getSdgPhoto(sdgNumber, "today", null)

            val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragemnt) as SupportMapFragment
            mapFragment.getMapAsync(this@SdgMapActivity)
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


    private fun addMapMarkers() {
        if (mClusterManager == null) {
            mClusterManager = ClusterManager<ClusterMarker>(applicationContext, mGoogleMap)
        }
        if (mClusterManagerRenderer == null) {
            mClusterManagerRenderer = ClusterManagerRenderer(applicationContext, mGoogleMap, mClusterManager, mClusterMarkers)
            mClusterManager!!.setRenderer(mClusterManagerRenderer)
        }

        try {

            if (sdgPhotoList.size > 0) {
                Log.d("tag", "sdgPhotoList: ${sdgPhotoList.toString()}")
                sdgPhotoList.forEach { sdgPhoto ->
                    Log.d("tag", sdgPhoto.toString())

                    val newClusterMarker: ClusterMarker = ClusterMarker(
                        userSdgId = sdgPhoto.userSdgId,
                        userId = sdgPhoto.userId,
                        sdgNumber = sdgPhoto.sdgNumber,
                        url = sdgPhoto.url,
                        caption = sdgPhoto.caption,
                        createdDate = sdgPhoto.createdDate,
                        institutionId = sdgPhoto.institutionId,
                        phototChall = sdgPhoto.phototChall,
                        institution = sdgPhoto.institution,
                        campus = sdgPhoto.campus,
                        institutionLogo = sdgPhoto.institutionLogo,
                        lat = sdgPhoto.lat,
                        long = sdgPhoto.long,
                        avatarUrl = sdgPhoto.avatarUrl
                    )

                    mClusterManager!!.addItem(newClusterMarker)
                    mClusterMarkers.add(newClusterMarker)
                }
            }
        } catch (e: Exception) {
            Log.d("xxxxxx", "${e.message}")
        }

//        // add cur loc
//        val newMarkerOptions = MarkerOptions()
//            .position(LatLng(curLocation.latitude, curLocation.longitude))
//            .title("This is you")
//            .snippet("????")

        // Add the new marker
//        mGoogleMap.addMarker(newMarkerOptions)
        mClusterManager!!.cluster()

        val zoomLevel = 12.0f
        mGoogleMap.uiSettings.isZoomControlsEnabled = true
        mGoogleMap.uiSettings.isZoomGesturesEnabled = true
//        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(curLocation.latitude, curLocation.longitude), zoomLevel));
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        addMapMarkers()
    }


    override fun onInfoWindowClick(p0: Marker) {
        TODO("Not yet implemented")
    }
}
