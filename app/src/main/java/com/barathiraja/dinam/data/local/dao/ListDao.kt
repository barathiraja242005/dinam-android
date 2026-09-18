package com.barathiraja.dinam.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.barathiraja.dinam.data.local.entity.ListEntity

@Dao
interface ListDao {

    @Query(
        """
        SELECT * FROM list
        WHERE user_id = :userId
          AND archived_at IS NULL
        ORDER BY created_at DESC
        """
    )
    suspend fun getActiveLists(
        userId: String
    ): List<ListEntity>

    @Query(
        """
        SELECT * FROM list
        WHERE id = :listId
        LIMIT 1
        """
    )
    suspend fun getById(
        listId: String
    ): ListEntity?

    @Insert
    suspend fun insert(
        list: ListEntity
    )

    @Update
    suspend fun update(
        list: ListEntity
    )
}