package com.barathiraja.dinam.data.repository

import com.barathiraja.dinam.data.local.dao.OccurrenceItemDao
import com.barathiraja.dinam.data.local.mapper.OccurrenceItemMapper
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
            .map { OccurrenceItemMapper.toDomain(it) }
    }

    override suspend fun insertItems(
        items: List<OccurrenceItem>
    ) {
        occurrenceItemDao.insertAll(
            items.map { OccurrenceItemMapper.toEntity(it) }
        )
    }

    override suspend fun updateItem(
        item: OccurrenceItem
    ) {
        occurrenceItemDao.update(
            OccurrenceItemMapper.toEntity(item)
        )
    }

    override suspend fun deleteItem(
        item: OccurrenceItem
    ) {
        occurrenceItemDao.delete(
            OccurrenceItemMapper.toEntity(item)
        )
    }
}