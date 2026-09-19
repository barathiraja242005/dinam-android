package com.barathiraja.dinam

import com.barathiraja.dinam.domain.util.Canonicalizer
import org.junit.Assert.assertEquals
import org.junit.Test

class CanonicalizerTest {

    @Test
    fun canonicalId_trimsAndLowercasesTextToSha256() {
        val result = Canonicalizer.canonicalId("   Raja Ride   ")
        assertEquals("c93f84d3bfa5c1350607c2cbc68fc81a39a3fd82be6b17bee51b1919bbebeb27", result)
    }

    @Test
    fun canonicalId_handlesEmptyText() {
        val result = Canonicalizer.canonicalId("")
        assertEquals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", result)
    }
}