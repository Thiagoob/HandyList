package com.celerocommerce.handylist.persistence.main

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.celerocommerce.handylist.persistence.BaseDao


@Dao
abstract class CustomerDao : BaseDao<CustomerPersistenceEntity>() {

    @Query(
        """
        UPDATE customers SET
        visit_order = :visitOrder,
        name = :name, phone_number = :phoneNumber,
        profile_picture_large = :profilePictureLarge,
        profile_picture_medium = :profilePictureMedium,
        profile_picture_thumbnail = :profilePictureThumbnail,
        street = :street,
        city = :city,
        state = :state,
        postal_code = :postalCode,
        country = :country,
        latitude = :latitude,
        longitude = :longitude
        WHERE
        id = :id
    """
    )
    abstract fun update(
        id: Long,
        visitOrder: Int,
        name: String,
        phoneNumber: String,
        profilePictureLarge: String,
        profilePictureMedium: String,
        profilePictureThumbnail: String,
        street: String,
        city: String,
        state: String,
        postalCode: String,
        country: String,
        latitude: Double,
        longitude: Double
    )

    override fun upsert(obj: CustomerPersistenceEntity) {
        val id = insert(obj)
        if (id == -1L) {
            update(
                id = obj.id,
                visitOrder = obj.visitOrder,
                name = obj.name,
                phoneNumber = obj.phoneNumber,
                profilePictureLarge = obj.profilePicturesPersistenceEntity.large,
                profilePictureMedium = obj.profilePicturesPersistenceEntity.medium,
                profilePictureThumbnail = obj.profilePicturesPersistenceEntity.thumbnail,
                street = obj.addressPersistenceEntity.street,
                city = obj.addressPersistenceEntity.city,
                state = obj.addressPersistenceEntity.state,
                postalCode = obj.addressPersistenceEntity.postalCode,
                country = obj.addressPersistenceEntity.country,
                latitude = obj.coordinatesPersistenceEntity.latitude,
                longitude = obj.coordinatesPersistenceEntity.longitude
            )
        }
    }

    @Query("SELECT * FROM customers ORDER BY visit_order ASC")
    abstract fun getAllCustomers(): LiveData<List<CustomerPersistenceEntity>>

    @Query("DELETE FROM customers WHERE id = :id")
    abstract fun deleteById(id: Long)

}