package com.example.testtanaw.util

import android.content.Context
import android.util.Log
import android.widget.Spinner
import android.widget.Toast
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import java.lang.reflect.Array.set
import java.util.UUID

class DB : Supabase() {
    private val defaultAvatarUrl = "https://srxhcymqociarjinmkpp.supabase.co/storage/v1/object/public/avatars/default/d8598e52-34b0-4f96-b7a7-e0ff3df7b2cb"
    @Serializable
    data class Institution(
        @SerialName("institution") val institution: String,
        @SerialName("institution_id") val institutionId: String,
        @SerialName("email_extension") val emailExtension: String
    )

    @Serializable
    private data class UserDataLogin(
        @SerialName("sr_code") val srCode: JsonElement?,
        @SerialName("email") val email: String?,
        @SerialName("first_name") val firstName: JsonElement?,
        @SerialName("last_name") val lastName: JsonElement?,
        @SerialName("institution_id") val institutionId: JsonElement?
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

    @Serializable
    private data class DefaultAvatar(
        @SerialName("user_id") val userId: String,
        @SerialName("avatar_url") val avatarUrl: String
    )

    @Serializable
    private data class DefaultDepartment(
        @SerialName("department_id") val departmentId: String
    )

    @Serializable
    private data class UserIntitutionId(
        @SerialName("institution_id") val institutionId: String
    )



    private suspend fun getDefaultDepartment(userId: Any): String? {
        return try {
            val institutionId = supabase.from("users").select(columns = Columns.list("institution_id")) {
                filter {
                    eq("user_id", userId)
                }
            }.decodeSingle<UserIntitutionId>()

            val defaultDepartment = supabase.from("dept_id").select(columns = Columns.list("department_id")) {
                filter {
                    eq("institution_id", institutionId.institutionId)
                }
            }.decodeSingle<DefaultDepartment>()

            defaultDepartment.departmentId
        } catch (e: Exception) {
            Log.d("xxxxxx", "ERROR GET DEFAULT DEPT: ${e.message}")
            null
        }
    }

    private suspend fun getUserId(): String? {
        return try {
            val user = supabase.auth.currentUserOrNull()

            if (user != null) {
                // cur user has no data -- upsert data to db
                val userData = supabase
                    .from("users")
                    .select() {
                        filter { eq("user_id", user.id) }
                    }.decodeList<UserDataLogin>()

                if (userData.isEmpty()) {
                    val addUser = UserDataLogin(
                        srCode = user.identities?.firstOrNull()?.identityData?.get("srCode"),
                        email = user.email,
                        firstName = user.identities?.firstOrNull()?.identityData?.get("firstName"),
                        lastName = user.identities?.firstOrNull()?.identityData?.get("lastName"),
                        institutionId = user.identities?.firstOrNull()?.identityData?.get("institutionId")
                    )

                    // add info from signin input
                    supabase.from("users").upsert(addUser) { onConflict="user_id"}

                    val addDefaultAvatar = DefaultAvatar(
                        userId = user.id,
                        avatarUrl = defaultAvatarUrl
                    )

                    // add default avatar
                    supabase.from("avatars").upsert(addDefaultAvatar) { onConflict="user_id"}

                    val cicsDeptId = getDefaultDepartment(user.id)


                    // add default department - cics
                    supabase.from("users").update({
                        DefaultDepartment::departmentId setTo cicsDeptId
                    }) {
                        filter {
                            eq("user_id", user.id)
                        }
                    }

                } else {
                    Log.d("xxxxxx", "user_data: $userData")
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

    suspend fun getInstitutions(context: Context, institutionSpinner: Spinner): List<Institution> {
        lateinit var institutionAdapter: InstitutionAdapter

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

            // Check if institutions are available
            if (results.isNotEmpty()) {
                // Create a list of institution names to display in the Spinner
                val institutionNames = results.map { it.institution }.toMutableList()
                institutionAdapter = InstitutionAdapter(context, institutionNames)
                institutionSpinner.adapter = institutionAdapter

                // Ensure the spinner has a valid selection by setting a default item
                institutionSpinner.setSelection(0)
            } else {
                // Handle the case where institutions are unavailable
                Toast.makeText(context, "No institutions available.", Toast.LENGTH_SHORT).show()
                Log.d("xxxxxx", "no institutions available")
            }

            results

        } catch (e: Exception) {
            Toast.makeText(context, "Error fetching data: ${e.message}", Toast.LENGTH_SHORT).show()
            Log.d("xxxxxx", "${e.message}")
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

    suspend fun signup(firstName: String, lastName: String, srCode: String, passwordInp: String, emailExtension: String, institutionId: String): UserInfo? {
        val emailInp = srCode + emailExtension

        Log.d("xxxxxx", "$firstName, $lastName, $srCode, $passwordInp, $emailExtension, $emailInp, $institutionId")

        return try {
            val user = supabase.auth.signUpWith(Email) {
                email = emailInp
                password = passwordInp
                data = buildJsonObject {
                    put("srCode", srCode)
                    put("firstName", firstName)
                    put("lastName", lastName)
                    put("institutionId", institutionId)
                }
            }

            user
        } catch (e: Exception) {
            Log.d("xxxxxx", "error signup ${e.message}")
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