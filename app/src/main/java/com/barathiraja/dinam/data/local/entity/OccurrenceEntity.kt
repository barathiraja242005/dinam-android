package com.barathiraja.dinam.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "occurrence",
    indices = [
        Index(
            value = ["user_id", "period_date"],
            unique = true
        )
    ]
)
data class OccurrenceEntity(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "user_id")
    val userId: String,

    @ColumnInfo(name = "period_date")
    val periodDate: String,

    @ColumnInfo(name = "created_at")
    val createdAt: Long
)