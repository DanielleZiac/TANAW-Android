package com.example.tanaw

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
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put


@Serializable
data class SdgPhoto (
    @SerialName("avatar_url") val avatarUrl: String,
    @SerialName("caption") val caption: String,
    @SerialName("created_date") val createdDate: String,
    @SerialName("sdg_number") val sdgNumber: String,
    @SerialName("url") val url: String,
    @SerialName("user_sdg_id") val userSdgId: String
)


class SdgContent : AppCompatActivity() {
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
            val sdgPhoto = getPhotoSdg(sdg)         // xxxxxxxxxxxxxxxxx

//            Log.d("tag", sdgPhoto.toString())
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