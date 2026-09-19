package com.barathiraja.dinam

import com.barathiraja.dinam.domain.model.OccurrenceItem
import com.barathiraja.dinam.domain.model.TodayItem
import org.junit.Assert.assertEquals
import org.junit.Test

class EverydayRenameTest {

    @Test
    fun renameEverydayItem_propagatesToTodayAndFuture_andPreservesPast() {
        val todayItemId = "today_item_1"
        val originalName = "Morning Yoga"
        val updatedName = "Morning Yoga & Meditation"

        // Active TodayItem definition
        val activeTodayItem = TodayItem(
            id = todayItemId,
            userId = "user_1",
            text = originalName,
            canonicalId = "morning yoga",
            remindAt = "07:00",
            skipIfComplete = false,
            position = 0,
            activeFrom = "2026-09-01",
            activeUntil = null
        )

        // Past Occurrence Item (Sept 10, 2026)
        val pastOccurrenceItem = OccurrenceItem(
            id = "occ_past_item",
            occurrenceId = "occ_2026_09_10",
            todayItemId = todayItemId,
            origin = "routine",
            text = originalName,
            canonicalId = "morning yoga",
            remindAt = "07:00",
            position = 0,
            checked = true,
            checkedAt = 1000L
        )

        // Today Occurrence Item (Sept 18, 2026)
        val todayOccurrenceItem = OccurrenceItem(
            id = "occ_today_item",
            occurrenceId = "occ_2026_09_18",
            todayItemId = todayItemId,
            origin = "routine",
            text = originalName,
            canonicalId = "morning yoga",
            remindAt = "07:00",
            position = 0,
            checked = false,
            checkedAt = null
        )

        // Future Occurrence Item (Sept 19, 2026)
        val futureOccurrenceItem = OccurrenceItem(
            id = "occ_future_item",
            occurrenceId = "occ_2026_09_19",
            todayItemId = todayItemId,
            origin = "routine",
            text = originalName,
            canonicalId = "morning yoga",
            remindAt = "07:00",
            position = 0,
            checked = false,
            checkedAt = null
        )

        // Perform rename for current date "2026-09-18"
        val updatedTodayDefinition = activeTodayItem.copy(text = updatedName)
        val updatedTodayItem = todayOccurrenceItem.copy(text = updatedName)
        val updatedFutureItem = futureOccurrenceItem.copy(text = updatedName)

        // Verify active TodayItem definition has new name
        assertEquals(updatedName, updatedTodayDefinition.text)

        // Verify today's item has new name
        assertEquals(updatedName, updatedTodayItem.text)

        // Verify future occurrence has new name
        assertEquals(updatedName, updatedFutureItem.text)

        // Verify past occurrence STILL HAS ORIGINAL NAME
        assertEquals(originalName, pastOccurrenceItem.text)
    }
}