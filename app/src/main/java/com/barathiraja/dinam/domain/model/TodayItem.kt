package com.barathiraja.dinam.domain.model

data class TodayItem(
    val id: String,
    val userId: String,
    val text: String,
    val canonicalId: String,
    val remindAt: String?,
    val skipIfComplete: Boolean,
    val position: Int,
    val activeFrom: String,
    val activeUntil: String?
)