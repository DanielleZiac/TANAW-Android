package com.example.testtanaw
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CameraActivity : AppCompatActivity() {

    private lateinit var fabCamera: ImageButton
    private lateinit var fabFlipCamera: ImageButton
    private lateinit var caption: EditText
    private lateinit var upload: ImageButton
    private lateinit var retake: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        fabCamera = findViewById(R.id.fab_camera)
        fabFlipCamera = findViewById(R.id.fab_flip_camera)
        caption = findViewById(R.id.caption)
        upload = findViewById(R.id.upload)
        retake = findViewById(R.id.retake)

        // Initial state
        setInitialState()

        // Camera FAB click listener
        fabCamera.setOnClickListener {
            takePhoto()
        }

        // Upload FAB click listener
        upload.setOnClickListener {
            uploadPhoto()
        }

        // Retake FAB click listener
        retake.setOnClickListener {
            retakePhoto()
        }

        // Flip camera FAB click listener
        fabFlipCamera.setOnClickListener {
            flipCamera()
        }
    }

    private fun setInitialState() {
        fabCamera.visibility = View.VISIBLE
        fabFlipCamera.visibility = View.VISIBLE
        caption.visibility = View.GONE
        upload.visibility = View.GONE
        retake.visibility = View.GONE
    }

    private fun takePhoto() {
        // Logic to freeze or capture the photo
        Toast.makeText(this, "Photo Captured!", Toast.LENGTH_SHORT).show()

        // Toggle visibility
        fabCamera.visibility = View.GONE
        fabFlipCamera.visibility = View.GONE
        caption.visibility = View.VISIBLE
        upload.visibility = View.VISIBLE
        retake.visibility = View.VISIBLE
    }

    private fun uploadPhoto() {
        // Logic to upload photo and caption
        val captionText = caption.text.toString()
        Toast.makeText(this, "Uploading photo with caption: $captionText", Toast.LENGTH_SHORT).show()

        // Navigate back to the previous activity
        finish()
    }

    private fun retakePhoto() {
        // Logic to reset the camera preview
        Toast.makeText(this, "Retaking Photo!", Toast.LENGTH_SHORT).show()

        // Revert to the initial state
        setInitialState()
    }

    private fun flipCamera() {
        // Logic to flip the camera
        Toast.makeText(this, "Camera Flipped!", Toast.LENGTH_SHORT).show()
    }
}
