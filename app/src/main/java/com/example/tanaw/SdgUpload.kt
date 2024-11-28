package com.example.tanaw

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Surface
import android.view.View
import android.view.WindowMetrics
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.OutputFileOptions
import androidx.camera.core.ImageCapture.OutputFileResults
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.UUID
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import kotlin.math.abs


class SdgUpload : AppCompatActivity() {

    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var camera: Camera
    private lateinit var cameraSelector: CameraSelector
    private var lensFacing = CameraSelector.LENS_FACING_BACK



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(mainBinding.)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sdg_upload)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val cameraView = findViewById<androidx.camera.view.PreviewView>(R.id.cameraView)
        val flipBtn = findViewById<Button>(R.id.flipBtn)
        val flashBtn = findViewById<Button>(R.id.flashBtn)
        val captureBtn = findViewById<Button>(R.id.captureBtn)
        val retakeBtn = findViewById<Button>(R.id.retakeBtn)
        val uploadBtn = findViewById<Button>(R.id.uploadBtn)


        flipBtn.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View) {
                Log.d("tag", "flip")

                lensFacing = if(lensFacing == CameraSelector.LENS_FACING_FRONT) {
                    CameraSelector.LENS_FACING_BACK
                    } else {
                    CameraSelector.LENS_FACING_FRONT
                    }
                bindCameraUserCases()
            }
        })

        flashBtn.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View) {
                Log.d("tag", "flash")
                setFlashIcon(camera)

            }
        })

        captureBtn.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View) {
                Log.d("tag", "capture")
                takePhoto()
            }
        })

        retakeBtn.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View) {
                Log.d("tag", "retake")
            }
        })

        uploadBtn.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View) {
                Log.d("tag", "upload")
            }
        })


        // permission
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestCameraPermission()
                startCamera()
                // do capture
            } else {
                Log.d("tag", "cam open permission already granted")
                startCamera()
            }
        } catch (e: Exception) {
            Log.d("tag", "error ${e.message}")
        }




    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("tag", "cam openn")
            startCamera()
            // do capture
        } else {
            Log.d("tag", "permission denied")
        }
    }

    private fun requestCameraPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }


    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            bindCameraUserCases()
        }, ContextCompat.getMainExecutor(this))
    }

    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = maxOf(width, height).toDouble() / minOf(width, height)
        return if(abs(previewRatio - 4.0 / 3.0 ) <= abs(previewRatio - 16.0 / 9.0)) {
            AspectRatio.RATIO_4_3
        } else {
            AspectRatio.RATIO_16_9
        }
    }


    private fun bindCameraUserCases() {
        val displayMetrics = DisplayMetrics()
        val width: Int
        val height: Int

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics: WindowMetrics = windowManager.currentWindowMetrics

            // Get the bounds of the display
            val bounds = windowMetrics.bounds
            width = bounds.width()
            height = bounds.height()

            println("Width: $width, Height: $height")
        } else {
            // For older APIs, use the deprecated method
            @Suppress("DEPRECATION")
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            width = displayMetrics.widthPixels
            height = displayMetrics.heightPixels

            println("Width: $width, Height: $height")
        }

        val screenAspectRatio = aspectRatio(width, height)

        // Determine the rotation
        val rotation = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            display?.rotation ?: Surface.ROTATION_0
        } else {
            @Suppress("DEPRECATION")
            windowManager.defaultDisplay.rotation
        }

        val resolutionSelector = ResolutionSelector.Builder()
            .setAspectRatioStrategy(
                AspectRatioStrategy(
                    screenAspectRatio,
                    AspectRatioStrategy.FALLBACK_RULE_AUTO
                )
            ).build()

        val preview = Preview.Builder()
            .setResolutionSelector(resolutionSelector)
            .setTargetRotation(rotation)
            .build()
            .also {
                it.setSurfaceProvider(findViewById<androidx.camera.view.PreviewView>(R.id.cameraView).surfaceProvider)
            }

        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
            .setResolutionSelector(resolutionSelector)
            .setTargetRotation(rotation)
            .build()

        cameraSelector = CameraSelector.Builder()
            .requireLensFacing(lensFacing)
            .build()

        try {
            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageCapture
            )
        } catch (e: Exception) {
            Log.d("tag", "Error bindcamera ${e.message}")
            e.printStackTrace()
        }
    }

    private fun setFlashIcon(camera: Camera) {
        if(camera.cameraInfo.hasFlashUnit()) {
            if(camera.cameraInfo.torchState.value == 0) {
                camera.cameraControl.enableTorch(true)
            } else {
                camera.cameraControl.enableTorch(false)
            }
        } else {
            Log.d("tag", "Flash not avail")
        }
    }

    private fun takePhoto() {
        val uuid = UUID.randomUUID().toString();

        val currentDate = Date(System.currentTimeMillis())  // Use milliseconds since epoch
        val fileDate = (currentDate.time / 1000).toString()

        val temp = "temp"
        // Create a temporary file (image not saved to disk yet)
        val imageFile = File.createTempFile(temp, ".jpg")
        val outputOption = OutputFileOptions.Builder(imageFile).build()

        val byteArrayOutputStream = ByteArrayOutputStream()

        imageCapture.takePicture(
            outputOption,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: OutputFileResults) {
                    try {
                        val file = outputFileResults.savedUri?.let { File(it.path) }
                        val inputStream = file?.inputStream()
                        inputStream?.let {
                            it.copyTo(byteArrayOutputStream)
                        }

                        val imageData = byteArrayOutputStream.toByteArray()

                        // Convert the byte array to Bitmap
                        val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)

                        // Get the ImageView from the layout
                        val capturedImageView: ImageView = findViewById(R.id.imgView)

                        // Set the Bitmap to the ImageView to display the captured image
                        capturedImageView.setImageBitmap(bitmap)

                        CoroutineScope(Dispatchers.Main).launch {

                            // change sdg number
                            val filename = "sdg1/${uuid}-${fileDate}.jpg"
                            Log.d("tag", "filename ${filename}")
                            try {
                                val bucket = supabase.storage.from("user_sdgs")
                                bucket.upload(filename, imageData)

//                                val url = supabase.storage.from("user_sdgs").publicUrl("myIcon.png")
//                                Log.d("tag", url)

                                // add db nalang


                            } catch(e: Exception) {
                                Log.d("tag", "error upload ${e.message}")
                            }
                        }

                    } catch (e: Exception) {
                        Log.e("tag", "Error saving or displaying image: ${e.message}")
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.d("tag", "Error: Image not captured")
                }
            }
        )
    }
}