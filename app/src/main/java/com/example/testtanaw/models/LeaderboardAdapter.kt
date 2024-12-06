package com.example.testtanaw.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testtanaw.R

class LeaderboardAdapter(private val leaderboardList: List<LeaderboardItem>) :
    RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_leaderboard, parent, false)
        return LeaderboardViewHolder(view)
    }

    override fun onBindViewHolder(holder: LeaderboardViewHolder, position: Int) {
        val leaderboardItem = leaderboardList[position]
        holder.bind(leaderboardItem, position + 1)
    }

    override fun getItemCount(): Int {
        return leaderboardList.size
    }

    class LeaderboardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val rankTextView: TextView = itemView.findViewById(R.id.rank)
        private val userNameTextView: TextView = itemView.findViewById(R.id.userName)
        private val scoreTextView: TextView = itemView.findViewById(R.id.score)

        fun bind(leaderboardItem: LeaderboardItem, rank: Int) {
            rankTextView.text = rank.toString()
            userNameTextView.text = leaderboardItem.userName
            scoreTextView.text = leaderboardItem.score.toString()
        }
    }
}
