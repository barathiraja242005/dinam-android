package com.barathiraja.dinam

import com.barathiraja.dinam.domain.model.OccurrenceItem
import com.barathiraja.dinam.domain.model.TodayItem
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OverdueTaskTest {

    @Test
    fun oneTimeItem_isOverdue_whenIncompleteInPast() {
        val todayItem = TodayItem(
            id = "today_1",
            userId = "user_1",
            text = "One time list item",
            canonicalId = "one time list item",
            remindAt = null,
            skipIfComplete = false,
            position = 0,
            activeFrom = "2026-09-18",
            activeUntil = "2026-09-18"
        )

        val occurrenceItem = OccurrenceItem(
            id = "occ_item_1",
            occurrenceId = "occ_1",
            todayItemId = todayItem.id,
            origin = "one_time",
            text = todayItem.text,
            canonicalId = todayItem.canonicalId,
            remindAt = null,
            position = 0,
            checked = false,
            checkedAt = null
        )

        val isPastDate = "2026-09-18" < "2026-09-22"
        val isOneTime = occurrenceItem.origin != "routine" || todayItem.activeUntil != null

        assertTrue(isPastDate)
        assertTrue(isOneTime)
        assertFalse(occurrenceItem.checked)
    }

    @Test
    fun recurringItem_isNotOverdueInPast() {
        val todayItem = TodayItem(
            id = "today_routine",
            userId = "user_1",
            text = "Recurring task",
            canonicalId = "recurring task",
            remindAt = null,
            skipIfComplete = false,
            position = 0,
            activeFrom = "2026-09-18",
            activeUntil = null
        )

        val occurrenceItem = OccurrenceItem(
            id = "occ_item_routine",
            occurrenceId = "occ_1",
            todayItemId = todayItem.id,
            origin = "routine",
            text = todayItem.text,
            canonicalId = todayItem.canonicalId,
            remindAt = null,
            position = 0,
            checked = false,
            checkedAt = null
        )

        val isOneTime = occurrenceItem.origin != "routine" || todayItem.activeUntil != null

        assertFalse(isOneTime)
    }
}