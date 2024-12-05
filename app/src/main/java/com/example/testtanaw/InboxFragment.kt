package com.example.testtanaw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testtanaw.R

class InboxFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var messageAdapter: MessageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_inbox, container, false)

        // Set up RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_inbox)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Mock data
        val messages = listOf(
            Message(
                1,
                "Image Uploaded Successfully",
                "Your image has been uploaded successfully!",
                "2024-11-25 02:34 PM",
                false
            ),
            Message(
                2,
                "Action Reminder",
                "Check out the latest updates in your dashboard.",
                "2024-11-24 08:34 AM",
                false
            ),
            Message(
                3,
                "Profile Update Confirmation",
                "Your profile has been updated successfully.",
                "2024-11-22 09:15 AM",
                true
            )
        )

        // Set adapter
        messageAdapter = MessageAdapter(messages)
        recyclerView.adapter = messageAdapter

        return view
    }
}

