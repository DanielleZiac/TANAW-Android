package com.example.testtanaw;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testtanaw.models.TopPost;
import java.util.List;

public class TopPostsAdapter extends RecyclerView.Adapter<TopPostsAdapter.TopPostViewHolder> {
    private final List<TopPost> topPosts;

    public TopPostsAdapter(List<TopPost> topPosts) {
        this.topPosts = topPosts;
    }

    @NonNull
    @Override
    public TopPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top_post, parent, false);
        return new TopPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopPostViewHolder holder, int position) {
        TopPost topPost = topPosts.get(position);
        holder.bind(topPost);
    }

    @Override
    public int getItemCount() {
        return topPosts.size();
    }

    public class TopPostViewHolder extends RecyclerView.ViewHolder {
        private final ImageView postImage;
        private final TextView postCaption;

        public TopPostViewHolder(@NonNull View itemView) {
            super(itemView);
            postImage = itemView.findViewById(R.id.postImage);
            postCaption = itemView.findViewById(R.id.postCaption);
        }

        public void bind(TopPost topPost) {
            postImage.setImageResource(topPost.getImageResId());
            postCaption.setText(topPost.getCaption());
        }
    }
}
