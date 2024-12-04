package com.example.testtanaw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton


class HomeFragment : Fragment() {

    private lateinit var fabToggleMenu: FloatingActionButton
    private lateinit var floatingMenu: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        fabToggleMenu = view.findViewById(R.id.fabToggleMenu)
        floatingMenu = view.findViewById(R.id.floatingMenu)

        val sdgHome: ImageView = view.findViewById(R.id.sdg_home)
        val institutions: ImageView = view.findViewById(R.id.institutions)
        val hallOfFame: ImageView = view.findViewById(R.id.hallOfFame)
        val leaderboard: ImageView = view.findViewById(R.id.leaderboard)

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

        return view
    }
}