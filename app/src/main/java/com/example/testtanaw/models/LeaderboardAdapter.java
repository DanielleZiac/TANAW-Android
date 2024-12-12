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

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private final List<LeaderboardItem> leaderboardList;

    public LeaderboardAdapter(List<LeaderboardItem> leaderboardList) {
        this.leaderboardList = leaderboardList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_leaderboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LeaderboardItem item = leaderboardList.get(position);
        holder.rank.setText(String.valueOf(position + 1));
        holder.collegeName.setText(item.getUserName());
        holder.collegeLogo.setImageResource(item.getLogo());
        holder.score.setText(String.valueOf(item.getScore()));
    }

    @Override
    public int getItemCount() {
        return leaderboardList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView rank, collegeName, score;
        ImageView collegeLogo;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            rank = itemView.findViewById(R.id.rank);
            collegeName = itemView.findViewById(R.id.college);
            score = itemView.findViewById(R.id.score);
            collegeLogo = itemView.findViewById(R.id.collegeLogo);
        }
    }
}
