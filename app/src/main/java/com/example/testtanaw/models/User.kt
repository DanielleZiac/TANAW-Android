package com.example.testtanaw.models

import android.os.Parcel
import android.os.Parcelable


data class UserParcelable(
    var userId: String,
    var email: String,
    var srCode: String,
    var firstName: String,
    var lastName: String,
    var institution: String,
    var institutionLogo: String,
    var campus: String,
    var department: String? = null,
    var avatarUrl: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        userId = parcel.readString()!!,
        email = parcel.readString()!!,
        srCode = parcel.readString()!!,
        firstName = parcel.readString()!!,
        lastName = parcel.readString()!!,
        institution = parcel.readString()!!,
        institutionLogo = parcel.readString()!!,
        campus = parcel.readString()!!,
        department = parcel.readString(),
        avatarUrl = parcel.readString()
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(userId)
        dest.writeString(email)
        dest.writeString(srCode)
        dest.writeString(firstName)
        dest.writeString(lastName)
        dest.writeString(institution)
        dest.writeString(institutionLogo)
        dest.writeString(campus)
        dest.writeString(department)
        dest.writeString(avatarUrl)
    }

    companion object CREATOR : Parcelable.Creator<UserParcelable> {
        override fun createFromParcel(parcel: Parcel): UserParcelable {
            return UserParcelable(parcel)
        }

        override fun newArray(size: Int): Array<UserParcelable?> {
            return arrayOfNulls(size)
        }
    }
}

