@file:Suppress("NewApi")

package com.barathiraja.dinam.domain.util

import java.time.LocalDate

interface DateProvider {
    fun today(): LocalDate
    fun todayString(): String = today().toString()

    companion object : DateProvider {
        private var overrideDate: LocalDate? = null

        fun setOverride(date: LocalDate?) {
            overrideDate = date
        }

        override fun today(): LocalDate {
            return overrideDate ?: LocalDate.now()
        }
    }
}