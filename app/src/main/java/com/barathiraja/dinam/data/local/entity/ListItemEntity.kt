package com.barathiraja.dinam.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "list_item")
data class ListItemEntity(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "list_id")
    val listId: String,

    val text: String,

    @ColumnInfo(name = "canonical_id")
    val canonicalId: String,

    val position: Int,

    val checked: Boolean,

    @ColumnInfo(name = "due_date")
    val dueDate: String? = null,

    @ColumnInfo(name = "remind_at")
    val remindAt: String? = null,

    @ColumnInfo(name = "remind_me")
    val remindMe: Boolean = false,

    @ColumnInfo(name = "snoozed_until")
    val snoozedUntil: Long? = null
)