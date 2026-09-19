package com.barathiraja.dinam.data.model

import java.util.UUID

data class TodoItem(
    val title: String,
    val time: String? = null,
    val checked: Boolean = false,
    val id: String = UUID.randomUUID().toString(),
    val listName: String? = null
)