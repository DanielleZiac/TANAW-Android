package com.example.tanaw

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileSdg(
    @SerialName("caption") val caption: String,
    @SerialName("created_at") val created_at: String,
    @SerialName("url") val url: String,
    @SerialName("user_sdg_id") val user_sdg_id: String,
    @SerialName("user_id") val user_id: String,
    )




///// PROFILE GALLERY PALA TOOOO
class SdgGallery : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sdg_gallery)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        CoroutineScope(Dispatchers.Main).launch {
            val user_id = getUserId();

            if (user_id == "") {
                val i = Intent(
                    this@SdgGallery,
                    Login::class.java
                )
                startActivity(i)
            } else {
                val photos = getPhotoByUserId(user_id)   // xxxxxxxxxx
            }
        }
    }

    private suspend fun getPhotoByUserId(user_id: String): List<ProfileSdg> {
        Log.d("tag", user_id)

        return try {
            val photos = supabase
                .from("user_sdgs")
                .select(columns = Columns.list("user_sdg_id", "url", "caption", "created_at", "user_id")) {
                    order(column = "created_at", order = Order.DESCENDING)
                    filter {
                        eq("user_id", user_id)
                    }
                }.decodeList<ProfileSdg>()

            Log.d("tag", photos.toString())

            photos
        } catch (e: Exception) {
            Log.d("tag", "error getPhotoByUserId ${e.message}")
            emptyList()
        }
    }
}
