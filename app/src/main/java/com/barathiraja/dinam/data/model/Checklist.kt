package com.barathiraja.dinam.data.model

import java.util.UUID

data class Checklist(
    val title: String,
    val progress: String,
    val id: String = UUID.randomUUID().toString()
)