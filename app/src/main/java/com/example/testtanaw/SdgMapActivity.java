package com.example.testtanaw;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class SdgMapActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdg_map);

        // Retrieve data passed from the adapter
        String sdgTitle = getIntent().getStringExtra("SDG_TITLE");
        int sdgNumber = getIntent().getIntExtra("sdgNumber", -1);

        // Add your logic here to handle the SDG details
    }
}
