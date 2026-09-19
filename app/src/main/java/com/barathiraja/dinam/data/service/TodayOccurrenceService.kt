@file:Suppress("NewApi")

package com.barathiraja.dinam.data.service

import com.barathiraja.dinam.domain.model.Occurrence
import com.barathiraja.dinam.domain.model.OccurrenceItem
import com.barathiraja.dinam.domain.model.TodayItem
import com.barathiraja.dinam.domain.repository.OccurrenceItemRepository
import com.barathiraja.dinam.domain.repository.OccurrenceRepository
import com.barathiraja.dinam.domain.repository.TodayRepository
import com.barathiraja.dinam.domain.util.IdGenerator
import java.time.LocalDate

class TodayOccurrenceService(
    private val occurrenceRepository: OccurrenceRepository,
    private val occurrenceItemRepository: OccurrenceItemRepository,
    private val todayRepository: TodayRepository,
    private val idGenerator: IdGenerator = IdGenerator.Default
) {

    suspend fun getOrCreateOccurrence(
        userId: String,
        periodDate: String
    ): Occurrence {

        val existingOccurrence =
            occurrenceRepository.getByPeriodDate(
                userId = userId,
                periodDate = periodDate
            )

        if (existingOccurrence != null) {
            return existingOccurrence
        }

        val occurrence = Occurrence(
            id = idGenerator.generateId(),
            userId = userId,
            periodDate = periodDate,
            createdAt = System.currentTimeMillis()
        )

        occurrenceRepository.insert(occurrence)

        if (isPastDate(periodDate)) {
            return occurrence
        }

        materializeActiveRoutineItems(
            userId = userId,
            periodDate = periodDate,
            occurrence = occurrence
        )

        return occurrence
    }

    suspend fun getOccurrenceItems(
        occurrenceId: String
    ): List<OccurrenceItem> {
        return occurrenceItemRepository.getItemsForOccurrence(
            occurrenceId = occurrenceId
        )
    }

    suspend fun addTodayItem(
        userId: String,
        periodDate: String,
        item: TodayItem
    ) {
        if (isPastDate(periodDate)) {
            return
        }

        val occurrence =
            getOrCreateOccurrence(
                userId = userId,
                periodDate = periodDate
            )

        todayRepository.insertItem(item)

        val existingOccurrenceItems =
            occurrenceItemRepository.getItemsForOccurrence(
                occurrenceId = occurrence.id
            )

        val occurrenceItem = OccurrenceItem(
            id = idGenerator.generateId(),
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

    suspend fun setEveryDay(
        userId: String,
        item: TodayItem,
        enabled: Boolean,
        periodDate: String = item.activeFrom
    ) {

        val updatedItem =
            if (enabled) {
                item.copy(
                    activeFrom = minDate(
                        item.activeFrom,
                        periodDate
                    ),
                    activeUntil = null
                )
            } else {
                item.copy(
                    activeUntil = periodDate
                )
            }

        todayRepository.updateItem(updatedItem)

        if (enabled) {
            addToExistingFutureOccurrences(
                userId = userId,
                fromDate = periodDate,
                item = updatedItem
            )
        } else {
            removeFromFutureOccurrences(
                userId = userId,
                fromDate = periodDate,
                todayItemId = item.id
            )
        }
    }

    suspend fun removeJustToday(
        userId: String,
        todayItemId: String,
        periodDate: String
    ) {
        if (isPastDate(periodDate)) {
            return
        }

        val occurrence =
            occurrenceRepository.getByPeriodDate(
                userId = userId,
                periodDate = periodDate
            )
                ?: return

        val occurrenceItems =
            occurrenceItemRepository.getItemsForOccurrence(
                occurrenceId = occurrence.id
            )

        occurrenceItems
            .filter { occurrenceItem ->
                occurrenceItem.origin == "routine" &&
                        occurrenceItem.todayItemId == todayItemId
            }
            .forEach { occurrenceItem ->
                occurrenceItemRepository.deleteItem(occurrenceItem)
            }
    }

    suspend fun removeTodayAndFuture(
        userId: String,
        todayItemId: String,
        periodDate: String
    ) {
        if (isPastDate(periodDate)) {
            return
        }

        val activeItems =
            todayRepository.getItemsForDate(
                userId = userId,
                periodDate = periodDate
            )

        val todayItem =
            activeItems.firstOrNull {
                it.id == todayItemId
            }

        if (todayItem != null) {
            todayRepository.updateItem(
                todayItem.copy(
                    activeUntil = periodDate
                )
            )
        }

        val occurrences =
            occurrenceRepository.getOccurrencesFromDate(
                userId = userId,
                fromDate = periodDate
            )

        occurrences.forEach { occurrence ->
            val occurrenceItems =
                occurrenceItemRepository.getItemsForOccurrence(
                    occurrenceId = occurrence.id
                )

            occurrenceItems
                .filter { occurrenceItem ->
                    occurrenceItem.origin == "routine" &&
                            occurrenceItem.todayItemId == todayItemId
                }
                .forEach { occurrenceItem ->
                    occurrenceItemRepository.deleteItem(occurrenceItem)
                }
        }
    }

    suspend fun setOccurrenceItemChecked(
        item: OccurrenceItem,
        checked: Boolean
    ) {
        val updatedItem =
            item.copy(
                checked = checked,
                checkedAt = if (checked) {
                    System.currentTimeMillis()
                } else {
                    null
                }
            )

        occurrenceItemRepository.updateItem(updatedItem)
    }

    private suspend fun materializeActiveRoutineItems(
        userId: String,
        periodDate: String,
        occurrence: Occurrence
    ) {
        val todayItems =
            todayRepository.getItemsForDate(
                userId = userId,
                periodDate = periodDate
            )

        val existingItems =
            occurrenceItemRepository.getItemsForOccurrence(
                occurrenceId = occurrence.id
            )

        val existingTodayItemIds =
            existingItems
                .mapNotNull { it.todayItemId }
                .toSet()

        val occurrenceItems =
            todayItems
                .filter { todayItem ->
                    todayItem.id !in existingTodayItemIds
                }
                .mapIndexed { index, todayItem ->
                    OccurrenceItem(
                        id = idGenerator.generateId(),
                        occurrenceId = occurrence.id,
                        todayItemId = todayItem.id,
                        origin = "routine",
                        text = todayItem.text,
                        canonicalId = todayItem.canonicalId,
                        remindAt = todayItem.remindAt,
                        position = existingItems.size + index,
                        checked = false,
                        checkedAt = null
                    )
                }

        if (occurrenceItems.isNotEmpty()) {
            occurrenceItemRepository.insertItems(occurrenceItems)
        }
    }

    private suspend fun addToExistingFutureOccurrences(
        userId: String,
        fromDate: String,
        item: TodayItem
    ) {
        val futureOccurrences =
            occurrenceRepository.getOccurrencesFromDate(
                userId = userId,
                fromDate = fromDate
            )

        futureOccurrences.forEach { occurrence ->
            if (occurrence.periodDate == item.activeFrom) {
                return@forEach
            }

            if (isPastDate(occurrence.periodDate)) {
                return@forEach
            }

            val existingItems =
                occurrenceItemRepository.getItemsForOccurrence(
                    occurrenceId = occurrence.id
                )

            val alreadyExists =
                existingItems.any { occurrenceItem ->
                    occurrenceItem.todayItemId == item.id
                }

            if (alreadyExists) {
                return@forEach
            }

            val occurrenceItem =
                OccurrenceItem(
                    id = idGenerator.generateId(),
                    occurrenceId = occurrence.id,
                    todayItemId = item.id,
                    origin = "routine",
                    text = item.text,
                    canonicalId = item.canonicalId,
                    remindAt = item.remindAt,
                    position = existingItems.size,
                    checked = false,
                    checkedAt = null
                )

            occurrenceItemRepository.insertItems(
                listOf(occurrenceItem)
            )
        }
    }

    private suspend fun removeFromFutureOccurrences(
        userId: String,
        fromDate: String,
        todayItemId: String
    ) {
        val futureOccurrences =
            occurrenceRepository.getOccurrencesFromDate(
                userId = userId,
                fromDate = fromDate
            )

        futureOccurrences.forEach { occurrence ->
            if (occurrence.periodDate == fromDate) {
                return@forEach
            }

            val items =
                occurrenceItemRepository.getItemsForOccurrence(
                    occurrenceId = occurrence.id
                )

            items
                .filter { occurrenceItem ->
                    occurrenceItem.origin == "routine" &&
                            occurrenceItem.todayItemId == todayItemId
                }
                .forEach { occurrenceItem ->
                    occurrenceItemRepository.deleteItem(occurrenceItem)
                }
        }
    }

    private fun minDate(
        first: String,
        second: String
    ): String {
        return try {
            val firstDate = LocalDate.parse(first)
            val secondDate = LocalDate.parse(second)
            if (firstDate.isBefore(secondDate)) {
                first
            } else {
                second
            }
        } catch (_: Exception) {
            second
        }
    }

    private fun isPastDate(
        periodDate: String
    ): Boolean {
        return try {
            LocalDate.parse(periodDate).isBefore(LocalDate.now())
        } catch (_: Exception) {
            false
        }
    }
}