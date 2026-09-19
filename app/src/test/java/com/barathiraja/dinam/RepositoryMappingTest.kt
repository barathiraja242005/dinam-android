package com.barathiraja.dinam

import com.barathiraja.dinam.data.local.entity.OccurrenceItemEntity
import com.barathiraja.dinam.domain.model.OccurrenceItem
import org.junit.Assert.assertEquals
import org.junit.Test

class RepositoryMappingTest {

    @Test
    fun occurrenceItem_mappingPreservesAllFields() {
        val entity = OccurrenceItemEntity(
            id = "occ_item_1",
            occurrenceId = "occ_1",
            todayItemId = "today_1",
            origin = "routine",
            text = "Raja ride",
            canonicalId = "raja ride",
            remindAt = "08:00",
            position = 0,
            checked = true,
            checkedAt = 123456789L
        )

        val domain = OccurrenceItem(
            id = entity.id,
            occurrenceId = entity.occurrenceId,
            todayItemId = entity.todayItemId,
            origin = entity.origin,
            text = entity.text,
            canonicalId = entity.canonicalId,
            remindAt = entity.remindAt,
            position = entity.position,
            checked = entity.checked,
            checkedAt = entity.checkedAt
        )

        assertEquals("occ_item_1", domain.id)
        assertEquals("occ_1", domain.occurrenceId)
        assertEquals("today_1", domain.todayItemId)
        assertEquals("Raja ride", domain.text)
        assertEquals(true, domain.checked)
        assertEquals(123456789L, domain.checkedAt)
    }
}