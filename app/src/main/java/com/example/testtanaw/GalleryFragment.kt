package com.example.testtanaw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testtanaw.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment() {

    private lateinit var binding: FragmentGalleryBinding
    private lateinit var sdgAdapter2: SDGAdapter2
    private lateinit var galleryAdapter: GalleryAdapter
    private var isUploadsTab = true // Tracks the active tab (uploads or events)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGalleryBinding.inflate(inflater, container, false)

        // SDG Data
        val sdgImages = (1..17).map { "sdg_$it" } // Image names for SDGs

        // Horizontal RecyclerView for SDGs
        sdgAdapter2 = SDGAdapter2(sdgImages)
        binding.sdgRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.sdgRecyclerView.adapter = sdgAdapter2

        // Grid RecyclerView for Gallery
        setupGalleryRecyclerView()

        // Tab Switching
        binding.uploadsTab.setOnClickListener {
            isUploadsTab = true
            setupGalleryRecyclerView()
        }
        binding.eventsTab.setOnClickListener {
            isUploadsTab = false
            setupGalleryRecyclerView()
        }

        return binding.root
    }

    private fun setupGalleryRecyclerView() {
        val images = if (isUploadsTab) {
            (1..6).map { "sdglink_$it" }
        } else {
            (7..12).map { "sdglink_$it" }
        }

        galleryAdapter = GalleryAdapter(images)
        binding.galleryRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.galleryRecyclerView.adapter = galleryAdapter

        // Update tab styles
        binding.uploadsTab.alpha = if (isUploadsTab) 1.0f else 0.5f
        binding.eventsTab.alpha = if (isUploadsTab) 0.5f else 1.0f
    }
}
