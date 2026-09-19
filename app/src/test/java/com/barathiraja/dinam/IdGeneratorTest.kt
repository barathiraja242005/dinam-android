package com.barathiraja.dinam

import com.barathiraja.dinam.domain.util.IdGenerator
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class IdGeneratorTest {

    @Test
    fun idGenerator_generatesNonEmptyString() {
        val id = IdGenerator.Default.generateId()
        assertNotNull(id)
        assertTrue(id.isNotEmpty())
    }
}