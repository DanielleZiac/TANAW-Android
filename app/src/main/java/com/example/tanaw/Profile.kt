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
data class InstitutionProfile(
    @SerialName("campus") val campus: String,
    @SerialName("institution") val institution: String,
)

@Serializable
data class UserProfile(
    @SerialName("sr_code") val srCode: String,
    @SerialName("first_name") val firstName: String,
    @SerialName("last_name") val lastName: String,
    @SerialName("institutions") val institutions: InstitutionProfile,
    @SerialName("department") val department: String,
    @SerialName("avatar_url") val avatarUrl: String
)

class Profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        CoroutineScope(Dispatchers.Main).launch {
            val user_id = intent.getStringExtra("user_id")
//            getUserById(user_id.toString())
//            Log.d("tag", user_id.toString())

            val user = getUserById(user_id.toString())    // xxxxx
            Log.d("tag", user.toString())
        }

    }

    private suspend fun getUserById(user_id: String): UserProfile? {
        return try {

            val columns = Columns.raw("""
                sr_code,
                first_name,
                last_name,
                institutions (
                  institution,
                  campus
                ),
                department,
                avatar_url
            """.trimIndent())

            val user = supabase
                .from("users")
                .select(columns = columns)
                { filter { eq("user_id", user_id) }}
                .decodeSingle<UserProfile>()

            user
        } catch (e: Exception) {
            Log.d("tag", "error getuserbyid ${e.message}")
            null
        }
    }
}
