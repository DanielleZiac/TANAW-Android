package com.example.testtanaw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testtanaw.models.LeaderboardItem

class LeaderboardFragment : Fragment() {

    private lateinit var leaderboardRecyclerView: RecyclerView
    private lateinit var leaderboardAdapter: LeaderboardAdapter

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

        // Example data, in a real case this would come from a backend
        val leaderboardList = listOf(
            LeaderboardItem("User1", 50),
            LeaderboardItem("User2", 45),
            LeaderboardItem("User3", 40),

            )

        // Set the Adapter
        leaderboardAdapter = LeaderboardAdapter(leaderboardList)
        leaderboardRecyclerView.adapter = leaderboardAdapter

        return rootView
    }
}