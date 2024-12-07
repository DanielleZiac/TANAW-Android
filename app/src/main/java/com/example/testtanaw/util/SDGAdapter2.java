package com.example.testtanaw.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testtanaw.R;
import java.util.List;

public class SDGAdapter2 extends RecyclerView.Adapter<SDGAdapter2.SDGViewHolder> {
    private final List<String> sdgImages;

    public SDGAdapter2(List<String> sdgImages) {
        this.sdgImages = sdgImages;
    }

    public static class SDGViewHolder extends RecyclerView.ViewHolder {
        public final ImageView imageView;

        public SDGViewHolder(@NonNull View view) {
            super(view);
            imageView = view.findViewById(R.id.sdgImageView);
        }
    }

    @NonNull
    @Override
    public SDGViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sdg_circle, parent, false);
        return new SDGViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SDGViewHolder holder, int position) {
        int resourceId = holder.itemView.getContext().getResources().getIdentifier(
                sdgImages.get(position), "drawable", holder.itemView.getContext().getPackageName()
        );
        holder.imageView.setImageResource(resourceId);
    }

    @Override
    public int getItemCount() {
        return sdgImages.size();
    }
}
