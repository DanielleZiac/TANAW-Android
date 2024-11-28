package com.example.tanaw

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val signupBtn = findViewById<View>(R.id.signupBtn)
        signupBtn.setOnClickListener(
            View.OnClickListener {
                val i = Intent(
                    this@MainActivity,
                    Signup::class.java
                )
                startActivity(i)
            }
        )

        val loginBtn = findViewById<View>(R.id.loginBtn)
        loginBtn.setOnClickListener(
            View.OnClickListener {
                val i = Intent(
                    this@MainActivity,
                    Login::class.java
                )
                startActivity(i)
            }
        )

        val galleryBtn = findViewById<View>(R.id.galleryBtn)
        galleryBtn.setOnClickListener(
            View.OnClickListener {
                Log.d("tag", "heree")
                val i = Intent(
                    this@MainActivity,
                    SdgGallery::class.java
                )
                startActivity(i)
            }
        )

        val profileBtn = findViewById<View>(R.id.profileBtn)
        profileBtn.setOnClickListener(
            View.OnClickListener {
                Log.d("tag", "heree")
                val i = Intent(
                    this@MainActivity,
                    Profile::class.java
                )
                startActivity(i)
            }
        )

        val contentBtn = findViewById<View>(R.id.contentBtn)
        contentBtn.setOnClickListener(
            View.OnClickListener {
                Log.d("tag", "heree")
                val i = Intent(
                    this@MainActivity,
                    SdgContent::class.java
                )
                startActivity(i)
            }
        )

        val uploadBtn = findViewById<View>(R.id.uploadBtn)
        uploadBtn.setOnClickListener(
            View.OnClickListener {
                Log.d("tag", "heree upload")
                val i = Intent(
                    this@MainActivity,
                    SdgUpload::class.java
                )
                startActivity(i)
            }
        )
    }
}

