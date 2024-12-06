package com.example.testtanaw

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.testtanaw.models.UserParcelable
import com.example.testtanaw.util.CRUD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AvatarActivity : AppCompatActivity() {
    private val crud = CRUD()
    private lateinit var avatarBackground: ImageView
    private lateinit var avatarGender: ImageView
    private lateinit var avatarEyes: ImageView
    private lateinit var avatarMouth: ImageView
    private lateinit var avatarGlasses: ImageView
    private lateinit var avatarShirt: ImageView

    private lateinit var bg: String
    private lateinit var sex: String
    private var eyewear: String?= null
    private lateinit var shirtStyle: String

    private var isUploaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avatar)

        val userData: UserParcelable? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("userData", UserParcelable::class.java)
        } else {
            @Suppress("DEPRECATION") // Suppress warning for deprecated method
            intent.getParcelableExtra("userData")
        }

        Log.d("xxxxxx", "AVATAR USERDATA: $userData")

        CoroutineScope(Dispatchers.Main).launch {
            if (userData != null) {
                val userAvatarData = crud.getUserAvatarData(userData.userId)

                Log.d("xxxxxx", "$userAvatarData")

                if (userAvatarData != null) {
                    // Initialize avatar ImageView components
                    avatarBackground = findViewById(R.id.avatarBackground)
                    avatarGender = findViewById(R.id.avatarGender)
                    avatarEyes = findViewById(R.id.avatarEyes)
                    avatarMouth = findViewById(R.id.avatarMouth)
                    avatarGlasses = findViewById(R.id.avatarGlasses)
                    avatarShirt = findViewById(R.id.avatarShirt)

                    // constant
                    avatarEyes.setImageResource(R.drawable.eyes_opened)
                    avatarMouth.setImageResource(R.drawable.mouth_closed)


                    // Set current avatar components
                    if (userAvatarData.eyewear != null) {
                        avatarGlasses.setImageResource(R.drawable.glasses)
                    }

                    when(userAvatarData.shirtStyle) {
                        "shirt" -> avatarShirt.setImageResource(R.drawable.shirt)
                        "polo" -> avatarShirt.setImageResource(R.drawable.polo)
                    }

                    Log.d("xxxxxx", "${userAvatarData.sex}")
                    when(userAvatarData.sex) {
                        "boy" -> avatarGender.setImageResource(R.drawable.boy)
                        "girl" -> avatarGender.setImageResource(R.drawable.girl)
                    }

                    when(userAvatarData.bg) {
                        "cics" -> avatarBackground.setImageResource(R.drawable.bg_cics)
                        "cet" -> avatarBackground.setImageResource(R.drawable.bg_cet)
                        "coe" -> avatarBackground.setImageResource(R.drawable.bg_coe)
                        "cafad" -> avatarBackground.setImageResource(R.drawable.bg_cafad)
                    }


                    // Toggle glasses visibility
                    val glassesSwitch: Switch = findViewById(R.id.glasses)
                    glassesSwitch.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            eyewear = "glasses"
                            avatarGlasses.setImageResource(R.drawable.glasses)
                        } else {
                            eyewear = null
                            avatarGlasses.setImageDrawable(null)
                        }
                    }


                    // Toggle shirt visibility
                    val shirtSwitch: Switch = findViewById(R.id.shirt)
                    shirtSwitch.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            shirtStyle = "polo"
                            avatarShirt.setImageResource(R.drawable.polo)
                        } else {
                            shirtStyle = "shirt"
                            avatarShirt.setImageResource(R.drawable.shirt)
                        }
                    }


                    // Gender buttons
                    val boyButton: Button = findViewById(R.id.boy)
                    val girlButton: Button = findViewById(R.id.girl)
                    boyButton.setOnClickListener {
                        sex = "boy"
                        avatarGender.setImageResource(R.drawable.boy)
                    }
                    girlButton.setOnClickListener {
                        sex = "girl"
                        avatarGender.setImageResource(R.drawable.girl)
                    }


                    // College buttons
                    val coeButton: Button = findViewById(R.id.coe)
                    val cicsButton: Button = findViewById(R.id.cics)
                    val cafadButton: Button = findViewById(R.id.cafad)
                    val cetButton: Button = findViewById(R.id.cet)

                    coeButton.setOnClickListener {
                        bg = "coe"
                        avatarBackground.setImageResource(R.drawable.bg_coe)
                    }
                    cicsButton.setOnClickListener {
                        bg = "cics"
                        avatarBackground.setImageResource(R.drawable.bg_cics)
                    }
                    cafadButton.setOnClickListener {
                        bg = "cafad"
                        avatarBackground.setImageResource(R.drawable.bg_cafad)
                    }
                    cetButton.setOnClickListener {
                        bg = "cet"
                        avatarBackground.setImageResource(R.drawable.bg_cet)
                    }

                    // Confirm avatar button
                    val confirmButton: Button = findViewById(R.id.confirm_avatar)
                    confirmButton.setOnClickListener {
                        Log.d("xxxxxx", "$eyewear, $shirtStyle, $sex, $bg")

                        CoroutineScope(Dispatchers.Main).launch {
                            val newUrl= crud.saveAvatar(resources, userData.userId, eyewear, shirtStyle, sex, bg)

                            Log.d("xxxxxx", "$newUrl")

                            val intent = Intent(
                                this@AvatarActivity,
                                ProfileActivity::class.java
                            )
//                            intent.putExtra("userData", newUserData)
                            startActivity(intent)
                            finish()

                        }

                    }
                } else {
                    Toast.makeText(this@AvatarActivity, "NO CUR AVATAR", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
