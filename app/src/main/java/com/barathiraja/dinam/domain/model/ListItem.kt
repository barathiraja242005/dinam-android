package com.barathiraja.dinam.domain.model

data class ListItem(
    val id: String,
    val listId: String,
    val text: String,
    val canonicalId: String,
    val position: Int,
    val checked: Boolean,
    val dueDate: String? = null,
    val remindAt: String? = null,
    val remindMe: Boolean = false,
    val snoozedUntil: Long? = null
)