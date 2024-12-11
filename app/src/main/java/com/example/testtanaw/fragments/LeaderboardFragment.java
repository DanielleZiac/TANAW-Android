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

        // Example data with college logos
        List<LeaderboardItem> leaderboardList = Arrays.asList(
                new LeaderboardItem("User1", 50, R.drawable.bsulogo),
                new LeaderboardItem("User2", 45, R.drawable.admulogo),
                new LeaderboardItem("User3", 40, R.drawable.dlsulogo)
        );

        // Set the Adapter
        leaderboardAdapter = new LeaderboardAdapter(leaderboardList);
        leaderboardRecyclerView.setAdapter(leaderboardAdapter);

        return rootView;
    }
}
