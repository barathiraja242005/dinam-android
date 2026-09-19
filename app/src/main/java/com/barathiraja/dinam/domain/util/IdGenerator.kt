package com.barathiraja.dinam.domain.util

import java.util.UUID

interface IdGenerator {
    fun generateId(): String

    object Default : IdGenerator {
        override fun generateId(): String = UUID.randomUUID().toString()
    }
}