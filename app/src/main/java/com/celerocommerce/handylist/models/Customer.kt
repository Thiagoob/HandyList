package com.celerocommerce.handylist.models

import android.os.Parcel
import android.os.Parcelable

data class Customer(
    val id: Long,
    val visitOrder: Int,
    val name: String?,
    val phoneNumber: String?,
    val profilePictures: ProfilePictures?,
    val location: Location?,
    val serviceReason: String?,
    val problemPictures: List<String>?,
    var isHandled: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(ProfilePictures::class.java.classLoader),
        parcel.readParcelable(Location::class.java.classLoader),
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeInt(visitOrder)
        parcel.writeString(name)
        parcel.writeString(phoneNumber)
        parcel.writeParcelable(profilePictures, flags)
        parcel.writeParcelable(location, flags)
        parcel.writeString(serviceReason)
        parcel.writeStringList(problemPictures)
        parcel.writeByte(if (isHandled) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Customer> {
        override fun createFromParcel(parcel: Parcel): Customer {
            return Customer(parcel)
        }

        override fun newArray(size: Int): Array<Customer?> {
            return arrayOfNulls(size)
        }
    }
}