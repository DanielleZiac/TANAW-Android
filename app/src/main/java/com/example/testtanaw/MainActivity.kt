package com.example.testtanaw

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.example.testtanaw.fragments.ExploreFragment
import com.example.testtanaw.fragments.FeedbackModalFragment
import com.example.testtanaw.fragments.GalleryFragment
import com.example.testtanaw.fragments.HomeFragment
import com.example.testtanaw.fragments.InboxFragment
import com.example.testtanaw.fragments.StickersFragment
import com.example.testtanaw.models.UserParcelable
import com.example.testtanaw.util.CRUD
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var navigationView: NavigationView
    private lateinit var userId: String

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var curLocation: Location
    private val FINE_PERMISSION_CODE: Int = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Apply edge-to-edge window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // user data
        val userData: UserParcelable? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("userData", UserParcelable::class.java)
        } else {
            @Suppress("DEPRECATION") // Suppress warning for deprecated method
            intent.getParcelableExtra("userData")
        }

        if (userData != null) {
            userId = userData.userId

            // loc
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            getLastLocation();

            // Initialize views
            drawerLayout = findViewById(R.id.drawer_layout)
            toolbar = findViewById(R.id.toolbar)
            navigationView = findViewById(R.id.navigationView)

            // Set the toolbar as the action bar
            setSupportActionBar(toolbar)

            // Disable the toolbar title display
            supportActionBar?.setDisplayShowTitleEnabled(false)

            // Handle hamburger icon click
            val hamburgerMenu: ImageButton = findViewById(R.id.hamburgerMenu)
            hamburgerMenu.setOnClickListener {
                drawerLayout.openDrawer(navigationView)  // Open the navigation drawer
            }

            // Handle profile icon click
            val profileIcon: ImageButton = findViewById(R.id.profileIcon)
            profileIcon.setOnClickListener {
                val intent = Intent(this, ProfileActivity::class.java)
                intent.putExtra("userData", userData)
                startActivity(intent)
            }

            // Handle item clicks in the navigation menu
            navigationView.setNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_settings -> {
                        val intent = Intent(this, ProfileActivity::class.java)
                        intent.putExtra("userData", userData)
                        startActivity(intent)
                    }

                    R.id.nav_about -> {
                    val intent = Intent(this, AboutUsActivity::class.java)
                    startActivity(intent)
                }

                    R.id.nav_contact -> {
                        val intent = Intent(this, ContactActivity::class.java)
                        startActivity(intent)
                    }

                    R.id.nav_feedback -> {
                        // Show the feedback dialog
                        val feedbackDialog = FeedbackModalFragment()
                        feedbackDialog.show(supportFragmentManager, "FeedbackDialog")
                    }

                    R.id.nav_help -> {
                        // Handle Help Center click
                    }

                    R.id.nav_terms -> {
                        val intent = Intent(this, TermsActivity::class.java)
                        startActivity(intent)
                    }

                    R.id.nav_logout -> {
                        // Handle Logout click
                    }

                    else -> {
                        return@setNavigationItemSelectedListener false
                    }
                }
                drawerLayout.closeDrawer(navigationView)  // Close the drawer after item selection
                true
            }


            // Bottom Navigation setup
            val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomAppBar)

            // Set HomeFragment as the default fragment when the app starts
            if (savedInstanceState == null) {
                loadFragment(HomeFragment()) // Load HomeFragment on app start
                bottomNavigationView.selectedItemId = R.id.nav_home // Set nav_home as selected
            }

            // Set up listener for bottom navigation item selection
            bottomNavigationView.setOnItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.nav_home -> loadFragment(HomeFragment())
                    R.id.nav_gallery -> loadFragment(GalleryFragment())
                    R.id.nav_explore -> loadFragment(ExploreFragment())  // Merged identical cases
                    R.id.nav_inbox -> loadFragment(InboxFragment())
                    R.id.nav_stickers -> loadFragment(StickersFragment())
                    else -> false
                }
                true

            }

//            val mapBtn = findViewById<View>(R.id.mapBtn)
//            mapBtn.setOnClickListener(
//                View.OnClickListener {
//                    Log.d("tag", "heree map")
//                    val i = Intent(
//                        this@MainActivity,
//                        Maps::class.java
//                    )
//                    startActivity(i)
//                }
//            )
        }
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
                val crud = CRUD()
                crud.saveUserLastLocation(userId, curLocation.latitude, curLocation.longitude);
                Log.d(
                    "tag",
                    curLocation.longitude.toString() + " " + curLocation.latitude.toString()
                )
            } else {
                Log.d("tag", "NO LOCATION")
            }
        }
    }

    // Handle permission result in the Activity
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, get the last location
                getLastLocation()
            } else {
                // Permission denied
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle the back button for the drawer if it's open
        if (item.itemId == android.R.id.home) {
            drawerLayout.openDrawer(navigationView)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    // Method to load fragment
    private fun loadFragment(fragment: androidx.fragment.app.Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)  // Optional: allows back navigation
        transaction.commit()
    }
}