package com.barathiraja.dinam.data.local.mapper

import com.barathiraja.dinam.data.local.entity.ListItemEntity
import com.barathiraja.dinam.domain.model.ListItem

object ListItemMapper {

    fun toDomain(entity: ListItemEntity): ListItem {
        return ListItem(
            id = entity.id,
            listId = entity.listId,
            text = entity.text,
            canonicalId = entity.canonicalId,
            position = entity.position,
            checked = entity.checked,
            dueDate = entity.dueDate,
            remindAt = entity.remindAt,
            remindMe = entity.remindMe,
            snoozedUntil = entity.snoozedUntil
        )
    }

    fun toEntity(domain: ListItem): ListItemEntity {
        return ListItemEntity(
            id = domain.id,
            listId = domain.listId,
            text = domain.text,
            canonicalId = domain.canonicalId,
            position = domain.position,
            checked = domain.checked,
            dueDate = domain.dueDate,
            remindAt = domain.remindAt,
            remindMe = domain.remindMe,
            snoozedUntil = domain.snoozedUntil
        )
    }
}