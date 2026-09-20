package com.barathiraja.dinam.data.local.mapper

import com.barathiraja.dinam.data.local.entity.OccurrenceItemEntity
import com.barathiraja.dinam.domain.model.OccurrenceItem

object OccurrenceItemMapper {

    fun toDomain(entity: OccurrenceItemEntity): OccurrenceItem {
        return OccurrenceItem(
            id = entity.id,
            occurrenceId = entity.occurrenceId,
            todayItemId = entity.todayItemId,
            origin = entity.origin,
            text = entity.text,
            canonicalId = entity.canonicalId,
            remindAt = entity.remindAt,
            position = entity.position,
            checked = entity.checked,
            checkedAt = entity.checkedAt,
            snoozedUntil = entity.snoozedUntil,
            listName = entity.listName
        )
    }

    fun toEntity(domain: OccurrenceItem): OccurrenceItemEntity {
        return OccurrenceItemEntity(
            id = domain.id,
            occurrenceId = domain.occurrenceId,
            todayItemId = domain.todayItemId,
            origin = domain.origin,
            text = domain.text,
            canonicalId = domain.canonicalId,
            remindAt = domain.remindAt,
            position = domain.position,
            checked = domain.checked,
            checkedAt = domain.checkedAt,
            snoozedUntil = domain.snoozedUntil,
            listName = domain.listName
        )
    }
}