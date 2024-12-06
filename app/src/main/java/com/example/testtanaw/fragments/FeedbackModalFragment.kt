package com.example.testtanaw.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment

import com.example.testtanaw.R

class FeedbackModalFragment : DialogFragment() {

    private var rating = 0
    private var feedback = ""

    private lateinit var feedbackEditText: EditText
    private lateinit var submitButton: TextView
    private lateinit var closeButton: ImageView
    private lateinit var starLayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_feedback_modal, container, false)

        // Initialize views
        feedbackEditText = view.findViewById(R.id.feedbackEditText)
        submitButton = view.findViewById(R.id.submitButton)
        closeButton = view.findViewById(R.id.closeButton)
        starLayout = view.findViewById(R.id.starLayout)

        // Set star click listeners for rating
        for (i in 0 until starLayout.childCount) {
            val star = starLayout.getChildAt(i) as ImageView
            star.setOnClickListener {
                rating = i + 1
                updateStarRating()
            }
        }

        // Close the modal
        closeButton.setOnClickListener {
            dismiss()
        }

        // Submit feedback
        submitButton.setOnClickListener {
            feedback = feedbackEditText.text.toString()
            if (rating > 0 && feedback.isNotEmpty()) {
                submitFeedback(rating, feedback)
            } else {
                Toast.makeText(requireContext(), "Please provide both a rating and feedback.", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun updateStarRating() {
        for (i in 0 until starLayout.childCount) {
            val star = starLayout.getChildAt(i) as ImageView
            if (i < rating) {
                star.setImageResource(R.drawable.star_filled) // Replace with your filled star icon
            } else {
                star.setImageResource(R.drawable.star_empty) // Replace with your empty star icon
            }
        }
    }

    private fun submitFeedback(rating: Int, feedback: String) {
        // Handle feedback submission logic
        Toast.makeText(requireContext(), "Feedback submitted: Rating - $rating, Feedback - $feedback", Toast.LENGTH_SHORT).show()
        dismiss()
    }

    companion object {
        fun newInstance(): FeedbackModalFragment {
            return FeedbackModalFragment()
        }
    }
}
