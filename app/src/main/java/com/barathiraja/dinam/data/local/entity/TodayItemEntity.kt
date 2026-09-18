package com.barathiraja.dinam.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "today_item")
data class TodayItemEntity(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "user_id")
    val userId: String,

    val text: String,

    @ColumnInfo(name = "canonical_id")
    val canonicalId: String,

    @ColumnInfo(name = "remind_at")
    val remindAt: String?,

    @ColumnInfo(name = "skip_if_complete")
    val skipIfComplete: Boolean,

    val position: Int,

    @ColumnInfo(name = "active_from")
    val activeFrom: String,

    @ColumnInfo(name = "active_until")
    val activeUntil: String?
)