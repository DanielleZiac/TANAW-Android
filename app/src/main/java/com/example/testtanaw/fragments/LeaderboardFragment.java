package com.example.testtanaw.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testtanaw.R;
import com.example.testtanaw.models.LeaderboardAdapter;
import com.example.testtanaw.models.LeaderboardItem;

import java.util.Arrays;
import java.util.List;

public class LeaderboardFragment extends Fragment {

    private RecyclerView leaderboardRecyclerView;
    private LeaderboardAdapter leaderboardAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        // Initialize RecyclerView
        leaderboardRecyclerView = rootView.findViewById(R.id.leaderboardRecyclerView);
        leaderboardRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        leaderboardRecyclerView.setHasFixedSize(true);

        // Hardcoded data
        List<LeaderboardItem> leaderboardList = Arrays.asList(
                new LeaderboardItem("College of Informatics and Computing Sciences", 15, R.drawable.cics),
                new LeaderboardItem("College of Engineering", 12, R.drawable.coe),
                new LeaderboardItem("College of Architecture and Fine Arts", 10, R.drawable.cafad)
        );

        // Set the Adapter with hardcoded data
        leaderboardAdapter = new LeaderboardAdapter(leaderboardList);
        leaderboardRecyclerView.setAdapter(leaderboardAdapter);

        return rootView;
    }
}
