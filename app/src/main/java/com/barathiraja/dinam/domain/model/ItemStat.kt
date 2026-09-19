package com.barathiraja.dinam.domain.model

data class ItemStat(
    val userId: String,
    val category: String,
    val canonicalId: String,
    val displayText: String,
    val useCount: Int,
    val lastUsedAt: Long?,
    val avgIntervalDays: Double?
)