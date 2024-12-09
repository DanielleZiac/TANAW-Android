package com.example.testtanaw.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testtanaw.R;
import com.example.testtanaw.util.CRUD;
import com.squareup.picasso.Picasso;
import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder> {
    private final List<CRUD.LeaderboardSchool> leaderboardList;

    public LeaderboardAdapter(List<CRUD.LeaderboardSchool> leaderboardList) {
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
        CRUD.LeaderboardSchool item = leaderboardList.get(position);
        holder.rank.setText(String.valueOf(position + 1)); // Display rank starting from 1
        holder.score.setText(String.valueOf(item.getCount()));

        Picasso.get()
               .load(item.getDepartmentLogo())
               .resize(1000, 1000)
               .centerInside()
               .placeholder(R.drawable.loading)
               .into(holder.collegeLogo);
    }

    @Override
    public int getItemCount() {
        return leaderboardList.size();
    }

    static class LeaderboardViewHolder extends RecyclerView.ViewHolder {
        final TextView rank;
        final ImageView collegeLogo;
        final TextView score;

        LeaderboardViewHolder(View itemView) {
            super(itemView);
            rank = itemView.findViewById(R.id.rank);
            collegeLogo = itemView.findViewById(R.id.collegeLogo);
            score = itemView.findViewById(R.id.score);
        }
    }
}
