// SDGDetailActivity.kt
package com.example.testtanaw

import android.location.Location
import android.os.Bundle
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.PopupMenu
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.testtanaw.data.PhotoChallenges.PHOTO_CHALLENGES
import com.example.testtanaw.models.ClusterMarker
import com.example.testtanaw.util.CRUD
import com.example.testtanaw.util.ClusterManagerRenderer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
//        val titleTextView = findViewById<TextView>(R.id.sdgTitleTextView)
//        titleTextView.text = sdgTitle

        CoroutineScope(Dispatchers.Main).launch {
            sdgPhotoList = crud.getSdgPhoto(sdgNumber, null, null)
        }
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
