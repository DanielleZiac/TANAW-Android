package com.example.tanaw

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put


class SdgContent : AppCompatActivity() {
    lateinit var sdgPhoto: List<SdgPhoto>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sdg_content)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        CoroutineScope(Dispatchers.Main).launch {
            val user_id = intent.getStringExtra("user_id")
            val sdg = 1;
            sdgPhoto = getPhotoSdg(sdg)         // xxxxxxxxxxxxxxxxx
            Log.d("tag", sdgPhoto.toString())

            val mapBtn: Button = findViewById(R.id.mapBtn)
            mapBtn.setOnClickListener(View.OnClickListener {
                val i = Intent(
                    this@SdgContent,
                    Maps::class.java
                )
                val jsonString = Json.encodeToString(ListSerializer(SdgPhoto.serializer()), sdgPhoto)
                i.putExtra("sdgPhoto", jsonString)
                startActivity(i)
            })

        }
    }

    private suspend fun getPhotoSdg(sdg: Int): List<SdgPhoto> {
        return try {
            val sdgPhoto = supabase
                .from("get_photo_and_avatar")
                .select() {
                    filter { eq("sdg_number", "sdg${sdg}") }
                }.decodeList<SdgPhoto>()

            sdgPhoto
        } catch (e: Exception) {
            Log.d("tag", "error getPhotoSdg ${e.message}")
            emptyList()
        }

    }
}