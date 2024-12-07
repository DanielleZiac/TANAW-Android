// SDGDetailActivity.kt
package com.example.testtanaw

import android.content.Intent
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
import com.example.testtanaw.util.DB
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
    val db = DB()
    lateinit var userId: String
    lateinit var curLocation: Location // pass location from map
    var sdgPhotoList: List<CRUD.SdgPhoto> = emptyList()

    private var mClusterManager: ClusterManager<ClusterMarker>? = null
    private var mClusterManagerRenderer: ClusterManagerRenderer? = null
    private val mClusterMarkers = ArrayList<ClusterMarker>()
    private lateinit var mGoogleMap: GoogleMap
    private lateinit var photoChallengeText: TextView
    private val challenges = PHOTO_CHALLENGES // Import your photo challenges
    private var currentChallengeIndex = 0
    private var sdgNumber: Int = 1 // Default SDG

    private var sdgNumberx: Int = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sdg_map)

//        Co

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


        sdgNumberx = intent.getIntExtra("sdgNumber", 0)
        toolbarTitle.text = "SDG: $sdgNumberx" // Set SDG title in the toolbar

        val institutionId = intent.getStringExtra("institutionId")

        Log.d("xxxxxx", "sdgNumber:  $sdgNumber")
        Log.d("xxxxxx", "institutionID:  $institutionId")


        CoroutineScope(Dispatchers.Main).launch {
            sdgPhotoList = crud.getSdgPhoto(sdgNumber, "today", null)
        }
        // Initialize the map
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        photoChallengeText = findViewById(R.id.photoChallengeText)
        val fabArrow: FloatingActionButton = findViewById(R.id.fab_arrow)
        val fabPlus: FloatingActionButton = findViewById(R.id.fab_plus)


        // Display the first challenge for the SDG
        updatePhotoChallenge()

        // Handle arrow button click to change challenges
        fabArrow.setOnClickListener {
            changePhotoChallenge()
        }

        // Handle the Plus FAB click
        fabPlus.setOnClickListener {
            Toast.makeText(this, "Plus Button Clicked!", Toast.LENGTH_SHORT).show()

            // Create an Intent to open CameraActivity
            intent.putExtra("photoChallenge", photoChallengeText.text)
            intent.putExtra("sdgNumber", sdgNumberx)
            val intent = Intent(this, CameraActivity::class.java)

//            sdgNumber = intent.getStringExtra("sdgNumber")
//            photoChallenge = intent.getStringExtra("photoChallenge")
//            lat = intent.getStringExtra("lat")?.toDouble()
//            long = intent.getStringExtra("long")?.toDouble()


            startActivity(intent)  // Start the activity
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
        Log.d("xxxxxxx", "PHOTO COUNT: ${sdgPhotoList.size}")
        mGoogleMap.clear()

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
        val newMarkerOptions = MarkerOptions()
            .position(LatLng(13.7864324, 121.0738981))
            .title("This is you")
            .snippet("your current location")

        // Add the new marker
        mGoogleMap.addMarker(newMarkerOptions)
        mClusterManager!!.cluster()

        val zoomLevel = 12.0f
        mGoogleMap.uiSettings.isZoomControlsEnabled = true
        mGoogleMap.uiSettings.isZoomGesturesEnabled = true
//        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(13.7864324, 121.0738981), zoomLevel));
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        addMapMarkers()
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(13.7864324, 121.0738981), 12.0f));
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
                R.id.filter_all -> {
                    CoroutineScope(Dispatchers.Main).launch {
                        sdgPhotoList = crud.getSdgPhoto(sdgNumber, "all", null)
                        addMapMarkers()
                    }
                    Toast.makeText(this@SdgMapActivity, "All selected", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.filter_today -> {
                    CoroutineScope(Dispatchers.Main).launch {
                        sdgPhotoList = crud.getSdgPhoto(sdgNumber, "today", null)
                        addMapMarkers()
                    }
                    Toast.makeText(this@SdgMapActivity, "Today selected", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.filter_yesterday -> {
                    CoroutineScope(Dispatchers.Main).launch {
                        sdgPhotoList = crud.getSdgPhoto(sdgNumber, "yesterday", null)
                        addMapMarkers()
                    }
                    Toast.makeText(this, "Yesterday selected", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.filter_last_week -> {
                    CoroutineScope(Dispatchers.Main).launch {
                        sdgPhotoList = crud.getSdgPhoto(sdgNumber, "last week", null)
                        addMapMarkers()
                    }
                    Toast.makeText(this, "Last Week selected", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.filter_last_month -> {
                    CoroutineScope(Dispatchers.Main).launch {
                        sdgPhotoList = crud.getSdgPhoto(sdgNumber, "last month", null)
                        addMapMarkers()
                    }
                    Toast.makeText(this, "Last Month selected", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }
}