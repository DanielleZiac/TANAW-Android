package com.example.testtanaw

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.widget.Toolbar
import com.example.testtanaw.models.UserParcelable
import com.example.testtanaw.util.DB


class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var navigationView: NavigationView

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

        val userData: UserParcelable? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("userData", UserParcelable::class.java)
        } else {
            @Suppress("DEPRECATION") // Suppress warning for deprecated method
            intent.getParcelableExtra("userData")
        }

        if (userData != null) {
            Log.d("xxxxxx", "email: ${userData.email}, avatarUrl: ${userData.avatarUrl}")
        }


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

        // Handle item clicks in the navigation menu
        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_settings -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_about -> {
                    // Handle Abous Us click
                }
                R.id.nav_contact -> {
                    // Handle Contacts click
                }
                R.id.nav_feedback -> {
                    // Handle Feedback click
                }
                R.id.nav_help -> {
                    // Handle Help Center click
                }
                R.id.nav_terms -> {
                    // Handle Terms and Conditions click
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

        val mapBtn = findViewById<View>(R.id.mapBtn)
        mapBtn.setOnClickListener(
            View.OnClickListener {
                Log.d("tag", "heree map")
                val i = Intent(
                    this@MainActivity,
                    Maps::class.java
                )
                startActivity(i)
            }
        )
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