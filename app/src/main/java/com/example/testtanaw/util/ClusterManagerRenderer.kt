package com.example.testtanaw.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Shader
import android.util.Log
import android.util.Log.*
import android.view.ViewGroup
import android.widget.ImageView
import com.example.testtanaw.R
import com.example.testtanaw.models.ClusterMarker
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import java.io.IOException
import java.net.URL


class ClusterManagerRenderer(
    context: Context,
    map: GoogleMap?,
    clusterManager: ClusterManager<ClusterMarker>?
) : DefaultClusterRenderer<ClusterMarker>(context, map, clusterManager) {

    private val iconGenerator: IconGenerator = IconGenerator(context?.applicationContext)
    private val imageView: ImageView = ImageView(context?.applicationContext)
    private val markerWidth = context?.resources?.getDimension(R.dimen.custom_marker_image)
    private val markerHeight = context?.resources?.getDimension(R.dimen.custom_marker_image)
    private val url =
        "https://srxhcymqociarjinmkpp.supabase.co/storage/v1/object/public/avatars/2e540f74-c560-43ca-b2fd-0311f49209c9/045ceb47-788d-471c-84c3-62093577d3e9?t=2024-12-02T09%3A45%3A32.671Z"

    init {


        imageView.layoutParams = ViewGroup.LayoutParams(
            markerWidth?.toInt() ?: 0,
            markerHeight?.toInt() ?: 0
        )

        val padding: Int = 2
        imageView.setPadding(padding, padding, padding, padding)
        iconGenerator.setContentView(imageView)
    }

    override fun onBeforeClusterItemRendered(item: ClusterMarker, markerOptions: MarkerOptions) {
        imageView.setImageResource(item.getIconPicture())
        val icon: Bitmap = iconGenerator.makeIcon()
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(item.title)
            .snippet(item.snippet);
    }


    override fun shouldRenderAsCluster(cluster: Cluster<ClusterMarker>): Boolean {
        return false
    }

    fun makeCircularImage(imageView: ImageView, bitmap: Bitmap) {
        val width = bitmap.width
        val height = bitmap.height
        val radius = Math.min(width, height) / 2

        val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

        // Draw a circle using the paint and canvas
        canvas.drawCircle(width / 2f, height / 2f, radius.toFloat(), paint)

        imageView.setImageBitmap(output)
    }

    private fun getImageBitmap(url: String): Bitmap? {
        try {
            val url = URL(url)
            val image = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            return image
        } catch (e: IOException) {
            println(e)
        }
        return null
    }
}
