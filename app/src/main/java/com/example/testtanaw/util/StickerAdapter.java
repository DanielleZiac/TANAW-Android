package com.example.testtanaw.util;

import android.util.Log;
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
import java.util.Objects;
import java.util.Set;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.SdgViewHolder> {
    private final List<Sdg> sdgList;
    private final List<String> avatarParts;

    public StickerAdapter(List<Sdg> sdgList, List<String> avatarParts) {
        this.sdgList = sdgList;
        this.avatarParts = avatarParts;
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

//        Log.d("STICKER ", avatarParts.get(1));
//        Log.d("STICKER ", avatarParts.get(2));
//        Log.d("STICKER ", avatarParts.get(3));

        holder.eye.setImageResource(R.drawable.eyes_opened);
        holder.mouth.setImageResource(R.drawable.mouth_closed);

        if (avatarParts.get(1).equals("glasses")) {
            Log.d("STICKER ", avatarParts.get(1) + "glasses");
            holder.eyewear.setImageResource(R.drawable.glasses);
        }

        if (avatarParts.get(2).equals("boy")) {
            Log.d("STICKER ", avatarParts.get(2) + "boy");
            holder.gender.setImageResource(R.drawable.boy);
        } else {
            holder.gender.setImageResource(R.drawable.girl);
        }

        if (avatarParts.get(3).equals("shirt")) {
            Log.d("STICKER ", avatarParts.get(3) + "shirt");
            holder.shirtStyle.setImageResource(R.drawable.shirt);
        } else {
            holder.shirtStyle.setImageResource(R.drawable.polo);
        }

        holder.imageView.setImageResource(sdg.getImageResId());

        holder.textView.setText(sdg.getTitle());
    }

    @Override
    public int getItemCount() {
        return sdgList.size();
    }

    public static class SdgViewHolder extends RecyclerView.ViewHolder {
        public final ImageView imageView, eyewear, gender, shirtStyle, eye, mouth;
        final TextView textView;

        public SdgViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            eyewear = itemView.findViewById(R.id.eyewear);
            gender = itemView.findViewById(R.id.gender);
            shirtStyle = itemView.findViewById(R.id.shirtStyle);
            eye = itemView.findViewById(R.id.eye);
            mouth = itemView.findViewById(R.id.mouth);
            textView = itemView.findViewById(R.id.textStickers);
        }
    }
}
