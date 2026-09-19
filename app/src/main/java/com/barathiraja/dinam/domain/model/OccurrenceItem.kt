package com.barathiraja.dinam.domain.model

data class OccurrenceItem(
    val id: String,
    val occurrenceId: String,
    val todayItemId: String?,
    val origin: String,
    val text: String,
    val canonicalId: String,
    val remindAt: String?,
    val position: Int,
    val checked: Boolean,
    val checkedAt: Long?,
    val snoozedUntil: Long? = null,
    val listName: String? = null
)