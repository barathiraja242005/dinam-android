package com.barathiraja.dinam.domain.model

data class OverdueItem(
    val item: OccurrenceItem,
    val originalDate: String
)