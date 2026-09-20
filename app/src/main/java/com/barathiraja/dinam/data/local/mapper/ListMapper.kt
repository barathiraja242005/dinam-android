package com.barathiraja.dinam.data.local.mapper

import com.barathiraja.dinam.data.local.entity.ListEntity
import com.barathiraja.dinam.domain.model.DinamList

object ListMapper {

    fun toDomain(entity: ListEntity): DinamList {
        return DinamList(
            id = entity.id,
            userId = entity.userId,
            title = entity.title,
            category = entity.category,
            archivedAt = entity.archivedAt,
            createdAt = entity.createdAt
        )
    }

    fun toEntity(domain: DinamList): ListEntity {
        return ListEntity(
            id = domain.id,
            userId = domain.userId,
            title = domain.title,
            category = domain.category,
            archivedAt = domain.archivedAt,
            createdAt = domain.createdAt
        )
    }
}