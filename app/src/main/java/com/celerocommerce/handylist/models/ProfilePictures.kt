package com.celerocommerce.handylist.models

import android.os.Parcel
import android.os.Parcelable

data class ProfilePictures(
    val large: String?,
    val medium: String?,
    val thumbnail: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(large)
        parcel.writeString(medium)
        parcel.writeString(thumbnail)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProfilePictures> {
        override fun createFromParcel(parcel: Parcel): ProfilePictures {
            return ProfilePictures(parcel)
        }

        override fun newArray(size: Int): Array<ProfilePictures?> {
            return arrayOfNulls(size)
        }
    }
}