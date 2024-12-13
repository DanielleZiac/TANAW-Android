package com.example.testtanaw.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.testtanaw.adapters.CustomInfoWindowAdapter;
import com.example.testtanaw.models.ClusterMarker;
import com.example.testtanaw.models.CustomInfoWindowData;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ClusterManagerRenderer extends DefaultClusterRenderer<ClusterMarker> {
    private static final String TAG = "ClusterManagerRenderer";


    private final IconGenerator iconGenerator;
    private final Context context;
    private final GoogleMap map;
    private final ClusterManager<ClusterMarker> clusterManager;
    private final ArrayList<ClusterMarker> clusterMarkers;
    private final Activity activity;

    public ClusterManagerRenderer(Activity activity, Context context, GoogleMap map, ClusterManager<ClusterMarker> clusterManager, ArrayList<ClusterMarker> clusterMarkers) {
        super(context, map, clusterManager);
        this.activity = activity;
        this.context = context;
        this.map = map;
        this.clusterManager = clusterManager;
        this.clusterMarkers = clusterMarkers;
        this.iconGenerator = new IconGenerator(context.getApplicationContext());
    }

    @Override
    protected void onBeforeClusterItemRendered(@NonNull ClusterMarker item, @NonNull MarkerOptions markerOptions) {
        // Set the placeholder/default icon initially
        Bitmap defaultIcon = iconGenerator.makeIcon();
        markerOptions.title("Loading...").snippet("Please wait");

        final Marker oldMarker = map.addMarker(markerOptions);

        // Fetch bitmap asynchronously
        new Thread(() -> {
            try {
                Bitmap fetchedBitmap = getBitmapFromURL(item.getAvatarUrl(), context);
                if (fetchedBitmap != null) {
                    // Use activity.runOnUiThread instead of casting context
                    activity.runOnUiThread(() -> {
                        MarkerOptions newMarkerOptions = new MarkerOptions()
                                .position(oldMarker != null ? oldMarker.getPosition() : item.getPosition())
                                .title(oldMarker != null ? oldMarker.getTitle() : item.getTitle())
                                .snippet(oldMarker != null ? oldMarker.getSnippet() : item.getSnippet())
                                .icon(BitmapDescriptorFactory.fromBitmap(fetchedBitmap));

                        Marker newMarker = map.addMarker(newMarkerOptions);

                        Log.d(TAG, item.getUserSdgId());
                        Log.d(TAG, item.getUserId());
                        Log.d(TAG, item.getSdgNumber());
                        Log.d(TAG, item.getCaption());
                        Log.d(TAG, item.getSdgPhotoUrl());
                        Log.d(TAG, item.getPhotoChallenge());
                        Log.d(TAG, item.getPosition().toString());
                        Log.d(TAG,item.getAvatarUrl());

                        try {
                            if (newMarker != null) {
                                newMarker.setTag(new CustomInfoWindowData(
                                        item.getUserSdgId(),
                                        item.getUserId(),
                                        item.getSdgNumber(),
                                        item.getCaption(),
                                        item.getSdgPhotoUrl(),
                                        item.getPhotoChallenge(),
                                        item.getPosition(),
                                        item.getAvatarUrl()
                                ));
                            }
                            map.setInfoWindowAdapter(new CustomInfoWindowAdapter(context));
                        } catch (Exception e) {
                            Log.d(TAG, "Error: " + e.getMessage());
                        }

                        if (oldMarker != null) {
                            oldMarker.remove();
                        }
                        clusterManager.removeItem(item);
                        clusterMarkers.remove(item);

                        clusterManager.cluster();
                    });
                }
            } catch (Exception e) {
                Log.e(TAG, "Bitmap fetch error: " + e.getMessage(), e);
            }
        }).start();
    }

    // Function to fetch the Bitmap from the URL
    public static Bitmap getBitmapFromURL(final String src, final Context context) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);

            // Resize the bitmap to 50dp
            float dpSize = 50f;
            int pxSize = dpToPx(context, dpSize); // Convert 50dp to pixels
            Bitmap teardropBitmap = createCircularBitmapWithBorder(context, bitmap);
            return resizeBitmap(teardropBitmap, pxSize, pxSize);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Convert dp to pixels
    public static int dpToPx(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density);
    }

    // Resize bitmap
    public static Bitmap resizeBitmap(Bitmap bitmap, int width, int height) {
        return Bitmap.createScaledBitmap(bitmap, width, height, false);
    }

    // Create a circular bitmap with border
    public static Bitmap createCircularBitmapWithBorder(Context context, Bitmap squareBitmap) {
        int width = squareBitmap.getWidth();
        int height = squareBitmap.getHeight();
        int size = Math.min(width, height);

        Bitmap circularBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(circularBitmap);

        float radius = size / 2f;
        float centerX = size / 2f;
        float centerY = size / 2f;

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        Path path = new Path();
        path.addCircle(centerX, centerY, radius, Path.Direction.CW);

        BitmapShader shader = new BitmapShader(squareBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setShader(shader);

        canvas.drawPath(path, paint);

        // Mask the image with the circular shape
        Paint dstInPaint = new Paint();
        dstInPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(squareBitmap, 0f, 0f, dstInPaint);

        // Add border
        Paint borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(Color.RED);
        borderPaint.setStrokeWidth(30f);
        canvas.drawCircle(centerX, centerY, radius - 15f, borderPaint);

        return circularBitmap;
    }
}