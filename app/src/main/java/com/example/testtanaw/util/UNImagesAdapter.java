package com.example.testtanaw.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testtanaw.R;
import com.example.testtanaw.fragments.ExploreFragment;
import java.util.Arrays;
import java.util.List;

public class UNImagesAdapter extends RecyclerView.Adapter<UNImagesAdapter.ViewHolder> {
    private final ExploreFragment fragment;
    private final List<Integer> unImages;
    private final List<String> overlayTexts;
    private final List<String> articles;
    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public UNImagesAdapter(ExploreFragment fragment) {
        this.fragment = fragment;
        this.unImages = Arrays.asList(
            R.drawable.un1,
            R.drawable.un2,
            R.drawable.un3
        );
        
        this.overlayTexts = Arrays.asList(
            "The United Nations General Assembly calls for urgent measures to end the humanitarian crisis in Gaza amidst ongoing conflict.",
            "Message 2",
            "Message 3"
        );
        
        this.articles = Arrays.asList(
            "On December 4, 2024, the United Nations General Assembly resumed its emergency special session, addressing the dire situation in Gaza. Delegates stressed the urgency of halting the ongoing violence and implementing measures to alleviate the suffering of civilians. The humanitarian crisis in Gaza has reached alarming levels, with many civilians facing critical shortages of basic necessities such as food, medicine, and shelter. The international community, including the UN, is calling for immediate interventions to ensure the protection of civilians and the provision of essential aid. Delegates also discussed the importance of a long-term solution that can bring lasting peace to the region and address the root causes of the conflict.",
            "On December 3, 2024, the UN General Assembly adopted three key resolutions aimed at advancing peace in the Middle East, with a focus on the two-state solution for Israel and Palestine. The resolutions call for a ceasefire in Gaza and greater humanitarian access to the region. The General Assembly has emphasized that a two-state solution remains the only viable path to lasting peace between Israelis and Palestinians. In addition, the resolutions advocate for the right of Palestinian refugees to return to their homes and for the international community to provide necessary assistance to both Israel and Palestine in achieving a sustainable peace.",
            "As the war between Russia and Ukraine continues into its third year, the UN has drawn attention to its devastating effects on children. During a Security Council meeting on December 4, 2024, a senior UN official shared troubling reports regarding the systematic abduction and adoption of Ukrainian children. The war has disrupted countless young lives, with many children subjected to violence, displacement, and separation from their families. The UN has called for international efforts to address these violations, highlighting the need for humanitarian assistance and the protection of children's rights in conflict zones."
        );
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_un_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageResource(unImages.get(position));
        holder.overlayTextView.setText(overlayTexts.get(position));
        holder.itemView.setOnClickListener(v -> 
            fragment.showImageDialog(unImages.get(position), articles.get(position))
        );
    }

    @Override
    public int getItemCount() {
        return unImages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView imageView;
        public final TextView overlayTextView;

        public ViewHolder(@NonNull View view) {
            super(view);
            imageView = view.findViewById(R.id.un_image);
            overlayTextView = view.findViewById(R.id.overlay_text);
        }
    }
}
