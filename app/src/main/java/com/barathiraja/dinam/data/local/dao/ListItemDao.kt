package com.barathiraja.dinam.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.barathiraja.dinam.data.local.entity.ListItemEntity

@Dao
interface ListItemDao {

    @Query(
        """
        SELECT * FROM list_item
        WHERE list_id = :listId
        ORDER BY position ASC
        """
    )
    suspend fun getByListId(
        listId: String
    ): List<ListItemEntity>

    @Insert
    suspend fun insert(
        item: ListItemEntity
    )

    @Insert
    suspend fun insertAll(
        items: List<ListItemEntity>
    )

    @Update
    suspend fun update(
        item: ListItemEntity
    )

    @Delete
    suspend fun delete(
        item: ListItemEntity
    )
}