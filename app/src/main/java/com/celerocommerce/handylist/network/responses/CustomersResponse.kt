package com.celerocommerce.handylist.network.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CustomerNetworkEntity(
    @SerializedName("identifier")
    @Expose
    val id: Long,

    @SerializedName("visitOrder")
    @Expose
    val visitOrder: Int,

    @SerializedName("name")
    @Expose
    val name: String,

    @SerializedName("phoneNumber")
    @Expose
    val phoneNumber: String,

    @SerializedName("profilePicture")
    @Expose
    val profilePicturesNetworkEntity: ProfilePicturesNetworkEntity,

    @SerializedName("location")
    @Expose
    val locationNetworkEntity: LocationNetworkEntity,

    @SerializedName("serviceReason")
    @Expose
    val serviceReason: String,

    @SerializedName("problemPictures")
    @Expose
    val problemPictures: List<String>
) {

    data class ProfilePicturesNetworkEntity(

        @SerializedName("large")
        @Expose
        val large: String,

        @SerializedName("medium")
        @Expose
        val medium: String,

        @SerializedName("thumbnail")
        @Expose
        val thumbnail: String,
    )

    data class LocationNetworkEntity(

        @SerializedName("address")
        @Expose
        val addressNetworkEntity: AddressNetworkEntity,

        @SerializedName("coordinate")
        @Expose
        val coordinatesNetworkEntity: CoordinatesNetworkEntity,
    ) {

        data class AddressNetworkEntity(

            @SerializedName("street")
            @Expose
            val street: String,

            @SerializedName("city")
            @Expose
            val city: String,

            @SerializedName("state")
            @Expose
            val state: String,

            @SerializedName("postalCode")
            @Expose
            val postalCode: String,

            @SerializedName("country")
            @Expose
            val country: String,
        )

        data class CoordinatesNetworkEntity(

            @SerializedName("latitude")
            @Expose
            val latitude: Double,

            @SerializedName("longitude")
            @Expose
            val longitude: Double
        )
    }
}