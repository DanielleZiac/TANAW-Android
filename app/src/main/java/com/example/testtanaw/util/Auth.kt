package com.example.testtanaw.util

import android.R
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class DB : Supabase() {
    @Serializable
    data class Institution(
        @SerialName("institution") val institution: String,
        @SerialName("institution_id") val institutionId: String,
        @SerialName("email_extension") val emailExtension: String
    )

    @Serializable
    private data class UserDataLogin(
        @SerialName("sr_code") val srCode: String?,
        @SerialName("email") val email: String?,
        @SerialName("first_name") val firstName: String?,
        @SerialName("last_name") val lastName: String?,
        @SerialName("institution_id") val institutionId: String?,
    )

    @Serializable
    data class UserData(
        @SerialName("user_id") val userId: String,
        @SerialName("email") val email: String,
        @SerialName("sr_code") val srCode: String,
        @SerialName("first_name") val firstName: String,
        @SerialName("last_name") val lastName: String,
        @SerialName("institutions") val institutions: HashMap<String, String>,
        @SerialName("departments") val departments: HashMap<String, String>? = null,
        @SerialName("avatars") val avatars: HashMap<String, String>? = null
    )


    private suspend fun getUserId(): String? {
        return try {
            val user = supabase.auth.currentUserOrNull()

            if (user != null) {
                Log.d("xxxxxx", "user_idddd: ${user.id}")

                // cur user has no data -- upsert data to db
                val user_data = supabase
                    .from("users")
                    .select() {
                        filter { eq("user_id", user.id) }
                    }.decodeList<UserDataLogin>()

                if (user_data.isEmpty()) {
                    Log.d("xxxxxx", "$user")
                    val add_user = UserDataLogin(
                        srCode = user.identities?.firstOrNull()?.identityData?.get("srCode")
                            .toString(),
                        email = user.email,
                        firstName = user.identities?.firstOrNull()?.identityData?.get("firstName")
                            .toString(),
                        lastName = user.identities?.firstOrNull()?.identityData?.get("lastName")
                            .toString(),
                        institutionId = user.identities?.firstOrNull()?.identityData?.get("institutionId")
                            .toString()
                    )
                    supabase.from("users").upsert(add_user)
                } else {
                    Log.d("xxxxxx", "user_data: $user_data")
                }

                user.id
            } else {
                Log.d("xxxxxx", "no user found")
                null
            }
        } catch (e: Exception) {
            Log.d("xxxxxx", "error getUser ${e.message}")
            null
        }
    }

    suspend fun getInstitutions(context: Context): List<Institution> {
        return try {
            val results = withContext(Dispatchers.IO) {
                supabase
                    .from("institutions")
                    .select(
                        columns = Columns.list(
                            "institution",
                            "email_extension",
                            "institution_id"
                        )
                    )
                    .decodeList<Institution>()
            }

            results

        } catch (e: Exception) {
            Toast.makeText(context, "Error fetching data: ${e.message}", Toast.LENGTH_SHORT).show()
            emptyList()
        }
    }

    suspend fun login(emailInp: String, passwordInp: String, context: Context): String? {
        return try {
            val user = supabase.auth.signInWith(Email) {
                email = emailInp
                password = passwordInp
            }

            Log.d("xxxxxx", user.toString())
            val user_id = getUserId();

            user_id
        } catch (e: Exception) {
            Toast.makeText(context, "Error signing up: ${e.message}", Toast.LENGTH_SHORT).show()
            Log.d("xxxxxx", "Error login: ${e.message}")
            null
        }
    }

    suspend fun getUserDataByUserId(userId: String): UserData? {
        val columns = Columns.raw(
            """
                user_id,
                email,
                sr_code,
                first_name,
                last_name,
                institutions (
                    institution,
                    institution_logo,
                    campus
                ),
                departments (
                    department
                ),
                avatars (
                    avatar_url
                )
            """.trimIndent()
        )
        return try {
            Log.d("xxxxxx", "hzxcxasd $userId")
            val userData = supabase
                .from("users")
                .select(columns = columns) {
                    filter {
                        eq("user_id", userId)
                    }
                }.decodeSingle<UserData>()

            Log.d("xxxxxx", "user dataaa: $userData")
            userData
        } catch (e: Exception) {
            Log.d("xxxxxx", "erroreeee get user ${e.message}")
            null
        }
    }
}