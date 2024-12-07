package com.example.testtanaw.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.testtanaw.R;

public class FeedbackModalFragment extends DialogFragment {

    private int rating = 0;
    private String feedback = "";

    private EditText feedbackEditText;
    private TextView submitButton;
    private ImageView closeButton;
    private LinearLayout starLayout;

    public static FeedbackModalFragment newInstance() {
        return new FeedbackModalFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                            @Nullable ViewGroup container,
                            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback_modal, container, false);

        // Initialize views
        feedbackEditText = view.findViewById(R.id.feedbackEditText);
        submitButton = view.findViewById(R.id.submitButton);
        closeButton = view.findViewById(R.id.closeButton);
        starLayout = view.findViewById(R.id.starLayout);

        // Set star click listeners for rating
        for (int i = 0; i < starLayout.getChildCount(); i++) {
            final int starPosition = i;
            ImageView star = (ImageView) starLayout.getChildAt(i);
            star.setOnClickListener(v -> {
                rating = starPosition + 1;
                updateStarRating();
            });
        }

        // Close the modal
        closeButton.setOnClickListener(v -> dismiss());

        // Submit feedback
        submitButton.setOnClickListener(v -> {
            feedback = feedbackEditText.getText().toString();
            if (rating > 0 && !feedback.isEmpty()) {
                submitFeedback(rating, feedback);
            } else {
                Toast.makeText(requireContext(), "Please provide both a rating and feedback.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void updateStarRating() {
        for (int i = 0; i < starLayout.getChildCount(); i++) {
            ImageView star = (ImageView) starLayout.getChildAt(i);
            star.setImageResource(i < rating ? R.drawable.star_filled : R.drawable.star_empty);
        }
    }

    private void submitFeedback(int rating, String feedback) {
        // Handle feedback submission logic
        Toast.makeText(requireContext(),
                "Feedback submitted: Rating - " + rating + ", Feedback - " + feedback,
                Toast.LENGTH_SHORT).show();
        dismiss();
    }
}
