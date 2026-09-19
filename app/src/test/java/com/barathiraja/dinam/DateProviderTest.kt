package com.barathiraja.dinam

import com.barathiraja.dinam.domain.util.DateProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class DateProviderTest {

    @Test
    fun dateProvider_returnsOverrideWhenSet() {
        val testDate = LocalDate.of(2026, 9, 18)
        DateProvider.setOverride(testDate)

        assertEquals(testDate, DateProvider.today())
        assertEquals("2026-09-18", DateProvider.todayString())

        DateProvider.setOverride(null)
    }
}