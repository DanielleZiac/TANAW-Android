package com.example.testtanaw

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testtanaw.models.LeaderboardAdapter
import com.example.testtanaw.models.LeaderboardItem
import com.example.testtanaw.models.UserParcelable
import com.example.testtanaw.util.CRUD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LeaderboardFragment(val userData: UserParcelable) : Fragment() {

    private lateinit var leaderboardRecyclerView: RecyclerView
    private lateinit var leaderboardAdapter: LeaderboardAdapter
    private lateinit var data: List<CRUD.LeaderboardSchool>

    private val crud = CRUD()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_leaderboard, container, false)

        // Initialize RecyclerView
        leaderboardRecyclerView = rootView.findViewById(R.id.leaderboardRecyclerView)
        leaderboardRecyclerView.layoutManager = LinearLayoutManager(context)
        leaderboardRecyclerView.setHasFixedSize(true)

        // Launch a coroutine on the main thread
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // Fetch the data asynchronously
                val data = withContext(Dispatchers.IO) {
                    crud.getLeaderboardsSchools(userData.institution) // Perform background operation
                }

                // Check if data is null or empty
                if (data != null && data.isNotEmpty()) {
                    // Set the Adapter after data is fetched
                    leaderboardAdapter = LeaderboardAdapter(data)
                    leaderboardRecyclerView.adapter = leaderboardAdapter
                } else {
                    // Handle the case where data is null or empty
                    Log.e("xxxxxx", "No data found or data is empty.")
                }

            } catch (e: Exception) {
                // Handle any exceptions during the async task
                Log.e("xxxxxx", "Error fetching leaderboards: ${e.message}")
                e.printStackTrace()
            }
        }

        // Return the root view after setting up the RecyclerView and starting the async task
        return rootView
    }
}