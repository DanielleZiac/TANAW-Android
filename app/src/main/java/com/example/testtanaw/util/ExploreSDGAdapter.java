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
    
    public static class SDG {
        private final String title;
        private final String description;
        private final int imageRes;

        public SDG(String title, String description, int imageRes) {
            this.title = title;
            this.description = description;
            this.imageRes = imageRes;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public int getImageRes() {
            return imageRes;
        }
    }

    private final List<SDG> sdgGoals;

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
        holder.titleTextView.setText(sdg.getTitle());
        holder.descriptionTextView.setText(sdg.getDescription());
        holder.imageView.setImageResource(sdg.getImageRes());
    }

    @Override
    public int getItemCount() {
        return sdgGoals.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView titleTextView;
        public final TextView descriptionTextView;
        public final ImageView imageView;

        public ViewHolder(@NonNull View view) {
            super(view);
            titleTextView = view.findViewById(R.id.sdg_title);
            descriptionTextView = view.findViewById(R.id.sdg_description);
            imageView = view.findViewById(R.id.sdg_image);
        }
    }
}
