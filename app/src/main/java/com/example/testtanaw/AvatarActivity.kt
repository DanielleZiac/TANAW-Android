package com.example.testtanaw;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.testtanaw.models.UserParcelable;
import com.example.testtanaw.util.CRUD;
import java.util.concurrent.CompletableFuture;

public class AvatarActivity extends AppCompatActivity {
    private final CRUD crud = new CRUD();
    private ImageView avatarBackground;
    private ImageView avatarGender;
    private ImageView avatarEyes;
    private ImageView avatarMouth;
    private ImageView avatarGlasses;
    private ImageView avatarShirt;

    private String bg;
    private String sex;
    private String eyewear = null;
    private String shirtStyle;

    private boolean isUploaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar);

        UserParcelable userData;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            userData = getIntent().getParcelableExtra("userData", UserParcelable.class);
        } else {
            userData = getIntent().getParcelableExtra("userData");
        }

        Log.d("xxxxxx", "AVATAR USERDATA: " + userData);

        if (userData != null) {
            String userId = userData.getUserId();
            CompletableFuture.supplyAsync(() -> {
                try {
                    return crud.getUserAvatarData(userId).get();
                } catch (Exception e) {
                    Log.e("AvatarActivity", "Error getting user avatar data", e);
                    return null;
                }
            }).thenAccept(userAvatarData -> runOnUiThread(() -> {
                if (userAvatarData != null) {
                    initializeViews();
                    setupConstantComponents();
                    setupCurrentAvatarComponents(userAvatarData);
                    setupSwitches();
                    setupButtons(userData);
                } else {
                    Toast.makeText(AvatarActivity.this, "NO CUR AVATAR", Toast.LENGTH_SHORT).show();
                }
            }));
        }
    }

    private void initializeViews() {
        avatarBackground = findViewById(R.id.avatarBackground);
        avatarGender = findViewById(R.id.avatarGender);
        avatarEyes = findViewById(R.id.avatarEyes);
        avatarMouth = findViewById(R.id.avatarMouth);
        avatarGlasses = findViewById(R.id.avatarGlasses);
        avatarShirt = findViewById(R.id.avatarShirt);
    }

    private void setupConstantComponents() {
        avatarEyes.setImageResource(R.drawable.eyes_opened);
        avatarMouth.setImageResource(R.drawable.mouth_closed);
    }

    private void setupCurrentAvatarComponents(CRUD.UserAvatarData userAvatarData) {
        if (userAvatarData.getEyewear() != null) {
            avatarGlasses.setImageResource(R.drawable.glasses);
        }

        switch (userAvatarData.getShirtStyle()) {
            case "shirt":
                avatarShirt.setImageResource(R.drawable.shirt);
                break;
            case "polo":
                avatarShirt.setImageResource(R.drawable.polo);
                break;
        }

        Log.d("xxxxxx", userAvatarData.getSex());
        switch (userAvatarData.getSex()) {
            case "boy":
                avatarGender.setImageResource(R.drawable.boy);
                break;
            case "girl":
                avatarGender.setImageResource(R.drawable.girl);
                break;
        }

        switch (userAvatarData.getBg()) {
            case "cics":
                avatarBackground.setImageResource(R.drawable.bg_cics);
                break;
            case "cet":
                avatarBackground.setImageResource(R.drawable.bg_cet);
                break;
            case "coe":
                avatarBackground.setImageResource(R.drawable.bg_coe);
                break;
            case "cafad":
                avatarBackground.setImageResource(R.drawable.bg_cafad);
                break;
        }
    }

    private void setupSwitches() {
        Switch glassesSwitch = findViewById(R.id.glasses);
        glassesSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                eyewear = "glasses";
                avatarGlasses.setImageResource(R.drawable.glasses);
            } else {
                eyewear = null;
                avatarGlasses.setImageDrawable(null);
            }
        });

        Switch shirtSwitch = findViewById(R.id.shirt);
        shirtSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                shirtStyle = "polo";
                avatarShirt.setImageResource(R.drawable.polo);
            } else {
                shirtStyle = "shirt";
                avatarShirt.setImageResource(R.drawable.shirt);
            }
        });
    }

    private void setupButtons(UserParcelable userData) {
        Button boyButton = findViewById(R.id.boy);
        Button girlButton = findViewById(R.id.girl);
        boyButton.setOnClickListener(v -> {
            sex = "boy";
            avatarGender.setImageResource(R.drawable.boy);
        });
        girlButton.setOnClickListener(v -> {
            sex = "girl";
            avatarGender.setImageResource(R.drawable.girl);
        });

        Button coeButton = findViewById(R.id.coe);
        Button cicsButton = findViewById(R.id.cics);
        Button cafadButton = findViewById(R.id.cafad);
        Button cetButton = findViewById(R.id.cet);

        coeButton.setOnClickListener(v -> {
            bg = "coe";
            avatarBackground.setImageResource(R.drawable.bg_coe);
        });
        cicsButton.setOnClickListener(v -> {
            bg = "cics";
            avatarBackground.setImageResource(R.drawable.bg_cics);
        });
        cafadButton.setOnClickListener(v -> {
            bg = "cafad";
            avatarBackground.setImageResource(R.drawable.bg_cafad);
        });
        cetButton.setOnClickListener(v -> {
            bg = "cet";
            avatarBackground.setImageResource(R.drawable.bg_cet);
        });

        Button confirmButton = findViewById(R.id.confirm_avatar);
        confirmButton.setOnClickListener(v -> {
            Log.d("xxxxxx", eyewear + ", " + shirtStyle + ", " + sex + ", " + bg);

            CompletableFuture.supplyAsync(() -> {
                try {
                    return crud.saveAvatar(getResources(), userData.getUserId(), 
                        eyewear, shirtStyle, sex, bg).get();
                } catch (Exception e) {
                    Log.e("AvatarActivity", "Error saving avatar", e);
                    return null;
                }
            }).thenAccept(newUrl -> runOnUiThread(() -> {
                Log.d("xxxxxx", String.valueOf(newUrl));
                Intent intent = new Intent(AvatarActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }));
        });
    }
}
