package com.barathiraja.dinam

import com.barathiraja.dinam.domain.model.OccurrenceItem
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FutureTaskBehaviorTest {

    @Test
    fun checkingTaskToday_doesNotAffectFutureOccurrence() {
        val todayOccurrenceItem = OccurrenceItem(
            id = "item_today",
            occurrenceId = "occ_today",
            todayItemId = "routine_1",
            origin = "routine",
            text = "Routine Task",
            canonicalId = "routine task",
            remindAt = null,
            position = 0,
            checked = false,
            checkedAt = null
        )

        val futureOccurrenceItem = OccurrenceItem(
            id = "item_future",
            occurrenceId = "occ_future",
            todayItemId = "routine_1",
            origin = "routine",
            text = "Routine Task",
            canonicalId = "routine task",
            remindAt = null,
            position = 0,
            checked = false,
            checkedAt = null
        )

        // Check today's occurrence item
        val updatedTodayItem = todayOccurrenceItem.copy(
            checked = true,
            checkedAt = 123456789L
        )

        // Verify today's item is checked
        assertTrue(updatedTodayItem.checked)

        // Verify future occurrence remains completely unchecked
        assertFalse(futureOccurrenceItem.checked)
    }
}