package com.celerocommerce.handylist.models

import android.os.Parcel
import android.os.Parcelable

data class Location(
    val address: Address?,
    val coordinates: Coordinates?
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Address::class.java.classLoader),
        parcel.readParcelable(Coordinates::class.java.classLoader)
    ) {
    }

    data class Address(
        val street: String?,
        val city: String?,
        val state: String?,
        val postalCode: String?,
        val country: String?
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(street)
            parcel.writeString(city)
            parcel.writeString(state)
            parcel.writeString(postalCode)
            parcel.writeString(country)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Address> {
            override fun createFromParcel(parcel: Parcel): Address {
                return Address(parcel)
            }

            override fun newArray(size: Int): Array<Address?> {
                return arrayOfNulls(size)
            }
        }
    }

    data class Coordinates(
        val latitude: Double,
        val longitude: Double
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readDouble(),
            parcel.readDouble()
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeDouble(latitude)
            parcel.writeDouble(longitude)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Coordinates> {
            override fun createFromParcel(parcel: Parcel): Coordinates {
                return Coordinates(parcel)
            }

            override fun newArray(size: Int): Array<Coordinates?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(address, flags)
        parcel.writeParcelable(coordinates, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Location> {
        override fun createFromParcel(parcel: Parcel): Location {
            return Location(parcel)
        }

        override fun newArray(size: Int): Array<Location?> {
            return arrayOfNulls(size)
        }
    }
}