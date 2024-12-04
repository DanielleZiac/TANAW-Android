package com.example.tanaw

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
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
import kotlinx.serialization.json.Json


class Maps : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    private val FINE_PERMISSION_CODE: Int = 1
    lateinit var curLocation: Location
    var sdgPhotoList: List<SdgPhoto> = emptyList()
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var mClusterManager: ClusterManager<ClusterMarker>? = null
    private var mClusterManagerRenderer: ClusterManagerRenderer? = null
    private val mClusterMarkers = ArrayList<ClusterMarker>()
    private var institutions: HashMap<String, Int> = mutableMapOf<String, Int>() as HashMap<String, Int>
    val predefinedColors = listOf(Color.BLACK, Color.DKGRAY, Color.GRAY, Color.LTGRAY, Color.WHITE, Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA)

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

        val extras = intent.extras
        Log.d("tag", "extras: ${extras.toString()}")

        if (extras != null && extras.containsKey("sdgPhoto")) {
            val sdgPhotoJson = extras.getString("sdgPhoto")
            if (sdgPhotoJson != null) {
                sdgPhotoList = Json.decodeFromString(sdgPhotoJson)
                Log.d("tag", "MAPS " + sdgPhotoList)
            }
        }


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation();
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

            if (sdgPhotoList != null && sdgPhotoList.size > 0) {
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


            // add cur loc
//            val myClusterMarker: ClusterMarker = ClusterMarker()
//            mClusterManager.addItem()
//            mClusterMarkers.add()
            mClusterManager!!.cluster()

            Log.d("tag", "cluster markers: ${mClusterMarkers.size.toString()}, ${mClusterManager?.algorithm?.items?.size.toString()}")

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