package com.celerocommerce.handylist.network.main

import com.celerocommerce.handylist.network.responses.CustomerNetworkEntity
import com.celerocommerce.handylist.network.responses.CustomerNetworkEntity.LocationNetworkEntity
import com.celerocommerce.handylist.network.responses.CustomerNetworkEntity.LocationNetworkEntity.AddressNetworkEntity
import com.celerocommerce.handylist.network.responses.CustomerNetworkEntity.LocationNetworkEntity.CoordinatesNetworkEntity
import com.celerocommerce.handylist.network.responses.CustomerNetworkEntity.ProfilePicturesNetworkEntity
import com.celerocommerce.handylist.persistence.main.CustomerPersistenceEntity
import com.celerocommerce.handylist.persistence.main.CustomerPersistenceEntity.LocationPersistenceEntity
import com.celerocommerce.handylist.persistence.main.CustomerPersistenceEntity.LocationPersistenceEntity.AddressPersistenceEntity
import com.celerocommerce.handylist.persistence.main.CustomerPersistenceEntity.LocationPersistenceEntity.CoordinatesPersistenceEntity
import com.celerocommerce.handylist.persistence.main.CustomerPersistenceEntity.ProfilePicturesPersistenceEntity
import com.celerocommerce.handylist.util.EntityMapper
import com.celerocommerce.handylist.util.JsonTypeConverter
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class CustomerNetworkMapper
@Inject
constructor() : EntityMapper<CustomerNetworkEntity, CustomerPersistenceEntity> {

    override fun mapFromEntityList(entityList: List<CustomerNetworkEntity>): List<CustomerPersistenceEntity> {
        return entityList.map { mapFromEntity(it) }
    }

    override fun mapToEntityList(domainModelList: List<CustomerPersistenceEntity>): List<CustomerNetworkEntity> {
        return domainModelList.map { mapToEntity(it) }
    }

    override fun mapFromEntity(entity: CustomerNetworkEntity): CustomerPersistenceEntity {
        return CustomerPersistenceEntity(
            id = entity.id,
            visitOrder = entity.visitOrder,
            name = entity.name,
            phoneNumber = entity.phoneNumber,
            profilePicturesPersistenceEntity = mapFromProfilePicturesEntity(entity.profilePicturesNetworkEntity),
            addressPersistenceEntity = mapFromAddressEntity(entity.locationNetworkEntity.addressNetworkEntity),
            coordinatesPersistenceEntity = mapFromCoordinatesEntity(entity.locationNetworkEntity.coordinatesNetworkEntity),
            serviceReason = entity.serviceReason,
            problemPictures = JsonTypeConverter.toJson(entity.problemPictures)
        )
    }

    override fun mapToEntity(domainModel: CustomerPersistenceEntity): CustomerNetworkEntity {
        return CustomerNetworkEntity(
            id = domainModel.id,
            visitOrder = domainModel.visitOrder,
            name = domainModel.name,
            phoneNumber = domainModel.phoneNumber,
            profilePicturesNetworkEntity = mapToProfilePicturesEntity(domainModel.profilePicturesPersistenceEntity),
            locationNetworkEntity = mapToLocationEntity(domainModel.addressPersistenceEntity, domainModel.coordinatesPersistenceEntity),
            serviceReason = domainModel.serviceReason,
            problemPictures = JsonTypeConverter.fromJson(domainModel.problemPictures)
        )
    }

    private fun mapFromProfilePicturesEntity(profilePicturesNetworkEntity: ProfilePicturesNetworkEntity): ProfilePicturesPersistenceEntity {
        return ProfilePicturesPersistenceEntity(
            large = profilePicturesNetworkEntity.large,
            medium = profilePicturesNetworkEntity.medium,
            thumbnail = profilePicturesNetworkEntity.thumbnail
        )
    }

    private fun mapToProfilePicturesEntity(profilePicturesPersistenceEntity: ProfilePicturesPersistenceEntity): ProfilePicturesNetworkEntity {
        return ProfilePicturesNetworkEntity(
            large = profilePicturesPersistenceEntity.large,
            medium = profilePicturesPersistenceEntity.medium,
            thumbnail = profilePicturesPersistenceEntity.thumbnail
        )
    }

    private fun mapFromLocationEntity(locationNetworkEntity: LocationNetworkEntity): LocationPersistenceEntity {
        return LocationPersistenceEntity(
            addressPersistenceEntity = mapFromAddressEntity(locationNetworkEntity.addressNetworkEntity),
            coordinatesPersistenceEntity = mapFromCoordinatesEntity(locationNetworkEntity.coordinatesNetworkEntity)
        )
    }

    private fun mapToLocationEntity(addressPersistenceEntity: AddressPersistenceEntity, coordinatesPersistenceEntity: CoordinatesPersistenceEntity): LocationNetworkEntity {
        return LocationNetworkEntity(
            addressNetworkEntity = mapToAddressEntity(addressPersistenceEntity),
            coordinatesNetworkEntity = mapToCoordinatesEntity(coordinatesPersistenceEntity)
        )
    }

    private fun mapFromAddressEntity(addressNetworkEntity: AddressNetworkEntity): AddressPersistenceEntity {
        return AddressPersistenceEntity(
            street = addressNetworkEntity.street,
            city = addressNetworkEntity.city,
            state = addressNetworkEntity.state,
            postalCode = addressNetworkEntity.postalCode,
            country = addressNetworkEntity.country,
        )
    }

    private fun mapToAddressEntity(addressPersistenceEntity: AddressPersistenceEntity): AddressNetworkEntity {
        return AddressNetworkEntity(
            street = addressPersistenceEntity.street,
            city = addressPersistenceEntity.city,
            state = addressPersistenceEntity.state,
            postalCode = addressPersistenceEntity.postalCode,
            country = addressPersistenceEntity.country
        )
    }

    private fun mapFromCoordinatesEntity(coordinatesNetworkEntity: CoordinatesNetworkEntity): CoordinatesPersistenceEntity {
        return CoordinatesPersistenceEntity(
            latitude = coordinatesNetworkEntity.latitude,
            longitude = coordinatesNetworkEntity.longitude
        )
    }

    private fun mapToCoordinatesEntity(coordinatesPersistenceEntity: CoordinatesPersistenceEntity): CoordinatesNetworkEntity {
        return CoordinatesNetworkEntity(
            latitude = coordinatesPersistenceEntity.latitude,
            longitude = coordinatesPersistenceEntity.longitude
        )
    }
}