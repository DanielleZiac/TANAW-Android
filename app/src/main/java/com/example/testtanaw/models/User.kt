package com.example.testtanaw.models

import android.os.Parcel
import android.os.Parcelable


class User(
    userId: String?,
    email: String?,
    srCode: String?,
    firstName: String?,
    lastName: String?,
    institution: String?,
    campus: String?,
    department: String?,
    avatarUrl: String?
) : Parcelable {

    private var userId: String?
    private var email: String?
    private var srCode: String?
    private var firstName: String?
    private var lastName: String?
    private var institution: String?
    private var campus: String?
    private var department: String?
    private var avatarUrl: String?

    constructor(parcel: Parcel) : this(
        userId = parcel.readString(),
        email = parcel.readString(),
        srCode = parcel.readString(),
        firstName = parcel.readString(),
        lastName = parcel.readString(),
        institution = parcel.readString(),
        campus = parcel.readString(),
        department = parcel.readString(),
        avatarUrl = parcel.readString()
    ) {

    }

    init {
        this.userId = userId
        this.email = email
        this.srCode = srCode
        this.firstName = firstName
        this.lastName = lastName
        this.institution = institution
        this.campus = campus
        this.department = department
        this.avatarUrl = avatarUrl
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(email)
        parcel.writeString(srCode)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(institution)
        parcel.writeString(campus)
        parcel.writeString(department)
        parcel.writeString(avatarUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }

    fun getAvatarUrl(): String? {
        return avatarUrl
    }

    fun setAvatarUrl(avatarUrl: String) {
        this.avatarUrl = avatarUrl
    }

    fun getCREATOR(): Parcelable.Creator<User> {
        return CREATOR
    }

    fun getEmail(): String? {
        return email
    }

    fun setEmail(email: String?) {
        this.email = email
    }

    fun getUserId(): String? {
        return userId
    }

    fun setUserId(userId: String) {
        this.userId = userId
    }

    fun getSrCode(): String? {
        return srCode
    }

    fun setSrCode(userId: String) {
        this.srCode = srCode
    }

    fun getFirstName(): String? {
        return firstName
    }

    fun setFirstName(firstName: String?) {
        this.firstName = firstName
    }

    fun getLastName(): String? {
        return lastName
    }

    fun setLastName(lastName: String?) {
        this.lastName = lastName
    }

    fun getInstitution(): String? {
        return institution
    }

    fun setInstitution(institution: String?) {
        this.institution = institution
    }

    fun getCampus(): String? {
        return campus
    }

    fun setCampus(campus: String?) {
        this.campus = campus
    }

    fun getDepartment(): String? {
        return department
    }

    fun setDepartment(department: String?) {
        this.department = department
    }

    override fun toString(): String {
        return "User{" +
                "userId='" + userId + '\'' +
                ", email=" + email + '\'' +
                ", srCode=" + srCode + '\'' +
                ", firstName=" + firstName + '\'' +
                ", lastName=" + lastName + '\'' +
                ", institution=" + institution + '\'' +
                ", campus=" + campus + '\'' +
                ", department=" + department + '\'' +
                ", avatarUrl=" + avatarUrl + '\''
    }
}