package com.celerocommerce.handylist.persistence.main

import com.celerocommerce.handylist.models.Customer
import com.celerocommerce.handylist.models.Location
import com.celerocommerce.handylist.models.Location.Address
import com.celerocommerce.handylist.models.Location.Coordinates
import com.celerocommerce.handylist.models.ProfilePictures
import com.celerocommerce.handylist.persistence.main.CustomerPersistenceEntity.LocationPersistenceEntity
import com.celerocommerce.handylist.persistence.main.CustomerPersistenceEntity.LocationPersistenceEntity.AddressPersistenceEntity
import com.celerocommerce.handylist.persistence.main.CustomerPersistenceEntity.LocationPersistenceEntity.CoordinatesPersistenceEntity
import com.celerocommerce.handylist.persistence.main.CustomerPersistenceEntity.ProfilePicturesPersistenceEntity
import com.celerocommerce.handylist.util.EntityMapper
import com.celerocommerce.handylist.util.JsonTypeConverter
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class CustomerPersistenceMapper
@Inject
constructor() : EntityMapper<CustomerPersistenceEntity, Customer> {

    override fun mapFromEntityList(entityList: List<CustomerPersistenceEntity>): List<Customer> {
        return entityList.map { mapFromEntity(it) }
    }

    override fun mapToEntityList(domainModelList: List<Customer>): List<CustomerPersistenceEntity> {
        return domainModelList.map { mapToEntity(it) }
    }

    override fun mapFromEntity(entity: CustomerPersistenceEntity): Customer {
        return Customer(
            id = entity.id,
            visitOrder = entity.visitOrder,
            name = entity.name,
            phoneNumber = entity.phoneNumber,
            profilePictures = mapFromProfilePicturesEntity(entity.profilePicturesPersistenceEntity),
            location = mapFromLocationEntity(entity.addressPersistenceEntity, entity.coordinatesPersistenceEntity),
            serviceReason = entity.serviceReason,
            problemPictures = JsonTypeConverter.fromJson(entity.problemPictures),
            isHandled = entity.isHandled
        )
    }

    override fun mapToEntity(domainModel: Customer): CustomerPersistenceEntity {
        return CustomerPersistenceEntity(
            id = domainModel.id,
            visitOrder = domainModel.visitOrder,
            name = domainModel.name!!,
            phoneNumber = domainModel.phoneNumber!!,
            profilePicturesPersistenceEntity = mapToProfilePicturesEntity(domainModel.profilePictures!!),
            addressPersistenceEntity = mapToAddressEntity(domainModel.location!!.address!!),
            coordinatesPersistenceEntity = mapToCoordinateEntity(domainModel.location.coordinates!!),
            serviceReason = domainModel.serviceReason!!,
            problemPictures = JsonTypeConverter.toJson(domainModel.problemPictures),
            isHandled = domainModel.isHandled
        )
    }

    private fun mapFromProfilePicturesEntity(profilePicturesPersistenceEntity: ProfilePicturesPersistenceEntity): ProfilePictures {
        return ProfilePictures(
            large = profilePicturesPersistenceEntity.large,
            medium = profilePicturesPersistenceEntity.medium,
            thumbnail = profilePicturesPersistenceEntity.thumbnail
        )
    }

    private fun mapToProfilePicturesEntity(profilePictures: ProfilePictures): ProfilePicturesPersistenceEntity {
        return ProfilePicturesPersistenceEntity(
            large = profilePictures.large!!,
            medium = profilePictures.medium!!,
            thumbnail = profilePictures.thumbnail!!
        )
    }

    private fun mapFromLocationEntity(addressPersistenceEntity: AddressPersistenceEntity, coordinatesPersistenceEntity: CoordinatesPersistenceEntity): Location {
        return Location(
            address = mapFromAddressEntity(addressPersistenceEntity),
            coordinates = mapFromCoordinateEntity(coordinatesPersistenceEntity)
        )
    }

    private fun mapToLocationEntity(location: Location): LocationPersistenceEntity {
        return LocationPersistenceEntity(
            addressPersistenceEntity = mapToAddressEntity(location.address!!),
            coordinatesPersistenceEntity = mapToCoordinateEntity(location.coordinates!!)
        )
    }

    private fun mapFromAddressEntity(addressPersistenceEntity: AddressPersistenceEntity): Address {
        return Address(
            street = addressPersistenceEntity.street,
            city = addressPersistenceEntity.city,
            state = addressPersistenceEntity.state,
            postalCode = addressPersistenceEntity.postalCode,
            country = addressPersistenceEntity.country,
        )
    }

    private fun mapToAddressEntity(address: Address): AddressPersistenceEntity {
        return AddressPersistenceEntity(
            street = address.street!!,
            city = address.city!!,
            state = address.state!!,
            postalCode = address.postalCode!!,
            country = address.country!!
        )
    }

    private fun mapFromCoordinateEntity(coordinatesPersistenceEntity: CoordinatesPersistenceEntity): Coordinates {
        return Coordinates(
            latitude = coordinatesPersistenceEntity.latitude,
            longitude = coordinatesPersistenceEntity.longitude
        )
    }

    private fun mapToCoordinateEntity(coordinates: Coordinates): CoordinatesPersistenceEntity {
        return CoordinatesPersistenceEntity(
            latitude = coordinates.latitude,
            longitude = coordinates.longitude
        )
    }
}