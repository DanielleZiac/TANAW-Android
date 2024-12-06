package com.example.testtanaw.util

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import com.example.testtanaw.R
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.UUID


class CRUD: Supabase() {
    @Serializable
    data class SdgPhoto (
        @SerialName("user_sdg_id") val userSdgId: String,
        @SerialName("user_id") val userId: String,
        @SerialName("sdg_number") val sdgNumber: String,
        @SerialName("url") val url: String,
        @SerialName("caption") val caption: String,
        @SerialName("created_date") val createdDate: String,
        @SerialName("institution_id") val institutionId: String,
        @SerialName("photo_challenge") val phototChall: String,
        @SerialName("institution") val institution: String,
        @SerialName("campus") val campus: String,
        @SerialName("institution_logo") val institutionLogo: String,
        @SerialName("lat") val lat: Double,
        @SerialName("long") val long: Double,
        @SerialName("avatar_url") val avatarUrl: String,
    )

    @Serializable
    private data class UserLocation (
        @SerialName("user_id") val userId: String,
        @SerialName("user_latitude") val userLatitude: Double,
        @SerialName("user_longitude") val userLongitude: Double
    )

    @Serializable
    data class UserAvatarData(
        @SerialName("avatar_id") val avatarId: String?,
        @SerialName("user_id") val userId: String?,
        @SerialName("avatar_url") val avatarUrl: String,
        @SerialName("bg") val bg: String,
        @SerialName("eye") val eye: String?,
        @SerialName("sex") val sex: String,
        @SerialName("shirt_style") val shirtStyle: String,
        @SerialName("smile") val smile: String?,
        @SerialName("eyewear") val eyewear: String?
    )


    @Serializable
    private data class UserAvatar (
        @SerialName("user_id") val userId: String,
        @SerialName("avatar_url") val avatarUrl: String,
        @SerialName("bg") val bg: String,
        @SerialName("eye") val eye: String,
        @SerialName("sex") val sex: String,
        @SerialName("shirt_style") val shirtStyle: String,
        @SerialName("smile") val smile: String,
        @SerialName("eyewear") val eyewear: String?
    )

    @Serializable
    data class LeaderboardSchool(
        @SerialName("institution_id") val institutionId: String,
        @SerialName("institution") val institution: String,
        @SerialName("campus") val campus: String,
        @SerialName("department_logo") val departmentLogo: String,
        @SerialName("department") val department: String,
        @SerialName("count") val count: String
    )

    suspend fun getSdgPhoto(sdg: Int?, date: String?, institution_id: String?): List<SdgPhoto> {
            val qryParams = mutableMapOf<String, Any>()


            // filter by sdg number
            if (sdg != null) {
                qryParams["sdg_number"] = "sdg${sdg}"
            }

            // filter by institution id
            if (institution_id != null) {
                qryParams["institution_id"] = institution_id
            }

            // filter by date
            val sdf = SimpleDateFormat("yyyy-M-dd")
            val currentDate = sdf.format(Date())
            val calendar = Calendar.getInstance()


            when(date) {
                "all", null -> {
                    Log.d("xxxxxx", "no date filter")
                }
                "today" -> {
                    qryParams["created_date"] = currentDate
                }
                "yesterday" -> {
                    // Subtract one day from the current date
                    calendar.add(Calendar.DAY_OF_YEAR, -1)
                    val yesterday = sdf.format(calendar.time)

                    qryParams["created_date"] = yesterday
                }
                "last week" -> {
                    calendar.add(Calendar.DAY_OF_YEAR, -7)
                    val lastWeek = sdf.format(calendar.time)

                    qryParams["gte"] = lastWeek
                    qryParams["lte"] = currentDate
                }
                "last month" -> {
                    calendar.set(Calendar.DAY_OF_MONTH, 1) // Set the day to the first day of the month
                    val firstDayOfMonth = sdf.format(calendar.time)

                    calendar.add(Calendar.MONTH, -1)
                    calendar.set(Calendar.DAY_OF_MONTH, 1) // Set the day to the first day of the month
                    val firstDayOfLastMonth = sdf.format(calendar.time)

                    qryParams["gte"] = firstDayOfLastMonth
                    qryParams["lt"] = firstDayOfMonth
                }
                else -> {
                    Log.d("xxxxxx", "invalid filter date")
                }
            }

            return try {
                val sdgPhoto = supabase
                    .from("get_photo_and_avatar")
                    .select() {
                        filter {
                            qryParams.forEach{ (key, value) ->
                                when {
                                    key.equals("gte") -> this.gte("created_date", value)
                                    key.equals("lte") -> this.lte("created_date", value)
                                    key.equals("lt") -> this.lt("created_date", value)
                                    else -> this.eq(key, value)
                                }
                            }
                        }
                    }.decodeList<SdgPhoto>()

//                if (sdgPhoto.isNotEmpty()) {
//                    Log.d("xxxxxx", "$sdgPhoto")
//                }

                sdgPhoto
            } catch (e: Exception) {
                Log.d("xxxxxx", "ERROR getSdgPhoto: ${e.message}")
                emptyList()
            }
    }


    fun saveUserLastLocation(userId: String, latitude: Double, longitude: Double) {
        CoroutineScope(Dispatchers.Main).launch {
            Log.d("tag", "hereee savedIserlastLocation")
            try {
                val user_location = UserLocation(
                    userId = userId,
                    userLatitude = latitude,
                    userLongitude = longitude
                )
                supabase.from("user_location").upsert(user_location) {
                    this.onConflict = "user_id"
                }
            } catch (e: Exception) {
                Log.d("tag", "error saveUserLastLocation: ${e.message}")
            }
        }
    }


    suspend fun getPhotoByUserId(userId: String): List<SdgPhoto>? {
        return try {
            val photos = supabase.from("get_photo_and_avatar").select() {
                filter {
                    eq("user_id", userId)
                }
            }.decodeList<SdgPhoto>()
            Log.d("xxxxxx", "photoooo ${photos}")
            photos
        } catch (e: Exception) {
            Log.d("xxxxxx", "${e.message}")
            null
        }
    }


    suspend fun getPhotoByInstitutionId(institutionId: String): List<SdgPhoto>? {
        return try {
            val photos = supabase.from("get_photo_and_avatar").select() {
                filter {
                    eq("institution_id", institutionId)
                }
            }.decodeList<SdgPhoto>()
            Log.d("xxxxxx", "photoooo ${photos}")
            photos

            emptyList()
        } catch (e: Exception) {
            Log.d("xxxxxx", "ERROR GET PHOTO BY INSTITUTION ID: ${e.message}")
            emptyList()
        }
    }


    suspend fun getUserAvatarData(userId: String): UserAvatarData? {
        return try {
            val userAvatarData = supabase.from("avatars").select() {
                filter {
                    eq("user_id", userId)
                }
            }.decodeSingle<UserAvatarData>()

            userAvatarData
        } catch (e: Exception) {
            Log.d("xxxxxx", "${e.message}")
            null
        }
    }

    private fun combineImg(resources: Resources, eyewear: String?, shirtStyle: String, sex: String, bg: String): ByteArray? {
        // Load the PNG images as Bitmaps
        lateinit var bgBitmap: Bitmap
        lateinit var sexBitmap: Bitmap
        lateinit var shirtStyleBitmap: Bitmap
        lateinit var eyewearBitmap: Bitmap
        val eyeBitmap = BitmapFactory.decodeResource(resources, R.drawable.eyes_opened)
        val smileBitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.mouth_closed)

        if (eyewear != null) {
            eyewearBitmap = BitmapFactory.decodeResource(resources, R.drawable.glasses)
        }

        when (shirtStyle) {
            "shirt" -> shirtStyleBitmap = BitmapFactory.decodeResource(resources, R.drawable.shirt)
            "polo" -> shirtStyleBitmap = BitmapFactory.decodeResource(resources, R.drawable.polo)
        }

        when (sex) {
            "boy" -> sexBitmap = BitmapFactory.decodeResource(resources, R.drawable.boy)
            "girl" -> sexBitmap = BitmapFactory.decodeResource(resources, R.drawable.girl)
        }

        when(bg) {
            "cics" -> bgBitmap = BitmapFactory.decodeResource(resources, R.drawable.bg_cics)
            "coe" -> bgBitmap = BitmapFactory.decodeResource(resources, R.drawable.bg_coe)
            "cet" -> bgBitmap = BitmapFactory.decodeResource(resources, R.drawable.bg_cet)
            "cafad" -> bgBitmap = BitmapFactory.decodeResource(resources, R.drawable.bg_cafad)
        }

        // Create a blank bitmap with the size of the base image
        val overlayedBitmap = Bitmap.createBitmap(bgBitmap.width, bgBitmap.height, Bitmap.Config.ARGB_8888)

        // Create a Canvas to draw on the overlayedBitmap
        val canvas = Canvas(overlayedBitmap)

        // Draw the base image
        canvas.drawBitmap(bgBitmap, 0f, 0f, null)

        // Draw the overlay images on top of the base image
        canvas.drawBitmap(sexBitmap, 0f, 0f, null)
        canvas.drawBitmap(shirtStyleBitmap, 0f, 0f, null)
        if (eyewear != null) {
            canvas.drawBitmap(eyewearBitmap, 0f, 0f, null)
        }
        canvas.drawBitmap(eyeBitmap, 0f, 0f, null)
        canvas.drawBitmap(smileBitmap, 0f, 0f, null)

        // Convert the bitmap to ByteArray
        val byteArrayOutputStream = ByteArrayOutputStream()
        overlayedBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }


    private suspend fun uploadAvatar(byteArray: ByteArray, userId: String): String? {
        val path = "$userId/${UUID.randomUUID()}.png"
        var publicUrl: String

        return try {
            val bucket = supabase.storage.from("avatars")

            Log.d("xxxxxx", "path: $path")

            if (byteArray != null) {
                // upload photo to storage
                bucket.upload(path, byteArray)
                publicUrl = supabase.storage.from("avatars").publicUrl(path)

                publicUrl
            } else {
                null
            }
        } catch (e: Exception) {
            Log.d("xxxxxx", "ERROR UPLOAD AVATAR: ${e.message}")

            null
        }
    }


    private suspend fun updateAvatar(userId: String, publicUrl: String, eyewear: String?, shirtStyle: String, sex: String, bg: String) {
        try {
            val userAvatar = UserAvatar(
                userId = userId,
                avatarUrl = publicUrl,
                bg = bg,
                eye = "eyes_opened",
                sex = sex,
                shirtStyle = shirtStyle,
                smile = "mouth_closed",
                eyewear =  eyewear
            )

            supabase.from("avatars").upsert(userAvatar) { onConflict="user_id"}
        } catch (e: Exception) {
            Log.d("xxxxxx", "ERROR UPDATE AVATAR: ${e.message}")
        }
    }


    suspend fun saveAvatar(resources: Resources, userId: String, eyewear: String?, shirtStyle: String, sex: String, bg: String): String? {
        var isUploaded = false

        // combine img into one
        val imageData = combineImg(resources, eyewear, shirtStyle, sex, bg)

        withContext(Dispatchers.IO) {
            // upload and fetch public url
            if (imageData != null) {
                val publicUrl = uploadAvatar(imageData, userId)

                Log.d("xxxxxx", "PUBLIC URL: $publicUrl")

                // update user avatar
                if (publicUrl != null) {
                    updateAvatar(userId, publicUrl, eyewear, shirtStyle, sex, bg)
                    Log.d("xxxxxx", "HERE NO ERROR????")
                    return@withContext publicUrl
                } else {
                }
            } else {
            }
        }
        return null
    }

    suspend fun getLeaderboardsSchools(institututionId: String): List<LeaderboardSchool> {
        val data = supabase.from("leaderboards_schools").select() {
            filter {
                eq("inistitution_id", institututionId)
            }
        }.decodeList<LeaderboardSchool>()

        return data
    }
}