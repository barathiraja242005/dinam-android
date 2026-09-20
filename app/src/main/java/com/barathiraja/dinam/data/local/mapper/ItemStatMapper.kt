package com.barathiraja.dinam.data.local.mapper

import com.barathiraja.dinam.data.local.entity.ItemStatEntity
import com.barathiraja.dinam.domain.model.ItemStat

object ItemStatMapper {

    fun toDomain(entity: ItemStatEntity): ItemStat {
        return ItemStat(
            userId = entity.userId,
            category = entity.category,
            canonicalId = entity.canonicalId,
            displayText = entity.displayText,
            useCount = entity.useCount,
            lastUsedAt = entity.lastUsedAt,
            avgIntervalDays = entity.avgIntervalDays
        )
    }

    fun toEntity(domain: ItemStat): ItemStatEntity {
        return ItemStatEntity(
            userId = domain.userId,
            category = domain.category,
            canonicalId = domain.canonicalId,
            displayText = domain.displayText,
            useCount = domain.useCount,
            lastUsedAt = domain.lastUsedAt,
            avgIntervalDays = domain.avgIntervalDays
        )
    }
}