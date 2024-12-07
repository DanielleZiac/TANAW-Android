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
import androidx.transition.TransitionInflater;
import com.example.testtanaw.R;
import com.example.testtanaw.util.ExploreSDGAdapter;
import com.example.testtanaw.util.SDG;
import com.example.testtanaw.util.UNImagesAdapter;

import java.util.Arrays;
import java.util.List;

public class ExploreFragment extends Fragment {

    private UNImagesAdapter unImagesAdapter;
    private ExploreSDGAdapter sdgAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                            @Nullable ViewGroup container,
                            @Nullable Bundle savedInstanceState) {
        // Set up the shared element transition
        setSharedElementEnterTransition(TransitionInflater.from(requireContext())
                .inflateTransition(android.R.transition.move));

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up RecyclerView for United Nations images
        RecyclerView recyclerView = view.findViewById(R.id.un_images_recycler_view);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        unImagesAdapter = new UNImagesAdapter(this);  // Pass the fragment to the adapter
        recyclerView.setAdapter(unImagesAdapter);

        // Set up SDG RecyclerView
        RecyclerView sdgRecyclerView = view.findViewById(R.id.sdg_recycler_view);
        sdgRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<SDG> sdgGoals = Arrays.asList(
            new SDG(
                "Goal 1: No Poverty",
                "End poverty in all its forms everywhere.",
                R.drawable.sdglink_1
            ),
            new SDG(
                "Goal 2: Zero Hunger",
                "End hunger and achieve food security.",
                R.drawable.sdglink_2
            ),
            new SDG(
                "Goal 3: Good Health and Well-Being",
                "Ensure healthy lives for all.",
                R.drawable.sdglink_3
            ),
            new SDG(
                "Goal 4: Quality Education",
                "Ensure inclusive and equitable quality education.",
                R.drawable.sdglink_4
            ),
            new SDG(
                "Goal 5: Gender Equality",
                "Achieve gender equality and empower all women and girls.",
                R.drawable.sdglink_5
            ),
            new SDG(
                "Goal 6: Clean Water and Sanitation",
                "Ensure availability and sustainable management of water.",
                R.drawable.sdglink_6
            ),
            new SDG(
                "Goal 7: Affordable and Clean Energy",
                "Ensure access to affordable and clean energy.",
                R.drawable.sdglink_7
            ),
            new SDG(
                "Goal 8: Decent Work and Economic Growth",
                "Promote inclusive and sustainable economic growth.",
                R.drawable.sdglink_8
            ),
            new SDG(
                "Goal 9: Industry, Innovation, and Infrastructure",
                "Build resilient infrastructure.",
                R.drawable.sdglink_9
            ),
            new SDG(
                "Goal 10: Reduced Inequalities",
                "Reduce inequalities within and among countries.",
                R.drawable.sdglink_10
            ),
            new SDG(
                "Goal 11: Sustainable Cities and Communities",
                "Make cities and human settlements inclusive.",
                R.drawable.sdglink_11
            ),
            new SDG(
                "Goal 12: Responsible Consumption and Production",
                "Ensure sustainable consumption and production patterns.",
                R.drawable.sdglink_12
            ),
            new SDG(
                "Goal 13: Climate Action",
                "Take urgent action to combat climate change.",
                R.drawable.sdglink_13
            ),
            new SDG(
                "Goal 14: Life Below Water",
                "Conserve and sustainably use the oceans.",
                R.drawable.sdglink_14
            ),
            new SDG(
                "Goal 15: Life on Land",
                "Protect, restore, and promote sustainable use of ecosystems.",
                R.drawable.sdglink_15
            ),
            new SDG(
                "Goal 16: Peace, Justice, and Strong Institutions",
                "Promote peaceful societies for sustainable development.",
                R.drawable.sdglink_16
            ),
            new SDG(
                "Goal 17: Partnerships for the Goals",
                "Strengthen the means of implementation and revitalize the global partnership for sustainable development.",
                R.drawable.sdglink_17
            )
        );

        sdgAdapter = new ExploreSDGAdapter(sdgGoals);
        sdgRecyclerView.setAdapter(sdgAdapter);

        // Adjust the height of the SDG RecyclerView dynamically based on the number of items
        adjustSdgRecyclerViewHeight(sdgRecyclerView);
    }

    // Method to show the dialog when an image is clicked
    public void showImageDialog(int imageResource, String articleText) {
        ImageArticleDialogFragment dialogFragment = ImageArticleDialogFragment.newInstance(imageResource, articleText);
        dialogFragment.show(getChildFragmentManager(), "ImageArticleDialog");
    }

    private void adjustSdgRecyclerViewHeight(RecyclerView recyclerView) {
        recyclerView.post(() -> {
            int itemCount = recyclerView.getAdapter() != null ? recyclerView.getAdapter().getItemCount() : 0;
            int itemHeight = getResources().getDimensionPixelSize(R.dimen.item_height);
            int totalHeight = itemHeight * itemCount;
            ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
            layoutParams.height = totalHeight;
            recyclerView.setLayoutParams(layoutParams);
        });
    }
}
