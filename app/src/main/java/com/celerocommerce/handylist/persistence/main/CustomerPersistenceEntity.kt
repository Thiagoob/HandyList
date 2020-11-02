package com.celerocommerce.handylist.persistence.main

import androidx.room.*
import com.celerocommerce.handylist.persistence.main.CustomerPersistenceEntity.LocationPersistenceEntity.*
import com.celerocommerce.handylist.util.JsonTypeConverter

@Entity(tableName = "customers")
data class CustomerPersistenceEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "visit_order")
    val visitOrder: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "phone_number")
    val phoneNumber: String,

    @Embedded
    val profilePicturesPersistenceEntity: ProfilePicturesPersistenceEntity,

    @Embedded
    val addressPersistenceEntity: AddressPersistenceEntity,

    @Embedded
    val coordinatesPersistenceEntity: CoordinatesPersistenceEntity,

    @ColumnInfo(name = "service_reason")
    val serviceReason: String,

    @ColumnInfo(name = "problem_pictures")
    val problemPictures: String,

    @ColumnInfo(name = "is_handled")
    val isHandled: Boolean = false

) {
    data class ProfilePicturesPersistenceEntity(
        @ColumnInfo(name = "profile_picture_large")
        val large: String,

        @ColumnInfo(name = "profile_picture_medium")
        val medium: String,

        @ColumnInfo(name = "profile_picture_thumbnail")
        val thumbnail: String,
    )

    data class LocationPersistenceEntity(
        val addressPersistenceEntity: AddressPersistenceEntity,
        val coordinatesPersistenceEntity: CoordinatesPersistenceEntity
    ) {
        data class AddressPersistenceEntity(
            @ColumnInfo(name = "street")
            val street: String,

            @ColumnInfo(name = "city")
            val city: String,

            @ColumnInfo(name = "state")
            val state: String,

            @ColumnInfo(name = "postal_code")
            val postalCode: String,

            @ColumnInfo(name = "country")
            val country: String
        )

        data class CoordinatesPersistenceEntity(
            @ColumnInfo(name = "latitude")
            val latitude: Double,

            @ColumnInfo(name = "longitude")
            val longitude: Double
        )
    }
}