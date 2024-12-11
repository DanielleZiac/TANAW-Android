package com.example.testtanaw;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class SdgMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sdg_map2);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        // Retrieve data passed from the adapter
        String sdgTitle = getIntent().getStringExtra("SDG_TITLE");
        int sdgNumber = getIntent().getIntExtra("sdgNumber", -1);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragemnt);
        mapFragment.getMapAsync(this);
    }

        // Add your logic here to handle the SDG details
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mGoogle = googleMap;
    }
}