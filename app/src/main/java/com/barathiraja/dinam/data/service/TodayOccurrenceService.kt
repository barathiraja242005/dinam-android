package com.barathiraja.dinam.data.service

import com.barathiraja.dinam.data.local.entity.OccurrenceEntity
import com.barathiraja.dinam.data.local.entity.OccurrenceItemEntity
import com.barathiraja.dinam.data.local.entity.TodayItemEntity
import com.barathiraja.dinam.data.repository.OccurrenceItemRepository
import com.barathiraja.dinam.data.repository.OccurrenceRepository
import com.barathiraja.dinam.data.repository.TodayRepository
import java.util.UUID

class TodayOccurrenceService(
    private val occurrenceRepository: OccurrenceRepository,
    private val occurrenceItemRepository: OccurrenceItemRepository,
    private val todayRepository: TodayRepository
) {

    suspend fun getOrCreateOccurrence(
        userId: String,
        periodDate: String
    ): OccurrenceEntity {

        val existingOccurrence = occurrenceRepository.getOccurrenceForDate(
            userId = userId,
            periodDate = periodDate
        )

        if (existingOccurrence != null) {
            return existingOccurrence
        }

        val occurrence = OccurrenceEntity(
            id = UUID.randomUUID().toString(),
            userId = userId,
            periodDate = periodDate,
            createdAt = System.currentTimeMillis()
        )

        occurrenceRepository.createOccurrence(occurrence)

        val todayItems = todayRepository.getItemsForDate(
            userId = userId,
            periodDate = periodDate
        )

        val occurrenceItems = todayItems.mapIndexed { index, todayItem ->
            OccurrenceItemEntity(
                id = UUID.randomUUID().toString(),
                occurrenceId = occurrence.id,
                todayItemId = todayItem.id,
                origin = "routine",
                text = todayItem.text,
                canonicalId = todayItem.canonicalId,
                remindAt = todayItem.remindAt,
                position = index,
                checked = false,
                checkedAt = null
            )
        }

        if (occurrenceItems.isNotEmpty()) {
            occurrenceItemRepository.insertItems(occurrenceItems)
        }

        return occurrence
    }

    suspend fun getOccurrenceItems(
        occurrenceId: String
    ): List<OccurrenceItemEntity> {
        return occurrenceItemRepository.getItemsForOccurrence(
            occurrenceId = occurrenceId
        )
    }

    suspend fun addTodayItem(
        userId: String,
        periodDate: String,
        item: TodayItemEntity
    ) {
        val occurrence = getOrCreateOccurrence(
            userId = userId,
            periodDate = periodDate
        )

        todayRepository.insertItem(item)

        val existingOccurrenceItems =
            occurrenceItemRepository.getItemsForOccurrence(
                occurrenceId = occurrence.id
            )

        val occurrenceItem = OccurrenceItemEntity(
            id = UUID.randomUUID().toString(),
            occurrenceId = occurrence.id,
            todayItemId = item.id,
            origin = "routine",
            text = item.text,
            canonicalId = item.canonicalId,
            remindAt = item.remindAt,
            position = existingOccurrenceItems.size,
            checked = false,
            checkedAt = null
        )

        occurrenceItemRepository.insertItems(
            listOf(occurrenceItem)
        )
    }

    suspend fun setOccurrenceItemChecked(
        item: OccurrenceItemEntity,
        checked: Boolean
    ) {
        val updatedItem = item.copy(
            checked = checked,
            checkedAt = if (checked) {
                System.currentTimeMillis()
            } else {
                null
            }
        )

        occurrenceItemRepository.updateItem(updatedItem)
    }
}