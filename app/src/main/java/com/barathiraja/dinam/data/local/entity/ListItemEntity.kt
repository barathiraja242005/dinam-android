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

    val checked: Boolean
)