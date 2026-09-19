package com.barathiraja.dinam.domain.repository

import com.barathiraja.dinam.domain.model.OccurrenceItem

interface OccurrenceItemRepository {

    suspend fun getItemsForOccurrence(
        occurrenceId: String
    ): List<OccurrenceItem>

    suspend fun insertItems(
        items: List<OccurrenceItem>
    )

    suspend fun updateItem(
        item: OccurrenceItem
    )

    suspend fun deleteItem(
        item: OccurrenceItem
    )
}