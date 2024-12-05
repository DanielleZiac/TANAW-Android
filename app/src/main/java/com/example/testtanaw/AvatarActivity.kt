package com.example.testtanaw

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AvatarActivity : AppCompatActivity() {

    private lateinit var avatarBackground: ImageView
    private lateinit var avatarGender: ImageView
    private lateinit var avatarEyes: ImageView
    private lateinit var avatarMouth: ImageView
    private lateinit var avatarGlasses: ImageView
    private lateinit var avatarShirt: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avatar)

        // Initialize avatar ImageView components
        avatarBackground = findViewById(R.id.avatarBackground)
        avatarGender = findViewById(R.id.avatarGender)
        avatarEyes = findViewById(R.id.avatarEyes)
        avatarMouth = findViewById(R.id.avatarMouth)
        avatarGlasses = findViewById(R.id.avatarGlasses)
        avatarShirt = findViewById(R.id.avatarShirt)

        // Set default avatar components
        avatarBackground.setImageResource(R.drawable.bg_bsu)
        avatarGender.setImageResource(R.drawable.boy)
        avatarEyes.setImageResource(R.drawable.eyes_opened)
        avatarMouth.setImageResource(R.drawable.mouth_closed)
        avatarShirt.setImageResource(R.drawable.shirt)

        // Toggle glasses visibility
        val glassesSwitch: Switch = findViewById(R.id.glasses)
        glassesSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                avatarGlasses.setImageResource(R.drawable.glasses)
            } else {
                avatarGlasses.setImageDrawable(null)
            }
        }

        // Toggle shirt visibility
        val shirtSwitch: Switch = findViewById(R.id.shirt)
        shirtSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                avatarShirt.setImageResource(R.drawable.polo)
            } else {
                avatarShirt.setImageResource(R.drawable.shirt)
            }
        }

        // Gender buttons
        val boyButton: Button = findViewById(R.id.boy)
        val girlButton: Button = findViewById(R.id.girl)
        boyButton.setOnClickListener {
            avatarGender.setImageResource(R.drawable.boy)
        }
        girlButton.setOnClickListener {
            avatarGender.setImageResource(R.drawable.girl)
        }

        // College buttons
        val coeButton: Button = findViewById(R.id.coe)
        val cicsButton: Button = findViewById(R.id.cics)
        val cafadButton: Button = findViewById(R.id.cafad)
        val cetButton: Button = findViewById(R.id.cet)

        coeButton.setOnClickListener {
            avatarBackground.setImageResource(R.drawable.bg_coe)
        }
        cicsButton.setOnClickListener {
            avatarBackground.setImageResource(R.drawable.bg_cics)
        }
        cafadButton.setOnClickListener {
            avatarBackground.setImageResource(R.drawable.bg_cafad)
        }
        cetButton.setOnClickListener {
            avatarBackground.setImageResource(R.drawable.bg_cet)
        }

        // Confirm avatar button
        val confirmButton: Button = findViewById(R.id.confirm_avatar)
        confirmButton.setOnClickListener {
            Toast.makeText(this, "Avatar confirmed!", Toast.LENGTH_SHORT).show()
        }
    }
}
