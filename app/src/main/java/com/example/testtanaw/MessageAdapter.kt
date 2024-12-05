package com.example.testtanaw

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testtanaw.R

class MessageAdapter(
    private val messages: List<Message>
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val subject: TextView = view.findViewById(R.id.message_subject)
        val preview: TextView = view.findViewById(R.id.message_preview)
        val timestamp: TextView = view.findViewById(R.id.message_timestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.subject.text = message.subject
        holder.preview.text = message.preview
        holder.timestamp.text = message.timestamp

        // Highlight unread messages
        val textColor = if (message.isRead) R.color.black else R.color.black
        holder.subject.setTextColor(holder.itemView.context.getColor(textColor))
    }

    override fun getItemCount(): Int = messages.size
}
