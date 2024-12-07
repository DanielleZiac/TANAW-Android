package com.example.testtanaw.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testtanaw.R;
import com.example.testtanaw.models.Sdg;
import java.util.List;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.SdgViewHolder> {
    private final List<Sdg> sdgList;

    public StickerAdapter(List<Sdg> sdgList) {
        this.sdgList = sdgList;
    }

    @NonNull
    @Override
    public SdgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sticker, parent, false);
        return new SdgViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SdgViewHolder holder, int position) {
        Sdg sdg = sdgList.get(position);
        holder.imageView.setImageResource(sdg.getImageResId());
        holder.textView.setText(sdg.getTitle());
    }

    @Override
    public int getItemCount() {
        return sdgList.size();
    }

    public static class SdgViewHolder extends RecyclerView.ViewHolder {
        public final ImageView imageView;
        public final TextView textView;

        public SdgViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textStickers);
        }
    }
}
