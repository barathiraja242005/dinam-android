package com.barathiraja.dinam.data.repository

import com.barathiraja.dinam.data.local.dao.OccurrenceItemDao
import com.barathiraja.dinam.data.local.entity.OccurrenceItemEntity
import com.barathiraja.dinam.domain.model.OccurrenceItem
import com.barathiraja.dinam.domain.repository.OccurrenceItemRepository

class OccurrenceItemRepositoryImpl(
    private val occurrenceItemDao: OccurrenceItemDao
) : OccurrenceItemRepository {

    override suspend fun getItemsForOccurrence(
        occurrenceId: String
    ): List<OccurrenceItem> {
        return occurrenceItemDao
            .getByOccurrenceId(occurrenceId)
            .map { it.toDomain() }
    }

    override suspend fun insertItems(
        items: List<OccurrenceItem>
    ) {
        occurrenceItemDao.insertAll(
            items.map { it.toEntity() }
        )
    }

    override suspend fun updateItem(
        item: OccurrenceItem
    ) {
        occurrenceItemDao.update(
            item.toEntity()
        )
    }

    override suspend fun deleteItem(
        item: OccurrenceItem
    ) {
        occurrenceItemDao.delete(
            item.toEntity()
        )
    }

    private fun OccurrenceItemEntity.toDomain(): OccurrenceItem {
        return OccurrenceItem(
            id = id,
            occurrenceId = occurrenceId,
            todayItemId = todayItemId,
            origin = origin,
            text = text,
            canonicalId = canonicalId,
            remindAt = remindAt,
            position = position,
            checked = checked,
            checkedAt = checkedAt,
            snoozedUntil = snoozedUntil
        )
    }

    private fun OccurrenceItem.toEntity(): OccurrenceItemEntity {
        return OccurrenceItemEntity(
            id = id,
            occurrenceId = occurrenceId,
            todayItemId = todayItemId,
            origin = origin,
            text = text,
            canonicalId = canonicalId,
            remindAt = remindAt,
            position = position,
            checked = checked,
            checkedAt = checkedAt,
            snoozedUntil = snoozedUntil
        )
    }
}