package com.example.testtanaw.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testtanaw.R;

import java.util.List;

public class ExploreSDGAdapter extends RecyclerView.Adapter<ExploreSDGAdapter.ViewHolder> {
    private final List<SDG> sdgGoals;

    public static class SDG {
        private final String title;
        private final String description;
        private final int imageRes;

        public SDG(String title, String description, int imageRes) {
            this.title = title;
            this.description = description;
            this.imageRes = imageRes;
        }
    }

    public ExploreSDGAdapter(List<SDG> sdgGoals) {
        this.sdgGoals = sdgGoals;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_sdg_goal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SDG sdg = sdgGoals.get(position);
        holder.titleTextView.setText(sdg.title);
        holder.descriptionTextView.setText(sdg.description);
        holder.imageView.setImageResource(sdg.imageRes);
    }

    @Override
    public int getItemCount() {
        return sdgGoals.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView titleTextView;
        final TextView descriptionTextView;
        final ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            titleTextView = view.findViewById(R.id.sdg_title);
            descriptionTextView = view.findViewById(R.id.sdg_description);
            imageView = view.findViewById(R.id.sdg_image);
        }
    }
}
