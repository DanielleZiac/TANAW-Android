package com.example.testtanaw.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testtanaw.R;
import com.example.testtanaw.models.Sdg;
import java.util.List;
import android.util.Log;

public class ExploreSDGAdapter extends RecyclerView.Adapter<ExploreSDGAdapter.ViewHolder> {

    private List<Sdg> sdgList;

    // Constructor to accept the list of Sdg objects
    public ExploreSDGAdapter(List<Sdg> sdgList) {
        this.sdgList = sdgList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sdg_goal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Sdg sdg = sdgList.get(position);

        // Null check for the views to avoid NullPointerException
        if (holder.title != null && sdg.getTitle() != null) {
            holder.title.setText(sdg.getTitle());
        } else {
            Log.e("ExploreSDGAdapter", "Title or TextView is null at position: " + position);
        }

        if (holder.description != null && sdg.getDescription() != null) {
            holder.description.setText(sdg.getDescription());
        } else {
            Log.e("ExploreSDGAdapter", "Description or TextView is null at position: " + position);
        }

        if (holder.image != null && sdg.getImageResId() != 0) {
            holder.image.setImageResource(sdg.getImageResId());
        } else {
            Log.e("ExploreSDGAdapter", "Image resource is missing or invalid at position: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return sdgList != null ? sdgList.size() : 0;
    }

    // ViewHolder class that holds references to the TextViews and ImageView
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.sdg_title); // Ensure the TextView id matches the one in XML
            description = itemView.findViewById(R.id.sdg_description); // Same here
            image = itemView.findViewById(R.id.sdg_image); // And here for the ImageView
        }
    }
}
