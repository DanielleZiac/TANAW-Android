package com.example.testtanaw
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
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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
import com.example.testtanaw.models.UserParcelable
import com.example.testtanaw.util.CRUD
import com.example.testtanaw.util.DB
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.Date
import java.util.UUID
import kotlin.math.abs

private val supabase = createSupabaseClient(
    supabaseUrl = BuildConfig.SUPABASE_URL,
    supabaseKey = BuildConfig.SUPABASE_ANON_KEY
) {
    install(Auth)
    install(Postgrest)
    install(Realtime)
    install(Storage)
    //install other modules
}

class CameraActivity : AppCompatActivity() {

    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var camera: Camera
    private lateinit var cameraSelector: CameraSelector
    private var lensFacing = CameraSelector.LENS_FACING_BACK
    private lateinit var curImageData: ByteArray
    val db = DB()
//    val crud = CRUD()
    var userId: String? = null
    var sdgNumber: String? = null
    var photoChallenge: String? = null
    var lat: Double? = 0.0
    var long: Double? = 0.0



    private lateinit var fabCamera: ImageButton
    private lateinit var fabFlipCamera: ImageButton
    private lateinit var caption: EditText
    private lateinit var upload: ImageButton
    private lateinit var retake: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        Log.d("xxxxxx", "HEREEEEEE")
        CoroutineScope(Dispatchers.Main).launch {
            userId = db.getUserId()
        }

        try {
            sdgNumber = intent.getStringExtra("sdgNumber")
            photoChallenge = intent.getStringExtra("photoChallenge")
            lat = intent.getStringExtra("lat")?.toDouble()
            long = intent.getStringExtra("long")?.toDouble()


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


        } catch (e: Exception) {
            Log.d("xxxxxx", "HEEEEE ${e.message}")
        }




        // permission
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestCameraPermission()
                startCamera()
                // do capture
            } else {
                Log.d("xxxxxx", "cam open permission already granted")
                startCamera()
            }
        } catch (e: Exception) {
            Log.d("xxxxxx", "error ${e.message}")
        }
    }

    private fun setInitialState() {
        fabCamera.visibility = View.VISIBLE
        fabFlipCamera.visibility = View.VISIBLE
        caption.visibility = View.GONE
        upload.visibility = View.GONE
        retake.visibility = View.GONE
    }

    private fun uploadPhoto() {
        // Logic to upload photo and caption

        if (lat != 0.0 || long != 0.0) {
            val captionText = caption.text.toString()
            Toast.makeText(this, "Uploading photo with caption: $captionText", Toast.LENGTH_SHORT).show()


            CoroutineScope(Dispatchers.Main).launch {

                val uuid = UUID.randomUUID().toString();

                val currentDate = Date(System.currentTimeMillis())  // Use milliseconds since epoch
                val fileDate = (currentDate.time / 1000).toString()

                // change sdg number
                val filename = "sdg1/${uuid}-${fileDate}.jpg"
                Log.d("xxxxxx", "filename ${filename}")
                try {
                    val bucket = supabase.storage.from("user_sdgs")
                    bucket.upload(filename, curImageData)

                    val url = supabase.storage.from("user_sdgs").publicUrl(filename)
                    Log.d("xxxxxx", "URLLLL: $url")

                    // add db

                    Log.d("xxxxxx", "$sdgNumber, $photoChallenge, $userId")

                    if (sdgNumber != null && photoChallenge != null && userId != null) {
                        supabase.from("user_sdgs").insert(
                            CRUD.UploadSdgPhoto(
                                userId=userId!!,
                                sdgNumber = "sdg${sdgNumber!!}",
                                type = "photo",
                                caption = captionText,
                                url = url,
                                filename = filename,
                                photoChallenge = photoChallenge!!,
                                lat = lat!!,
                                long = long!!,
                            )
                        )
                    } else {
                        Log.d("xxxxxx", "$sdgNumber, $photoChallenge, $userId")
                    }
                } catch(e: Exception) {
                    Log.d("xxxxxx", "error upload ${e.message}")
                }
            }

            // Navigate back to the previous activity
            finish()

        }
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

        Log.d("xxxxxx", "flip")

        lensFacing = if(lensFacing == CameraSelector.LENS_FACING_FRONT) {
            CameraSelector.LENS_FACING_BACK
        } else {
            CameraSelector.LENS_FACING_FRONT
        }
        bindCameraUserCases()
    }





    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("xxxxxx", "cam openn")
            startCamera()
            // do capture
        } else {
            Log.d("xxxxxx", "permission denied")
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
            Log.d("xxxxxx", "Error bindcamera ${e.message}")
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
            Log.d("xxxxxx", "Flash not avail")
        }
    }

    private fun takePhoto() {
        Toast.makeText(this, "Photo Captured!", Toast.LENGTH_SHORT).show()

        // Toggle visibility
        fabCamera.visibility = View.GONE
        fabFlipCamera.visibility = View.GONE
        caption.visibility = View.VISIBLE
        upload.visibility = View.VISIBLE
        retake.visibility = View.VISIBLE


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


                        // wait for retake initialize variable muna

                        curImageData = imageData

                    } catch (e: Exception) {
                        Log.e("xxxxxx", "Error saving or displaying image: ${e.message}")
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.d("xxxxxx", "Error: Image not captured")
                }
            }
        )
    }
}
