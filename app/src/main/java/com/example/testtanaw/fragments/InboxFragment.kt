package com.example.testtanaw.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testtanaw.models.Message;
import com.example.testtanaw.util.MessageAdapter;
import com.example.testtanaw.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InboxFragment extends Fragment {

    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inbox, container, false);

        // Set up RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_inbox);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Mock data
        List<Message> messages = new ArrayList<>(Arrays.asList(
            new Message(
                1,
                "Image Uploaded Successfully",
                "Your image has been uploaded successfully!",
                "2024-11-25 02:34 PM",
                false
            ),
            new Message(
                2,
                "Action Reminder",
                "Check out the latest updates in your dashboard.",
                "2024-11-24 08:34 AM",
                false
            ),
            new Message(
                3,
                "Profile Update Confirmation",
                "Your profile has been updated successfully.",
                "2024-11-22 09:15 AM",
                true
            )
        ));

        // Set adapter
        messageAdapter = new MessageAdapter(messages);
        recyclerView.setAdapter(messageAdapter);

        return view;
    }
}

