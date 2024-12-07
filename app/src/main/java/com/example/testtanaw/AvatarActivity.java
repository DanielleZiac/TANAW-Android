package com.example.testtanaw;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;
import com.example.testtanaw.util.CRUD;

public class AvatarActivity extends AppCompatActivity {
    private ImageView avatarBackground;
    private ImageView avatarGender;
    private ImageView avatarEyes;
    private ImageView avatarMouth;
    private ImageView avatarGlasses;
    private ImageView avatarShirt;
    private Switch glassesSwitch;
    private Switch shirtSwitch;
    private Button boyButton;
    private Button girlButton;
    private Button coeButton;
    private Button cicsButton;
    private Button cafadButton;
    private Button cetButton;
    private Button confirmAvatarButton;
    private String selectedGender = "boy";
    private String selectedCollege = "cics";
    private final CRUD crud = new CRUD();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar);

        // Initialize views
        initializeViews();
        
        // Set up click listeners
        setupClickListeners();
        
        // Set initial avatar state
        updateAvatarDisplay();
    }

    private void initializeViews() {
        avatarBackground = findViewById(R.id.avatarBackground);
        avatarGender = findViewById(R.id.avatarGender);
        avatarEyes = findViewById(R.id.avatarEyes);
        avatarMouth = findViewById(R.id.avatarMouth);
        avatarGlasses = findViewById(R.id.avatarGlasses);
        avatarShirt = findViewById(R.id.avatarShirt);
        glassesSwitch = findViewById(R.id.glasses);
        shirtSwitch = findViewById(R.id.shirt);
        boyButton = findViewById(R.id.boy);
        girlButton = findViewById(R.id.girl);
        coeButton = findViewById(R.id.coe);
        cicsButton = findViewById(R.id.cics);
        cafadButton = findViewById(R.id.cafad);
        cetButton = findViewById(R.id.cet);
        confirmAvatarButton = findViewById(R.id.confirm_avatar);
    }

    private void setupClickListeners() {
        glassesSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            avatarGlasses.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            if (isChecked) {
                avatarGlasses.setImageResource(R.drawable.glasses);
            }
        });

        shirtSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                avatarShirt.setImageResource(R.drawable.polo);
            } else {
                avatarShirt.setImageResource(R.drawable.shirt);
            }
        });

        boyButton.setOnClickListener(v -> {
            selectedGender = "boy";
            updateAvatarDisplay();
        });

        girlButton.setOnClickListener(v -> {
            selectedGender = "girl";
            updateAvatarDisplay();
        });

        coeButton.setOnClickListener(v -> {
            selectedCollege = "coe";
            updateAvatarDisplay();
        });

        cicsButton.setOnClickListener(v -> {
            selectedCollege = "cics";
            updateAvatarDisplay();
        });

        cafadButton.setOnClickListener(v -> {
            selectedCollege = "cafad";
            updateAvatarDisplay();
        });

        cetButton.setOnClickListener(v -> {
            selectedCollege = "cet";
            updateAvatarDisplay();
        });

        confirmAvatarButton.setOnClickListener(v -> saveAvatar());
    }

    private void updateAvatarDisplay() {
        // Update gender
        int genderDrawable = selectedGender.equals("boy") ? 
            R.drawable.boy : R.drawable.girl;
        avatarGender.setImageResource(genderDrawable);

        // Update college background
        int backgroundDrawable;
        switch (selectedCollege) {
            case "coe":
                backgroundDrawable = R.drawable.bg_coe;
                break;
            case "cics":
                backgroundDrawable = R.drawable.bg_cics;
                break;
            case "cafad":
                backgroundDrawable = R.drawable.bg_cafad;
                break;
            case "cet":
                backgroundDrawable = R.drawable.bg_cet;
                break;
            default:
                backgroundDrawable = R.drawable.bg_bsu;
                break;
        }
        avatarBackground.setImageResource(backgroundDrawable);
    }

    private void saveAvatar() {
        String userId = "user123"; // Replace with actual user ID
        String eyewear = glassesSwitch.isChecked() ? "glasses" : null;
        String shirtStyle = shirtSwitch.isChecked() ? "polo" : "shirt";

        crud.saveAvatar(
            getResources(),
            userId,
            eyewear,
            shirtStyle,
            selectedGender,
            selectedCollege
        );

        finish();
    }
}
