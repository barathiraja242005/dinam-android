package com.barathiraja.dinam.domain.model

data class DinamList(
    val id: String,
    val userId: String,
    val title: String,
    val category: String,
    val archivedAt: Long?,
    val createdAt: Long
)