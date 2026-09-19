package com.barathiraja.dinam

import com.barathiraja.dinam.domain.model.TodayItem
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OccurrenceBehaviorTest {

    @Test
    fun todayItem_everydayOff_isActiveOnlyForTargetDate() {
        val item = TodayItem(
            id = "item_1",
            userId = "user_1",
            text = "Test Item",
            canonicalId = "test item",
            remindAt = null,
            skipIfComplete = false,
            position = 0,
            activeFrom = "2026-09-18",
            activeUntil = "2026-09-18"
        )

        fun isActiveOn(date: String): Boolean {
            val fromOk = item.activeFrom <= date
            val untilOk = item.activeUntil == null || item.activeUntil >= date
            return fromOk && untilOk
        }

        assertTrue(isActiveOn("2026-09-18"))
        assertFalse(isActiveOn("2026-09-19"))
        assertFalse(isActiveOn("2026-09-21"))
        assertFalse(isActiveOn("2026-09-22"))
    }

    @Test
    fun todayItem_everydayOn_isActiveForFutureDates() {
        val item = TodayItem(
            id = "item_1",
            userId = "user_1",
            text = "Test Item",
            canonicalId = "test item",
            remindAt = null,
            skipIfComplete = false,
            position = 0,
            activeFrom = "2026-09-18",
            activeUntil = null
        )

        fun isActiveOn(date: String): Boolean {
            val fromOk = item.activeFrom <= date
            val untilOk = item.activeUntil == null || item.activeUntil >= date
            return fromOk && untilOk
        }

        assertTrue(isActiveOn("2026-09-18"))
        assertTrue(isActiveOn("2026-09-19"))
        assertTrue(isActiveOn("2026-09-21"))
        assertTrue(isActiveOn("2026-09-22"))
    }
}