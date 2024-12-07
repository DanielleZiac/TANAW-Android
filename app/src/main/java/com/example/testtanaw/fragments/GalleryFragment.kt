package com.example.testtanaw.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testtanaw.util.GalleryAdapter
import com.example.testtanaw.util.SDGAdapter2
import com.example.testtanaw.databinding.FragmentGalleryBinding
import com.example.testtanaw.util.CRUD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GalleryFragment(val userId: String) : Fragment() {

    private lateinit var binding: FragmentGalleryBinding
    private lateinit var sdgAdapter2: SDGAdapter2
    private lateinit var galleryAdapter: GalleryAdapter
    private var isUploadsTab = true // Tracks the active tab (uploads or events)
//    private var userId: String? = null

    // This is where the 'userId' is passed to the fragment.
//    companion object {
//        fun newInstance(userId: String): GalleryFragment {
//            val fragment = GalleryFragment()
//            val args = Bundle()
//            args.putString("userId", userId)
//            fragment.arguments = args
//            return fragment
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGalleryBinding.inflate(inflater, container, false)

        // Retrieve the userId from arguments
//        userId = arguments?.getString("userId")

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

        CoroutineScope(Dispatchers.Main).launch {
            val crud = CRUD()
            Log.d("xxxxxx", "$userId")
            val photoDetails = crud.getPhotoByUserId(userId) // Use userId here
            val photoList = mutableListOf<String>()
            photoDetails?.forEach { res ->
                Log.d("xxxxxx", res.url)
                photoList.add(res.url) // Add the photo URL to the list
            }

            Log.d("xxxxxx", "$photoDetails $photoList")

            galleryAdapter = GalleryAdapter(photoList)
            binding.galleryRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
            binding.galleryRecyclerView.adapter = galleryAdapter

            // Update tab styles
            binding.uploadsTab.alpha = if (isUploadsTab) 1.0f else 0.5f
            binding.eventsTab.alpha = if (isUploadsTab) 0.5f else 1.0f
        }
    }
}
