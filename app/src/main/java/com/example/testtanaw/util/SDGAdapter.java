package com.example.testtanaw.util;

import android.content.Context;
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
    private final OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(SDGItem sdgItem);
    }

    public SDGAdapter(List<SDGItem> sdgList, OnItemClickListener onItemClickListener) {
        this.sdgList = sdgList;
        this.onItemClickListener = onItemClickListener;
    }

    public static class SDGViewHolder extends RecyclerView.ViewHolder {
        public ImageView sdgIcon;
        public TextView sdgNumber;

        public SDGViewHolder(@NonNull View itemView) {
            super(itemView);
            sdgIcon = itemView.findViewById(R.id.sdgIcon);
            sdgNumber = itemView.findViewById(R.id.sdgNumber);
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
        SDGItem sdgItem = sdgList.get(position);

        holder.sdgIcon.setImageResource(sdgItem.getIconResId());
        holder.sdgNumber.setText("SDG " + (position + 1));

        // Adjust margins dynamically based on position
        int marginTop;
        int marginBottom;
        int marginLeft;
        int marginRight;

        switch (position % 6) {
            case 1:
            case 4:
                marginTop = 50;
                marginBottom = -50;
                marginLeft = 0;
                marginRight = 0;
                break;
            case 2:
            case 3:
                marginTop = 100;
                marginBottom = -100;
                marginLeft = 0;
                marginRight = 0;
                break;
            default:
                marginTop = 0;
                marginBottom = 0;
                marginLeft = 0;
                marginRight = 0;
                break;
        }

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
        params.setMargins(marginLeft, marginTop, marginRight, marginBottom);
        holder.itemView.setLayoutParams(params);

        // Initially hide the title
        holder.sdgNumber.setVisibility(View.GONE);

        // Set touch listener for showing/hiding the title
        holder.itemView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                holder.sdgNumber.setVisibility(View.VISIBLE);
            } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                holder.sdgNumber.setVisibility(View.GONE);
            }
            return false;
        });

        // Set click listener to navigate to SdgMapActivity
        holder.itemView.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();
            Intent intent = new Intent(context, SdgMapActivity.class);
            intent.putExtra("SDG_TITLE", sdgItem.getTitle());
            intent.putExtra("SDG_NUMBER", position + 1);
            context.startActivity(intent);
        });

        // Optional: Handle item click logic
//        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(sdgItem));
    }

    @Override
    public int getItemCount() {
        return sdgList.size();
    }
}
