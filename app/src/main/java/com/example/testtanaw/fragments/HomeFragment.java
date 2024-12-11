package com.example.testtanaw.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.testtanaw.LeaderboardFragment;
import com.example.testtanaw.InstitutionFragment;
import com.example.testtanaw.R;
import com.example.testtanaw.fragments.SdgHomeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment {

    private FloatingActionButton fabToggleMenu;
    private View floatingMenu;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        fabToggleMenu = view.findViewById(R.id.fabToggleMenu);
        floatingMenu = view.findViewById(R.id.floatingMenu);

        // Set initial fragment
        replaceFragment(new SdgHomeFragment());

        ImageView sdgHome = view.findViewById(R.id.sdg_home);
        ImageView institutions = view.findViewById(R.id.institutions);
        //ImageView hallOfFame = view.findViewById(R.id.hallOfFame);
        ImageView leaderboard = view.findViewById(R.id.leaderboard);

        // Toggle dropdown menu visibility
        fabToggleMenu.setOnClickListener(v -> {
            floatingMenu.setVisibility(
                    floatingMenu.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE
            );
        });

        // Set click listeners for dropdown menu items
        sdgHome.setOnClickListener(v -> {
            fabToggleMenu.setImageResource(R.drawable.baseline_loyalty_24);
            replaceFragment(new SdgHomeFragment());
            floatingMenu.setVisibility(View.GONE);
        });

        institutions.setOnClickListener(v -> {
            fabToggleMenu.setImageResource(R.drawable.baseline_home_work_24);
            replaceFragment(new InstitutionFragment());
            floatingMenu.setVisibility(View.GONE);
        });

        leaderboard.setOnClickListener(v -> {
            fabToggleMenu.setImageResource(R.drawable.baseline_leaderboard_24);
            replaceFragment(new LeaderboardFragment());
            floatingMenu.setVisibility(View.GONE);
        });

        return view;
    }

    private void replaceFragment(Fragment fragment) {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}
