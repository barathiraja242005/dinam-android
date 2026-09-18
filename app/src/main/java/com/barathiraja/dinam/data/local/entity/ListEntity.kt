package com.barathiraja.dinam.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "list")
data class ListEntity(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "user_id")
    val userId: String,

    val title: String,

    val category: String,

    @ColumnInfo(name = "archived_at")
    val archivedAt: Long?,

    @ColumnInfo(name = "created_at")
    val createdAt: Long
)