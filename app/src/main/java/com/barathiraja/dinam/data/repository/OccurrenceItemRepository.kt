package com.barathiraja.dinam.data.repository

import com.barathiraja.dinam.data.local.dao.OccurrenceItemDao
import com.barathiraja.dinam.data.local.entity.OccurrenceItemEntity

class OccurrenceItemRepository(
    private val occurrenceItemDao: OccurrenceItemDao
) {

    suspend fun getItemsForOccurrence(
        occurrenceId: String
    ): List<OccurrenceItemEntity> {
        return occurrenceItemDao.getByOccurrenceId(
            occurrenceId = occurrenceId
        )
    }

    suspend fun insertItems(
        items: List<OccurrenceItemEntity>
    ) {
        occurrenceItemDao.insertAll(items)
    }

    suspend fun updateItem(
        item: OccurrenceItemEntity
    ) {
        occurrenceItemDao.update(item)
    }
}