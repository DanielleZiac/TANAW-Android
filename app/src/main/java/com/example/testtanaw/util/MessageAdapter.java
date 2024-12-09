package com.example.testtanaw.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testtanaw.R;
import com.example.testtanaw.models.Message;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private final List<Message> messages;

    public MessageAdapter(List<Message> messages) {
        this.messages = messages;
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public final TextView subject;
        public final TextView preview;
        public final TextView timestamp;

        public MessageViewHolder(View view) {
            super(view);
            subject = view.findViewById(R.id.message_subject);
            preview = view.findViewById(R.id.message_preview);
            timestamp = view.findViewById(R.id.message_timestamp);
        }
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.subject.setText(message.getSubject());
        holder.preview.setText(message.getPreview());
        holder.timestamp.setText(message.getTimestamp());

        // Highlight unread messages
        int textColor = message.isRead() ? R.color.black : R.color.black;
        holder.subject.setTextColor(holder.itemView.getContext().getColor(textColor));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
