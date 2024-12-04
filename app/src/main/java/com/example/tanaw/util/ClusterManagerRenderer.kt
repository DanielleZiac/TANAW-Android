package com.example.tanaw.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Shader
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.tanaw.CustomInfoWindowAdapter
import com.example.tanaw.R
import com.example.tanaw.models.ClusterMarker
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class ClusterManagerRenderer(
    context: Context,
    private val map: GoogleMap?,
    private val clusterManager: ClusterManager<ClusterMarker>?,
    private val clusterMarkers: ArrayList<ClusterMarker>,
) : DefaultClusterRenderer<ClusterMarker>(context, map, clusterManager) {

    private val iconGenerator: IconGenerator = IconGenerator(context.applicationContext)
    private val imageView: ImageView = ImageView(context.applicationContext)
//    private val markerWidth = context.resources?.getDimension(R.dimen.custom_marker_image)
//    private val markerHeight = context.resources?.getDimension(R.dimen.custom_marker_image)
    private val context = context
    private val url = "https://srxhcymqociarjinmkpp.supabase.co/storage/v1/object/public/avatars/308e180d-0d08-480a-a626-5d0c2da7bc04/9355f132-4fa3-4dd9-84bc-bec78e001e3e"

    init {


//        imageView.layoutParams = ViewGroup.LayoutParams(
//            markerWidth?.toInt() ?: 0,
//            markerHeight?.toInt() ?: 0
//        )

//        val padding: Int = 2
//        imageView.setPadding(padding, padding, padding, padding)
//        iconGenerator.setContentView(imageView)
    }

    override fun onBeforeClusterItemRendered(item: ClusterMarker, markerOptions: MarkerOptions) {
        // Set the placeholder/default icon initially
        val defaultIcon: Bitmap = iconGenerator.makeIcon()
        markerOptions.title(item.title).snippet(item.snippet)

        val oldMarker = map?.addMarker(markerOptions)

        // Asynchronously fetch the bitmap
        CoroutineScope(Dispatchers.IO).launch {
            val fetchedBitmap = getBitmapFromURL(url, context) // Use the correct URL from your item
            if (fetchedBitmap != null) {
                withContext(Dispatchers.Main) {
                    // Create a new marker with the same properties as the old one but with the fetched bitmap
                    val newMarkerOptions = MarkerOptions()
                        .position(oldMarker?.position ?: item.position)
                        .title(oldMarker?.title ?: item.title)
                        .snippet(oldMarker?.snippet ?: item.snippet)
                        .icon(BitmapDescriptorFactory.fromBitmap(fetchedBitmap))

                    // Add the new marker
                    val newMarker = map?.addMarker(newMarkerOptions)

                    try {
                        map?.setInfoWindowAdapter(CustomInfoWindowAdapter(context))
                    } catch (e: Exception) {
                        Log.d("tag", "errorrorororoorr" + e.message)
                    }

                    // Remove the old marker
                    oldMarker?.remove()
                    clusterManager?.removeItem(item)
                    clusterMarkers.remove(item)
//
//                    item.icon.bitmap = fetchedBitmap
//                    clusterManager?.addItem(item)
//                    clusterMarkers.add(item)
                    clusterManager?.cluster()
                }
            }
        }
    }

    override fun setOnClusterItemClickListener(listener: ClusterManager.OnClusterItemClickListener<ClusterMarker>?) {
        Log.d("tag", "heereeaaaaaaaaaaaaaaaaaaaee")
//        map?.setInfoWindowAdapter(CustomInfoWindowAdapter(context))
    }



    override fun shouldRenderAsCluster(cluster: Cluster<ClusterMarker>): Boolean {
        return false
    }
}

suspend fun getBitmapFromURL(src: String?, context: Context): Bitmap? {
    return withContext(Dispatchers.IO) {
        try {
            val url = URL(src)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            val bitmap = BitmapFactory.decodeStream(input)


//            Resize the bitmap to 50dp
            val dpSize = 50f
            val pxSize = dpToPx(context, dpSize) // Convert 50dp to pixels
            val teardropBitmap = createCircularBitmapWithBorder(context, bitmap)
            val resizedBitmap: Bitmap = resizeBitmap(teardropBitmap, pxSize, pxSize)
            return@withContext resizedBitmap
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}

// Convert dp to pixels
fun dpToPx(context: Context, dp: Float): Int {
    val density = context.resources.displayMetrics.density
    return (dp * density).toInt()
}


fun resizeBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap {
    return Bitmap.createScaledBitmap(bitmap, width, height, false)
}


fun createCircularBitmapWithBorder(context: Context, squareBitmap: Bitmap): Bitmap {
    // Create a new bitmap with the same size as the square image
    val width = squareBitmap.width
    val height = squareBitmap.height
    val size = Math.min(width, height)  // Ensure the circular image fits in the smaller dimension

    // Create a new bitmap to hold the circular image
    val circularBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)

    // Create a canvas to draw on the new bitmap
    val canvas = Canvas(circularBitmap)

    // Define the circular shape (using the smaller dimension for a perfect circle)
    val radius = size / 2f
    val centerX = size / 2f
    val centerY = size / 2f

    // Create a paint object for drawing the image
    val paint = Paint()
    paint.isAntiAlias = true
    paint.style = Paint.Style.FILL

    // Create a path to define the circle
    val path = Path()
    path.addCircle(centerX, centerY, radius, Path.Direction.CW)

    // Create a BitmapShader for the original square image
    val shader = BitmapShader(squareBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    paint.shader = shader

    // Draw the circular image on the canvas
    canvas.drawPath(path, paint)

    // Paint for masking the image with the circular shape
    val dstInPaint = Paint()
    dstInPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
    canvas.drawBitmap(squareBitmap, 0f, 0f, dstInPaint)

    // Create a paint object for the border
    val borderPaint = Paint()
    borderPaint.isAntiAlias = true
    borderPaint.style = Paint.Style.STROKE
    borderPaint.color = Color.RED
    borderPaint.strokeWidth = 30F

    // Draw the border circle around the image
    canvas.drawCircle(centerX, centerY, radius - 30F / 2, borderPaint)

    return circularBitmap
}

