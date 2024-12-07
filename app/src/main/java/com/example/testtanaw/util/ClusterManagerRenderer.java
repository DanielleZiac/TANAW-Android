package com.example.testtanaw.util;

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
import com.example.testtanaw.models.ClusterMarker;
import com.example.testtanaw.models.CustomInfoWindowData;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executors;

public class ClusterManagerRenderer extends DefaultClusterRenderer<ClusterMarker> {
    private final Context context;
    private final IconGenerator iconGenerator;
    private final GoogleMap map;
    private final ClusterManager<ClusterMarker> clusterManager;
    private final ArrayList<ClusterMarker> clusterMarkers;

    public ClusterManagerRenderer(Context context, GoogleMap map,
                                ClusterManager<ClusterMarker> clusterManager,
                                ArrayList<ClusterMarker> clusterMarkers) {
        super(context, map, clusterManager);
        this.context = context;
        this.map = map;
        this.clusterManager = clusterManager;
        this.clusterMarkers = clusterMarkers;
        this.iconGenerator = new IconGenerator(context.getApplicationContext());
    }

    @Override
    protected void onBeforeClusterItemRendered(@NonNull ClusterMarker item,
                                             @NonNull MarkerOptions markerOptions) {
        // Set initial placeholder icon
        Bitmap defaultIcon = iconGenerator.makeIcon();
        markerOptions.title("Loading...").snippet("Please wait");

        Marker oldMarker = map.addMarker(markerOptions);
        if (oldMarker != null) {
            final Marker finalOldMarker = oldMarker;

            // Fetch bitmap asynchronously
            Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    Bitmap fetchedBitmap = getBitmapFromURL(item.getAvatarUrl(), context);
                    if (fetchedBitmap != null) {
                        context.getMainExecutor().execute(() -> {
                            // Create new marker with fetched bitmap
                            MarkerOptions newMarkerOptions = new MarkerOptions()
                                    .position(finalOldMarker.getPosition())
                                    .title(finalOldMarker.getTitle())
                                    .snippet(finalOldMarker.getSnippet())
                                    .icon(BitmapDescriptorFactory.fromBitmap(fetchedBitmap));

                            Marker newMarker = map.addMarker(newMarkerOptions);
                            if (newMarker != null) {
                                try {
                                    newMarker.setTag(new CustomInfoWindowData(
                                            item.getUserSdgId(),
                                            item.getUserId(),
                                            item.getSdgNumber(),
                                            item.getUrl(),
                                            item.getCaption(),
                                            item.getCreatedDate(),
                                            item.getInstitutionId(),
                                            item.getPhototChall(),
                                            item.getInstitution(),
                                            item.getCampus(),
                                            item.getInstitutionLogo(),
                                            item.getPosition(),
                                            item.getAvatarUrl()
                                    ));
                                    map.setInfoWindowAdapter(new CustomInfoWindowAdapter(context));
                                } catch (Exception e) {
                                    Log.d("tag", "Error setting marker tag: " + e.getMessage());
                                }
                            }

                            finalOldMarker.remove();
                            clusterManager.removeItem(item);
                            clusterMarkers.remove(item);
                            clusterManager.cluster();
                        });
                    }
                } catch (Exception e) {
                    Log.e("ClusterManagerRenderer", "Error loading bitmap: " + e.getMessage());
                }
            });
        }
    }

    @Override
    public void setOnClusterItemClickListener(ClusterManager.OnClusterItemClickListener<ClusterMarker> listener) {
        Log.d("tag", "setOnClusterItemClickListener called");
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster<ClusterMarker> cluster) {
        return false;
    }

    public static Bitmap getBitmapFromURL(String src, Context context) throws IOException {
        URL url = new URL(src);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.connect();
        
        Bitmap bitmap = BitmapFactory.decodeStream(connection.getInputStream());
        if (bitmap == null) return null;

        // Resize to 50dp
        float dpSize = 50f;
        int pxSize = dpToPx(context, dpSize);
        Bitmap teardropBitmap = createCircularBitmapWithBorder(context, bitmap);
        return resizeBitmap(teardropBitmap, pxSize, pxSize);
    }

    private static int dpToPx(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density);
    }

    private static Bitmap resizeBitmap(Bitmap bitmap, int width, int height) {
        return Bitmap.createScaledBitmap(bitmap, width, height, false);
    }

    private static Bitmap createCircularBitmapWithBorder(Context context, Bitmap squareBitmap) {
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

        BitmapShader shader = new BitmapShader(squareBitmap, 
                Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setShader(shader);

        canvas.drawPath(path, paint);

        Paint dstInPaint = new Paint();
        dstInPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(squareBitmap, 0f, 0f, dstInPaint);

        Paint borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(Color.RED);
        borderPaint.setStrokeWidth(30f);

        canvas.drawCircle(centerX, centerY, radius - 30f / 2, borderPaint);

        return circularBitmap;
    }
}
