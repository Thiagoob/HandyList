package com.celerocommerce.handylist.util

interface EntityMapper<Entity, DomainModel> {

    fun mapFromEntityList(entityList: List<Entity>): List<DomainModel>

    fun mapToEntityList(domainModelList: List<DomainModel>): List<Entity>

    fun mapFromEntity(entity: Entity): DomainModel

    fun mapToEntity(domainModel: DomainModel): Entity
}