package com.barathiraja.dinam

import com.barathiraja.dinam.domain.model.OccurrenceItem
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DateSpecificCompletionTest {

    @Test
    fun completionState_isOccurrenceSpecific_andIndependentPerDate() {
        val occurrenceSept18 = OccurrenceItem(
            id = "item_sept_18",
            occurrenceId = "occ_2026_09_18",
            todayItemId = "routine_task_1",
            origin = "routine",
            text = "Morning Ride",
            canonicalId = "morning ride",
            remindAt = "08:00",
            position = 0,
            checked = true,
            checkedAt = 100000L
        )

        val occurrenceSept19 = OccurrenceItem(
            id = "item_sept_19",
            occurrenceId = "occ_2026_09_19",
            todayItemId = "routine_task_1",
            origin = "routine",
            text = "Morning Ride",
            canonicalId = "morning ride",
            remindAt = "08:00",
            position = 0,
            checked = false,
            checkedAt = null
        )

        val occurrenceSept20 = OccurrenceItem(
            id = "item_sept_20",
            occurrenceId = "occ_2026_09_20",
            todayItemId = "routine_task_1",
            origin = "routine",
            text = "Morning Ride",
            canonicalId = "morning ride",
            remindAt = "08:00",
            position = 0,
            checked = false,
            checkedAt = null
        )

        // Sept 18 is checked
        assertTrue(occurrenceSept18.checked)

        // Sept 19 & Sept 20 remain unchecked independently
        assertFalse(occurrenceSept19.checked)
        assertFalse(occurrenceSept20.checked)
    }
}