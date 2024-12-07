package com.example.testtanaw.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testtanaw.R;
import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder> {
    @NonNull private final List<LeaderboardItem> leaderboardList;

    public LeaderboardAdapter(@NonNull List<LeaderboardItem> leaderboardList) {
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
        holder.rank.setText(String.valueOf(position + 1));
        holder.userName.setText(item.getUserName());
        holder.score.setText(String.valueOf(item.getScore()));
        holder.collegeLogo.setImageResource(item.getLogo());
    }

    @Override
    public int getItemCount() {
        return leaderboardList.size();
    }

    public static class LeaderboardViewHolder extends RecyclerView.ViewHolder {
        @NonNull private final TextView rank;
        @NonNull private final TextView userName;
        @NonNull private final ImageView collegeLogo;
        @NonNull private final TextView score;

        public LeaderboardViewHolder(@NonNull View itemView) {
            super(itemView);
            rank = itemView.findViewById(R.id.rank);
            userName = itemView.findViewById(R.id.userName);
            collegeLogo = itemView.findViewById(R.id.collegeLogo);
            score = itemView.findViewById(R.id.score);
        }
    }
}

