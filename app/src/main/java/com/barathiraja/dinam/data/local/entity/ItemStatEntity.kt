package com.barathiraja.dinam.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "item_stat",
    primaryKeys = [
        "user_id",
        "category",
        "canonical_id"
    ]
)
data class ItemStatEntity(
    @ColumnInfo(name = "user_id")
    val userId: String,

    val category: String,

    @ColumnInfo(name = "canonical_id")
    val canonicalId: String,

    @ColumnInfo(name = "display_text")
    val displayText: String,

    @ColumnInfo(name = "use_count")
    val useCount: Int,

    @ColumnInfo(name = "last_used_at")
    val lastUsedAt: Long?,

    @ColumnInfo(name = "avg_interval_days")
    val avgIntervalDays: Double?
)