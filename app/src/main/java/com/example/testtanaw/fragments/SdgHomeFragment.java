package com.example.testtanaw.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testtanaw.R;
import com.example.testtanaw.models.GridSpacingItemDecoration;
import com.example.testtanaw.models.SDGItem;
import com.example.testtanaw.util.SDGAdapter;
import java.util.ArrayList;
import java.util.List;

public class SdgHomeFragment extends Fragment {

    private long lastToastTime = 0; // Track the last toast time

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sdg_home, container, false);

        // Create the list of SDG items
        List<SDGItem> sdgList = new ArrayList<>();
        sdgList.add(new SDGItem("No Poverty", R.drawable.sdg_1));
        sdgList.add(new SDGItem("Goal 2", R.drawable.sdg_2));
        sdgList.add(new SDGItem("Goal 3", R.drawable.sdg_3));
        sdgList.add(new SDGItem("Goal 4", R.drawable.sdg_4));
        sdgList.add(new SDGItem("Goal 5", R.drawable.sdg_5));
        sdgList.add(new SDGItem("Goal 6", R.drawable.sdg_6));
        sdgList.add(new SDGItem("Goal 7", R.drawable.sdg_7));
        sdgList.add(new SDGItem("Goal 8", R.drawable.sdg_8));
        sdgList.add(new SDGItem("Goal 9", R.drawable.sdg_9));
        sdgList.add(new SDGItem("Goal 10", R.drawable.sdg_10));
        sdgList.add(new SDGItem("Goal 11", R.drawable.sdg_11));
        sdgList.add(new SDGItem("Goal 12", R.drawable.sdg_12));
        sdgList.add(new SDGItem("Goal 13", R.drawable.sdg_13));
        sdgList.add(new SDGItem("Goal 14", R.drawable.sdg_14));
        sdgList.add(new SDGItem("Goal 15", R.drawable.sdg_15));
        sdgList.add(new SDGItem("Goal 16", R.drawable.sdg_16));
        sdgList.add(new SDGItem("Goal 17", R.drawable.sdg_17));

        // Initialize the adapter with a click listener
        SDGAdapter adapter = new SDGAdapter(sdgList, sdgItem -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastToastTime > 2000) { // Show Toast if 2 seconds have passed
                Toast.makeText(getContext(), "Clicked SDG: " + sdgItem.getTitle(), Toast.LENGTH_SHORT).show();
                lastToastTime = currentTime;
            }
        });

        // Set up the RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.sdgRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3)); // 3 items per row
        recyclerView.setAdapter(adapter);

        // Add item decoration for spacing
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_spacing); // Define this dimension in res/values/dimens.xml
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, spacingInPixels, true));

        return view;
    }
}
