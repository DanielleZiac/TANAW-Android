package com.example.testtanaw.models;

import android.view.LayoutInflater;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testtanaw.R;
import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder> {
    private final List<LeaderboardItem> leaderboardList;

    public LeaderboardAdapter(List<LeaderboardItem> leaderboardList) {
        this.leaderboardList = leaderboardList;
    }

    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_leaderboard, parent, false);
        return new LeaderboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder holder, int position) {
        LeaderboardItem item = leaderboardList.get(position);
        holder.rank.setText(String.valueOf(position + 1)); // Display rank starting from 1
        holder.score.setText(String.valueOf(item.getScore()));
        holder.collegeLogo.setImageResource(item.getLogo()); // Bind the logo image
    }

    @Override
    public int getItemCount() {
        return leaderboardList.size();
    }

    public static class LeaderboardViewHolder extends RecyclerView.ViewHolder {
        public final TextView rank;
        public final ImageView collegeLogo;
        public final TextView score;

        public LeaderboardViewHolder(View itemView) {
            super(itemView);
            rank = itemView.findViewById(R.id.rank);
            collegeLogo = itemView.findViewById(R.id.collegeLogo); // College logo
            score = itemView.findViewById(R.id.score);
        }
    }
}

