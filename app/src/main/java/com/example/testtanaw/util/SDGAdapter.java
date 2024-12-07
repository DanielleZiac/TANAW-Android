package com.example.testtanaw.util;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testtanaw.R;
import com.example.testtanaw.SdgMapActivity;
import com.example.testtanaw.models.SDGItem;
import java.util.List;

public class SDGAdapter extends RecyclerView.Adapter<SDGAdapter.SDGViewHolder> {
    private final List<SDGItem> sdgList;
    private final OnItemClickListener onItemClick;

    public interface OnItemClickListener {
        void onItemClick(SDGItem item);
    }

    public SDGAdapter(List<SDGItem> sdgList, OnItemClickListener onItemClick) {
        this.sdgList = sdgList;
        this.onItemClick = onItemClick;
    }

    public static class SDGViewHolder extends RecyclerView.ViewHolder {
        public final ImageView sdgIcon;
        public final TextView sdgNumber;

        public SDGViewHolder(@NonNull View view) {
            super(view);
            sdgIcon = view.findViewById(R.id.sdgIcon);
            sdgNumber = view.findViewById(R.id.sdgNumber);
        }
    }

    @NonNull
    @Override
    public SDGViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sdg, parent, false);
        return new SDGViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SDGViewHolder holder, int position) {
        SDGItem sdg = sdgList.get(position);

        holder.sdgIcon.setImageResource(sdg.getIconResId());
        holder.sdgNumber.setText("SDG " + (position + 1));

        // Apply alignment logic based on the index
        int marginTop;
        int marginBottom;
        int marginLeft;
        int marginRight;

        switch (position % 6) {
            case 1:
            case 4:
                // Justify-center
                marginTop = 50;
                marginBottom = -50;
                marginLeft = 0;
                marginRight = 0;
                break;
            case 2:
            case 3:
                // Justify-end
                marginTop = 100;
                marginBottom = -100;
                marginLeft = 0;
                marginRight = 0;
                break;
            default:
                // Justify-start
                marginTop = 0;
                marginBottom = 0;
                marginLeft = 0;
                marginRight = 0;
                break;
        }

        // Apply dynamic margins
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
        params.setMargins(marginLeft, marginTop, marginRight, marginBottom);
        holder.itemView.setLayoutParams(params);

        // Initially hide the title
        holder.sdgNumber.setVisibility(View.GONE);

        // Set the touch listener to detect press and release
        holder.itemView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // Show title when pressed
                    holder.sdgNumber.setVisibility(View.VISIBLE);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    // Hide title when released
                    holder.sdgNumber.setVisibility(View.GONE);
                    break;
            }

            // Detect click event and call performClick for accessibility
            if (event.getAction() == MotionEvent.ACTION_UP) {
                v.performClick(); // Call performClick to ensure proper accessibility behavior
            }

            // Return true to indicate that the touch event has been handled
            return true;
        });

        // Set onClickListener for SDG image to navigate to the new activity
        holder.itemView.setOnClickListener(v -> {
            // Create an Intent to open the SDGDetailActivity
            Intent intent = new Intent(holder.itemView.getContext(), SdgMapActivity.class);

            // Pass the SDG title (or other relevant information) to the new activity
            intent.putExtra("SDG_TITLE", sdg.getTitle());
            intent.putExtra("sdgNumber", position + 1);

            // Start the new activity
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return sdgList.size();
    }
}
