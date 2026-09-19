package com.barathiraja.dinam.domain.util

import java.time.LocalDate

object DateProvider {

    fun today(): LocalDate {
        return LocalDate.now()
    }

    fun todayString(): String {
        return today().toString()
    }
}