package com.example.testtanaw.util

import android.location.Location
import android.util.Log
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

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
}