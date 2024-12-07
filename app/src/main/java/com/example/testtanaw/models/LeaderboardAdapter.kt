package com.example.testtanaw.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testtanaw.R
import com.example.testtanaw.util.CRUD
import com.squareup.picasso.Picasso

class LeaderboardAdapter(private val leaderboardList: List<CRUD.LeaderboardSchool>) :
    RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_leaderboard, parent, false)
        return LeaderboardViewHolder(view)
    }

    override fun onBindViewHolder(holder: LeaderboardViewHolder, position: Int) {
        val item = leaderboardList[position]
        holder.rank.text = (position + 1).toString() // Display rank starting from 1
        holder.score.text = item.count.toString()

        // di nagloload??? anong imageview to
        Picasso.get().load(item.departmentLogo).resize(1000, 1000).centerInside().placeholder(R.drawable.loading).into(holder.collegeLogo)
//        holder.collegeLogo.setImageResource(item.departmentLogo) // Bind the logo image
    }

    override fun getItemCount(): Int = leaderboardList.size

    class LeaderboardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rank: TextView = itemView.findViewById(R.id.rank)
        val collegeLogo: ImageView = itemView.findViewById(R.id.collegeLogo) // College logo
        val score: TextView = itemView.findViewById(R.id.score)
    }
}

