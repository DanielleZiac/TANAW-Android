package com.example.tanaw

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment


class Maps : AppCompatActivity(), OnMapReadyCallback {
    private  var mGoogleMap: GoogleMap? = null
    private var mLocationPermissionGranted: Boolean = false

    val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("tag", "Location permission granted")
            mLocationPermissionGranted = true
//            enableMap()
        } else {
            Log.d("tag", "permission denied")
            mLocationPermissionGranted = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_maps)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val curLoc = findViewById<TextView>(R.id.curLoc)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragemnt) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if (checkServices() && mLocationPermissionGranted) {
            Log.d("tag", "Setting up map")
        }

    }

    override fun onMapReady(gooleMap: GoogleMap) {
        mGoogleMap = gooleMap
    }

    private fun isServicesOk(): Boolean {
        Log.d("tag", "isServicesOk: checking google services version")

        val available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)

        if (available === ConnectionResult.SUCCESS) {
            Log.d("tag", "isServicesOK: Google Play Services is working");
            return true
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError((available))) {
            Log.d("tag", "isServicesOK: an error occured but we can fix it");
            val dialog: Dialog? = GoogleApiAvailability.getInstance().getErrorDialog(
                this@Maps, available, 9001
            )
            dialog?.show()
        } else {
            Log.d("tag", "You can't make map requests")
        }
        return false
    }

    // Request location permission
    private fun requestLocPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    fun isMapEnabled(): Boolean {
        val manager: LocationManager  = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!manager.isProviderEnabled( LocationManager.GPS_PROVIDER )) {
            Log.d("tag", "GPS is disabled, asking user to enable it.")
            // send request
            requestLocPermission()
            Log.d("tag", "asdasdsadbcuxbciad"+manager.isProviderEnabled( LocationManager.GPS_PROVIDER));
            return false
        }
        Log.d("tag", "GPS is enabled.")
        return true
    }

    private fun checkServices(): Boolean {
        return isServicesOk() && isMapEnabled()
    }
}