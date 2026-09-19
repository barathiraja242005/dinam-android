package com.barathiraja.dinam.data.service

import com.barathiraja.dinam.data.local.entity.OccurrenceEntity
import com.barathiraja.dinam.data.local.entity.OccurrenceItemEntity
import com.barathiraja.dinam.data.local.entity.TodayItemEntity
import com.barathiraja.dinam.data.repository.OccurrenceItemRepository
import com.barathiraja.dinam.data.repository.OccurrenceRepository
import com.barathiraja.dinam.data.repository.TodayRepository
import java.time.LocalDate
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

        val existingOccurrence =
            occurrenceRepository.getOccurrenceForDate(
                userId = userId,
                periodDate = periodDate
            )

        if (existingOccurrence != null) {
            /*
             * Existing occurrences are already frozen.
             *
             * We must NEVER rebuild them from the current
             * routine definitions because that could change
             * historical/frozen data.
             */
            return existingOccurrence
        }

        val occurrence = OccurrenceEntity(
            id = UUID.randomUUID().toString(),
            userId = userId,
            periodDate = periodDate,
            createdAt = System.currentTimeMillis()
        )

        occurrenceRepository.createOccurrence(
            occurrence
        )

        /*
         * Past dates must remain empty when there is no
         * previously stored occurrence.
         *
         * We never populate history from today's routines.
         */
        if (isPastDate(periodDate)) {
            return occurrence
        }

        /*
         * Today and future occurrences are generated lazily
         * from the active routine definitions.
         */
        materializeActiveRoutineItems(
            userId = userId,
            periodDate = periodDate,
            occurrence = occurrence
        )

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

        /*
         * A past date can never be modified.
         */
        if (isPastDate(periodDate)) {
            return
        }

        val occurrence =
            getOrCreateOccurrence(
                userId = userId,
                periodDate = periodDate
            )

        /*
         * Store the live routine definition.
         */
        todayRepository.insertItem(
            item
        )

        /*
         * Add the frozen copy to the current occurrence.
         */
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

    suspend fun setEveryDay(
        userId: String,
        item: TodayItemEntity,
        enabled: Boolean
    ) {

        val today = LocalDate.now()
        val todayString = today.toString()

        /*
         * Every day ON:
         *
         * The item becomes active from its original start date
         * through all future dates.
         *
         * Every day OFF:
         *
         * The item remains active for today only.
         */
        val updatedItem =
            if (enabled) {
                item.copy(
                    activeFrom = minDate(
                        item.activeFrom,
                        todayString
                    ),
                    activeUntil = null
                )
            } else {
                item.copy(
                    activeUntil = todayString
                )
            }

        todayRepository.updateItem(
            updatedItem
        )

        if (enabled) {

            addToExistingFutureOccurrences(
                userId = userId,
                today = today,
                item = updatedItem
            )

        } else {

            removeFromFutureOccurrences(
                userId = userId,
                today = today,
                todayItemId = item.id
            )
        }
    }

    /*
     * ---------------------------------------------------------
     * REMOVE — JUST TODAY
     * ---------------------------------------------------------
     *
     * Removes the item from today's frozen occurrence only.
     *
     * The live TodayItemEntity is deliberately untouched.
     * Therefore the routine remains available for tomorrow
     * and future dates.
     */
    suspend fun removeJustToday(
        userId: String,
        todayItemId: String,
        periodDate: String
    ) {

        /*
         * This action is valid only for today.
         */
        if (isPastDate(periodDate)) {
            return
        }

        val occurrence =
            occurrenceRepository.getOccurrenceForDate(
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
                occurrenceItemRepository.deleteItem(
                    occurrenceItem
                )
            }
    }

    /*
     * ---------------------------------------------------------
     * REMOVE — TODAY AND FUTURE
     * ---------------------------------------------------------
     *
     * Ends the live routine today and removes its frozen copies
     * from today and every existing future occurrence.
     *
     * Past occurrences are never touched.
     */
    suspend fun removeTodayAndFuture(
        userId: String,
        todayItemId: String,
        periodDate: String
    ) {

        if (isPastDate(periodDate)) {
            return
        }

        /*
         * First end the live routine today.
         *
         * This prevents the item from being generated again
         * when a new future occurrence is created.
         */
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

        /*
         * Remove the item from today and all existing future
         * occurrences.
         */
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
                    occurrenceItemRepository.deleteItem(
                        occurrenceItem
                    )
                }
        }
    }

    suspend fun setOccurrenceItemChecked(
        item: OccurrenceItemEntity,
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

        occurrenceItemRepository.updateItem(
            updatedItem
        )
    }

    private suspend fun materializeActiveRoutineItems(
        userId: String,
        periodDate: String,
        occurrence: OccurrenceEntity
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

                    OccurrenceItemEntity(
                        id = UUID.randomUUID().toString(),
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
            occurrenceItemRepository.insertItems(
                occurrenceItems
            )
        }
    }

    private suspend fun addToExistingFutureOccurrences(
        userId: String,
        today: LocalDate,
        item: TodayItemEntity
    ) {

        val futureStartDate =
            today.plusDays(1).toString()

        val futureOccurrences =
            occurrenceRepository.getOccurrencesFromDate(
                userId = userId,
                fromDate = futureStartDate
            )

        futureOccurrences.forEach { occurrence ->

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
                OccurrenceItemEntity(
                    id = UUID.randomUUID().toString(),
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
        today: LocalDate,
        todayItemId: String
    ) {

        val futureStartDate =
            today.plusDays(1).toString()

        val futureOccurrences =
            occurrenceRepository.getOccurrencesFromDate(
                userId = userId,
                fromDate = futureStartDate
            )

        futureOccurrences.forEach { occurrence ->

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
                    occurrenceItemRepository.deleteItem(
                        occurrenceItem
                    )
                }
        }
    }

    private fun minDate(
        first: String,
        second: String
    ): String {

        return try {

            val firstDate =
                LocalDate.parse(first)

            val secondDate =
                LocalDate.parse(second)

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

            LocalDate.parse(periodDate)
                .isBefore(
                    LocalDate.now()
                )

        } catch (_: Exception) {

            false
        }
    }
}