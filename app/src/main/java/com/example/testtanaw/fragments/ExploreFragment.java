package com.example.testtanaw.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionInflater;
import com.example.testtanaw.util.ExploreSDGAdapter;
import com.example.testtanaw.R;
import com.example.testtanaw.models.Sdg;
import com.example.testtanaw.util.UNImagesAdapter;
import java.util.Arrays;
import java.util.List;

public class ExploreFragment extends Fragment {

    private UNImagesAdapter unImagesAdapter;
    private ExploreSDGAdapter sdgAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Set up the shared element transition
        setSharedElementEnterTransition(TransitionInflater.from(requireContext())
                .inflateTransition(android.R.transition.move));

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up RecyclerView for United Nations images
        RecyclerView recyclerView = view.findViewById(R.id.un_images_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        unImagesAdapter = new UNImagesAdapter(this);  // Pass the fragment to the adapter
        recyclerView.setAdapter(unImagesAdapter);

        // Set up SDG RecyclerView
        RecyclerView sdgRecyclerView = view.findViewById(R.id.sdg_recycler_view);
        sdgRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Sdg> sdgGoals = Arrays.asList(
                new Sdg(R.drawable.sdglink_1, "Goal 1: No Poverty", "End poverty in all its forms everywhere."),
                new Sdg(R.drawable.sdglink_2, "Goal 2: Zero Hunger", "End hunger and achieve food security."),
                new Sdg(R.drawable.sdglink_3, "Goal 3: Good Health and Well-Being", "Ensure healthy lives for all."),
                new Sdg(R.drawable.sdglink_4, "Goal 4: Quality Education", "Ensure inclusive and equitable quality education."),
                new Sdg(R.drawable.sdglink_5, "Goal 5: Gender Equality", "Achieve gender equality and empower all women and girls."),
                new Sdg(R.drawable.sdglink_6, "Goal 6: Clean Water and Sanitation", "Ensure availability and sustainable management of water."),
                new Sdg(R.drawable.sdglink_7, "Goal 7: Affordable and Clean Energy", "Ensure access to affordable and clean energy."),
                new Sdg(R.drawable.sdglink_8, "Goal 8: Decent Work and Economic Growth", "Promote inclusive and sustainable economic growth."),
                new Sdg(R.drawable.sdglink_9, "Goal 9: Industry, Innovation, and Infrastructure", "Build resilient infrastructure."),
                new Sdg(R.drawable.sdglink_10, "Goal 10: Reduced Inequalities", "Reduce inequalities within and among countries."),
                new Sdg(R.drawable.sdglink_11, "Goal 11: Sustainable Cities and Communities", "Make cities and human settlements inclusive."),
                new Sdg(R.drawable.sdglink_12, "Goal 12: Responsible Consumption and Production", "Ensure sustainable consumption and production patterns."),
                new Sdg(R.drawable.sdglink_13, "Goal 13: Climate Action", "Take urgent action to combat climate change."),
                new Sdg(R.drawable.sdglink_14, "Goal 14: Life Below Water", "Conserve and sustainably use the oceans."),
                new Sdg(R.drawable.sdglink_15, "Goal 15: Life on Land", "Protect, restore, and promote sustainable use of ecosystems."),
                new Sdg(R.drawable.sdglink_16, "Goal 16: Peace, Justice, and Strong Institutions", "Promote peaceful societies for sustainable development."),
                new Sdg(R.drawable.sdglink_17, "Goal 17: Partnerships for the Goals", "Strengthen the means of implementation and revitalize the global partnership for sustainable development.")
        );

        sdgAdapter = new ExploreSDGAdapter(sdgGoals);
        sdgRecyclerView.setAdapter(sdgAdapter);

        // Adjust the height of the SDG RecyclerView dynamically based on the number of items
        adjustSdgRecyclerViewHeight(sdgRecyclerView);
    }

    // Function to show the dialog when an image is clicked
    public void showImageDialog(int imageResource, String articleText) {
        ImageArticleDialogFragment dialogFragment = ImageArticleDialogFragment.newInstance(imageResource, articleText);
        dialogFragment.show(getChildFragmentManager(), "ImageArticleDialog");
    }

    private void adjustSdgRecyclerViewHeight(final RecyclerView recyclerView) {
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                int itemCount = recyclerView.getAdapter() != null ? recyclerView.getAdapter().getItemCount() : 0;
                int itemHeight = getResources().getDimensionPixelSize(R.dimen.item_height); // Define item height in dimens.xml
                int totalHeight = itemHeight * itemCount;
                ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
                layoutParams.height = totalHeight;
                recyclerView.setLayoutParams(layoutParams);
            }
        });
    }
}
