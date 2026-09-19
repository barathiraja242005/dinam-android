package com.barathiraja.dinam.domain.model

data class Occurrence(
    val id: String,
    val userId: String,
    val periodDate: String,
    val createdAt: Long
)