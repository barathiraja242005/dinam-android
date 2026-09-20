package com.barathiraja.dinam.data.local.mapper

import com.barathiraja.dinam.data.local.entity.TodayItemEntity
import com.barathiraja.dinam.domain.model.TodayItem

object TodayItemMapper {

    fun toDomain(entity: TodayItemEntity): TodayItem {
        return TodayItem(
            id = entity.id,
            userId = entity.userId,
            text = entity.text,
            canonicalId = entity.canonicalId,
            remindAt = entity.remindAt,
            skipIfComplete = entity.skipIfComplete,
            position = entity.position,
            activeFrom = entity.activeFrom,
            activeUntil = entity.activeUntil
        )
    }

    fun toEntity(domain: TodayItem): TodayItemEntity {
        return TodayItemEntity(
            id = domain.id,
            userId = domain.userId,
            text = domain.text,
            canonicalId = domain.canonicalId,
            remindAt = domain.remindAt,
            skipIfComplete = domain.skipIfComplete,
            position = domain.position,
            activeFrom = domain.activeFrom,
            activeUntil = domain.activeUntil
        )
    }
}