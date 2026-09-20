package com.barathiraja.dinam.data.local.mapper

import com.barathiraja.dinam.data.local.entity.OccurrenceEntity
import com.barathiraja.dinam.domain.model.Occurrence

object OccurrenceMapper {

    fun toDomain(entity: OccurrenceEntity): Occurrence {
        return Occurrence(
            id = entity.id,
            userId = entity.userId,
            periodDate = entity.periodDate,
            createdAt = entity.createdAt
        )
    }

    fun toEntity(domain: Occurrence): OccurrenceEntity {
        return OccurrenceEntity(
            id = domain.id,
            userId = domain.userId,
            periodDate = domain.periodDate,
            createdAt = domain.createdAt
        )
    }
}