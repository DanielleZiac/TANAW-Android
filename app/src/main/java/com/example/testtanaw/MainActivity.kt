package com.example.testtanaw

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import android.view.View
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var fabToggleMenu: FloatingActionButton
    private lateinit var floatingMenu: View

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

        // Initialize components
        fabToggleMenu = findViewById(R.id.fabToggleMenu)
        floatingMenu = findViewById(R.id.floatingMenu)

        val sdgHome: ImageView = findViewById(R.id.sdg_home)
        val institutions: ImageView = findViewById(R.id.institutions)
        val hallOfFame: ImageView = findViewById(R.id.hallOfFame)
        val leaderboard: ImageView = findViewById(R.id.leaderboard)

        // Toggle dropdown menu visibility
        fabToggleMenu.setOnClickListener {
            floatingMenu.visibility = if (floatingMenu.visibility == View.VISIBLE) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        // Set click listeners for dropdown menu items
        sdgHome.setOnClickListener {
            fabToggleMenu.setImageResource(R.drawable.baseline_loyalty_24)
            floatingMenu.visibility = View.GONE
        }

        institutions.setOnClickListener {
            fabToggleMenu.setImageResource(R.drawable.baseline_home_work_24)
            floatingMenu.visibility = View.GONE
        }

        hallOfFame.setOnClickListener {
            fabToggleMenu.setImageResource(R.drawable.baseline_local_fire_department_24)
            floatingMenu.visibility = View.GONE
        }

        leaderboard.setOnClickListener {
            fabToggleMenu.setImageResource(R.drawable.baseline_leaderboard_24)
            floatingMenu.visibility = View.GONE
        }

        // Bottom Navigation setup
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomAppBar)

        // Set HomeFragment as the default fragment when the app starts
        if (savedInstanceState == null) {
            loadFragment(HomeFragment()) // Load HomeFragment on app start
            bottomNavigationView.selectedItemId = R.id.nav_home // Set nav_home as selected
        }

        // Set up listener for bottom navigation item selection
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    // Load Home Fragment or activity
                    loadFragment(HomeFragment())
                    true
                }
                R.id.nav_search -> {
                    // Load Search Fragment or activity
                    loadFragment(ExploreFragment())
                    true
                }
                R.id.nav_explore -> {
                    // Load Explore Fragment or activity
                    loadFragment(ExploreFragment())
                    true
                }
                R.id.nav_inbox -> {
                    // Load Inbox Fragment or activity
                    loadFragment(InboxFragment())
                    true
                }
                R.id.nav_stickers -> {
                    // Load Stickers Fragment or activity
                    loadFragment(StickersFragment())
                    true
                }
                else -> false
            }
        }
    }

    // Method to load fragment
    private fun loadFragment(fragment: androidx.fragment.app.Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)  // Optional: allows back navigation
        transaction.commit()
    }

//    // Method to toggle dropdown menu visibility
//    private fun toggleDropdownMenu() {
//        val floatingMenu = findViewById<LinearLayout>(R.id.floatingMenu)
//        if (floatingMenu.visibility == View.GONE) {
//            floatingMenu.visibility = View.VISIBLE
//        } else {
//            floatingMenu.visibility = View.GONE
//        }
//    }
}
