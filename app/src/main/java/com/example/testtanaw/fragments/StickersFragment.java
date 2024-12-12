package com.example.testtanaw.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testtanaw.AvatarActivity;
import com.example.testtanaw.R;
import com.example.testtanaw.models.Sdg;
import com.example.testtanaw.util.StickerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StickersFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stickers, container, false);

        // Initialize the first RecyclerView (Unlocked)
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        List<Sdg> sdgList = generateSdgList();
        StickerAdapter adapter = new StickerAdapter(sdgList);
        recyclerView.setAdapter(adapter);

        // Adjust height of the first RecyclerView
        adjustRecyclerViewHeight(recyclerView, sdgList.size());

        // Initialize the second RecyclerView (Locked)
        RecyclerView recyclerView2 = view.findViewById(R.id.recyclerView2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(requireContext()));

        ArrayList<Sdg> placeholderList = new ArrayList<>(); // Placeholder variable
        placeholderList.addAll(sdgList); // Copy data from sdgList
        StickerAdapter adapter2 = new StickerAdapter(placeholderList);
        recyclerView2.setAdapter(adapter2);

        // Adjust height of the second RecyclerView
        adjustRecyclerViewHeight(recyclerView2, placeholderList.size());

        // Set up the OnClickListener for the "Edit Avatar" button
        Button editAvatarButton = view.findViewById(R.id.editAvatar);
        editAvatarButton.setOnClickListener(v -> {
            // Navigate to AvatarActivity
            Intent intent = new Intent(requireContext(), AvatarActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private List<Sdg> generateSdgList() {
        List<String> sdgTitles = Arrays.asList(
                "No Poverty", "Zero Hunger", "Good Health and Well-Being",
                "Quality Education", "Gender Equality", "Clean Water and Sanitation",
                "Affordable and Clean Energy", "Decent Work and Economic Growth",
                "Industry, Innovation and Infrastructure", "Reduced Inequalities",
                "Sustainable Cities and Communities", "Responsible Consumption and Production",
                "Climate Action", "Life Below Water", "Life on Land",
                "Peace, Justice and Strong Institutions", "Partnerships for the Goals"
        );

        List<Sdg> sdgList = new ArrayList<>();
        for (int i = 1; i <= 17; i++) {
            int imageResId = getResources().getIdentifier("background_sdg" + i, "drawable",
                    requireContext().getPackageName());
            sdgList.add(new Sdg(imageResId, "SDG " + i + ": " + sdgTitles.get(i - 1), "This is SDG " + i)); // Added third parameter
        }

        return sdgList;
    }

    private void adjustRecyclerViewHeight(RecyclerView recyclerView, int itemCount) {
        recyclerView.post(() -> {
            int itemHeight = getResources().getDimensionPixelSize(R.dimen.item_height);
            int totalHeight = itemHeight * itemCount;
            ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
            layoutParams.height = totalHeight;
            recyclerView.setLayoutParams(layoutParams);
        });
    }
}