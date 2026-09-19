package com.barathiraja.dinam.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "occurrence_item")
data class OccurrenceItemEntity(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "occurrence_id")
    val occurrenceId: String,

    @ColumnInfo(name = "today_item_id")
    val todayItemId: String?,

    val origin: String,

    val text: String,

    @ColumnInfo(name = "canonical_id")
    val canonicalId: String,

    @ColumnInfo(name = "remind_at")
    val remindAt: String?,

    val position: Int,

    val checked: Boolean,

    @ColumnInfo(name = "checked_at")
    val checkedAt: Long?,

    @ColumnInfo(name = "snoozed_until")
    val snoozedUntil: Long? = null,

    @ColumnInfo(name = "list_name")
    val listName: String? = null
)